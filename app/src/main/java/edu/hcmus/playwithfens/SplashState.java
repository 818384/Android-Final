package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;


public class SplashState {

    private static int SPLASH_TIME_OUT = 2000;
    private Paint title;
    private int x;
    private int y;
    private GameView gameView;
    Bitmap bmLogo;
    Bitmap bmTitile;
    Bitmap btnStart;
    private Paint welcomeMessage = new Paint();
    private Sprite logo;

    public SplashState(GameView gameView) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        bmLogo = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.splash_logo, option);
        bmTitile = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.titile_splash_state);
        btnStart = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.btn_start, option);

        Typeface typeface = Typeface.create("Helvetica", Typeface.BOLD);
        this.gameView = gameView;
        welcomeMessage.setTypeface(typeface);
        welcomeMessage.setColor(Color.WHITE);
        welcomeMessage.setTextSize(60);
        welcomeMessage.setTextAlign(Paint.Align.CENTER);
        x = gameView.getWidth() / 2;
        y = gameView.getHeight() / 6;
    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas) {

        int width = bmLogo.getWidth();
        int height = bmLogo.getHeight();
        float left = (gameView.getWidth() - bmLogo.getWidth()) / 2.0f;
        float top = (gameView.getHeight() - bmLogo.getHeight()) / 2.0f;
        RectF dst = new RectF(left, top, left + bmLogo.getWidth(), top + bmLogo.getHeight());
        canvas.drawBitmap(bmLogo, null, dst, null);

        float leftbtn = (gameView.getWidth() - bmTitile.getWidth()) / 2.0f;
        float topbtn = (gameView.getHeight() - bmTitile.getHeight());
        RectF dstBtnStart = new RectF(leftbtn, topbtn, leftbtn + bmTitile.getWidth(), topbtn + bmTitile.getHeight());
        canvas.drawBitmap(bmTitile, null, dstBtnStart, null);

    }

//    private boolean inBounds(MotionEvent event, int x, int y, int width, int height)
//    {
//        if (event.getAction() == MotionEvent.ACTION_POINTER_UP){
//
//        }
//        if(event.x > x && event.x < x + width - 1 &&
//                event.y > y && event.y < y + height - 1)
//            return true;
//        else
//            return false;
//    }


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
