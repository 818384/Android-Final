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
    private GameObject rocket;
    private Bitmap rocketBitmap;
    private ViewGroup.LayoutParams myLayout;
    private float x;
    private float y;

    public GamePlayState(GameView gameView) {
        this.gameView = gameView;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        background = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.background, option);
        rocketBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.rocket_100, option);
        rocket = new GameObject(rocketBitmap);

//        myLayout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        myLayout.set
    }

    @SuppressLint("WrongCall")
    public void update(float x, float y, Canvas canvas) {
        System.out.println("update");
        //onTouchEvent(event, canvas);
        drawRocket(x, y, canvas);
    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas) {
        RectF dst = new RectF(0, 0, 0 + gameView.getWidth(), 0 + gameView.getHeight());
        canvas.drawBitmap(background, null, dst, null);

    }

    public void drawRocket(float x, float y, Canvas canvas) {
        if (y > 0) {
            float left = (x - rocket.getBitmap().getWidth() / 2.0f);
            float top = (y - rocket.getBitmap().getHeight() / 2.0f);
            RectF dst = new RectF(left, top, left + rocket.getBitmap().getWidth(), top + rocket.getBitmap().getHeight());
            canvas.drawBitmap(rocket.getBitmap(), null, dst, null);
        }

//        while (y >= rocket.getImgSource().getHeight()){
//
//            left = (x - rocket.getImgSource().getWidth() / 2.0f);
//            top = (y - rocket.getImgSource().getHeight() / 2.0f);
//            System.out.println("left: " + left + "--top: " + top);
//            dst = new RectF(left, top, left + rocket.getImgSource().getWidth(), top + rocket.getImgSource().getHeight());
//            canvas.drawBitmap(rocket, null, dst, null);
//            y--;
//        }

    }

    public boolean onTouchEvent(MotionEvent event, Canvas canvas) {
        if (event != null) {
            x = event.getX();
            y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    System.out.println("x: " + x + "-y: " + y);
                    drawRocket(x, y, canvas);
                    break;
            }
            return false;
        }
        return false;
    }

}
