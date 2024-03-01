package com.example.friend.myfriend.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding.ItemFriendBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<FriendRequestView> items;
    private FriendListener listener;

    public FriendAdapter(List<FriendRequestView> items, FriendListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<FriendRequestView> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFriendBinding binding = ItemFriendBinding
                .inflate(layoutInflater, parent, false);
        return new FriendViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendRequestView requestView = items.get(position);
        holder.bind(position, requestView);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendBinding binding;

        public FriendViewHolder(@NonNull ItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position, FriendRequestView friendRequestView) {
            binding.setRequest(friendRequestView);
            binding.setListener(listener);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
