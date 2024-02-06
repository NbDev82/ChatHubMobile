package com.example.infrastructure;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;

public abstract class BaseViewModel extends ViewModel {

    protected final MutableLiveData<String> mSuccessToastMessage = new MutableLiveData<>();
    protected final MutableLiveData<String> mErrorToastMessage = new MutableLiveData<>();

    protected AuthService mAuthService;

    public MutableLiveData<String> getSuccessToastMessage() {
        return mSuccessToastMessage;
    }

    public MutableLiveData<String> getErrorToastMessage() {
        return mErrorToastMessage;
    }

    public BaseViewModel() {
    }

    public void setOnlineStatus(boolean status) {
        String uid = mAuthService.getCurrentUid();
        mAuthService.updateOnlineStatus(uid, status);
    }
}
