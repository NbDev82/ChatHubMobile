package com.example.friend.repository;

import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface FriendRequestRepos {
    Task<List<FriendRequestView>> getPendingFriendRequestsBySenderId(String senderId);
    Task<List<FriendRequestView>> getPendingFriendRequestsByRecipientId(String recipientId);
    Task<FriendRequest.EStatus> getFriendRequestStatus(String senderId, String recipientId);
    Task<Void> addFriendRequest(FriendRequest request);
    Task<Void> updateFriendRequestStatus(String friendRequestId, FriendRequest.EStatus status);
    Task<FriendRequest> getFriendRequest(String senderId, String recipientId);
    Task<List<FriendRequestView>> getAcceptedFriendRequests(String userId);
}
