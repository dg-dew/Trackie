package com.example.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize Views
        ImageView boxImage = findViewById(R.id.image_running_box);

        // 2. Start Animations
        startBoxAnimation(boxImage);
        startLoadingDotsAnimation();

        // 3. Navigate to Login page after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 6000);
    }

    private void startBoxAnimation(ImageView boxImage) {
        if (boxImage != null) {
            // Hopping animation
            ObjectAnimator bouncer = ObjectAnimator.ofFloat(boxImage, "translationY", 0f, -40f);
            bouncer.setDuration(400);
            bouncer.setRepeatCount(ValueAnimator.INFINITE);
            bouncer.setRepeatMode(ValueAnimator.REVERSE);
            bouncer.start();
        }
    }

    private void startLoadingDotsAnimation() {
        View dot1 = findViewById(R.id.dot1);
        View dot2 = findViewById(R.id.dot2);
        View dot3 = findViewById(R.id.dot3);
        View dot4 = findViewById(R.id.dot4);
        View dot5 = findViewById(R.id.dot5);

        if (dot1 == null) return;

        View[] dots = {dot1, dot2, dot3, dot4, dot5};
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator[] animators = new ObjectAnimator[dots.length];

        for (int i = 0; i < dots.length; i++) {
            dots[i].setAlpha(0f);
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(dots[i], "alpha", 0f, 1f);
            fadeIn.setDuration(500);
            fadeIn.setStartDelay(i * 200);
            fadeIn.setRepeatCount(ValueAnimator.INFINITE);
            fadeIn.setRepeatMode(ValueAnimator.REVERSE);
            animators[i] = fadeIn;
        }

        animatorSet.playTogether(animators);
        animatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Welcome to Trackie", Toast.LENGTH_SHORT).show();
    }
}
