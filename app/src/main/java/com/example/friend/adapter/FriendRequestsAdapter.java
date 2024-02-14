package com.example.friend.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.databinding.ItemFriendRequestBinding;

import java.util.List;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestViewHolder> {

    private List<FriendRequestView> friendRequests;
    private FriendRequestListener listener;

    public FriendRequestsAdapter(List<FriendRequestView> friendRequests, FriendRequestListener listener) {
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
        holder.bind(request);
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

        public void bind(FriendRequestView request) {
            binding.setRequest(request);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
