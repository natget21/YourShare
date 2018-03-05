package com.natiit.www.yourshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;

/**
 * Created by NAT on 3/4/2018.
 */

public class SplashScreen extends AppCompatActivity {
    private ImageView iv;
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        Animation glowFromTop = AnimationUtils.loadAnimation(this,R.anim.glow_from_top);
        Animation glowFromBottom = AnimationUtils.loadAnimation(this,R.anim.glow_from_bottom);

        iv = (ImageView)findViewById(R.id.splash_logo);
        tv = (TextView)findViewById(R.id.logo_text);

        iv.startAnimation(glowFromTop);
        tv.startAnimation(glowFromBottom);

        final Intent i = new Intent(SplashScreen.this, Main2Activity.class);

        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(i);
                    finish();
                }
            }
        });

        timer.start();


    }
}
