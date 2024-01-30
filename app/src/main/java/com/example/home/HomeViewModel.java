package com.example.home;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;

import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.User;

public class HomeViewModel extends BaseObservable {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final AuthService authService;
    private User user = new User();

    private MutableLiveData<Boolean> mNavigateToLogin = new MutableLiveData<>();

    public MutableLiveData<Boolean> getNavigateToLogin() {
        return mNavigateToLogin;
    }

    @Bindable
    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(com.example.BR.toastMessage);
    }

    public HomeViewModel() {
        authService = new AuthServiceImpl();

        authService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    this.user = user;

                    if (user != null) {
                        setEmail(user.getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                });
    }

    public void signOut() {
        setToastMessage("Sign out");
        authService.signOut();
        navigateToLogin();
    }

    private void navigateToLogin() {
        mNavigateToLogin.setValue(true);
    }
}
