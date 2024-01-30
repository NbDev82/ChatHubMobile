package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.User;
import com.example.user.login.LoginActivity;

public class HomeViewModel extends BaseObservable {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final Context context;
    private final AuthService authService;

    private User user = new User();

    @Bindable
    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    public HomeViewModel(Context context) {
        this.context = context;
        authService = new AuthServiceImpl();

        authService.getCurrentUser()
                .addOnSuccessListener(user -> {
                    this.user = user;

                    setEmail(user.getEmail());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                });
    }

    public void onBackBtnClick(Context context) {
        Log.i(TAG, "Back to login");

        Toast.makeText(context, "Sign out", Toast.LENGTH_SHORT).show();
        signOut();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void signOut() {
        authService.signOut(context);
    }
}
