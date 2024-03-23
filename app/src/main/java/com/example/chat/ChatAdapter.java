package com.example.chat;

import static com.example.infrastructure.Utils.decodeImage;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.chat.listener.ImageListener;
import com.example.chat.message.Message;
import com.example.databinding.ItemContainerReceivedImageBinding;
import com.example.databinding.ItemContainerReceivedMessageBinding;
import com.example.databinding.ItemContainerSentImageBinding;
import com.example.databinding.ItemContainerSentMessageBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> messages;
    private final String senderId;
    private final ImageListener listener;

    public static final int VIEW_MESSAGE_SENT = 1;
    public static final int VIEW_IMAGE_SENT = 2;
    public static final int VIEW_MESSAGE_RECEIVED = 3;
    public static final int VIEW_IMAGE_RECEIVED = 4;

    public ChatAdapter(List<Message> messages, String senderId, ImageListener listener) {
        this.messages = messages;
        this.senderId = senderId;
        this.listener = listener;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_MESSAGE_RECEIVED: {
                ItemContainerReceivedMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_received_message, parent, false);
                return new ReceivedMessageViewHolder(binding);
            }
            case VIEW_IMAGE_RECEIVED: {
                ItemContainerReceivedImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_received_image, parent, false);
                return new ReceivedImageViewHolder(binding);
            }
            case VIEW_MESSAGE_SENT: {
                ItemContainerSentMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_sent_message, parent, false);
                return new SendMessageViewHolder(binding);
            }
            case VIEW_IMAGE_SENT: {
                ItemContainerSentImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_sent_image, parent, false);
                return new SendImageViewHolder(binding);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_MESSAGE_RECEIVED: {
                ((ReceivedMessageViewHolder) holder).setData(messages.get(position));
                break;
            }
            case VIEW_IMAGE_RECEIVED: {
                ((ReceivedImageViewHolder) holder).setData(messages.get(position), position);
                break;
            }
            case VIEW_MESSAGE_SENT: {
                ((SendMessageViewHolder) holder).setData(messages.get(position));
                break;
            }
            case VIEW_IMAGE_SENT: {
                ((SendImageViewHolder) holder).setData(messages.get(position), position);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if(message.getSenderId().equals(senderId)) {
            return message.getType().equals(Message.EType.TEXT) ? VIEW_MESSAGE_SENT : VIEW_IMAGE_SENT;
        } else {
            return message.getType().equals(Message.EType.TEXT) ? VIEW_MESSAGE_RECEIVED : VIEW_IMAGE_RECEIVED;
        }
    }

    public class SendMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;

        SendMessageViewHolder(ItemContainerSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Message message){
            binding.textMessage.setText(message.getMessage());
            binding.textDateTime.setText(message.getSendingTime());

            binding.executePendingBindings();
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            this.binding = itemContainerReceivedMessageBinding;
        }

        void setData(Message message){
            binding.imageProfile.setImageBitmap(message.getSenderImage());
            binding.textMessage.setText(message.getMessage());
            binding.userName.setText(message.getSenderName());
            binding.textDateTime.setText(message.getSendingTime());

            binding.executePendingBindings();
        }
    }

    public class ReceivedImageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerReceivedImageBinding binding;

        ReceivedImageViewHolder(ItemContainerReceivedImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Message message, int position){
            Bitmap bitmap = decodeImage(message.getMessage());
            binding.image.setImageBitmap(bitmap);
            binding.textDateTime.setText(message.getSendingTime());
            binding.userName.setText(message.getSenderName());
            binding.imageProfile.setImageBitmap(message.getSenderImage());
            binding.setListener(listener);
            binding.setPosition(position);

            binding.executePendingBindings();
        }
    }

    public class SendImageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentImageBinding binding;

        SendImageViewHolder(ItemContainerSentImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Message message, int position){
            Bitmap bitmap = decodeImage(message.getMessage());
            binding.image.setImageBitmap(bitmap);
            binding.textDateTime.setText(message.getSendingTime());
            binding.setListener(listener);
            binding.setPosition(position);

            binding.executePendingBindings();
        }
    }
}
