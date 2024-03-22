package com.example.user.accountlink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.emailpassworddialog.EmailPasswordDialogFragment;
import com.example.customcontrol.emailpassworddialog.EmailPasswordDialogModel;
import com.example.customcontrol.inputdialogfragment.InputDialogFragment;
import com.example.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.customcontrol.phonecredential.PhoneCredentialDialogFragment;
import com.example.customcontrol.phonecredential.PhoneCredentialDialogModel;
import com.example.databinding.ActivityAccountLinkingBinding;
import com.example.infrastructure.BaseActivity;
import com.example.navigation.EAnimationType;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class AccountLinkingActivity extends BaseActivity<AccountLinkingViewModel, ActivityAccountLinkingBinding> {

    private UserRepos userRepos;
    private AuthRepos authRepos;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_account_linking;
    }

    @Override
    protected Class<AccountLinkingViewModel> getViewModelClass() {
        return AccountLinkingViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        userRepos = new UserReposImpl();
        authRepos = new AuthReposImpl(userRepos);
        return new AccountLinkingViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getOpenGoogleSignIn().observe(this, open -> {
            if (open) {
                displayOneTapSignInUI();
            }
        });

        viewModel.getOpenEmailPasswordDialog().observe(this, this::openEmailPasswordDialog);

        viewModel.getOpenPhoneCredentialDialog().observe(this, this::openPhoneCredentialDialog);

        viewModel.getOpenGithubLinkingFlow().observe(this, this::openGithubSignInFlow);
    }

    private void displayOneTapSignInUI() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(AccountLinkingActivity.this, gso);
        googleSignInClient.signOut();
        Intent intent = googleSignInClient.getSignInIntent();
        loginWithGoogle.launch(intent);
    }

    private ActivityResultLauncher<Intent> loginWithGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    viewModel.handleGoogleSignInResult(task);
                }
            }
    );

    private void openEmailPasswordDialog(EmailPasswordDialogModel model) {
        EmailPasswordDialogFragment dialog = new EmailPasswordDialogFragment(model);
        dialog.show(getSupportFragmentManager(), EmailPasswordDialogFragment.TAG);
    }

    private void openPhoneCredentialDialog(PhoneCredentialDialogModel model) {
        PhoneCredentialDialogFragment dialog =
                new PhoneCredentialDialogFragment(userRepos, authRepos, model);
        dialog.show(getSupportFragmentManager(), PhoneCredentialDialogFragment.TAG);
    }

    private void openGithubSignInFlow(String email) {
        authRepos.linkGithubWithCurrentUser(AccountLinkingActivity.this, email)
                .thenAccept(authResult -> {
                    viewModel.loginGithubSuccessfully();
                })
                .exceptionally(e -> {
                    viewModel.loginGithubUnSuccessfully((Exception) e);
                    return null;
                });
    }
}