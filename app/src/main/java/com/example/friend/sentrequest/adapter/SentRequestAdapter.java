package com.example.friend.sentrequest.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding.ItemSentRequestBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.SentRequestViewHolder> {

    private List<FriendRequestView> items;
    private SentRequestListener listener;

    public SentRequestAdapter(List<FriendRequestView> items, SentRequestListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<FriendRequestView> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSentRequestBinding binding = ItemSentRequestBinding
                .inflate(layoutInflater, parent, false);
        return new SentRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SentRequestViewHolder holder, int position) {
        FriendRequestView friendRequestView = items.get(position);
        holder.bind(position, friendRequestView);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }


    public class SentRequestViewHolder extends RecyclerView.ViewHolder {
        private final ItemSentRequestBinding binding;

        public SentRequestViewHolder(@NonNull ItemSentRequestBinding binding) {
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
