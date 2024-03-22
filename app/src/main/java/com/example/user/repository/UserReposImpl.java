package com.example.user.repository;

import android.util.Log;

import com.example.user.EGender;
import com.example.user.EUserField;
import com.example.user.User;
import com.example.user.UserNotFoundException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UserReposImpl implements UserRepos {

    private static final String TAG = UserReposImpl.class.getSimpleName();

    private FirebaseFirestore db;

    public UserReposImpl() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public CompletableFuture<Void> addUser(String uid, User user) {
        DocumentReference userRef = getUserRef().document(uid);
        CompletableFuture<Void> future = new CompletableFuture<>();

        Map<String, Object> data = new HashMap<>();
        data.put(EUserField.FULL_NAME.getName(), user.getFullName());
        data.put(EUserField.EMAIL.getName(), user.getEmail());
        data.put(EUserField.PHONE_NUMBER.getName(), user.getPhoneNumber());
        data.put(EUserField.GENDER.getName(), user.getGender());
        data.put(EUserField.BIRTHDAY.getName(), user.getBirthday());
        data.put(EUserField.IMAGE_URL.getName(), user.getImageUrl());
        data.put(EUserField.IS_ONLINE.getName(), user.isOnline());
        data.put(EUserField.IS_DELETED.getName(), user.isDeleted());

        userRef.set(data)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> updateOnlineStatus(String uid, boolean isOnline) {
        DocumentReference userRef = getUserRef().document(uid);
        CompletableFuture<Void> future = new CompletableFuture<>();

        userRef.update(EUserField.IS_ONLINE.getName(), isOnline)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> updateEmail(String uid, String email) {
        DocumentReference userRef = getUserRef().document(uid);
        CompletableFuture<Void> future = new CompletableFuture<>();

        userRef.update(EUserField.EMAIL.getName(), email)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> updatePhoneNumber(String uid, String phoneNumber) {
        DocumentReference userRef = getUserRef().document(uid);
        CompletableFuture<Void> future = new CompletableFuture<>();

        userRef.update(EUserField.PHONE_NUMBER.getName(), phoneNumber)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Boolean> checkUserExistsByEmail(String email) {
        CollectionReference usersRef = getUserRef();
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Query query = usersRef.whereEqualTo(EUserField.EMAIL.getName(), email);
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    boolean exists = !querySnapshot.isEmpty();
                    future.complete(exists);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> updateBasicUser(String uid, User user) {
        CollectionReference usersRef = getUserRef();
        DocumentReference userRef = usersRef.document(uid);
        CompletableFuture<Void> future = new CompletableFuture<>();

        Map<String, Object> updates = new HashMap<>();
        updates.put(EUserField.IMAGE_URL.getName(), user.getImageUrl());
        updates.put(EUserField.FULL_NAME.getName(), user.getFullName());
        updates.put(EUserField.GENDER.getName(), user.getGender());
        updates.put(EUserField.BIRTHDAY.getName(), user.getBirthday());

        userRef.update(updates)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Boolean> existsByPhoneNumber(String phoneNumber) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        CollectionReference usersRef = getUserRef();
        Query query = usersRef
                .whereEqualTo(EUserField.PHONE_NUMBER.getName(), phoneNumber)
                .limit(1);
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    boolean exists = !querySnapshot.isEmpty();
                    future.complete(exists);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<User> getUserByUid(String uid) {
        if (uid == null || uid.isEmpty()) {
            CompletableFuture<User> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalArgumentException("Invalid UID"));
            return future;
        }

        CompletableFuture<User> future = new CompletableFuture<>();
        getUserRef().document(uid)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = convertDocumentToModel(document);
                        future.complete(user);
                    } else {
                        future.completeExceptionally(new UserNotFoundException("Could not find any user with uid=" + uid));
                    }
                } else {
                    future.completeExceptionally(task.getException());
                }
            });

        return future;
    }

    @Override
    public CompletableFuture<List<User>> getAll() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        getUserRef().get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        User user = convertDocumentToModel(documentSnapshot);
                        users.add(user);
                    }
                    future.complete(users);
                } else {
                    future.completeExceptionally(task.getException());
                }
            });

        return future;
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

    private CollectionReference getUserRef() {
        return db.collection(EUserField.COLLECTION_NAME.getName());
    }
}
