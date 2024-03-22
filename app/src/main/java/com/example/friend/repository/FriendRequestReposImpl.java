package com.example.friend.repository;

import android.util.Log;

import com.example.friend.EFriendRequestField;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestNotFoundException;
import com.example.friend.FriendRequestView;
import com.example.friend.profileviewer.EFriendshipStatus;
import com.example.infrastructure.Utils;
import com.example.user.User;
import com.example.user.repository.UserRepos;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FriendRequestReposImpl implements FriendRequestRepos {

    private static final String TAG = FriendRequestReposImpl.class.getSimpleName();

    private final UserRepos userRepos;
    private final FirebaseFirestore db;

    public FriendRequestReposImpl(UserRepos userRepos) {
        this.userRepos = userRepos;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsBySenderId(String senderId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        CompletableFuture<List<FriendRequestView>> future = new CompletableFuture<>();

        friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), senderId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.PENDING)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<FriendRequest> friendRequests =
                            convertQueryDocumentSnapshotsToFriendRequests(documentSnapshots);

                    convertModelsToRecipientModelViews(friendRequests)
                            .addOnSuccessListener(friendRequestViews -> {
                                future.complete(friendRequestViews);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error converting FriendRequests to FriendRequestViews: " + e.getMessage(), e);
                                future.completeExceptionally(e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting friend requests by sender ID: " + e.getMessage(), e);
                    future.completeExceptionally(e);
                });

        return future;
    }

    @Override
    public Task<List<FriendRequestView>> getPendingFriendRequestsByRecipientId(String recipientId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        TaskCompletionSource<List<FriendRequestView>> taskCompletionSource = new TaskCompletionSource<>();

        friendRequestsRef
                .whereEqualTo(EFriendRequestField.RECIPIENT_ID.getName(), recipientId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.PENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<FriendRequest> friendRequests =
                            convertQueryDocumentSnapshotsToFriendRequests(querySnapshot);

                    Task<List<FriendRequestView>> conversionTask = convertModelsToSenderModelViews(friendRequests);
                    conversionTask.addOnSuccessListener(friendRequestViews -> {
                        taskCompletionSource.setResult(friendRequestViews);
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Error converting FriendRequests to FriendRequestViews: " + e.getMessage(), e);
                        taskCompletionSource.setException(e);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting friend requests by sender ID: " + e.getMessage(), e);
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }

    private List<FriendRequest> convertQueryDocumentSnapshotsToFriendRequests(
            QuerySnapshot documentSnapshots
    ) {
        List<FriendRequest> friendRequests = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshot : documentSnapshots) {
            FriendRequest friendRequest = convertDocumentToModel(queryDocumentSnapshot);
            if (friendRequest != null) {
                friendRequests.add(friendRequest);
            }
        }
        return friendRequests;
    }

    private FriendRequest convertDocumentToModel(DocumentSnapshot documentSnapshot) {
        try {
            String id = documentSnapshot.getId();
            String senderId = documentSnapshot.getString(EFriendRequestField.SENDER_ID.getName());
            String recipientId = documentSnapshot.getString(EFriendRequestField.RECIPIENT_ID.getName());
            String statusStr = documentSnapshot.getString(EFriendRequestField.STATUS.getName());
            FriendRequest.EStatus status = FriendRequest.EStatus.valueOf(statusStr);
            Date createdTime = documentSnapshot.getDate(EFriendRequestField.CREATED_TIME.getName());
            return new FriendRequest(id, senderId, recipientId, status, createdTime);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            return null;
        }
    }


    @Override
    public Task<FriendRequest.EStatus> getFriendRequestStatus(String senderId, String recipientId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        Query query = friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), senderId)
                .whereEqualTo(EFriendRequestField.RECIPIENT_ID.getName(), recipientId);

        return query
            .get()
            .continueWith(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot == null || querySnapshot.isEmpty()) {
                        return FriendRequest.EStatus.NOT_FOUND;
                    }

                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String statusStr = documentSnapshot.getString(EFriendRequestField.STATUS.getName());
                    return FriendRequest.EStatus.valueOf(statusStr);
                } else {
                    Exception exception = task.getException();
                    Log.e(TAG, "Error getting friend request status: " + exception.getMessage(), exception);
                    return FriendRequest.EStatus.NOT_FOUND;
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

    @Override
    public Task<FriendRequest> getFriendRequest(String friendRequestId) {
        if (Utils.isEmpty(friendRequestId)) {
            return Tasks.forException(new IllegalArgumentException("Invalid friendRequestId"));
        }

        return getFriendRequestsRef()
                .document(friendRequestId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        FriendRequest friendRequest = convertDocumentToModel(document);
                        if (friendRequest == null) {
                            throw new FriendRequestNotFoundException("Could not find any request with id=" + friendRequestId);
                        }
                        return friendRequest;
                    } else {
                        throw task.getException();
                    }
                });
    }

    @Override
    public Task<FriendRequest> getFriendRequest(String senderId, String recipientId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();
        return friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), senderId)
                .whereEqualTo(EFriendRequestField.RECIPIENT_ID.getName(), recipientId)
                .limit(1)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if (documentSnapshots.isEmpty()) {
                            return null;
                        }
                        DocumentSnapshot documentSnapshot = documentSnapshots.get(0);
                        return convertDocumentToModel(documentSnapshot);
                    } else {
                        throw task.getException();
                    }
                });
    }

    @Override
    public Task<List<FriendRequestView>> getAcceptedFriendRequests(String userId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        Query senderQuery = friendRequestsRef
                .whereEqualTo(EFriendRequestField.SENDER_ID.getName(), userId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.ACCEPTED);
        Query recipientQuery = friendRequestsRef
                .whereEqualTo(EFriendRequestField.RECIPIENT_ID.getName(), userId)
                .whereEqualTo(EFriendRequestField.STATUS.getName(), FriendRequest.EStatus.ACCEPTED);

        Task<QuerySnapshot> senderTask = senderQuery.get();
        Task<QuerySnapshot> recipientTask = recipientQuery.get();
        TaskCompletionSource<List<FriendRequestView>> taskCompletionSource = new TaskCompletionSource<>();

        Tasks.whenAllSuccess(senderTask, recipientTask)
                .addOnSuccessListener(objects -> {
                    List<FriendRequest> friendRequests = new ArrayList<>();
                    for (Object object : objects) {
                        if (object instanceof QuerySnapshot) {
                            QuerySnapshot querySnapshot = (QuerySnapshot) object;
                            List<FriendRequest> fetchedFriendRequests =
                                    convertQueryDocumentSnapshotsToFriendRequests(querySnapshot);
                            friendRequests.addAll(fetchedFriendRequests);
                        }
                    }
                    Task<List<FriendRequestView>> conversionTask = convertModelsToRecipientModelViews(friendRequests);
                    conversionTask.addOnSuccessListener(friendRequestViews -> {
                        taskCompletionSource.setResult(friendRequestViews);
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Error converting FriendRequests to FriendRequestViews: " + e.getMessage(), e);
                        taskCompletionSource.setException(e);
                    });
                });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<List<FriendRequestView>> getRecommendedFriends(String userId) {
        TaskCompletionSource<List<FriendRequestView>> taskCompletionSource = new TaskCompletionSource<>();

        userRepos.getAll()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        List<User> users = task.getResult();
                        List<Task<EFriendshipStatus>> tasks = new ArrayList<>();
                        List<FriendRequestView> friendRequestViews = new ArrayList<>();

                        users.removeIf(u -> u.getId().equals(userId));

                        for (User potentialFriend : users) {
                            String potentialFriendId = potentialFriend.getId();
                            Task<EFriendshipStatus> firstTask = getEFriendshipStatus(userId, potentialFriendId)
                                    .addOnSuccessListener(firstStatus -> {
                                        if (firstStatus == EFriendshipStatus.NOT_FOUND
                                                || firstStatus == EFriendshipStatus.NOT_FRIEND) {
                                            FriendRequestView friendRequestView =
                                                    convertUserToFriendRequestView(userId, potentialFriend);
                                            friendRequestViews.add(friendRequestView);
                                        }
                                    });

                            tasks.add(firstTask);
                        }

                        return Tasks.whenAllComplete(tasks)
                                .continueWith(task1 -> friendRequestViews);
                    } else {
                        throw task.getException();
                    }
                })
                .addOnSuccessListener(taskCompletionSource::setResult)
                .addOnFailureListener(taskCompletionSource::setException);

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Void> delete(String friendRequestId) {
        CollectionReference friendRequestsRef = getFriendRequestsRef();

        return friendRequestsRef
                .document(friendRequestId)
                .delete();
    }

    private Task<List<FriendRequestView>> convertModelsToSenderModelViews(List<FriendRequest> friendRequests) {
        List<Task<FriendRequestView>> conversionTasks = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            conversionTasks.add(convertModelToSenderModelView(friendRequest));
        }
        return Tasks.whenAllSuccess(conversionTasks);
    }

    private Task<FriendRequestView> convertModelToSenderModelView(@NotNull FriendRequest friendRequest) {
        String senderId = friendRequest.getSenderId();
        String recipientId = friendRequest.getRecipientId();

        TaskCompletionSource<FriendRequestView> taskCompletionSource = new TaskCompletionSource<>();

        userRepos.getUserByUid(senderId)
                .addOnSuccessListener(sender -> {
                    String senderImgUrl = sender.getImageUrl();
                    String senderName = sender.getFullName();

                    int mutualFriends = getNumberOfMutualFriends(senderId, recipientId);
                    FriendRequestView friendRequestView = new FriendRequestView(friendRequest,
                            senderImgUrl, senderName, mutualFriends, false);
                    taskCompletionSource.setResult(friendRequestView);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user information: " + e.getMessage(), e);
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }

    private Task<List<FriendRequestView>> convertModelsToRecipientModelViews(List<FriendRequest> friendRequests) {
        List<Task<FriendRequestView>> conversionTasks = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            conversionTasks.add(convertModelToRecipientModelView(friendRequest));
        }
        return Tasks.whenAllSuccess(conversionTasks);
    }

    private Task<FriendRequestView> convertModelToRecipientModelView(@NotNull FriendRequest friendRequest) {
        String senderId = friendRequest.getSenderId();
        String recipientId = friendRequest.getRecipientId();

        TaskCompletionSource<FriendRequestView> taskCompletionSource = new TaskCompletionSource<>();

        userRepos.getUserByUid(recipientId)
                .addOnSuccessListener(recipient -> {
                    String recipientImgUrl = recipient.getImageUrl();
                    String recipientName = recipient.getFullName();

                    int mutualFriends = getNumberOfMutualFriends(senderId, recipientId);
                    FriendRequestView friendRequestView = new FriendRequestView(friendRequest,
                            recipientImgUrl, recipientName, mutualFriends, false);
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

    private FriendRequestView convertUserToFriendRequestView(String curUserId, User potentialFriend) {
        FriendRequest friendRequest = new FriendRequest(curUserId, potentialFriend.getId(), FriendRequest.EStatus.NOT_FOUND, null);
        int mutualFriends = 0;
        return new FriendRequestView(friendRequest, potentialFriend.getImageUrl(), potentialFriend.getFullName(), mutualFriends, false);
    }

    private Task<EFriendshipStatus> getEFriendshipStatus(String curUserId, String userIdToCheck) {
        Task<FriendRequest.EStatus> curUserToUserTask = getFriendRequestStatus(curUserId, userIdToCheck);
        Task<FriendRequest.EStatus> userToCurUserTask = getFriendRequestStatus(userIdToCheck, curUserId);

        return Tasks.whenAllSuccess(curUserToUserTask, userToCurUserTask)
                .continueWith(task -> {
                    FriendRequest.EStatus curUserToUserStatus = curUserToUserTask.getResult();
                    FriendRequest.EStatus userToCurUserStatus = userToCurUserTask.getResult();

                    switch (curUserToUserStatus) {
                        case PENDING:
                            return EFriendshipStatus.SENT_REQUEST;
                        case ACCEPTED:
                            return EFriendshipStatus.FRIEND;
                        case REJECTED:
                            return EFriendshipStatus.NOT_FRIEND;
                    }

                    switch (userToCurUserStatus) {
                        case PENDING:
                            return EFriendshipStatus.RECEIVED_REQUEST;
                        case ACCEPTED:
                            return EFriendshipStatus.FRIEND;
                        case REJECTED:
                            return EFriendshipStatus.NOT_FRIEND;
                        default:
                            return EFriendshipStatus.NOT_FOUND;
                    }
                });
    }

    private CollectionReference getFriendRequestsRef() {
        return db.collection(EFriendRequestField.COLLECTION_NAME.getName());
    }
}
