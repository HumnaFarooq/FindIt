package com.example.findit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean hasNavigated = false;

    private final Runnable navigateToHomeRunnable = this::navigateToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Tap anywhere on splash to skip immediately
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setOnClickListener(v -> navigateToHome());
        }

        // Auto navigate after 2 seconds
        handler.postDelayed(navigateToHomeRunnable, 2000);
    }

    private synchronized void navigateToHome() {
        if (!hasNavigated) {
            hasNavigated = true;
            handler.removeCallbacks(navigateToHomeRunnable);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(navigateToHomeRunnable);
    }
}
