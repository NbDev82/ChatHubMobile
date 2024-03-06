package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityChatBinding;
import com.example.infrastructure.PreferenceManager;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.User;
import com.example.chat.message.Message;
import com.example.user.login.SignInRequest;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

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

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);

        authRepos.signInWithEmailPassword(new SignInRequest("hoangdhkt12345@gmail.com","123456789"));

        viewModel = new ChatViewModel(preferenceManager, userRepos, authRepos);
        binding.setViewModel(viewModel);

        init();

        binding.setLifecycleOwner(this);
    }

    private void init() {
        chatMessages = viewModel.getMessages().getValue();

        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedStringUrl(receiverUser.getImageUrl()),
                preferenceManager.getString(Utils.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    private Bitmap getBitmapFromEncodedStringUrl(String encodedImageUrl) {
        if (encodedImageUrl != null) {
            return com.example.infrastructure.Utils.decodeImage(encodedImageUrl);
        } else {
            return null;
        }
    }
}