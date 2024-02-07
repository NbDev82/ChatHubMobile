package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.example.R;
import com.example.infrastructure.Utils;
import com.example.navigation.NavigationManager;
import com.example.navigation.NavigationManagerImpl;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SplashActivity.this);
        setContentView(R.layout.activity_splash);

        navigationManager = new NavigationManagerImpl(this);
        AuthService mAuthService = new AuthServiceImpl();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuthService.isLoggedIn()) {
                    navigationManager.navigateToHome();
                } else {
                    navigationManager.navigateToLogin();
                }
                finish();
            }
        }, SPLASH_DURATION);
    }
}