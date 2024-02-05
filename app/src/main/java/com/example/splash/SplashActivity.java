package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.R;
import com.example.home.HomeActivity;
import com.example.infrastructure.Utils;
import com.example.user.AuthService;
import com.example.user.AuthServiceImpl;
import com.example.user.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private final AuthService mAuthService = new AuthServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        if (mAuthService.isLoggedIn()) {
            navigateToHomeActivityWithDelay();
        } else {
            navigateToLoginActivityWithDelay();
        }
    }

    private void navigateToLoginActivityWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }

    private void navigateToHomeActivityWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}