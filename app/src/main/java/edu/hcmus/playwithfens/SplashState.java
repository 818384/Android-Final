package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashState {

    private static int SPLASH_TIME_OUT = 2000;
    private Paint title;
    private int x;
    private int y;
    private GameView gameView;
    Bitmap bmLogo;
    Bitmap bmTitile;
    private Paint welcomeMessage = new Paint();

    public SplashState(GameView gameView){
        bmLogo = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.splash_logo);
        Bitmap bmTitile = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.titile_splash_state);

        Typeface typeface = Typeface.create("Helvetica",Typeface.BOLD);
        this.gameView = gameView;
        welcomeMessage.setTypeface(typeface);
        welcomeMessage.setColor(Color.WHITE);
        welcomeMessage.setTextSize(60);
        welcomeMessage.setTextAlign(Paint.Align.CENTER);
        x = gameView.getWidth()/2;
        y = gameView.getHeight()/6;
    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas){
        canvas.drawText("Android Space Invader",x,y,welcomeMessage);
        canvas.drawText("Tap to start...",x, (int) (5.5 * y) ,welcomeMessage);



        int width = bmLogo.getWidth() / 2;
        int height = bmLogo.getHeight() / 2;
        int srcX = 0 * width;
        int srcY = 0 * height;

        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(30, 30, 30 + width, 30 + height);

        src.left=srcX;
        src.top = srcY;
        src.right = srcX + width;
        src.bottom = srcY + height;
        dst.left=30;
        dst.top=30;
        dst.right=30+width;
        dst.bottom=30+height;
        canvas.drawBitmap(bmLogo, src, dst, null);
    }


//    private void initWork() {
//        logo = (ImageView) findViewById(R.id.imgLogo);
//        nameGame = (TextView) findViewById(R.id.nameGame);
//
//        logo.setImageResource(R.drawable.splash_logo);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashState.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
//    }
}
