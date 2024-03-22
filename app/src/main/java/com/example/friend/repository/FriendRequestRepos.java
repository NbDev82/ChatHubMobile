package com.example.friend.repository;

import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FriendRequestRepos {
    CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsBySenderId(String senderId);

    CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsByRecipientId(String recipientId);

    CompletableFuture<Void> addFriendRequest(FriendRequest request);

    CompletableFuture<Void> updateFriendRequestStatus(String friendRequestId, FriendRequest.EStatus status);

    CompletableFuture<FriendRequest> getFriendRequest(String friendRequestId);

    CompletableFuture<List<FriendRequestView>> getAcceptedFriendRequests(String userId);

    CompletableFuture<List<FriendRequestView>> getRecommendedFriends(String userId);

    CompletableFuture<Void> delete(String friendRequestId);
}
