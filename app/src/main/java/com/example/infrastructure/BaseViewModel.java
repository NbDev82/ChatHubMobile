package com.example.infrastructure;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class BaseViewModel extends AndroidViewModel {

    private AuthService authService;

    public BaseViewModel(Application application) {
        super(application);

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
