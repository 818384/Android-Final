package edu.hcmus.playwithfens;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GamePlayState extends Activity {

    private GameView gameView;
    private Bitmap background;
    private GameObject rocket;
    private Bitmap rocketBitmap;
    private Bitmap shipBitmap;
    private ViewGroup.LayoutParams myLayout;
    private int widthScreen;
    private int heightScreen;
    private float YDes = 0.0f;
    private float x, y;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        gameView = new GameView(this);
        this.widthScreen = gameView.getWidth();
        this.heightScreen = gameView.getHeight();
        background = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.background, option);
        rocketBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.rocket_100, option);
        shipBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.ship, option);
        rocket = new GameObject(rocketBitmap);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_common, null);
        LinearLayout layout = view.findViewById(R.id.layoutGame);
        RectF dst = new RectF(0, 0, 0 + gameView.getWidth(), 0 + gameView.getHeight());
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX();
                y = event.getY();
                update(x, y);
                return true;
            }
        });
        layout.addView(gameView);
        draw(background, dst, "BACKGROUND");
//        initShip(5);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void update(float x, float y) {
        System.out.println("update position: x = " + x + ", y = " + y);
        //onTouchEvent(event, canvas);

        if (rocket.isLive()) {
            if (YDes == 0.0f && y != 0.0f) {
                YDes = y;
                YDes = getYOfPointSymmetry(YDes);
                System.out.println("Symmetric Y value: " + YDes);
            }
            if (y >= YDes) {
                drawRocket(x, y);
            } else {
                YDes = 0.0f;
            }
        } else {
            YDes = 0.0f;
        }
    }

    public void draw(Bitmap bitmap, RectF dst, String type) {
        Map params = new HashMap<>();
        params.put("BITMAP", bitmap);
        params.put("RECT", dst);
        params.put("TYPE", type);
        gameView.setParams(params);
        gameView.postInvalidate();
    }

    private float getYOfPointSymmetry(float y) {
        return (heightScreen / 2.0f) - ((heightScreen / 2.0f) - (heightScreen - y));
    }

    public void drawRocket(float x, float y) {
        float left = (x - rocket.getBitmap().getWidth() / 2.0f);
        float top = (y - rocket.getBitmap().getHeight() / 2.0f);
        RectF dst = new RectF(left, top, left + rocket.getBitmap().getWidth(), top + rocket.getBitmap().getHeight());
        draw(rocket.getBitmap(), dst, "ROCKET");

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

    private void initShip(int num) {
        float left = (x - shipBitmap.getWidth() / 2.0f);
        float top = (y - shipBitmap.getHeight() / 2.0f);
        RectF dst = new RectF(left, top, left + rocket.getBitmap().getWidth(), top + rocket.getBitmap().getHeight());
        for (int i = 0; i < num; i++) {
            draw(shipBitmap, dst, "SHIP");
        }
    }
//    public boolean onTouchEvent(MotionEvent event, Canvas canvas) {
//        if (event != null) {
//            x = event.getX();
//            y = event.getY();
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_UP:
//                    System.out.println("x: " + x + "-y: " + y);
//                    drawRocket(x, y, canvas);
//                    break;
//            }
//            return false;
//        }
//        return false;
//    }

}
