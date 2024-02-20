package com.example.infrastructure;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.authservice.AuthService;

public abstract class BaseViewModel extends ViewModel {

    protected final MutableLiveData<String> successToastMessage = new MutableLiveData<>();
    protected final MutableLiveData<String> errorToastMessage = new MutableLiveData<>();

    protected AuthService authService;

    public MutableLiveData<String> getSuccessToastMessage() {
        return successToastMessage;
    }

    public MutableLiveData<String> getErrorToastMessage() {
        return errorToastMessage;
    }

    public BaseViewModel() {
    }

    public void setOnlineStatus(boolean status) {
        String uid = authService.getCurrentUid();
        authService.updateOnlineStatus(uid, status);
    }
}
