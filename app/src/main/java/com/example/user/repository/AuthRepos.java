package com.example.user.repository;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.example.user.User;
import com.example.user.changepassword.UpdatePasswordRequest;
import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AuthRepos {
    CompletableFuture<Void> signUp(SignUpRequest signUpRequest);

    CompletableFuture<Void> signInWithEmailPassword(SignInRequest signInRequest);

    CompletableFuture<Void> signInWithGithub(Activity activity, String email);

    String getCurrentUid();

    CompletableFuture<User> getCurrentUser();

    void signOut();

    CompletableFuture<Void> sendPasswordResetEmail(String email);

    boolean isLoggedIn();

    CompletableFuture<Void> updatePassword(String newPassword);

    CompletableFuture<Void> updatePassword(UpdatePasswordRequest updateRequest);

    CompletableFuture<Void> checkOldPassword(String email, String password);

    CompletableFuture<List<String>> fetchSignInMethods();

    CompletableFuture<Void> linkCurrentUserWithCredential(AuthCredential authCredential);

    CompletableFuture<Void> linkCurrentUserWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential);

    CompletableFuture<Void> linkEmailPasswordWithCurrentUser(String email, String password);

    boolean isCurrentUserEmail(@Nullable String email);

    String getCurrentEmail();

    CompletableFuture<Boolean> checkCurrentEmailVerificationStatus();

    CompletableFuture<Void> sendCurrentUserEmailVerification();

    FirebaseAuth getFirebaseAuth();

    CompletableFuture<Void> signInWithCredential(AuthCredential authCredential);

    void sendOtp(Activity activity,
                 String phoneNumber,
                 boolean isResend,
                 PhoneAuthProvider.ForceResendingToken resendingToken,
                 PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks);

    CompletableFuture<AuthResult> linkGithubWithCurrentUser(Activity activity, String email);
}
