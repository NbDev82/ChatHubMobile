package com.example.infrastructure;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.user.UserService;
import com.example.user.UserServiceImpl;

public class BaseViewModel extends AndroidViewModel {

    private UserService userService;

    public BaseViewModel(Application application) {
        super(application);

        userService = new UserServiceImpl();
    }

    public void onPause() {
        String uid = userService.getCurrentUid();
        userService.updateOnlineStatus(uid, false);
    }

    public void onResume() {
        String uid = userService.getCurrentUid();
        userService.updateOnlineStatus(uid, true);
    }
}
