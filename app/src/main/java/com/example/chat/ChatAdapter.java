package com.example.chat;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.chat.message.Message;
import com.example.databinding.ItemContainerReceivedMessageBinding;
import com.example.databinding.ItemContainerSentMessageBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> messages;
    private final Bitmap receiverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<Message> messages, Bitmap receiverProfileImage, String senderId) {
        this.messages = messages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENT){
            ItemContainerSentMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_sent_message, parent, false);
            return new SendMessageViewHolder(binding);
        } else {
            ItemContainerReceivedMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_received_message, parent, false);
            return new ReceivedMessageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SendMessageViewHolder) holder).setData(messages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(messages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getSenderId().equals(senderId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    static class SendMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;

        SendMessageViewHolder(ItemContainerSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Message message){
            binding.textMessage.setText(message.getMessage());
            binding.textDateTime.setText(message.getSendingTime().toString());
            binding.executePendingBindings();
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            this.binding = itemContainerReceivedMessageBinding;
        }

        void setData(Message message, Bitmap receiverProfileImage){
            binding.textMessage.setText(message.getMessage());
            binding.textDateTime.setText(message.getSendingTime().toString());
            binding.imageProfile.setImageBitmap(receiverProfileImage);
            binding.executePendingBindings();
        }
    }
}
