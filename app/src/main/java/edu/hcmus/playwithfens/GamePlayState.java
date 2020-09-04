package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class GamePlayState {

    private GameView gameView;
    private Bitmap background;
    private Bitmap rocket;
    private ViewGroup.LayoutParams myLayout;
    private float x;
    private float y;

    public GamePlayState(GameView gameView)
    {
        this.gameView = gameView;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        background = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.background, option);
        rocket = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.rocket, option);

//        myLayout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        myLayout.set
    }

    @SuppressLint("WrongCall")
    public void update(float x, float y, Canvas canvas){
        System.out.println("update");
        //onTouchEvent(event, canvas);
        drawRocket(x, y, canvas);
    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas)
    {
        RectF dst = new RectF(0, 0, 0 + gameView.getWidth(), 0 + gameView.getHeight());
        canvas.drawBitmap(background, null, dst, null);

    }

    public void drawRocket(float x, float y, Canvas canvas){
        if (y > 0){
            float left = (x - rocket.getWidth() / 2.0f);
            float top = (y - rocket.getHeight() / 2.0f);
            RectF dst = new RectF(left, top, left + rocket.getWidth(), top + rocket.getHeight());
            canvas.drawBitmap(rocket, null, dst, null);
        }

//        while (y >= rocket.getHeight()){
//
//            left = (x - rocket.getWidth() / 2.0f);
//            top = (y - rocket.getHeight() / 2.0f);
//            System.out.println("left: " + left + "--top: " + top);
//            dst = new RectF(left, top, left + rocket.getWidth(), top + rocket.getHeight());
//            canvas.drawBitmap(rocket, null, dst, null);
//            y--;
//        }

    }
    public boolean onTouchEvent(MotionEvent event, Canvas canvas) {
        if (event != null){
            x = event.getX();
            y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    System.out.println("x: " + x + "-y: "+ y);
                    drawRocket(x, y, canvas);
                    break;
            }
            return false;
        }
        return false;
    }

}
