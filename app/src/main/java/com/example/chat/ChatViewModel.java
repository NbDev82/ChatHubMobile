package com.example.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chat.enums.Evisible;
import com.example.chat.message.Message;
import com.example.chat.message.repos.MessageRepos;
import com.example.chat.message.repos.MessageReposImpl;
import com.example.customcontrol.customalertdialog.AlertDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.PreferenceManager;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatViewModel extends BaseViewModel {

    private static final String TAG = ChatViewModel.class.getSimpleName();

    private final UserRepos userRepos;
    private final MessageRepos messageRepos;
    private final AuthRepos authRepos;

    private final MutableLiveData<String> curSenderUid = new MutableLiveData<>("");
    private final MutableLiveData<String> curUsername = new MutableLiveData<>("");
    private final MutableLiveData<String> curConversationId = new MutableLiveData<>(null);;
    private final MutableLiveData<String> curConversationName = new MutableLiveData<>("");
    private final MutableLiveData<String> curReceivedUid = new MutableLiveData<>("");
    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> messageInput = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isReceiverAvailable = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> openCustomAlertDialog = new MutableLiveData<>();

    public LiveData<AlertDialogModel> getOpenCustomAlertDialog() {
        return openCustomAlertDialog;
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<String> getCurUsername() {
        return curUsername;
    }

    public LiveData<String> getCurSenderUid() {
        return curSenderUid;
    }

    public LiveData<String> getCurReceivedUid() {
        return curReceivedUid;
    }

    public MutableLiveData<String> getCurConversationId() {
        return curConversationId;
    }

    public MutableLiveData<String> getCurConversationName() {
        return curConversationName;
    }

    public MutableLiveData<String> getMessageInput() {
        return messageInput;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }


    public LiveData<Boolean> getIsReceiverAvailable() {
        return isReceiverAvailable;
    }

    public ChatViewModel(PreferenceManager preferenceManager, UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;

        messageRepos = new MessageReposImpl();

        this.authRepos.getCurrentUser()
                .addOnSuccessListener(user -> {
                    if (user != null) {
                        curUsername.postValue(user.getFullName());
                        curSenderUid.postValue(user.getId());

                        String conversationId = preferenceManager.getString(Utils.KEY_CONVERSATION_ID);
                        curConversationId.postValue(conversationId);

                        String receivedUid = preferenceManager.getString(Utils.KEY_RECEIVER_ID);
                        curReceivedUid.postValue(receivedUid);
                    } else {
                        Log.d(TAG, "Unauthorize: ");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: " + e);
                });
    }

    private void openConversationNotFoundDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Conversation Not Found")
                .setMessage("The conversation you are trying to access was not found! Click OK to quit!")
                .setPositiveButton("Ok", aVoid -> {
                    navigateBack();
                })
                .build();
        openCustomAlertDialog.postValue(model);
    }

    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void onSendBtnClick() {
        Message message = new Message();

        message.setMessage(messageInput.getValue());
        message.setSenderId(curSenderUid.getValue());
        message.setConversationId(curConversationId.getValue());
        message.setSendingTime(formatLocalDateTime(LocalDateTime.now()));
        message.setVisibility(Evisible.ACTIVE);

        messageRepos.sendMessage(message);
        messageInput.postValue("");

        Log.i(TAG, "Send button clicked");
    }

    private String formatLocalDateTime(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return formatter.format(now);
    }

    public void listenMessages() {
        messageRepos.listenMessages(
                curConversationId.getValue(),
                eventListener);

        messageRepos.listenMessages(
                curConversationId.getValue(),
                eventConversationListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            List<Message> newMessages = new ArrayList<>();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Message chatMessage = new Message();
                    chatMessage.convertDocumentChangeToModel(documentChange);
                    newMessages.add(chatMessage);
                }
            }

            List<Message> currentMessages = messages.getValue();
            if (currentMessages == null)
                currentMessages = new ArrayList<>();
            currentMessages.addAll(newMessages);
            Collections.sort(currentMessages, Comparator.comparing(Message::getDateObject));

            messages.postValue(currentMessages);
        }
    };

    private final EventListener<QuerySnapshot> eventConversationListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    curConversationName.postValue(documentChange.getDocument().getString(Utils.KEY_CONVERSATION_NAME));
                }
            }
        }
    };
}
