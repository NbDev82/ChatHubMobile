package com.example.user.repository;

import com.example.user.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepos {
    CompletableFuture<Void> addUser(String uid, User user);

    CompletableFuture<Void> updateOnlineStatus(String uid, boolean isOnline);

    CompletableFuture<Void> updateEmail(String uid, String email);

    CompletableFuture<Void> updatePhoneNumber(String uid, String phoneNumber);

    CompletableFuture<Boolean> checkUserExistsByEmail(String email);

    CompletableFuture<Void> updateBasicUser(String uid, User user);

    CompletableFuture<Boolean> existsByPhoneNumber(String phoneNumber);

    CompletableFuture<User> getUserByUid(String uid);

    CompletableFuture<List<User>> getAll();
}
