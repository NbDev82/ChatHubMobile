package com.example.user.repository;

import com.example.user.User;
import com.google.android.gms.tasks.Task;

public interface UserRepos {
    Task<Void> addUser(String uid, User user);

    Task<Void> updateOnlineStatus(String uid, boolean isOnline);

    Task<Void> updateEmail(String uid, String email);

    Task<Void> updatePhoneNumber(String uid, String phoneNumber);

    Task<Boolean> checkUserExistsByEmail(String email);

    Task<Void> updateBasicUser(String uid, User user);

    Task<Boolean> existsByPhoneNumber(String phoneNumber);

    Task<User> getUserByUid(String uid);
}
