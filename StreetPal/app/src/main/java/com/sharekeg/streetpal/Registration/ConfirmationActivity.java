package com.sharekeg.streetpal.Registration;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.sharekeg.streetpal.R;


public class ConfirmationActivity extends AppCompatActivity {
    private TextView timerView,tvWelcomeUser;
    private String welcomeUserName;
    long startTime;
    long countUp;
    private Button btnSubmit;

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        timerView = (TextView) findViewById(R.id.TimerView);
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();
        timerView = (TextView) findViewById(R.id.TimerView);
        btnSubmit=(Button)findViewById(R.id.btn_Submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new  Intent(ConfirmationActivity.this,SelectTrustedContactsActivity.class);
                startActivity(i);
            }
        });
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);
                timerView.setText(asText);
            }
        });
        stopWatch.start();

   tvWelcomeUser=(TextView)findViewById(R.id.tv_welcome_user);
        welcomeUserName=getIntent().getExtras().getString("UserNameWelcomeText");
        tvWelcomeUser.setText("Welcome "+welcomeUserName+" !");

    }


}
