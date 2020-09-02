package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    private MainActivity activity;
    private Context context;
    private GameLoopThread gameLoopThread;
    private boolean firstStart = true;
    private int gameState = 1; // 1 Splash State, 2 Play.

    SplashState splashState;

    public GameView(Context context, MainActivity activity)
    {
        super(context);
        this.activity = activity;
        this.context = context;

        gameLoopThread = new GameLoopThread(this);

        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createScreens();
                gameLoopThread.setRunning(true);
                if(firstStart){
                    gameLoopThread.start();
                    firstStart=false;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
        });
    }

    public GameLoopThread getGameLoopThread()
    {
        return gameLoopThread;
    }

    private void createScreens(){
        this.splashState = new SplashState(this);
    }

    @SuppressLint("WrongCall")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        switch (gameState){
            case 1: //Splash State
                splashState.draw(canvas);
                break;
            case 2: //Play game
                break;
            default:
                gameState = 1;
                break;
        }
    }


}
