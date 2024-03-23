package com.example.chat;

import static com.example.infrastructure.Utils.encodeImage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.R;
import com.example.chat.image.ImageActivity;
import com.example.chat.message.repos.MessageRepos;
import com.example.chat.message.repos.MessageReposImpl;
import com.example.databinding.ActivityChatBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends BaseActivity<ChatViewModel,ActivityChatBinding> {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatAdapter chatAdapter;
    private PreferenceManagerRepos preferenceManager;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;

    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected Class<ChatViewModel> getViewModelClass() {
        return ChatViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        MessageRepos messageRepos = new MessageReposImpl();
        preferenceManager = new PreferenceManagerRepos(getApplicationContext());
        return new ChatViewModelFactory(authRepos, userRepos, messageRepos, preferenceManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatAdapter = new ChatAdapter(
                new ArrayList<>(),
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
                intent.putExtra(Utils.KEY_IMAGE_CLICKED_URL, imageClickedUrl);
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
                    Log.d(TAG, "Number of items selected: " + uris.size());
                } else {
                    Log.d(TAG, "No media selected");
                }
            });
        } catch (Exception e){
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private String handleSelectedMedia(Uri uri) {
        if (isImage(uri)) {
            try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream);
                return encodeImage(bitmapImage);
            } catch (IOException e) {
                Log.e(TAG, "Error handling image: ", e);
            }
        } else if (isVideo(uri)) {
            Log.d(TAG, "Video selected: " + uri.toString());
            Log.d(TAG, "Unsupported media type");
        } else {
            Log.d(TAG, "Unsupported media type");
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