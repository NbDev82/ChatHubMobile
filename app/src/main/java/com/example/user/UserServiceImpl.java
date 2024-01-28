package com.example.user;

import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserServiceImpl implements UserService {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public UserServiceImpl() {
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

            DocumentReference documentRef = db
                    .collection(EUserField.COLLECTION_NAME.getName())
                    .document(uid);

            User user = new User(email, password);
            documentRef.set(user)
                    .addOnCompleteListener(t -> onSuccess.accept(null))
                    .addOnFailureListener(onFailure::accept);
        });
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
}
