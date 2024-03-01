package com.example.infrastructure;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.repository.AuthRepos;

public abstract class BaseViewModel extends ViewModel {

    protected final MutableLiveData<String> successToastMessage = new MutableLiveData<>();
    protected final MutableLiveData<String> errorToastMessage = new MutableLiveData<>();

    protected AuthRepos authRepos;

    public MutableLiveData<String> getSuccessToastMessage() {
        return successToastMessage;
    }

    public MutableLiveData<String> getErrorToastMessage() {
        return errorToastMessage;
    }

    public BaseViewModel() {
    }
}
