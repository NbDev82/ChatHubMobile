package com.example.user;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class AuthServiceImpl implements AuthService {

    private static final String TAG = AuthServiceImpl.class.getSimpleName();

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public AuthServiceImpl() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void signUp(SignUpRequest signUpRequest,
                       Consumer<Void> onSuccess,
                       Consumer<Exception> onFailure) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            FirebaseUser firebaseUser = auth.getCurrentUser();
            if (!task.isSuccessful() || firebaseUser == null) {
                onFailure.accept(task.getException());
                return;
            }
            String uid = firebaseUser.getUid();
            User user = new User(email);
            addUser(uid, user, onSuccess, onFailure);
        });
    }

    private void addUser(String uid, User user,
                         Consumer<Void> onSuccess, Consumer<Exception> onFailure) {
        DocumentReference documentRef = db
                .collection(EUserField.COLLECTION_NAME.getName())
                .document(uid);

        documentRef.set(user)
                .addOnCompleteListener(t -> onSuccess.accept(null))
                .addOnFailureListener(onFailure::accept);
    }

    @Override
    public void signIn(SignInRequest signInRequest, Consumer<Void> onSuccess, Consumer<Exception> onFailure) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onSuccess.accept(null);
            } else {
                onFailure.accept(task.getException());
            }
        });
    }

    @Override
    public void signInOrSignUpWithGoogle(String idToken, Consumer<Void> onSuccess, Consumer<Exception> onFailure) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    FirebaseUser curUser = auth.getCurrentUser();
                    if (!task.isSuccessful() || curUser == null) {
                        onFailure.accept(task.getException());
                        return;
                    }

                    checkUserExitsByEmail(curUser.getEmail())
                            .addOnSuccessListener(exits -> {
                                if (exits) {
                                    onSuccess.accept(null);
                                    return;
                                }

                                String uid = curUser.getUid();
                                User user = new User(curUser.getEmail());
                                addUser(uid, user, onSuccess, e -> {
                                    onFailure.accept(e);
                                    curUser.delete();
                                });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching user data: " + e);
                            });

                });
    }

    @Override
    public void signInOrSignUpWithGithub(Activity activity, String email,
                                         Consumer<AuthResult> onSuccess,
                                         Consumer<Exception> onFailure) {
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
                    .addOnSuccessListener(onSuccess::accept)
                    .addOnFailureListener(onFailure::accept);
        } else {
            auth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser curUser = authResult.getUser();

                        checkUserExitsByEmail(curUser.getEmail())
                                .addOnSuccessListener(exits -> {
                                    if (exits) {
                                        onSuccess.accept(null);
                                        return;
                                    }
                                    String uid = curUser.getUid();
                                    User user = new User(curUser.getEmail());
                                    addUser(uid, user, aVoid -> {
                                        onSuccess.accept(authResult);
                                    }, e -> {
                                        onFailure.accept(e);
                                        curUser.delete();
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error fetching user data: " + e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        onFailure.accept(e);
                    });
        }
    }

    @Override
    public void updateOnlineStatus(String uid, boolean isOnline) {
        DocumentReference userRef = db.collection(EUserField.COLLECTION_NAME.getName())
                .document(uid);
        Map<String, Object> updates = new HashMap<>();
        updates.put(EUserField.IS_ONLINE.getName(), isOnline);

        userRef.update(updates);
    }

    @Override
    public String getCurrentUid() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    @Override
    public Task<User> getCurrentUser() {
        String uid = getCurrentUid();
        return getUserByUid(uid);
    }

    @Override
    public Task<User> getUserByUid(String uid) {
        return db.collection(EUserField.COLLECTION_NAME.getName())
                .document(uid)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            return document.toObject(User.class);
                        }
                    }
                    return null;
                });
    }

    @Override
    public Task<Boolean> checkUserExitsByEmail(String email) {
        CollectionReference usersRef = db.collection(EUserField.COLLECTION_NAME.getName());

        Query query = usersRef.whereEqualTo(EUserField.EMAIL.getName(), email);
        return query.get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return !task.getResult().isEmpty();
                    } else {
                        throw Objects.requireNonNull(task.getException());
                    }
                });
    }

    @Override
    public void signOut() {
        auth.signOut();
    }

    @Override
    public void sendPasswordResetEmail(String email, Consumer<Void> onSuccess, Consumer<Exception> onFailure) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(onSuccess::accept)
                .addOnFailureListener(onFailure::accept);
    }

    @Override
    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    @Override
    public Task<Void> updateBasicUser(String uid, User user) {
        CollectionReference usersRef = db.collection(EUserField.COLLECTION_NAME.getName());
        DocumentReference userRef = usersRef.document(uid);

        Map<String, Object> updates = new HashMap<>();
        updates.put(EUserField.IMAGE_URL.getName(), user.getImageUrl());
        updates.put(EUserField.FULL_NAME.getName(), user.getFullName());
        updates.put(EUserField.GENDER.getName(), user.getGender());
        updates.put(EUserField.BIRTHDAY.getName(), user.getBirthday());

        return userRef.update(updates);
    }

    @Override
    public void updatePassword(String newPassword,
                               Consumer<Void> onSuccess,
                               Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        user.updatePassword(newPassword)
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        onSuccess.accept(null);
                        Log.i(TAG, "Password updated");
                    } else {
                        onFailure.accept(updateTask.getException());
                        Log.e(TAG, "Error: Password not updated", updateTask.getException());
                    }
                });
    }

    @Override
    public void checkOldPassword(String email, String oldPassword,
                                 Consumer<Void> onSuccess,
                                 Consumer<Exception> onFailure) {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
        user.reauthenticate(credential)
                .addOnSuccessListener(result -> {
                    onSuccess.accept(null);
                })
                .addOnFailureListener(onFailure::accept);
    }

    @Override
    public void fetchSignInMethods(Consumer<List<ESignInMethod>> onSuccess,
                                   Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        List<ESignInMethod> signInMethodEnums = new ArrayList<>();
        for (UserInfo userInfo : user.getProviderData()) {
            String providerId = userInfo.getProviderId();
            ESignInMethod signInMethod = ESignInMethod.fromProviderId(providerId);
            if (signInMethod != null) {
                signInMethodEnums.add(signInMethod);
            }
        }
        onSuccess.accept(signInMethodEnums);
    }

    @Override
    public void linkCurrentUserWithCredential(AuthCredential authCredential,
                                              Consumer<Void> onSuccess,
                                              Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        // the email of current user the same with email which get from authCredential instance

        user.linkWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser linkedUser = task.getResult().getUser();

                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
//                        if (!isNewUser) {
//                            mergeUserData(user, linkedUser);
//                        }

                        onSuccess.accept(null);
                    } else {
                        Exception exception = task.getException();
                        onFailure.accept(exception);
                    }
                });
    }

    @Override
    public void linkEmailPasswordWithCurrentUser(String email,
                                                 String password,
                                                 Consumer<Void> onSuccess,
                                                 Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        if (!TextUtils.equals(user.getEmail(), email)) {
            onFailure.accept(new EmailMismatchException("Email does not match the current user's email"));
            return;
        }

        AuthCredential emailCredential = EmailAuthProvider.getCredential(email, password);

        user.linkWithCredential(emailCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onSuccess.accept(null);
                    } else {
                        Exception exception = task.getException();
                        onFailure.accept(exception);
                    }
                });
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
    public void disableCurrentUser(Consumer<Void> onSuccess, Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("disabled", true);

        String uid = user.getUid();
        DocumentReference userRef = db.collection(EUserField.COLLECTION_NAME.getName())
                .document(uid);


    }

    @Override
    public void checkCurrentEmailVerificationStatus(Consumer<Boolean> onSuccess,
                                                    Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        user.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isEmailVerified = user.isEmailVerified();
                onSuccess.accept(isEmailVerified);
            } else {
                onFailure.accept(task.getException());
            }
        });
    }

    @Override
    public void sendCurrentUserEmailVerification(Consumer<Void> onSuccess,
                                                 Consumer<Exception> onFailure) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            onFailure.accept(new UserNotAuthenticatedException("User is not authenticated"));
            return;
        }

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onSuccess.accept(null);
                    } else {
                        Exception exception = task.getException();
                        onFailure.accept(exception);
                    }
                });
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    @Override
    public void signInWithPhoneCredential(PhoneAuthCredential phoneAuthCredential,
                                          Consumer<Void> onSuccess,
                                          Consumer<Exception> onFailure) {

        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    FirebaseUser curUser = auth.getCurrentUser();
                    if (!task.isSuccessful() || curUser == null) {
                        onFailure.accept(task.getException());
                        return;
                    }

                    checkUserExitsByEmail(curUser.getEmail())
                            .addOnSuccessListener(exits -> {
                                if (exits) {
                                    onSuccess.accept(null);
                                    return;
                                }

                                String uid = curUser.getUid();
                                User user = new User(curUser.getEmail());
                                addUser(uid, user, onSuccess, e -> {
                                    onFailure.accept(e);
                                    curUser.delete();
                                });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching user data: " + e);
                            });

                });
    }

    @Override
    public void existsByPhoneNumber(String phoneNumber,
                                    Consumer<Boolean> onSuccess,
                                    Consumer<Exception> onFailure) {
        CollectionReference usersRef = db.collection(EUserField.COLLECTION_NAME.getName());
        Query query = usersRef.whereEqualTo(EUserField.PHONE_NUMBER.getName(), phoneNumber)
                .limit(1);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean exists = !task.getResult().isEmpty();
                onSuccess.accept(exists);
            } else {
                Exception exception = task.getException();
                onFailure.accept(exception);
            }
        });
    }
}