package com.example.infrastructure;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class BaseViewModel extends ViewModel {

    protected final MutableLiveData<String> mSuccessToastMessage = new MutableLiveData<>();
    protected final MutableLiveData<String> mErrorToastMessage = new MutableLiveData<>();

    private AuthService authService;

    public MutableLiveData<String> getSuccessToastMessage() {
        return mSuccessToastMessage;
    }

    public MutableLiveData<String> getErrorToastMessage() {
        return mErrorToastMessage;
    }

    public BaseViewModel() {
        authService = new AuthServiceImpl();
    }

    public void onPause() {
        String uid = authService.getCurrentUid();
        authService.updateOnlineStatus(uid, false);
    }

    public void onResume() {
        String uid = authService.getCurrentUid();
        authService.updateOnlineStatus(uid, true);
    }
}
