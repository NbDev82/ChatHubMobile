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

public class UserReposImpl implements UserRepos {

    private static final String TAG = UserReposImpl.class.getSimpleName();

    private FirebaseFirestore db;

    public UserReposImpl() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public Task<Void> addUser(String uid, User user) {
        DocumentReference userRef = getUserRef()
                .document(uid);

        Map<String, Object> data = new HashMap<>();
        data.put(EUserField.FULL_NAME.getName(), user.getFullName());
        data.put(EUserField.EMAIL.getName(), user.getEmail());
        data.put(EUserField.PHONE_NUMBER.getName(), user.getPhoneNumber());
        data.put(EUserField.GENDER.getName(), user.getGender());
        data.put(EUserField.BIRTHDAY.getName(), user.getBirthday());
        data.put(EUserField.IMAGE_URL.getName(), user.getImageUrl());
        data.put(EUserField.IS_ONLINE.getName(), user.isOnline());
        data.put(EUserField.IS_DELETED.getName(), user.isDeleted());

        return userRef.set(data);
    }

    @Override
    public Task<Void> updateOnlineStatus(String uid, boolean isOnline) {
        DocumentReference userRef = getUserRef()
                .document(uid);
        return userRef.update(EUserField.IS_ONLINE.getName(), isOnline);
    }

    @Override
    public Task<Void> updateEmail(String uid, String email) {
        DocumentReference userRef = getUserRef()
                .document(uid);
        return userRef.update(EUserField.EMAIL.getName(), email);
    }

    @Override
    public Task<Void> updatePhoneNumber(String uid, String phoneNumber) {
        DocumentReference userRef = getUserRef()
                .document(uid);
        return userRef.update(EUserField.PHONE_NUMBER.getName(), phoneNumber);
    }

    @Override
    public Task<Boolean> checkUserExistsByEmail(String email) {
        CollectionReference usersRef = getUserRef();

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
    public Task<Void> updateBasicUser(String uid, User user) {
        CollectionReference usersRef = getUserRef();
        DocumentReference userRef = usersRef.document(uid);

        Map<String, Object> updates = new HashMap<>();
        updates.put(EUserField.IMAGE_URL.getName(), user.getImageUrl());
        updates.put(EUserField.FULL_NAME.getName(), user.getFullName());
        updates.put(EUserField.GENDER.getName(), user.getGender());
        updates.put(EUserField.BIRTHDAY.getName(), user.getBirthday());

        return userRef.update(updates);
    }

    @Override
    public Task<Boolean> existsByPhoneNumber(String phoneNumber) {
        TaskCompletionSource<Boolean> source = new TaskCompletionSource<>();

        CollectionReference usersRef = getUserRef();
        Query query = usersRef.whereEqualTo(EUserField.PHONE_NUMBER.getName(), phoneNumber)
                .limit(1);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean exists = !task.getResult().isEmpty();
                source.setResult(exists);
            } else {
                Exception exception = task.getException();
                source.setException(exception);
            }
        });

        return source.getTask();
    }

    @Override
    public Task<User> getUserByUid(String uid) {
        if (uid == null || uid.isEmpty()) {
            return Tasks.forException(new IllegalArgumentException("Invalid UID"));
        }

        return getUserRef()
                .document(uid)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        User user = convertDocumentToModel(document);
                        if (user == null) {
                            throw new UserNotFoundException("Could not find any user with uid=" + uid);
                        }
                        return user;
                    } else {
                        throw task.getException();
                    }
                });
    }

    @Override
    public Task<List<User>> getAll() {
        return getUserRef()
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            User user = convertDocumentToModel(documentSnapshot);
                            users.add(user);
                        }
                        return users;
                    } else {
                        throw task.getException();
                    }
                });
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
