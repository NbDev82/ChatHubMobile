package com.example.user.accountlink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.customcontrol.emailpassworddialog.EmailPasswordDialogFragment;
import com.example.customcontrol.emailpassworddialog.EmailPasswordDialogModel;
import com.example.customcontrol.phonecredential.PhoneCredentialDialogFragment;
import com.example.customcontrol.phonecredential.PhoneCredentialDialogModel;
import com.example.databinding.ActivityAccountLinkingBinding;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class AccountLinkingActivity extends AppCompatActivity {

    private NavigationManager navigationManager;
    private AccountLinkingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        navigationManager = new NavigationManagerImpl(this);

        AuthService authService = new AuthServiceImpl();
        AccountLinkingViewModelFactory factory = new AccountLinkingViewModelFactory(authService);
        viewModel = new ViewModelProvider(this, factory).get(AccountLinkingViewModel.class);

        ActivityAccountLinkingBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_account_linking);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupObservers();
    }

    private void setupObservers() {
        viewModel.getNavigateToSettings().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSettings(EAnimationType.FADE_OUT);
            }
        });

        viewModel.getOpenGoogleSignIn().observe(this, open -> {
            if (open) {
                displayOneTapSignInUI();
            }
        });

        viewModel.getOpenEmailPasswordDialog().observe(this, this::openEmailPasswordDialog);

        viewModel.getOpenPhoneCredentialDialog().observe(this, this::openPhoneCredentialDialog);
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
        PhoneCredentialDialogFragment dialog = new PhoneCredentialDialogFragment(model);
        dialog.show(getSupportFragmentManager(), PhoneCredentialDialogFragment.TAG);
    }
}