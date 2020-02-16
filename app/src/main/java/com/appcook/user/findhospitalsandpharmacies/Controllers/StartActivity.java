package com.appcook.user.findhospitalsandpharmacies.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.appcook.user.findhospitalsandpharmacies.R;

public class StartActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private Animation fadeIn, fadeOut;
    private Button bstart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bstart = (Button) findViewById(R.id.bstart);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(fadeIn);
        viewFlipper.setOutAnimation(fadeOut);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();

        bstart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
