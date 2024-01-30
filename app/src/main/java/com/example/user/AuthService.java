package com.example.user;

import android.content.Context;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;

import java.util.function.Consumer;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void signIn(SignInRequest signInRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void signInOrSignUpWithGoogle(String idToken, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void updateOnlineStatus(String uid, boolean isOnline);
    String getCurrentUid();
    Task<User> getCurrentUser();
    Task<User> getUserByUid(String uid);
    Task<Boolean> checkUserExitsByEmail(String email);
    void signOut(Context context);
}
