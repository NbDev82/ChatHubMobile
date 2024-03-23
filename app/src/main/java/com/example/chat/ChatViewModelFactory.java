package com.example.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat.message.repos.MessageRepos;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

public class ChatViewModelFactory implements ViewModelProvider.Factory{
    protected final AuthRepos authRepos;
    protected final UserRepos userRepos;
    protected final MessageRepos messageRepos;
    protected final PreferenceManagerRepos preferenceManager;
    public ChatViewModelFactory(AuthRepos authRepos,
                                UserRepos userRepos,
                                MessageRepos messageRepos,
                                PreferenceManagerRepos preferenceManager) {
        this.authRepos = authRepos;
        this.userRepos = userRepos;
        this.messageRepos = messageRepos;
        this.preferenceManager = preferenceManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(preferenceManager, userRepos, authRepos, messageRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
