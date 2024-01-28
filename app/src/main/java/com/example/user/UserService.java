package com.example.user;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;

import java.util.function.Consumer;

public interface UserService {
    void signUp(SignUpRequest signUpRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void signIn(SignInRequest signInRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure);
    void updateOnlineStatus(String uid, boolean isOnline);
    String getCurrentUid();
}
