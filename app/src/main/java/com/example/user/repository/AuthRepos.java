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

public interface AuthRepos {
    Task<Void> signUp(SignUpRequest signUpRequest);

    Task<AuthResult> signInWithEmailPassword(SignInRequest signInRequest);

    Task<AuthResult> signInWithGithub(Activity activity, String email);

    String getCurrentUid();

    Task<User> getCurrentUser();

    void signOut();

    Task<Void> sendPasswordResetEmail(String email);

    boolean isLoggedIn();

    Task<Void> updatePassword(String newPassword);

    Task<Void> updatePassword(UpdatePasswordRequest updateRequest);

    Task<Void> checkOldPassword(String email, String password);

    Task<List<String>> fetchSignInMethods();

    Task<AuthResult> linkCurrentUserWithCredential(AuthCredential authCredential);

    Task<AuthResult> linkCurrentUserWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential);

    Task<AuthResult> linkEmailPasswordWithCurrentUser(String email, String password);

    boolean isCurrentUserEmail(@Nullable String email);

    String getCurrentEmail();

    Task<Boolean> checkCurrentEmailVerificationStatus();

    Task<Void> sendCurrentUserEmailVerification();

    FirebaseAuth getFirebaseAuth();

    Task<Void> signInWithCredential(AuthCredential authCredential);

    void sendOtp(Activity activity,
                 String phoneNumber,
                 boolean isResend,
                 PhoneAuthProvider.ForceResendingToken resendingToken,
                 PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks);

    Task<AuthResult> linkGithubWithCurrentUser(Activity activity, String email);
}
