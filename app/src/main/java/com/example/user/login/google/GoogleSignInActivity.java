package com.example.user.login.google;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.LoadingDialog;
import com.example.home.HomeActivity;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class GoogleSignInActivity extends LoginActivity {

    private GoogleSignInViewModel mViewModel;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthService authService = new AuthServiceImpl();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInViewModelFactory factory =
                new GoogleSignInViewModelFactory(authService, googleSignInClient);
        mViewModel = new ViewModelProvider(this, factory).get(GoogleSignInViewModel.class);

        mLoadingDialog = new LoadingDialog(GoogleSignInActivity.this);

        mViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                mLoadingDialog.show();
            } else {
                mLoadingDialog.dismiss();
            }
        });

        mViewModel.isSignInSuccess().observe(this, isSignInSuccess -> {
            if (isSignInSuccess) {
                navigateToHome();
            } else {
                showErrorToast();
            }
        });

        mViewModel.signIn(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleSignInViewModel.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            mViewModel.handleSignInResult(task);
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(GoogleSignInActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showErrorToast() {
        Toast.makeText(this, "Failed to sign in with Google", Toast.LENGTH_SHORT).show();
    }
}