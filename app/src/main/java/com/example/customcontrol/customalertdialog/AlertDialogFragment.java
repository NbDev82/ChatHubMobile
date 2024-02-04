package com.example.customcontrol.customalertdialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.FragmentAlertDialogBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AlertDialogFragment extends AppCompatDialogFragment {

    private FragmentAlertDialogBinding mBinding;
    private AlertDialogViewModel mViewModel;

    public AlertDialogFragment(AlertDialogViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity()
                .getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mViewModel == null) {
            mViewModel = new ViewModelProvider(requireActivity()).get(AlertDialogViewModel.class);
        }

        mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.fragment_alert_dialog, null, false);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mViewModel.getPositiveButtonClicked().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                dismiss();
            }
        });

        mViewModel.getNegativeButtonClicked().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                dismiss();
            }
        });

//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
//                .setTitle("test")
//                .setMessage("Test")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dismiss();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dismiss();
//                    }
//                });
//
//        builder.create().show();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
