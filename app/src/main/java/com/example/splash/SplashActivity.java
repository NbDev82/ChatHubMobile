package com.example.splash;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.infrastructure.PreferenceManager;
import com.example.infrastructure.Utils;
import com.example.navigation.EAnimationType;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private NavigationManager navigationManager;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SplashActivity.this);
        setContentView(R.layout.activity_splash);

        navigationManager = new NavigationManagerImpl(this);
        UserRepos userRepos = new UserReposImpl();
        AuthRepos authRepos = new AuthReposImpl(userRepos);

        preferenceManager = new PreferenceManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!authRepos.isLoggedIn()) {
                    navigationManager.navigateToLogin(EAnimationType.FADE_IN);
                    finish();
                    return;
                }

                String passcode = preferenceManager.getString(Utils.KEY_PASSCODE);
                if (Utils.isEmpty(passcode)) {
                    navigationManager.navigateToHome(EAnimationType.FADE_IN);
                    finish();
                    return;
                }

                navigationManager.navigateToUnlockApp(EAnimationType.FADE_IN);
                finish();
            }
        }, SPLASH_DURATION);
    }
}