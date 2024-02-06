package com.example.user.login.google;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.LoadingDialog;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GoogleSignInActivity extends LoginActivity {

    private GoogleSignInViewModel mViewModel;
    private LoadingDialog mLoadingDialog;
    private SignInClient oneTapClient;
    private BeginSignInRequest mSignInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        AuthService authService = new AuthServiceImpl();
        GoogleSignInViewModelFactory factory = new GoogleSignInViewModelFactory(authService);
        mViewModel = new ViewModelProvider(this, factory).get(GoogleSignInViewModel.class);

        mLoadingDialog = new LoadingDialog(GoogleSignInActivity.this);
        setObservers();

        initGoogleSignIn();
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
                navigateToHome();
            }
        });
    }

    private void initGoogleSignIn() {
        oneTapClient = Identity.getSignInClient(this);
        mSignInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
    }

    private void displayOneTapSignInUI() {
        oneTapClient.beginSignIn(mSignInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        IntentSenderRequest oneTapRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender())
                                .build();
                        loginWithGoogle.launch(oneTapRequest);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private ActivityResultLauncher<IntentSenderRequest> loginWithGoogle = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                        String idToken = credential.getGoogleIdToken();
                        if (idToken !=  null) {
                            Log.d(TAG, "Got ID token.");
                            mViewModel.signInWithIdToken(idToken);
                        }
                    } catch (ApiException e) {
                        switch (e.getStatusCode()) {
                            case CommonStatusCodes.CANCELED:
                                Log.e(TAG, "One-tap dialog was closed.");
                                break;
                            case CommonStatusCodes.NETWORK_ERROR:
                                Log.e(TAG, "One-tap encountered a network error.");
                                break;
                            default:
                                Log.e(TAG, "Couldn't get credential from result."
                                        + e.getLocalizedMessage());
                                break;
                        }
                    }
                }
            }
    );

    private void navigateToHome() {
        Intent intent = new Intent(GoogleSignInActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}