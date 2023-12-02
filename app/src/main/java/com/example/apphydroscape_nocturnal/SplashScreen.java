package com.example.apphydroscape_nocturnal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 1000;
    Animation topAnim, bottomAnim;
    View image, logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.linear_layout);
        slogan = findViewById(R.id.textView3);

        // Set visibility to GONE for logo and slogan initially
        logo.setVisibility(View.GONE);
        slogan.setVisibility(View.GONE);

        // Set visibility to GONE for the image initially
        image.setVisibility(View.GONE);


        // Delay for image animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Image animation completes, show the image
                image.setVisibility(View.VISIBLE);
                image.setAnimation(topAnim);
            }
        }, SPLASH_SCREEN);

        // Delay for logo animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Animation for logo
                logo.setVisibility(View.VISIBLE);
                logo.setAnimation(bottomAnim);
                bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Logo animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Animation for slogan
                        slogan.setVisibility(View.VISIBLE);
                        slogan.setAnimation(bottomAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }, SPLASH_SCREEN);

        // Start the next screen after all animations finish
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Add a delay before starting the next activity (e.g., 1 second)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, Onboarding.class);
                        // Attach all the elements you want to animate in design
                        Pair[] pairs = new Pair[2];
                        pairs[0] = new Pair<View, String>(image, "logo_image");
                        pairs[1] = new Pair<View, String>(logo, "logo_text");
                        // Wrap the call in API level 21 or higher
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                            startActivity(intent, options.toBundle());
                        }
                    }
                }, 2100); // Delay for 1 second before starting the next activity
            }
        }, SPLASH_SCREEN + bottomAnim.getDuration());
    }
}
