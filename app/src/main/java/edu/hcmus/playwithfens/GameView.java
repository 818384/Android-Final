package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import java.io.IOException;

public class GameView extends SurfaceView {
    private MainActivity activity;
    private Context context;
    private GameLoopThread gameLoopThread;
    private boolean firstStart = true;
    private int gameState = 1; // 1 Splash State, 2 Play.
    private int delay = 0;
    private MotionEvent eventGameView;
    private float x;
    private float y;

    private SplashState splashState;
    private GamePlayState gamePlayState;

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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
    public Activity getGameViewAcivity(){
        return this.activity;
    }
    public Context getGameViewContext(){
        return this.context;
    }

    private void createScreens()
    {
        if (gameState == 1){
            this.splashState = new SplashState(this);
        }

        this.gamePlayState = new GamePlayState(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


            synchronized (getHolder()) {

                switch(gameState){

                    case 1://Welcome Screen
                        gameState = 2;

                        break;
                    case 2:// Play game
                        if (event.getAction() == MotionEvent.ACTION_UP){
                            x = event.getX();
                            y = event.getY();
                        }
                        break;
                    default:
                        gameState = 1;
                        break;
                }
            }
        return true;
    }

    @SuppressLint("WrongCall")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        switch (gameState){
            case 1: //Splash State
                splashState.draw(canvas);
                // tap to start.

                try{
                    Thread.sleep(1000);
                    delay++;
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                if (delay == 2){
                    gameState = 2;
                }
                break;
            case 2: //Play game
                gamePlayState.draw(canvas);
                gamePlayState.update(x, y-=50, canvas);

                break;
            default:
                gameState = 1;
                break;
        }
    }


}
