package com.example.friend.friendrequest.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding.ItemFriendRequestBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private List<FriendRequestView> friendRequests;
    private FriendRequestListener listener;

    public FriendRequestAdapter(List<FriendRequestView> friendRequests, FriendRequestListener listener) {
        this.friendRequests = friendRequests;
        this.listener = listener;
    }

    public void setFriendRequests(List<FriendRequestView> friendRequests) {
        this.friendRequests = friendRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFriendRequestBinding binding = ItemFriendRequestBinding
                .inflate(layoutInflater, parent, false);
        return new FriendRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequestView request = friendRequests.get(position);
        holder.bind(position, request);
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendRequestBinding binding;

        public FriendRequestViewHolder(@NonNull ItemFriendRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position, FriendRequestView request) {
            binding.setRequestView(request);
            binding.setListener(listener);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
