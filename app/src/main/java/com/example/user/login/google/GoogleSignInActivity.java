package com.example.user.login.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.CustomToast;
import com.example.customcontrol.LoadingDialog;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class GoogleSignInActivity extends LoginActivity {

    private NavigationManager navigationManager;
    private GoogleSignInViewModel viewModel;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        AuthService authService = new AuthServiceImpl();
        GoogleSignInViewModelFactory factory = new GoogleSignInViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(GoogleSignInViewModel.class);

        loadingDialog = new LoadingDialog(GoogleSignInActivity.this);
        setupObservers();

        displayOneTapSignInUI();
    }

    private void setupObservers() {
        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.show();
            } else {
                loadingDialog.dismiss();
            }
        });

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                CustomToast.showSuccessToast(this, "Login successfully");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigationManager.navigateToHome(EAnimationType.FADE_IN);
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
                    viewModel.handleSignInResult(task);
                }
            }
    );
}