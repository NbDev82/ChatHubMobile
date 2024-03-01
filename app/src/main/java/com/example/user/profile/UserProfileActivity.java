package com.example.user.profile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.DatePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogFragment;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.customcontrol.inputdialogfragment.InputDialogFragment;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.EGender;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private NavigationManager navigationManager;
    private UserProfileViewModel profileViewModel;
    private int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        ActivityUserProfileBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_user_profile);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(userRepos, authRepos);
        profileViewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);

        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        profileViewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_IN);
            }
        });

        profileViewModel.getOpenImagePicker().observe(this, pick -> {
            if (pick) {
                openImagePicker();
            }
        });

        profileViewModel.getOpenCustomInputDialog().observe(this, this::openCustomInputDialog);

        profileViewModel.getOpenDatePickerDialog().observe(this, this::openDatePickerDialog);

        profileViewModel.getOpenCustomAlertDialog().observe(this, this::openCustomAlertDialog);

        profileViewModel.getOpenSingleChoiceGender().observe(this, this::openSingleChoiceGender);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);
    }

    private final ActivityResultLauncher pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imgUri = result.getData().getData();
                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(imgUri);
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "Error: " + e);
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        profileViewModel.setImageBitmap(bitmap);
                    }
                }
            }
    );

    private void openCustomInputDialog(InputDialogModel inputDialogModel) {
        InputDialogFragment dialog = new InputDialogFragment(inputDialogModel);
        dialog.show(getSupportFragmentManager(), InputDialogFragment.TAG);
    }

    private void openDatePickerDialog(Calendar currentDate) {
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                profileViewModel.setBirthday(selectedDate.getTime());
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void openCustomAlertDialog(AlertDialogModel alertDialogModel) {
        AlertDialogFragment dialog = new AlertDialogFragment(alertDialogModel);
        dialog.show(getSupportFragmentManager(), AlertDialogFragment.TAG);
    }

    private void openSingleChoiceGender(int selectedItem) {
        this.selectedItem = selectedItem;
        String[] genderStrs = EGender.getAllDisplays();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_gender)
                .setTitle("Gender")
                .setSingleChoiceItems(genderStrs, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserProfileActivity.this.selectedItem = which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EGender curSelected = EGender.values()[UserProfileActivity.this.selectedItem];
                        profileViewModel.setGender(curSelected);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}