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
    private GameObject ship;
    private Bitmap rocketBitmap;
    private Bitmap shipBitmap;
    private ViewGroup.LayoutParams myLayout;
    private int widthScreen;
    private int heightScreen;
    private float YDes = 0.0f;
    private float xDrag;
    private float yDrag;

    public GamePlayState(GameView gameView) {
        this.gameView = gameView;
        this.widthScreen = gameView.getWidth();
        this.heightScreen = gameView.getHeight();
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        background = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.background, option);
        rocketBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.rocket_100, option);
        shipBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.rocket, option);
        rocket = new GameObject(rocketBitmap);
        ship = new GameObject(500, 500, shipBitmap);

//        myLayout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        myLayout.set
    }

    @SuppressLint("WrongCall")
    public void update(float x, float y, Canvas canvas)
    {
        //System.out.println("update");
        //onTouchEvent(event, canvas);
        if (x != 0 && y != 0)
        {
            ship.setX(x);
            ship.setY(y);
        }
        if (rocket.isLive()){
            if (YDes == 0.0f && y != 0.0f){
                YDes = y;
                YDes = getYOfPointSymmetry(YDes);
                //System.out.println(YDes);
            }
            if (y >= YDes && y != 0.0f){
                drawRocket(x, y, canvas);
                if (rocket.checkIsCollition(ship)){
                    System.out.println("VA CHAM");
                }
            }
            else {
                YDes = 0.0f;
            }
        }
        else {
            YDes = 0.0f;
        }

    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas) {
        RectF dst = new RectF(0, 0, 0 + gameView.getWidth(), 0 + gameView.getHeight());
        canvas.drawBitmap(background, null, dst, null);
        float left = (ship.getX() - ship.getBitmap().getWidth() / 2.0f);
        float top = (ship.getY() - ship.getBitmap().getHeight() / 2.0f);
        RectF dstShip = new RectF(left, top, left + ship.getBitmap().getWidth(), top + ship.getBitmap().getHeight());
        canvas.drawBitmap(ship.getBitmap(), null, dstShip, null);

    }

    public GameObject getShip(){
        return ship;
    }

    private float getYOfPointSymmetry(float y){
        return (heightScreen / 2.0f) - ((heightScreen / 2.0f) - (heightScreen - y));
    }

    public void drawRocket(float x, float y, Canvas canvas)
    {
        float left = (x - rocket.getBitmap().getWidth() / 2.0f);
        float top = (y - rocket.getBitmap().getHeight() / 2.0f);
        RectF dst = new RectF(left, top, left + rocket.getBitmap().getWidth(), top + rocket.getBitmap().getHeight());
        canvas.drawBitmap(rocket.getBitmap(), null, dst, null);

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
