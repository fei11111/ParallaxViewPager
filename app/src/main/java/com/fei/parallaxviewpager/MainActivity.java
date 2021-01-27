package com.fei.parallaxviewpager;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.fei.parallaxviewpager.parallax.ParallaxViewPager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParallaxViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setLayouts(getSupportFragmentManager(), R.layout.fragment_page_first, R.layout.fragment_page_second, R.layout.fragment_page_third);

        final SplashView splashView = findViewById(R.id.splash_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashView.disappear();
            }
        },3000);
    }
}