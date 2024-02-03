package com.example.customcontrol.customalertdialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.function.Consumer;

public class AlertDialogViewModel extends ViewModel {

    private final MutableLiveData<String> mTitle = new MutableLiveData<>();
    private final MutableLiveData<String> mMessage = new MutableLiveData<>();
    private final MutableLiveData<String> mPositiveBtnTitle = new MutableLiveData<>();
    private final MutableLiveData<String> mNegativeBtnTitle = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mPositiveButtonClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNegativeButtonClicked = new MutableLiveData<>();
    private Consumer<Void> mPositiveButtonClickListener;
    private Consumer<Void> mNegativeButtonClickListener;

    public LiveData<String> getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle.postValue(title);
    }

    public LiveData<String> getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage.postValue(message);
    }

    public void setPositiveButton(String title, Consumer<Void> listener) {
        mPositiveBtnTitle.postValue(title);
        mPositiveButtonClickListener = listener;
    }

    public void setNegativeButton(String title, Consumer<Void> listener) {
        mNegativeBtnTitle.postValue(title);
        mNegativeButtonClickListener = listener;
    }

    public LiveData<String> getPositiveBtnTitle() {
        return mPositiveBtnTitle;
    }

    public LiveData<String> getNegativeBtnTitle() {
        return mNegativeBtnTitle;
    }

    public LiveData<Boolean> getPositiveButtonClicked() {
        return mPositiveButtonClicked;
    }

    public LiveData<Boolean> getNegativeButtonClicked() {
        return mNegativeButtonClicked;
    }

    public void onPositiveButtonClicked() {
        if (mPositiveButtonClickListener != null) {
            mPositiveButtonClickListener.accept(null);
        }
        mPositiveButtonClicked.postValue(true);
    }

    public void onNegativeButtonClicked() {
        if (mNegativeButtonClickListener != null) {
            mNegativeButtonClickListener.accept(null);
        }
        mNegativeButtonClicked.postValue(true);
    }

    public void setAlertDialogModel(AlertDialogModel alertDialogModel) {

    }
}
