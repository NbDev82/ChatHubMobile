package com.example.user.login.google;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.user.AuthService;
import com.example.user.login.LoginViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleSignInViewModelFactory extends LoginViewModelFactory {

    private final GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInViewModelFactory(AuthService authService, GoogleSignInClient googleSignInClient) {
        super(authService);
        this.mGoogleSignInClient = googleSignInClient;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GoogleSignInViewModel.class)) {
            return (T) new GoogleSignInViewModel(mAuthService, mGoogleSignInClient);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
