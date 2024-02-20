package com.example.friend.friendsuggestion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding.ItemFriendRequestBinding;
import com.example.databinding.ItemFriendSuggestionBinding;
import com.example.friend.FriendRequestView;
import com.example.friend.friendrequest.adapter.FriendRequestAdapter;

import java.util.List;

public class FriendSuggestionAdapter extends RecyclerView.Adapter<FriendSuggestionAdapter.FriendSuggestionViewHolder> {

    private List<FriendRequestView> items;
    private FriendSuggestionListener listener;

    public FriendSuggestionAdapter(List<FriendRequestView> items, FriendSuggestionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<FriendRequestView> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFriendSuggestionBinding binding = ItemFriendSuggestionBinding
                .inflate(layoutInflater, parent, false);
        return new FriendSuggestionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendSuggestionViewHolder holder, int position) {
        FriendRequestView request = items.get(position);
        holder.bind(position, request);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class FriendSuggestionViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendSuggestionBinding binding;

        public FriendSuggestionViewHolder(@NonNull ItemFriendSuggestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position, FriendRequestView friendRequestView) {
            binding.setRequestView(friendRequestView);
            binding.setPosition(position);
            binding.setListener(listener);
        }
    }
}
