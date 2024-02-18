package com.example.friend;

public class FriendRequestView {
    private FriendRequest friendRequest;
    private String imageUrl;
    private String displayName;
    private int mutualFriends;
    private boolean isLoading;

    public FriendRequestView() {
    }

    public FriendRequestView(FriendRequest friendRequest, String imageUrl,
                             String displayName, int mutualFriends, boolean isLoading) {
        this.friendRequest = friendRequest;
        this.imageUrl = imageUrl;
        this.displayName = displayName;
        this.mutualFriends = mutualFriends;
        this.isLoading = isLoading;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
