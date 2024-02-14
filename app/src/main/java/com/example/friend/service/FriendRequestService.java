package com.example.friend.service;

import com.example.friend.FriendRequest;
import com.example.friend.adapter.FriendRequestView;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface FriendRequestService {
    Task<List<FriendRequestView>> getPendingFriendRequestsBySenderId(String uid);
    Task<Void> addFriendRequest(FriendRequest request);
    Task<Void> updateFriendRequestStatus(String friendRequestId, FriendRequest.EStatus status);
}
