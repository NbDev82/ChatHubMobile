package com.example.user.repository;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.infrastructure.Utils;
import com.example.user.EGender;
import com.example.user.EUserField;
import com.example.user.EmailMismatchException;
import com.example.user.User;
import com.example.user.UserNotAuthenticatedException;
import com.example.user.changepassword.UpdatePasswordRequest;
import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuthReposImpl implements AuthRepos {

    private static final String TAG = AuthReposImpl.class.getSimpleName();

    private final UserRepos userRepos;
    private FirebaseAuth auth;

    public AuthReposImpl(UserRepos userRepos) {
        this.userRepos = userRepos;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public Task<Void> signUp(SignUpRequest signUpRequest) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();

        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String uid = firebaseUser.getUid();
                User user = new User(email);
                userRepos.addUser(uid, user).addOnSuccessListener(aVoid -> {
                    source.setResult(null);
                }).addOnFailureListener(source::setException);
            } else {
                source.setException(task.getException());
            }
        });

        return source.getTask();
    }

    @Override
    public Task<AuthResult> signInWithEmailPassword(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        return auth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInWithGithub(Activity activity, String email) {
        TaskCompletionSource<AuthResult> source = new TaskCompletionSource<>();

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(source::setResult)
                    .addOnFailureListener(source::setException);
        } else {
            auth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser curUser = authResult.getUser();

                        userRepos.checkUserExistsByEmail(curUser.getEmail())
                                .addOnSuccessListener(exits -> {
                                    if (exits) {
                                        source.setResult(authResult);
                                        return;
                                    }
                                    String uid = curUser.getUid();
                                    User user = new User(curUser.getEmail());
                                    userRepos.addUser(uid, user)
                                            .addOnSuccessListener(aVoid -> source.setResult(authResult))
                                            .addOnFailureListener(e -> {
                                                source.setException(e);
                                                curUser.delete();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    source.setException(e);
                                    Log.e(TAG, "Error fetching user data: " + e);
                                });
                    })
                    .addOnFailureListener(source::setException);
        }
        return source.getTask();
    }

    @Override
    public String getCurrentUid() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    @Override
    public Task<User> getCurrentUser() {
        String uid = getCurrentUid();
        return userRepos.getUserByUid(uid);
    }

    private User convertDocumentToModel(DocumentSnapshot documentSnapshot) {
        try {
            String id = documentSnapshot.getId();
            String fullName = documentSnapshot.getString(EUserField.FULL_NAME.getName());
            String email = documentSnapshot.getString(EUserField.EMAIL.getName());
            String phoneNumber = documentSnapshot.getString(EUserField.PHONE_NUMBER.getName());
            String genderStr = documentSnapshot.getString(EUserField.GENDER.getName());
            EGender gender = EGender.valueOf(genderStr);
            Date birthday = documentSnapshot.getDate(EUserField.BIRTHDAY.getName());
            String imageUrl = documentSnapshot.getString(EUserField.IMAGE_URL.getName());
            boolean isOnline = getBooleanField(documentSnapshot, EUserField.IS_ONLINE);
            boolean isDeleted = getBooleanField(documentSnapshot, EUserField.IS_DELETED);

            return new User(id, fullName, email, phoneNumber, gender, birthday,
                    imageUrl, isOnline, isDeleted);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            return null;
        }
    }

    private boolean getBooleanField(DocumentSnapshot documentSnapshot, EUserField field) {
        Object value = documentSnapshot.get(field.getName());
        return value instanceof Boolean ? (Boolean) value : false;
    }

    @Override
    public void signOut() {
        auth.signOut();
    }

    @Override
    public Task<Void> sendPasswordResetEmail(String email) {
        return auth.sendPasswordResetEmail(email);
    }

    @Override
    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public Task<Void> updatePassword(String newPassword) {
        if (!isLoggedIn()) {
            return Tasks.forException(new UserNotAuthenticatedException("User is not authenticated"));
        }

        FirebaseUser user = auth.getCurrentUser();
        return user.updatePassword(newPassword);
    }

    @Override
    public Task<Void> updatePassword(UpdatePasswordRequest updateRequest) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();

        if (!isLoggedIn()) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        checkOldPassword(updateRequest.getEmail(), updateRequest.getOldPassword())
                .addOnSuccessListener(aCheckVoid -> {
                    updatePassword(updateRequest.getNewPassword())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    source.setResult(task.getResult());
                                } else {
                                    Exception exception = task.getException();
                                    source.setException(exception);
                                }
                            });
                })
                .addOnFailureListener(source::setException);

        return source.getTask();
    }

    @Override
    public Task<Void> checkOldPassword(String email, String oldPassword) {
        if (!isLoggedIn()) {
            return Tasks.forException(new UserNotAuthenticatedException("User is not authenticated"));
        }

        FirebaseUser user = auth.getCurrentUser();
        try {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
            return user.reauthenticate(credential);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            return Tasks.forException(e);
        }
    }

    @Override
    public Task<List<String>> fetchSignInMethods() {
        TaskCompletionSource<List<String>> source = new TaskCompletionSource<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        List<String> providerIds = new ArrayList<>();
        for (UserInfo userInfo : user.getProviderData()) {
            String providerId = userInfo.getProviderId();
            providerIds.add(providerId);
        }

        source.setResult(providerIds);
        return source.getTask();
    }

    @Override
    public Task<AuthResult> linkCurrentUserWithCredential(AuthCredential authCredential) {
        TaskCompletionSource<AuthResult> source = new TaskCompletionSource<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        user.linkWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        source.setResult(task.getResult());
                    } else {
                        Exception exception = task.getException();
                        source.setException(exception);
                    }
                });
        return source.getTask();
    }

    @Override
    public Task<AuthResult> linkCurrentUserWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        TaskCompletionSource<AuthResult> source = new TaskCompletionSource<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        user.linkWithCredential(phoneAuthCredential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser linkedUser = authResult.getUser();
                    String phoneNumber = linkedUser.getPhoneNumber();
                    userRepos.updatePhoneNumber(user.getUid(), phoneNumber)
                            .addOnSuccessListener(aVoid -> {
                                source.setResult(authResult);
                            })
                            .addOnFailureListener(source::setException);
                })
                .addOnFailureListener(source::setException);
        return source.getTask();
    }

    @Override
    public Task<AuthResult> linkEmailPasswordWithCurrentUser(String email, String password) {
        TaskCompletionSource<AuthResult> source = new TaskCompletionSource<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        if (!TextUtils.equals(user.getEmail(), email)) {
            source.setException(new EmailMismatchException("Email does not match the current user's email"));
            return source.getTask();
        }

        AuthCredential emailCredential = EmailAuthProvider.getCredential(email, password);

        user.linkWithCredential(emailCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        source.setResult(task.getResult());
                    } else {
                        source.setException(task.getException());
                    }
                });

        return source.getTask();
    }

    @Override
    public boolean isCurrentUserEmail(@Nullable String email) {
        FirebaseUser user = auth.getCurrentUser();
        return user != null && user.getEmail() != null && user.getEmail().equals(email);
    }

    @Override
    public String getCurrentEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getEmail() : "";
    }

    @Override
    public Task<Boolean> checkCurrentEmailVerificationStatus() {
        TaskCompletionSource<Boolean> source = new TaskCompletionSource<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        user.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isEmailVerified = user.isEmailVerified();
                source.setResult(isEmailVerified);
            } else {
                source.setException(task.getException());
            }
        });

        return source.getTask();
    }

    @Override
    public Task<Void> sendCurrentUserEmailVerification() {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        source.setResult(null);
                    } else {
                        source.setException(task.getException());
                    }
                });

        return source.getTask();
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    @Override
    public Task<Void> signInWithCredential(AuthCredential authCredential) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();

        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        source.setException(task.getException());
                        return;
                    }

                    FirebaseUser curUser = auth.getCurrentUser();
                    if (curUser == null) {
                        source.setException(new Exception("Current user is null"));
                        return;
                    }

                    userRepos.checkUserExistsByEmail(curUser.getEmail())
                            .addOnSuccessListener(exists -> {
                                if (exists) {
                                    source.setResult(null);
                                    return;
                                }
                                String uid = curUser.getUid();
                                User user = new User(curUser.getEmail());
                                userRepos.addUser(uid, user)
                                        .addOnSuccessListener(aVoid -> source.setResult(null))
                                        .addOnFailureListener(e -> {
                                            source.setException(e);
                                            curUser.delete();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                source.setException(e);
                                Log.e(TAG, "Error fetching user data: " + e);
                            });
                });

        return source.getTask();
    }

    @Override
    public void sendOtp(Activity activity,
                        String phoneNumber,
                        boolean isResend,
                        PhoneAuthProvider.ForceResendingToken resendingToken,
                        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthOptions.Builder builder = PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(Utils.OTP_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks);
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder
                    .setForceResendingToken(resendingToken)
                    .build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    @Override
    public Task<AuthResult> linkGithubWithCurrentUser(Activity activity, String email) {
        TaskCompletionSource<AuthResult> source = new TaskCompletionSource<>();
        if (!isLoggedIn()) {
            source.setException(new UserNotAuthenticatedException("User is not authenticated"));
            return source.getTask();
        }

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(source::setResult)
                    .addOnFailureListener(source::setException);
        } else {
            FirebaseUser currentUser = auth.getCurrentUser();
            currentUser
                    .startActivityForLinkWithProvider(activity, provider.build())
                    .addOnSuccessListener(source::setResult)
                    .addOnFailureListener(source::setException);
        }
        return source.getTask();
    }
}
