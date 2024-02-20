package com.example.user.accountlink;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.customcontrol.emailpassworddialog.EmailPasswordDialogModel;
import com.example.customcontrol.inputdialogfragment.EInputType;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.customcontrol.phonecredential.PhoneCredentialDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.user.authservice.AuthService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

public class AccountLinkingViewModel extends BaseViewModel {

    private static final String TAG = AccountLinkingViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isInAppPasswordLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isInAppAdding = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGoogleAccountLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGoogleAdding = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openGoogleSignIn = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGithubAccountLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGithubAdding = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSmsAccountLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSmsAdding = new MutableLiveData<>();
    private final MutableLiveData<EmailPasswordDialogModel> openEmailPasswordDialog = new MutableLiveData<>();
    private final MutableLiveData<PhoneCredentialDialogModel> openPhoneCredentialDialog = new MutableLiveData<>();
    private final MutableLiveData<InputDialogModel> openInputDialog = new MutableLiveData<>();
    private final MutableLiveData<String> openGithubLinkingFlow = new MutableLiveData<>();
    private final AuthService authService;

    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }

    public LiveData<Boolean> getIsInAppPasswordLinked() {
        return isInAppPasswordLinked;
    }

    public LiveData<Boolean> getIsInAppAdding() {
        return isInAppAdding;
    }

    public LiveData<Boolean> getIsGoogleAccountLinked() {
        return isGoogleAccountLinked;
    }

    public LiveData<Boolean> getIsGoogleAdding() {
        return isGoogleAdding;
    }

    public LiveData<Boolean> getOpenGoogleSignIn() {
        return openGoogleSignIn;
    }

    public LiveData<Boolean> getIsGithubAccountLinked() {
        return isGithubAccountLinked;
    }

    public LiveData<Boolean> getIsGithubAdding() {
        return isGithubAdding;
    }

    public LiveData<Boolean> getIsSmsAccountLinked() {
        return isSmsAccountLinked;
    }

    public LiveData<Boolean> getIsSmsAdding() {
        return isSmsAdding;
    }

    public LiveData<EmailPasswordDialogModel> getOpenEmailPasswordDialog() {
        return openEmailPasswordDialog;
    }

    public LiveData<PhoneCredentialDialogModel> getOpenPhoneCredentialDialog() {
        return openPhoneCredentialDialog;
    }

    public LiveData<InputDialogModel> getOpenInputDialog() {
        return openInputDialog;
    }

    public LiveData<String> getOpenGithubLinkingFlow() {
        return openGithubLinkingFlow;
    }

    public AccountLinkingViewModel(AuthService authService) {
        this.authService = authService;

        loadProviders();
    }

    public void loadProviders() {
        isInAppPasswordLinked.postValue(false);
        isGoogleAccountLinked.postValue(false);
        isSmsAccountLinked.postValue(false);
        isGithubAccountLinked.postValue(false);

        authService.fetchSignInMethods()
                .addOnSuccessListener(providerIds -> {
                    for (String providerId : providerIds) {
                        switch (providerId) {
                            case EmailAuthProvider.PROVIDER_ID:
                                isInAppPasswordLinked.postValue(true);
                                break;
                            case GoogleAuthProvider.PROVIDER_ID:
                                isGoogleAccountLinked.postValue(true);
                                break;
                            case GithubAuthProvider.PROVIDER_ID:
                                isGithubAccountLinked.postValue(true);
                                break;
                            case PhoneAuthProvider.PROVIDER_ID:
                                isSmsAccountLinked.postValue(true);
                                break;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error: ", e);
                });
    }

    public void navigateToSettings() {
        navigateToSettings.postValue(true);
    }

    public void addInAppPassword() {
        EmailPasswordDialogModel model = new EmailPasswordDialogModel.Builder()
                .setTitle("In-app password")
                .setSubTitle("Enter your information for password setup")
                .setEmail( authService.getCurrentEmail() )
                .setPassword("")
                .setSubmitButtonClickListener((email, password) -> {
                    isInAppAdding.postValue(true);
                    if (!authService.isCurrentUserEmail(email)) {
                        errorToastMessage.postValue("Your typed email mismatch");
                        isInAppAdding.postValue(false);
                        return;
                    }

                    authService.updatePassword(password)
                            .addOnSuccessListener(aVoid -> {
                                loadProviders();
                                isInAppAdding.postValue(false);
                                successToastMessage.postValue("Add password successfully");
                            })
                            .addOnFailureListener(e -> {
                                errorToastMessage.postValue("Add password unsuccessfully");
                                isInAppAdding.postValue(false);
                                Log.e(TAG, "Error during in-App password linking: " + e.getMessage(), e);
                            });
                })
                .build();
        openEmailPasswordDialog.postValue(model);
    }

    public void addGoogleAccount() {
        openGoogleSignIn();
    }

    private void openGoogleSignIn() {
        openGoogleSignIn.postValue(true);
    }

    public void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        isGoogleAdding.postValue(true);
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            AuthCredential googleCredential = GoogleAuthProvider.getCredential(idToken, null);
            authService.linkCurrentUserWithCredential(googleCredential)
                    .addOnSuccessListener(authResult -> {
                        loadProviders();
                        successToastMessage.postValue("Link google account successfully");
                        isGoogleAdding.postValue(false);
                    })
                    .addOnFailureListener(e -> {
                        isGoogleAdding.postValue(false);
                        errorToastMessage.postValue("Link google account unsuccessfully");
                        Log.e(TAG, "Error during Google linking: " + e.getMessage(), e);
                    });
        } catch (ApiException e) {
            errorToastMessage.postValue("Link google account unsuccessfully");
            isGoogleAdding.postValue(false);
            Log.e(TAG, "Error: ", e);
        }
    }

    public void addGithubAccount() {
        InputDialogModel model = new InputDialogModel.Builder()
                .setTitle("Email")
                .setType(EInputType.EMAIL)
                .setCurrentContent("")
                .setSubmitButtonClickListener(email -> {
                    if (email.isEmpty()) {
                        errorToastMessage.postValue("Email is required");
                        return;
                    }
                    isGithubAdding.postValue(true);
                    this.openGithubLinkingFlow.postValue(email);
                })
                .build();
        openInputDialog.postValue(model);
    }

    public void loginGithubSuccessfully() {
        isGithubAdding.postValue(false);
        loadProviders();
        successToastMessage.postValue("Link Github successfully");
    }

    public void loginGithubUnSuccessfully(Exception e) {
        isGithubAdding.postValue(false);
        errorToastMessage.postValue("Link Github unsuccessfully");
        Log.e(TAG, "Error during Github linking: " + e.getMessage(), e);
    }

    public void addSmsAccount() {
        PhoneCredentialDialogModel model = new PhoneCredentialDialogModel.Builder()
                .setVerifyButtonClickListener(phoneAuthCredential -> {
                    isSmsAdding.postValue(true);
                    authService.linkCurrentUserWithPhoneAuthCredential(phoneAuthCredential)
                            .addOnSuccessListener(authResult -> {
                                loadProviders();
                                isSmsAdding.postValue(false);
                                successToastMessage.postValue("Link phone number successfully");
                            })
                            .addOnFailureListener(e -> {
                                isSmsAdding.postValue(false);
                                errorToastMessage.postValue("Link phone number unsuccessfully");
                                Log.e(TAG, "Error during SMS linking: " + e.getMessage(), e);
                            });
                })
                .build();
        openPhoneCredentialDialog.postValue(model);
    }
}
