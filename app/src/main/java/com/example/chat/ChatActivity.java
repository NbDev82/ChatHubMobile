package com.example.chat;

import static com.example.infrastructure.Utils.decodeImage;
import static com.example.infrastructure.Utils.encodeImage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.R;
import com.example.chat.image.ImageActivity;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private ActivityChatBinding binding;
    private ChatViewModel viewModel;
    private User receiverUser;
    private List<Message> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(getApplicationContext());
        navigationManager = new NavigationManagerImpl(this);

        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_chat);

        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);

        viewModel = new ChatViewModel(preferenceManager, userRepos, authRepos);
        binding.setViewModel(viewModel);

        chatMessages = new ArrayList<>();

        chatAdapter = new ChatAdapter(
                chatMessages,
                decodeImage(receiverUser.getImageUrl()),
                preferenceManager.getString(Utils.KEY_USER_ID),
                viewModel
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);

        setObservers();
        setListeners();
        setRegisterForActivityResult();

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

        viewModel.getMessageInput().observe(this, message -> {
            binding.chooseImage.setVisibility(message.isEmpty() ? View.VISIBLE : View.GONE);
            binding.iconSendMessage.setVisibility(message.isEmpty() ? View.GONE : View.VISIBLE);
        });

        viewModel.getIsOpenImageDialog().observe(this, isOpenImageDialog -> {
            if(isOpenImageDialog) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });

        viewModel.getNavigateBack().observe(this, isNavigateBack ->
                startActivity(new Intent(getApplicationContext(), HomeActivity.class)));

        viewModel.getIsImageClicked().observe(this, imageClickedUrl -> {
            if(!imageClickedUrl.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("imageClickedUrl", imageClickedUrl);
                startActivity(intent);
            }
        });
    }

    private void setRegisterForActivityResult() {
        try {
            pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    List<String> mediaImageUrls = new ArrayList<>();
                    for(Uri uri : uris) {
                        mediaImageUrls.add(handleSelectedMedia(uri));
                    }
                    viewModel.sendImages(mediaImageUrls);
                    Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
        } catch (Exception e){
            Log.e("PhotoPicker", Objects.requireNonNull(e.getMessage()));
        }
    }

    private String handleSelectedMedia(Uri uri) {
        if (isImage(uri)) {
            try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream);
                return encodeImage(bitmapImage);
            } catch (IOException e) {
                Log.e("MediaPicker", "Error handling image: ", e);
            }
        } else if (isVideo(uri)) {
            Log.d("MediaPicker", "Video selected: " + uri.toString());
            Log.d("MediaPicker", "Unsupported media type");
        } else {
            Log.d("MediaPicker", "Unsupported media type");
        }
        return null;
    }

    private boolean isImage(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("image/");
    }

    private boolean isVideo(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("video/");
    }
}