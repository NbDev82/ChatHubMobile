package com.example.user.login.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.CustomToast;
import com.example.R;
import com.example.customcontrol.CustomBindingAdapters;
import com.example.customcontrol.LoadingDialog;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
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
        Utils.setStatusBarGradiant(this);

        AuthService authService = new AuthServiceImpl();
        GoogleSignInViewModelFactory factory = new GoogleSignInViewModelFactory(authService);
        mViewModel = new ViewModelProvider(this, factory).get(GoogleSignInViewModel.class);

        mLoadingDialog = new LoadingDialog(GoogleSignInActivity.this);
        setObservers();

        displayOneTapSignInUI();
    }

    private void setObservers() {
        mViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                mLoadingDialog.show();
            } else {
                mLoadingDialog.dismiss();
            }
        });

        mViewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                CustomToast.showToastMessage(this, "Login successfully", icon -> {
                    icon.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_circle_check_solid,
                            getTheme()));
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToHome();
                    }
                }, 100);
            }
        });
    }

    private void displayOneTapSignInUI() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(GoogleSignInActivity.this, gso);
        googleSignInClient.signOut();
        Intent intent = googleSignInClient.getSignInIntent();
        loginWithGoogle.launch(intent);
    }

    private ActivityResultLauncher<Intent> loginWithGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    mViewModel.handleSignInResult(task);
                }
            }
    );

    private void navigateToHome() {
        Intent intent = new Intent(GoogleSignInActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}