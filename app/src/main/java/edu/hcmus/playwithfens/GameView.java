package edu.hcmus.playwithfens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.icu.text.SymbolTable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.constraintlayout.solver.state.Dimension;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private MainActivity activity;
    private Context context;
    //private GameLoopThread gameLoopThread;
    private Thread thread;
    private boolean firstStart = true;
    private int gameState = 1; // 1 Splash State, 2 Play.
    private int delay = 0;
    private MotionEvent eventGameView;
    private float x;
    private float y;
    private float xDrag;
    private float yDrag;
    private boolean checkDrag = false;
    private float dt;
    GameObject shipp;
    private boolean isPlaying = true;

    private SplashState splashState;
    private GamePlayState gamePlayState;

    private Bitmap background;
    private GameObject rocket;
    private ArrayList<GameObject> arrayShip = new ArrayList<GameObject>();
    private GameObject btnStart;
    private ViewGroup.LayoutParams myLayout;
    private int widthScreen;
    private int heightScreen;
    private float YDes = 0.0f;
    private int stepGame = 1; // Di chuyển các chiến hạm.
    private float xMsg = 0;

    public GameView(Context context, MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.context = context;

        // Khởi tạo màn hình tìm kiếm đối thủ.
        // Khởi tạo màn khởi đầu.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.widthScreen = displayMetrics.widthPixels;
        this.heightScreen = displayMetrics.heightPixels;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background, option);

        // Rocket.
        Bitmap rocketBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rocket_100, option);
        rocket = new GameObject(rocketBitmap);
        rocket.setLive(false);
        // Nút bắt đầu khi sắp tàu xong.
        Bitmap startButton = BitmapFactory.decodeResource(getResources(), R.drawable.btn_start, option);
        btnStart = new GameObject(this.widthScreen/2, this.heightScreen/4, startButton);

        // Sắp 4 tàu ngẫu nhiên
        ArrayList<Float> arrayTemp = new ArrayList<Float>();
        for (int i = 0; i < 4; i++)
        {
            Random rd = new Random();
            float x, y;
            do {
                x = rd.nextInt((this.widthScreen - 0 + 1) + 0);
                for (int j = 0; j < arrayTemp.size(); j++){
                    if (arrayTemp.get(j) == x)
                        x = -1.0f;
                }
            }while (x == -1.0f);
            arrayTemp.add(x);
            //float y = rd.nextInt((this.heightScreen - this.heightScreen/2 + 1) + this.heightScreen/2);
            do{
                y = rd.nextInt((this.heightScreen - this.heightScreen/2 + 1) + this.heightScreen/2);

            }while (y < this.heightScreen / 2);
            String nameShip = "ship" + (i + 1);
            int resourceId = getResources().getIdentifier(nameShip, "drawable", this.context.getPackageName());
            Bitmap shipBitmap = BitmapFactory.decodeResource(getResources(), resourceId, option);
//            if(xMsg != 0){
//                GameObject ship = new GameObject(xMsg, y, shipBitmap);
//                arrayShip.add(ship);
//            }
//            else{
//                GameObject ship = new GameObject(x, y, shipBitmap);
//                arrayShip.add(ship);
//            }
            GameObject ship = new GameObject(x, y, shipBitmap);
            arrayShip.add(ship);
            System.out.println("Đang chuẩn bị game" + i);
        }
//        gameLoopThread = new GameLoopThread(this);
//
//        getHolder().addCallback(new SurfaceHolder.Callback() {
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                boolean retry = true;
//                gameLoopThread.setRunning(false);
//                while (retry) {
//                    try {
//                        gameLoopThread.join();
//                        retry = false;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                createScreens();
//                gameLoopThread.setRunning(true);
//                if (firstStart) {
//                    gameLoopThread.start();
//                    dt = gameLoopThread.getDt();
//                    firstStart = false;
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//        });
    }

    //    public GameLoopThread getGameLoopThread() {
//        return gameLoopThread;
//    }
//
//    public Activity getGameViewAcivity() {
//        return this.activity;
//    }
//
//    public Context getGameViewContext() {
//        return this.context;
//    }
//
//    private void createScreens() {
//        if (gameState == 1) {
//            this.splashState = new SplashState(this);
//        }
//
//        this.gamePlayState = new GamePlayState(this);
//    }


    private float getYOfPointSymmetry(float y){
        return (heightScreen / 2.0f) - ((heightScreen / 2.0f) - (heightScreen - y));
    }
    private void update() {
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
    private void draw()
    {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            RectF dst = new RectF(0, 0, 0 + getWidth(), 0 + getHeight());
            canvas.drawBitmap(background, null, dst, null);
            for (GameObject ship : arrayShip) {
                RectF dstShip = ship.getDstRectF();
                canvas.drawBitmap(ship.getBitmap(), null, dstShip, null);
            }

            switch (stepGame) {
                case 1:
                    canvas.drawBitmap(btnStart.getBitmap(), null, btnStart.getDstRectF(), null);
                    break;
                case 2:
                    //do{
                    canvas.drawBitmap(rocket.getBitmap(), null, rocket.getDstRectF(), null);
                    //System.out.println(rocket.getY());
                    //}while (rocket.run() >= YDes);
                    for (GameObject ship : arrayShip) {
                        if (rocket.checkIsCollition(ship)) {
                            System.out.println("VA CHAM");
                        }
                    }
//                canvas.drawBitmap(rocket.getBitmap(), null, rocket.getDstRectF(), null);
//                if (rocket.run() < 0)
//                    rocket.setCheckLock(false);
                    break;
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        synchronized (getHolder())
        {
            switch (gameState) {

                case 1://Welcome Screen
                    gameState = 2;

                    break;
                case 2:// Play game
                    //ArrayList<GameObject> arrayShip = gamePlayState.getArrayShip();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        x = event.getX();
                        y = event.getY();
                        checkDrag = false;
                        System.out.println("ACTION_UP");
                        break;
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN && stepGame == 1) {
                        for (GameObject ship : arrayShip) {
                            if (ship.checkIsCollitionPoint(event.getX(), event.getY())) {
                                xDrag = event.getX();
                                yDrag = event.getY();
                                shipp = ship;
                                checkDrag = true;
                                System.out.println("ACTION_DOWN");
                                break;
                            }
                        }
                        break;
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE && checkDrag && stepGame == 1) {
                        System.out.println("ACTION_MOVE");
                        shipp.setX(event.getX());
                        shipp.setY(event.getY());
                    }
                    if (stepGame == 2) {
                        x = 0;
                        y = 0;
                    }
                    break;
                default:
                    gameState = 1;
                    break;
            }
        }
        return true;
    }

//    @SuppressLint("WrongCall")
//    @Override
//    protected void onDraw(Canvas canvas) {
//        //canvas.drawColor(Color.BLACK);
//
//        switch (gameState) {
//            case 1: //Splash State
//                splashState.draw(canvas);
//                // tap to start.
//
//                try {
//                    Thread.sleep(1000);
//                    delay++;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (delay == 2) {
//                    gameState = 2;
//                }
//                break;
//            case 2: //Play game
//                gamePlayState.update(x, y, dt);
//                gamePlayState.draw(canvas);
////                if (y != 0.0f)
////                    y -= 50;
//
//                break;
//            default:
//                gameState = 1;
//                break;
//        }
//    }


    public void setxMsg(float xMsg) {
        this.xMsg = xMsg;
    }

    @Override
    public void run() {
        System.out.println("Đang chạy nè");
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }
}
