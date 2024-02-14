package com.example.friend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.friend.adapter.FriendRequestListener;
import com.example.friend.adapter.FriendRequestView;
import com.example.friend.service.FriendRequestService;
import com.example.friend.service.FriendRequestServiceImpl;
import com.example.infrastructure.BaseViewModel;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsViewModel extends BaseViewModel implements FriendRequestListener {

    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isRequestsLoading = new MutableLiveData<>();
    private final AuthService authService = new AuthServiceImpl();
    private final FriendRequestService friendRequestService = new FriendRequestServiceImpl(authService);

    public MutableLiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
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
    }

    public void navigateToHome() {
        this.navigateToHome.postValue(true);
    }

    @Override
    public void onItemClick(FriendRequestView request) {
        errorToastMessage.postValue("Without implementation");
    }

    @Override
    public void onAcceptClick(FriendRequestView request) {
        friendRequestService.updateFriendRequestStatus(request.getId(), FriendRequest.EStatus.ACCEPTED);
    }

    @Override
    public void onRejectClick(FriendRequestView request) {
        friendRequestService.updateFriendRequestStatus(request.getId(), FriendRequest.EStatus.REJECTED);
    }
}
