package com.example.user;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;

public interface AuthService {
    Task<Void> signUp(SignUpRequest signUpRequest);

    Task<AuthResult> signInWithEmailPassword(SignInRequest signInRequest);

    Task<AuthResult> signInWithGithub(Activity activity, String email);

    void updateOnlineStatus(String uid, boolean isOnline);

    String getCurrentUid();

    Task<User> getCurrentUser();

    Task<User> getUserByUid(String uid);

    Task<Boolean> checkUserExistsByEmail(String email);

    void signOut();

    Task<Void> sendPasswordResetEmail(String email);

    boolean isLoggedIn();

    Task<Void> updateBasicUser(String uid, User user);

    Task<Void> updatePassword(String newPassword);

    Task<Void> checkOldPassword(String email, String password);

    Task<List<String>> fetchSignInMethods();

    Task<Void> linkCurrentUserWithCredential(AuthCredential googleCredential);

    Task<AuthResult> linkEmailPasswordWithCurrentUser(String email, String password);

    boolean isCurrentUserEmail(@Nullable String email);

    String getCurrentEmail();

    Task<Boolean> checkCurrentEmailVerificationStatus();

    Task<Void> sendCurrentUserEmailVerification();

    FirebaseAuth getFirebaseAuth();

    Task<Void> signInWithCredential(AuthCredential authCredential);

    Task<Boolean> existsByPhoneNumber(String phoneNumber);

    void sendOtp(Activity activity,
                 String phoneNumber,
                 boolean isResend,
                 PhoneAuthProvider.ForceResendingToken resendingToken,
                 PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks);
}
