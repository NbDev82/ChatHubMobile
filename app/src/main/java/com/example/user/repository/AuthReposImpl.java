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
import java.util.concurrent.CompletableFuture;
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
    public CompletableFuture<Void> signUp(SignUpRequest signUpRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String uid = firebaseUser.getUid();
                User user = new User(email);
                userRepos.addUser(uid, user)
                        .thenAccept(future::complete)
                        .exceptionally(e -> {
                            future.completeExceptionally(e);
                            return null;
                        });
            })
            .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> signInWithEmailPassword(SignInRequest signInRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    future.completeExceptionally(null);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> signInWithGithub(Activity activity, String email) {
        CompletableFuture<Void> future = new CompletableFuture<>();
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
                    .addOnSuccessListener(authResult -> future.complete(null))
                    .addOnFailureListener(future::completeExceptionally);
        } else {
            auth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser curUser = authResult.getUser();

                        userRepos.checkUserExistsByEmail(curUser.getEmail())
                                .thenAccept(exits -> {
                                    if (exits) {
                                        future.complete(null);
                                        return;
                                    }
                                    String uid = curUser.getUid();
                                    User user = new User(curUser.getEmail());
                                    userRepos.addUser(uid, user)
                                            .thenAccept(aVoid -> future.complete(null))
                                            .exceptionally(e -> {
                                                future.completeExceptionally(e);
                                                curUser.delete();
                                                return null;
                                            });
                                })
                                .exceptionally(e -> {
                                    Log.e(TAG, "Error fetching user data: " + e);
                                    future.completeExceptionally(e);
                                    return null;
                                });
                    })
                    .addOnFailureListener(future::completeExceptionally);
        }
        return future;
    }

    @Override
    public String getCurrentUid() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    @Override
    public CompletableFuture<User> getCurrentUser() {
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
    public CompletableFuture<Void> sendPasswordResetEmail(String email) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public CompletableFuture<Void> updatePassword(String newPassword) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!isLoggedIn()) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.updatePassword(newPassword)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> updatePassword(UpdatePasswordRequest updateRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!isLoggedIn()) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        checkOldPassword(updateRequest.getEmail(), updateRequest.getOldPassword())
            .thenAccept(aCheckVoid -> {
                updatePassword(updateRequest.getNewPassword())
                    .thenAccept(aUpdateVoid -> {
                        future.complete(null);
                    })
                    .exceptionally(e -> {
                        future.completeExceptionally(e);
                        return null;
                    });
            })
            .exceptionally(e -> {
                future.completeExceptionally(e);
                return null;
            });

        return future;
    }

    @Override
    public CompletableFuture<Void> checkOldPassword(String email, String oldPassword) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!isLoggedIn()) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        FirebaseUser user = auth.getCurrentUser();
        try {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            future.complete(null);
                        } else {
                            Exception exception = task.getException();
                            future.completeExceptionally(exception);
                        }
                    });
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    @Override
    public CompletableFuture<List<String>> fetchSignInMethods() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        List<String> providerIds = new ArrayList<>();
        for (UserInfo userInfo : user.getProviderData()) {
            String providerId = userInfo.getProviderId();
            providerIds.add(providerId);
        }

        future.complete(providerIds);
        return future;
    }

    @Override
    public CompletableFuture<Void> linkCurrentUserWithCredential(AuthCredential authCredential) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.linkWithCredential(authCredential)
                .addOnSuccessListener(authResult -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    @Override
    public CompletableFuture<Void> linkCurrentUserWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.linkWithCredential(phoneAuthCredential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser linkedUser = authResult.getUser();
                    String phoneNumber = linkedUser.getPhoneNumber();
                    userRepos.updatePhoneNumber(user.getUid(), phoneNumber)
                            .thenAccept(future::complete)
                            .exceptionally(e -> {
                                future.completeExceptionally(e);
                                return null;
                            });
                })
                .addOnFailureListener(e -> {
                    future.completeExceptionally(e);
                });
        return future;
    }

    @Override
    public CompletableFuture<Void> linkEmailPasswordWithCurrentUser(String email, String password) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        if (!TextUtils.equals(user.getEmail(), email)) {
            future.completeExceptionally(new EmailMismatchException("Email does not match the current user's email"));
            return future;
        }

        AuthCredential emailCredential = EmailAuthProvider.getCredential(email, password);

        user.linkWithCredential(emailCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
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
    public CompletableFuture<Boolean> checkCurrentEmailVerificationStatus() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isEmailVerified = user.isEmailVerified();
                future.complete(isEmailVerified);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Void> sendCurrentUserEmailVerification() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    @Override
    public CompletableFuture<Void> signInWithCredential(AuthCredential authCredential) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        future.completeExceptionally(task.getException());
                        return;
                    }

                    FirebaseUser curUser = auth.getCurrentUser();
                    if (curUser == null) {
                        future.completeExceptionally(new Exception("Current user is null"));
                        return;
                    }

                    userRepos.checkUserExistsByEmail(curUser.getEmail())
                            .thenAccept(exists -> {
                                if (exists) {
                                    future.complete(null);
                                    return;
                                }
                                String uid = curUser.getUid();
                                User user = new User(curUser.getEmail());
                                userRepos.addUser(uid, user)
                                        .thenAccept(future::complete)
                                        .exceptionally(e -> {
                                            future.completeExceptionally(e);
                                            curUser.delete();
                                            return null;
                                        });
                            })
                            .exceptionally(e -> {
                                Log.e(TAG, "Error fetching user data: " + e);
                                future.completeExceptionally(e);
                                return null;
                            });
                });

        return future;
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
    public CompletableFuture<AuthResult> linkGithubWithCurrentUser(Activity activity, String email) {
        CompletableFuture<AuthResult> future = new CompletableFuture<>();

        if (!isLoggedIn()) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);

        List<String> scopes = new ArrayList<>();
        scopes.add("user:email");
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(future::complete)
                    .addOnFailureListener(future::completeExceptionally);
        } else {
            FirebaseUser currentUser = auth.getCurrentUser();
            currentUser.startActivityForLinkWithProvider(activity, provider.build())
                    .addOnSuccessListener(future::complete)
                    .addOnFailureListener(future::completeExceptionally);
        }
        return future;
    }
}
