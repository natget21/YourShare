package com.natiit.www.yourshare;

/**
 * Created by NAT on 3/9/2018.
 */


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by HP on 10/23/2016.
 */
public class MyIntro extends AppIntro {
    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        //adding the three slides for introduction app you can ad as many you needed
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro_1));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro_2));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro_3));

        // Show and Hide Skip and Done buttons
        showStatusBar(false);
        showSkipButton(false);

        // Turn vibration on and set intensity
        // You will need to add VIBRATE permission in Manifest file
        setVibrate(true);
        setVibrateIntensity(10);

        //Add animation to the intro slider
        setDepthAnimation();

        /*If you want to change the animation type you can replace setDepthAnimation(); with
                setFadeAnimation(); or
                setZoomAnimation(); or
                setFlowAnimation(); or
                setSlideOverAnimation(); or
                setDepthAnimation(); or
                setCustomTransformer(yourCustomTransformer);*/
    }

    @Override
    public void onSkipPressed() {
        // Do something here when users click or tap on Skip button.
        Toast.makeText(getApplicationContext(),
                getString(R.string.app_intro_skip), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(i);
    }

    @Override
    public void onNextPressed() {
        // Do something here when users click or tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something here when users click or tap tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something here when slide is changed
    }
}