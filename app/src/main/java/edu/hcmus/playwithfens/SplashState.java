package edu.hcmus.playwithfens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashState extends Activity {

    private ImageView logo;
    private TextView nameGame;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashstate);
        initWork();
    }

    private void initWork() {
        logo = (ImageView) findViewById(R.id.imgLogo);
        nameGame = (TextView) findViewById(R.id.nameGame);

        logo.setImageResource(R.drawable.splash_image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashState.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
