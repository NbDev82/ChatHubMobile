package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.example.R;
import com.example.databinding.ActivityChatBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.PreferenceManager;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.User;
import com.example.chat.message.Message;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private ActivityChatBinding binding;
    private ChatViewModel viewModel;
    private User receiverUser;
    private List<Message> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(getApplicationContext());
        navigationManager = new NavigationManagerImpl(this);

        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_chat);

        chatMessages = new ArrayList<>();

        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedStringUrl(receiverUser.getImageUrl()),
                preferenceManager.getString(Utils.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);

        viewModel = new ChatViewModel(preferenceManager, userRepos, authRepos);
        binding.setViewModel(viewModel);

        setObservers();
        setListeners();

        binding.setLifecycleOwner(this);
    }

    private void setListeners() {
        viewModel.listenMessages();
    }

    private void setObservers() {
        viewModel.getMessages().observe(this, messages -> {
            int count = messages.size();
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.setMessages(messages);
                chatAdapter.notifyItemRangeInserted(count, count);
                binding.chatRecyclerView.smoothScrollToPosition(count - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        });

        viewModel.getNavigateBack().observe(this, isNavigateBack -> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });
    }

    private Bitmap getBitmapFromEncodedStringUrl(String encodedImageUrl) {
        if (encodedImageUrl != null) {
            return com.example.infrastructure.Utils.decodeImage(encodedImageUrl);
        } else {
            return null;
        }
    }
}