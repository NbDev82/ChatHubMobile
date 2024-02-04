package com.example.user.profile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.EGender;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfileViewModel mProfileViewModel;
    private int mSelectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        ActivityUserProfileBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_user_profile);

        AuthService authService = new AuthServiceImpl();
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(authService);
        mProfileViewModel = new ViewModelProvider(this, factory).get(UserProfileViewModel.class);

        binding.setViewModel(mProfileViewModel);
        binding.setLifecycleOwner(this);

        setObservers();
    }

    private void setObservers() {
        mProfileViewModel.getNavigateToHome().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        mProfileViewModel.getOpenDatePickerDialog().observe(this, this::openDatePickerDialog);

        mProfileViewModel.getOpenCustomAlertDialog().observe(this, this::openCustomAlertDialog);

        mProfileViewModel.getOpenSingleChoiceGender().observe(this, this::openSingleChoiceGender);
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
                mProfileViewModel.setBirthday(selectedDate.getTime());
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void openCustomAlertDialog(AlertDialogModel alertDialogModel) {
        View view = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.layout_alert_dialog, null);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(UserProfileActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        TextView titleTxv = view.findViewById(R.id.titleTxv);
        TextView messageTxv = view.findViewById(R.id.messageTxv);
        Button positiveBtn = view.findViewById(R.id.positiveBtn);
        Button negativeBtn = view.findViewById(R.id.negativeBtn);

        titleTxv.setText( alertDialogModel.getTitle() );
        messageTxv.setText( alertDialogModel.getMessage() );
        positiveBtn.setText( alertDialogModel.getPositiveBtnTitle() );
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogModel.getPositiveButtonClickListener().accept(null);
                alertDialog.dismiss();
            }
        });
        negativeBtn.setText( alertDialogModel.getNegativeBtnTitle() );
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogModel.getNegativeButtonClickListener().accept(null);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void openSingleChoiceGender(int selectedItem) {
        mSelectedItem = selectedItem;
        String[] genderStrs = EGender.getAllDisplays();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_gender)
                .setTitle("Gender")
                .setSingleChoiceItems(genderStrs, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedItem = which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EGender curSelected = EGender.values()[mSelectedItem];
                        mProfileViewModel.setGender(curSelected);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}