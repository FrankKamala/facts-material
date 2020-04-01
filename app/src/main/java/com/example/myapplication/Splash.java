package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {
    ConstraintLayout myConstraint;
    AnimationDrawable myAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myConstraint=(ConstraintLayout)findViewById(R.id.splashYetu);
        myAnimator=(AnimationDrawable)myConstraint.getBackground();
// call errors
//        myAnimator.setEnterFadeDuration(2000);
//        myAnimator.setExitFadeDuration(2000);
        myAnimator.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this,LoginActivity.class)); //starts main Activity
                finish();

            }
        },  3000);
    }
    }

