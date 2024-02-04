package com.example.user;

import android.app.Activity;
import android.util.Log;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
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

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthServiceImpl() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void signUp(SignUpRequest signUpRequest,
                       Consumer<Void> onSuccess,
                       Consumer<Exception> onFailure) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
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

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    FirebaseUser curUser = mAuth.getCurrentUser();
                    if (!task.isSuccessful() || curUser == null) {
                        onFailure.accept(task.getException());
                        return;
                    }

                    checkUserExitsByEmail(curUser.getEmail())
                            .addOnSuccessListener(exits -> {
                                if (!exits) {
                                    String uid = curUser.getUid();
                                    User user = new User(curUser.getEmail());
                                    addUser(uid, user, onSuccess, e -> {
                                        onFailure.accept(e);
                                        curUser.delete();
                                    });
                                }
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

        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(onSuccess::accept)
                    .addOnFailureListener(onFailure::accept);
        } else {
            mAuth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser curUser = authResult.getUser();

                        checkUserExitsByEmail(curUser.getEmail())
                                .addOnSuccessListener(exits -> {
                                    if (!exits) {
                                        String uid = curUser.getUid();
                                        User user = new User(curUser.getEmail());
                                        addUser(uid, user, aVoid -> {
                                            onSuccess.accept(authResult);
                                        }, e -> {
                                            onFailure.accept(e);
                                            curUser.delete();
                                        });
                                    }
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
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
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
        mAuth.signOut();
    }

    @Override
    public void sendPasswordResetEmail(String email, Consumer<Void> onSuccess, Consumer<Exception> onFailure) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(onSuccess::accept)
                .addOnFailureListener(onFailure::accept);
    }

    @Override
    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
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
}