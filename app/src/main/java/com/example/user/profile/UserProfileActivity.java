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
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogFragment;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.customcontrol.inputdialogfragment.InputDialogFragment;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.EGender;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class UserProfileActivity extends BaseActivity<UserProfileViewModel, ActivityUserProfileBinding> {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private int selectedItem = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected Class<UserProfileViewModel> getViewModelClass() {
        return UserProfileViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new UserProfileViewModelFactory(userRepos, authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_IN);
            }
        });

        viewModel.getOpenImagePicker().observe(this, pick -> {
            if (pick) {
                openImagePicker();
            }
        });

        viewModel.getOpenDatePickerDialog().observe(this, this::openDatePickerDialog);

        viewModel.getOpenSingleChoiceGender().observe(this, this::openSingleChoiceGender);
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
                        viewModel.setImageBitmap(bitmap);
                    }
                }
            }
    );

    private void openDatePickerDialog(Calendar currentDate) {
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                viewModel.setBirthday(selectedDate.getTime());
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
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
                        viewModel.setGender(curSelected);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}