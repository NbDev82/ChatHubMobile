package com.example.user;

import android.content.Context;
import android.util.Log;

import com.example.user.login.GoogleSignInActivity;
import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
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
                        if (document.exists()) {
                            return document.toObject(User.class);
                        }
                    }
                    return null;
                });
    }

    @Override
    public Task<Boolean> checkUserExitsByEmail(String email) {
        CollectionReference usersRef = db.collection(EUserField.FULL_NAME.getName());

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
    public void signOut(Context context) {
        mAuth.signOut();
    }
}
