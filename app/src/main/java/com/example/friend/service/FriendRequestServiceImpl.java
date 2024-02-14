package com.example.friend.service;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.friend.EFriendRequestField;
import com.example.friend.FriendRequest;
import com.example.friend.adapter.FriendRequestView;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendRequestServiceImpl implements FriendRequestService {

    private static final String TAG = FriendRequestServiceImpl.class.getSimpleName();

    private final FirebaseFirestore db;
    private final AuthService authService;

    public FriendRequestServiceImpl(AuthService authService) {
        db = FirebaseFirestore.getInstance();
        this.authService = authService;
    }

    @Override
    public Task<List<FriendRequestView>> getPendingFriendRequestsBySenderId(String senderId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        TaskCompletionSource<List<FriendRequestView>> taskCompletionSource = new TaskCompletionSource<>();

        friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), senderId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.PENDING)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<FriendRequestView> friendRequestViews =
                            convertQueryDocumentSnapshotsToFriendRequests(documentSnapshots);
                    taskCompletionSource.setResult(friendRequestViews);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting friend requests by sender ID: " + e.getMessage(), e);
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }

    private List<FriendRequestView> convertQueryDocumentSnapshotsToFriendRequests(
            QuerySnapshot documentSnapshots
    ) {
        List<FriendRequestView> friendRequestViews = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshot : documentSnapshots) {
            FriendRequest friendRequest = convertDocumentToModel(queryDocumentSnapshot);
            if (friendRequest != null) {
                convertModelToModelView(friendRequest)
                        .addOnSuccessListener(friendRequestViews::add)
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error converting FriendRequest to FriendRequestView: " +
                                    e.getMessage(), e);
                        });
            }
        }
        return friendRequestViews;
    }

    private FriendRequest convertDocumentToModel(QueryDocumentSnapshot queryDocumentSnapshot) {
        try {
            String id = queryDocumentSnapshot.getId();
            String senderId = queryDocumentSnapshot.getString(EFriendRequestField.SENDER_ID.getName());
            String recipientId = queryDocumentSnapshot.getString(EFriendRequestField.RECIPIENT_ID.getName());
            String statusStr = queryDocumentSnapshot.getString(EFriendRequestField.STATUS.getName());
            FriendRequest.EStatus status = FriendRequest.EStatus.valueOf(statusStr);
            Date createdTime = queryDocumentSnapshot.getDate(EFriendRequestField.CREATED_TIME.getName());
            return new FriendRequest(id, senderId, recipientId, status, createdTime);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            return null;
        }
    }

    private Task<FriendRequestView> convertModelToModelView(@NotNull FriendRequest friendRequest) {
        String friendRequestId = friendRequest.getId();
        String senderId = friendRequest.getSenderId();
        String recipientId = friendRequest.getRecipientId();
        FriendRequest.EStatus status = friendRequest.getStatus();
        Date createdTime = friendRequest.getCreatedTime();

        TaskCompletionSource<FriendRequestView> taskCompletionSource = new TaskCompletionSource<>();

        authService.getUserByUid(recipientId)
                .addOnSuccessListener(user -> {
                    String recipientAvatarStr = user.getImageUrl();
                    String recipientName = user.getFullName();

                    Bitmap recipientAvatar = Utils.decodeImage(recipientAvatarStr);
                    int mutualFriends = getNumberOfMutualFriends(senderId, recipientId);
                    String timeAgo = Utils.calculateTimeAgo(createdTime);
                    FriendRequestView friendRequestView = new FriendRequestView(friendRequestId,
                            recipientAvatar, recipientName, mutualFriends, timeAgo, status);
                    taskCompletionSource.setResult(friendRequestView);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user information: " + e.getMessage(), e);
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }

    private int getNumberOfMutualFriends(String firstUserId, String secondUserId) {
        return 0;
    }

    private Task<Boolean> hasFriendship(String firstUserId, String secondUserId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        Query firstUserQuery = friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), firstUserId)
                .whereEqualTo(EFriendRequestField.RECIPIENT_ID.getName(), secondUserId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.ACCEPTED);

        Query secondUserQuery = friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), secondUserId)
                .whereEqualTo(EFriendRequestField.RECIPIENT_ID.getName(), firstUserId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.ACCEPTED);

        return Tasks.whenAllSuccess(firstUserQuery.get(), secondUserQuery.get())
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot firstUserResult = (QuerySnapshot) task.getResult().get(0);
                        QuerySnapshot secondUserResult = (QuerySnapshot) task.getResult().get(1);

                        return !firstUserResult.isEmpty() || !secondUserResult.isEmpty();
                    } else {
                        Exception exception = task.getException();
                        Log.e(TAG, "Error checking if users are friends: " +
                                exception.getMessage(), exception);
                        return false;
                    }
                });
    }

    @Override
    public Task<Void> addFriendRequest(FriendRequest request) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        Map<String, Object> data = new HashMap<>();
        data.put(EFriendRequestField.SENDER_ID.getName(), request.getSenderId());
        data.put(EFriendRequestField.RECIPIENT_ID.getName(), request.getRecipientId());
        data.put(EFriendRequestField.STATUS.getName(), request.getStatus());
        data.put(EFriendRequestField.CREATED_TIME.getName(), request.getCreatedTime());

        return friendRequestsRef
                .document()
                .set(data);
    }

    @Override
    public Task<Void> updateFriendRequestStatus(String friendRequestId, FriendRequest.EStatus status) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();
        DocumentReference friendRequestRef = friendRequestsRef.document(friendRequestId);

        return friendRequestRef
                .update(EFriendRequestField.STATUS.getName(), status);
    }

    private CollectionReference getFriendRequestsRef() {
        return db.collection(EFriendRequestField.COLLECTION_NAME.getName());
    }
}
