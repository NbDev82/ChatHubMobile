package com.example.friend.friendrequest;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.friend.FriendRequest;
import com.example.friend.friendrequest.adapter.FriendRequestListener;
import com.example.friend.friendrequest.adapter.FriendRequestView;
import com.example.friend.service.FriendRequestService;
import com.example.friend.service.FriendRequestServiceImpl;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsViewModel extends BaseViewModel implements FriendRequestListener {

    private static final String TAG = FriendRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isRequestsLoading = new MutableLiveData<>();
    private final AuthService authService = new AuthServiceImpl();
    private final FriendRequestService friendRequestService = new FriendRequestServiceImpl(authService);

    public MutableLiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    public LiveData<List<FriendRequestView>> getFriendRequests() {
        return friendRequests;
    }

    public LiveData<Boolean> getIsRequestsLoading() {
        return isRequestsLoading;
    }

    public FriendRequestsViewModel() {
    }

    public void loadFriendRequests() {
        List<FriendRequestView> views = new ArrayList<>();
        views.add(new FriendRequestView("123", null, "Van An",
                10, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1233", null, "Van Bae",
                11, "3m", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        views.add(new FriendRequestView("1235", null, "Hoang Long",
                5, "3w", FriendRequest.EStatus.PENDING));
        this.friendRequests.postValue(views);

//        this.isRequestsLoading.postValue(true);
//        String uid = authService.getCurrentUid();
//        friendRequestService.getPendingFriendRequestsBySenderId(uid)
//                .addOnSuccessListener(friendRequests -> {
//                    this.isRequestsLoading.postValue(false);
//                    this.friendRequests.postValue(friendRequests);
//                })
//                .addOnFailureListener(e -> {
//                    this.isRequestsLoading.postValue(false);
//                    Log.e(TAG, "Error: " + e.getMessage(), e);
//                });
    }

    public void navigateToHome() {
        this.navigateToHome.postValue(true);
    }

    @Override
    public void onItemClick(int position) {
        FriendRequestView request = this.friendRequests.getValue().get(position);
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, request.getId());
        this.navigateToProfileViewer.postValue(data);
    }

    @Override
    public void onAcceptClick(int position) {
        successToastMessage.postValue(String.valueOf(position));
//        FriendRequestView request = this.friendRequests.getValue().get(position);
//        friendRequestService
//                .updateFriendRequestStatus(request.getId(), FriendRequest.EStatus.ACCEPTED)
//                .addOnSuccessListener(aVoid -> {
//
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error: " + e.getMessage(), e);
//                });
    }

    @Override
    public void onRejectClick(int position) {
        successToastMessage.postValue(String.valueOf(position));
//        FriendRequestView request = this.friendRequests.getValue().get(position);
//        friendRequestService
//                .updateFriendRequestStatus(request.getId(), FriendRequest.EStatus.REJECTED)
//                .addOnSuccessListener(aVoid -> {
//
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error: " + e.getMessage(), e);
//                });
    }
}
