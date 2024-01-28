package com.example.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.user.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends BaseObservable {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Bindable
    public String getEmail() {
        return mUser.getEmail();
    }

    public HomeViewModel(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void onBackBtnClick(Context context) {
        Log.i(TAG, "Back to login");

        Toast.makeText(context, "Sign out", Toast.LENGTH_SHORT).show();
        signOut();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void signOut() {
        mAuth.signOut();
    }
}
