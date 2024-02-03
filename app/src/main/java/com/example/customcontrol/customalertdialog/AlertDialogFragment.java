package com.example.customcontrol.customalertdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.databinding.FragmentAlertDialogBinding;

public class AlertDialogFragment extends DialogFragment {

    private FragmentAlertDialogBinding mBinding;
    private AlertDialogViewModel mViewModel;

    public AlertDialogFragment(AlertDialogViewModel viewModel) {
        mViewModel = viewModel;
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
        mBinding.setLifecycleOwner(requireActivity());

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

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
    }
}
