package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class GamePlayState {

    private GameView gameView;
    private Bitmap background;
    private GameObject rocket;
    private ArrayList<GameObject> arrayShip = new ArrayList<GameObject>();
    private GameObject btnStart;
    private ViewGroup.LayoutParams myLayout;
    private int widthScreen;
    private int heightScreen;
    private float YDes = 0.0f;
    private Canvas canvas;
    private float xDrag;
    private float yDrag;
    private int stepGame = 1; // Di chuyển các chiến hạm.
    private float xMsg = 0;

    public GamePlayState(GameView gameView) {
        this.gameView = gameView;
        this.widthScreen = gameView.getWidth();
        this.heightScreen = gameView.getHeight();
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;

        background = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.background, option);

        // Rocket.
        Bitmap rocketBitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.rocket_100, option);
        rocket = new GameObject(rocketBitmap);
        rocket.setLive(false);
        // Nút bắt đầu khi sắp tàu xong.
        Bitmap startButton = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.btn_start, option);
        btnStart = new GameObject(this.widthScreen/2, this.heightScreen/4, startButton);

        // Sắp 4 tàu ngẫu nhiên
        ArrayList<Float> arrayTemp = new ArrayList<Float>();
        for (int i = 0; i < 4; i++)
        {
            Random rd = new Random();
            float x = rd.nextInt((this.widthScreen - 0 + 1) + 0);
            do {
                x = rd.nextInt((this.widthScreen - 0 + 1) + 0);
                for (int j = 0; j < arrayTemp.size(); j++){
                    if (arrayTemp.get(j) == x)
                        x = -1.0f;
                }
            }while (x == -1.0f);
            arrayTemp.add(x);
            float y = rd.nextInt((this.heightScreen - this.heightScreen/2 + 1) + this.heightScreen/2);
            do{
                y = rd.nextInt((this.heightScreen - this.heightScreen/2 + 1) + this.heightScreen/2);

            }while (y < this.heightScreen / 2);
            String nameShip = "ship" + (i + 1);
            int resourceId = gameView.getResources().getIdentifier(nameShip, "drawable",gameView.getContext().getPackageName());
            Bitmap shipBitmap = BitmapFactory.decodeResource(gameView.getResources(), resourceId, option);
            if(xMsg != 0){
                GameObject ship = new GameObject(xMsg, y, shipBitmap);
                arrayShip.add(ship);
            }
            else{
                GameObject ship = new GameObject(x, y, shipBitmap);
                arrayShip.add(ship);
            }

        }


//        myLayout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        myLayout.set
    }

    @SuppressLint("WrongCall")
    public void update(float x, float y, float dt)
    {
        //System.out.println("update");
        //onTouchEvent(event, canvas);
        switch (stepGame)
        {
            case 1:
                if (btnStart.checkIsCollitionPoint(x, y)){
                    stepGame = 2;
                    rocket.setLive(true);
                    rocket.setCheckLock(false);
                    System.out.println("STEP GAME = " + stepGame);
                }
                break;
            case 2:
                if (rocket.isLive() && !rocket.isCheckLock()) {
//                    rocket.setX(x);
//                    rocket.setY(y);
//                    rocket.setCheckLock(true);
//                    YDes = getYOfPointSymmetry(YDes);
                    if (YDes == 0.0f && y != 0.0f) {
                        YDes = y;
                        YDes = getYOfPointSymmetry(YDes);
                        //System.out.println(YDes);
                    }
                    if (y >= YDes && y != 0.0f) {
//                        drawRocket(x, y);
//                        for (GameObject ship : arrayShip){
//                            if (rocket.checkIsCollition(ship)){
//                                System.out.println("VA CHAM");
//                            }
//                        }
                        rocket.setX(x);
                        rocket.setY(y);
                        rocket.setCheckLock(true);
                    } else {
                        YDes = 0.0f;
                    }
                }else {
                    rocket.run();
                }
                break;
        }
    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        RectF dst = new RectF(0, 0, 0 + gameView.getWidth(), 0 + gameView.getHeight());
        canvas.drawBitmap(background, null, dst, null);
        for(GameObject ship : arrayShip){
            RectF dstShip = ship.getDstRectF();
            canvas.drawBitmap(ship.getBitmap(), null, dstShip, null);
        }

        switch (stepGame){
            case 1:
                canvas.drawBitmap(btnStart.getBitmap(), null, btnStart.getDstRectF(), null);
                break;
            case 2:
                //do{
                    canvas.drawBitmap(rocket.getBitmap(), null, rocket.getDstRectF(), null);
                    //System.out.println(rocket.getY());
                //}while (rocket.run() >= YDes);
                for(GameObject ship : arrayShip){
                    if (rocket.checkIsCollition(ship)){
                        System.out.println("VA CHAM");
                    }
                }
//                canvas.drawBitmap(rocket.getBitmap(), null, rocket.getDstRectF(), null);
//                if (rocket.run() < 0)
//                    rocket.setCheckLock(false);
                break;
        }
    }

    public ArrayList<GameObject> getArrayShip(){
        return arrayShip;
    }

    private float getYOfPointSymmetry(float y){
        return (heightScreen / 2.0f) - ((heightScreen / 2.0f) - (heightScreen - y));
    }

    public void drawRocket(float x, float y)
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

    public int getStepGame(){
        return stepGame;
    }

    public void setxMsg(float x){
        this.xMsg = x;
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
