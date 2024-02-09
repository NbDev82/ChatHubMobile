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
import java.util.function.Consumer;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest,
                Consumer<Void> onSuccess,
                Consumer<Exception> onFailure);

    void signInWithEmailPassword(SignInRequest signInRequest,
                                 Consumer<Void> onSuccess,
                                 Consumer<Exception> onFailure);

    void signInWithGithub(Activity activity,
                          String email,
                          Consumer<AuthResult> onSuccess,
                          Consumer<Exception> onFailure);

    void updateOnlineStatus(String uid, boolean isOnline);

    String getCurrentUid();

    Task<User> getCurrentUser();

    Task<User> getUserByUid(String uid);

    Task<Boolean> checkUserExitsByEmail(String email);

    void signOut();

    void sendPasswordResetEmail(String email,
                                Consumer<Void> onSuccess,
                                Consumer<Exception> onFailure);

    boolean isLoggedIn();

    Task<Void> updateBasicUser(String uid, User user);

    void updatePassword(String newPassword,
                        Consumer<Void> onSuccess,
                        Consumer<Exception> onFailure);

    void checkOldPassword(String email,
                          String password,
                          Consumer<Void> onSuccess,
                          Consumer<Exception> onFailure);

    void fetchSignInMethods(Consumer<List<ESignInMethod>> onSuccess,
                            Consumer<Exception> onFailure);

    void linkCurrentUserWithCredential(AuthCredential googleCredential,
                                       Consumer<Void> onSuccess,
                                       Consumer<Exception> onFailure);

    void linkEmailPasswordWithCurrentUser(String email, String password,
                                          Consumer<Void> onSuccess,
                                          Consumer<Exception> onFailure);

    boolean isCurrentUserEmail(@Nullable String email);

    String getCurrentEmail();

    void disableCurrentUser(Consumer<Void> onSuccess, Consumer<Exception> onFailure);

    void checkCurrentEmailVerificationStatus(Consumer<Boolean> onSuccess,
                                             Consumer<Exception> onFailure);

    void sendCurrentUserEmailVerification(Consumer<Void> onSuccess,
                                          Consumer<Exception> onFailure);

    FirebaseAuth getFirebaseAuth();

    void signInWithCredential(AuthCredential authCredential,
                              Consumer<Void> onSuccess,
                              Consumer<Exception> onFailure);

    void existsByPhoneNumber(String phoneNumber,
                             Consumer<Boolean> onSuccess,
                             Consumer<Exception> onFailure);

    void sendOtp(Activity activity,
                 String phoneNumber,
                 boolean isResend,
                 PhoneAuthProvider.ForceResendingToken resendingToken,
                 PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks);
}
