package com.example.user;

import android.app.Activity;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.function.Consumer;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void signIn(SignInRequest signInRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void signInOrSignUpWithGoogle(String idToken, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void signInOrSignUpWithGithub(Activity activity, String email,
                                  Consumer<AuthResult> onSuccess, Consumer<Exception> onFailure);
    void updateOnlineStatus(String uid, boolean isOnline);
    String getCurrentUid();
    Task<User> getCurrentUser();
    Task<User> getUserByUid(String uid);
    Task<Boolean> checkUserExitsByEmail(String email);
    void signOut();
    void sendPasswordResetEmail(String email, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    boolean isLoggedIn();
}
