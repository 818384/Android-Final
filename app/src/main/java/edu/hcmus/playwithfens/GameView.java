package edu.hcmus.playwithfens;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private MainActivity activity;
    private Context context;
    //private GameLoopThread gameLoopThread;
    private Thread thread;
    private boolean firstStart = true;
    private int gameState = 0; // 1 Splash State, 2 Play.
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
    private Bitmap background2;
    private Bitmap backgroundWinner;
    private Bitmap backgroundLoser;
    private GameObject backgroundGame;
    private GameObject rocket;
    private ArrayList<GameObject> arrayShip = new ArrayList<GameObject>();
    private GameObject btnStart;
    private GameObject btnFeature1;
    private GameObject btnFeature2;
    private ViewGroup.LayoutParams myLayout;
    private int widthScreen;
    private int heightScreen;
    private float YDes = 0.0f;
    private int stepGame = 0; // Di chuyển các chiến hạm.
    private float xMsg = 0;
    private boolean changBackground = false;

    private BroadcastReceiver mReceiver;
    private WifiManager wifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private IntentFilter mIntentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;
    static final int MESSAGE_READ = 1;
    private GameObject btnDiscovery;
    private Bitmap discovery;
    private Bitmap discoveryStart;
    private Bitmap discoveryFailed;
    private ArrayList<GameObject> arrayButtonDevice = new ArrayList<GameObject>();
    private ArrayList<GameObject> arrayShipEnemy = new ArrayList<GameObject>();
    private ArrayList<GameObject> arrayTempShipEnemy = new ArrayList<GameObject>();
    private GameObject rocketEnemy;
    private boolean hostPlay = true;
    private boolean clientPlay = true;
    private String play = "";
    private int runPlay = 0;
    private boolean drawShipEnemy = false;
    private boolean winer = false;
    private boolean loser = false;
    private GameObject btnHomeDiscovery;
    private int shipLive = 4;
    private int shipEnemyLive = 4;
    private boolean fireWall = false;
    private boolean hostSwitchFireWall = false;
    private boolean clientSwitchFireWall = false;
    private boolean sound = true;

    private ServerClass serverClass;
    private ClientClass clientClass;
    private SendReceive sendReceive;
    private SoundPool soundPool;
    private int hitSoundId;
    private int firingSoundId;

    public GameView(Context context, MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.context = context;

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.widthScreen = displayMetrics.widthPixels;
        this.heightScreen = displayMetrics.heightPixels;

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        hitSoundId = soundPool.load(getContext(), R.raw.explosion, 1);
        firingSoundId = soundPool.load(getContext(), R.raw.missle_fire, 1);
        // Khởi tạo màn hình tìm kiếm đối thủ.
        exqListener();
        discovery = getBitmapFromSvg(getContext(), R.drawable.btn_discover);
//        discovery = BitmapFactory.decodeResource(getResources(), R.drawable.btn_discover, option);
        discoveryStart = discovery;
//        discoveryStart = BitmapFactory.decodeResource(getResources(), R.drawable.btn_discoverystart, option);
        discoveryFailed = BitmapFactory.decodeResource(getResources(), R.drawable.btn_discoveryfailed, option);
        System.out.println(this.widthScreen + " - " + this.heightScreen);
        btnDiscovery = new GameObject(this.widthScreen / 2, this.heightScreen / 15, discovery);


        // Khởi tạo màn khởi đầu.
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_final, option);
        background2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_firewall, option);
        backgroundWinner = background2;
//        backgroundWinner = BitmapFactory.decodeResource(getResources(), R.drawable.winner, option);
        backgroundLoser = background2;
//        backgroundLoser = BitmapFactory.decodeResource(getResources(), R.drawable.loser, option);
        backgroundGame = new GameObject(0, 0, background);

        // Rocket.
        Bitmap rocketBitmap = getBitmapFromSvg(getContext(), R.drawable.rocket_orange);
//        Bitmap rocketBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rocket_100, option);
        rocket = new GameObject(rocketBitmap);
        rocket.setLive(false);
        // Nút bắt đầu khi sắp tàu xong.
        Bitmap startButton = getBitmapFromSvg(getContext(), R.drawable.btn_start);
//        Bitmap startButton = BitmapFactory.decodeResource(getResources(), R.drawable.btn_start, option);
        btnStart = new GameObject(this.widthScreen / 2, this.heightScreen / 2, startButton);
        Bitmap feature1 = getBitmapFromSvg(getContext(), R.drawable.btn_firewall);
//        Bitmap feature1 = BitmapFactory.decodeResource(getResources(), R.drawable.btn_feature1, option);
        btnFeature1 = new GameObject(this.widthScreen / 2.0f, this.heightScreen / 20, feature1);
        Bitmap feature2 = getBitmapFromSvg(getContext(), R.drawable.btn_soundon);
//        Bitmap feature2 = new BitmapFactory().decodeResource(getResources(), R.drawable.btn_feature2, option);
        btnFeature2 = new GameObject(this.widthScreen - 60, this.heightScreen / 20, feature2);
        Bitmap homeDiscovery = getBitmapFromSvg(getContext(), R.drawable.btn_homediscovery);
//        Bitmap homeDiscovery = BitmapFactory.decodeResource(getResources(), R.drawable.btn_homediscovery, option);
        btnHomeDiscovery = new GameObject(this.widthScreen / 2, this.heightScreen / 2, homeDiscovery);
        btnHomeDiscovery.setLive(false);
        // Sắp 4 tàu ngẫu nhiên
        ArrayList<Float> arrayTemp = new ArrayList<Float>();
        Bitmap shipBitmap = getBitmapFromSvg(getContext(), R.drawable.orange_ship);
//        Bitmap shipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.orange_ship, option);

        for (int i = 0; i < 4; i++) {
            Random rd = new Random();
            float x, y;
            do {
                x = rd.nextInt((this.widthScreen - 0 + 1) + 0);
                for (int j = 0; j < arrayTemp.size(); j++) {
                    if (arrayTemp.get(j) == x)
                        x = -1.0f;
                }
            } while (x == -1.0f);
            arrayTemp.add(x);
            //float y = rd.nextInt((this.heightScreen - this.heightScreen/2 + 1) + this.heightScreen/2);
            do {
                y = rd.nextInt((this.heightScreen - this.heightScreen / 2 + 1) + this.heightScreen / 2);

            } while (y < this.heightScreen / 2 || y > this.heightScreen - 50);
            String nameShip = "ship" + (i + 1);
//            int resourceId = getResources().getIdentifier(nameShip, "drawable", this.context.getPackageName());
//            Bitmap shipBitmap = BitmapFactory.decodeResource(getResources(), resourceId, option);
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


    private float getYOfPointSymmetry(float y) {
        return (heightScreen / 2.0f) - ((heightScreen / 2.0f) - (heightScreen - y));
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            RectF dst = new RectF(0, 0, 0 + getWidth(), 0 + getHeight());
            canvas.drawBitmap(backgroundGame.getBitmap(), null, dst, null);


            switch (stepGame) {
                case 0:// kết nối.
                    if (btnDiscovery.isLive())
                        canvas.drawBitmap(btnDiscovery.getBitmap(), null, btnDiscovery.getDstRectF(), null);
                    if (arrayButtonDevice.size() > 0) {
                        for (GameObject buttonDeviceName : arrayButtonDevice) {
                            if (buttonDeviceName.isLive())
                                canvas.drawBitmap(buttonDeviceName.getBitmap(), null, buttonDeviceName.getDstRectF(), null);
                        }
                    }
                    break;
                case 1:
                    canvas.drawBitmap(btnStart.getBitmap(), null, btnStart.getDstRectF(), null);
                    for (GameObject ship : arrayShip) {
                        if (ship.isLive()) {
                            canvas.drawBitmap(ship.getBitmap(), null, ship.getDstRectF(), null);
                        }
                    }
                    break;
                case 2:
                    //do{
                    if (btnFeature1.isLive()) {
                        canvas.drawBitmap(btnFeature1.getBitmap(), null, btnFeature1.getDstRectF(), null);
                    }
                    if (btnFeature2.isLive()) {
                        canvas.drawBitmap(btnFeature2.getBitmap(), null, btnFeature2.getDstRectF(), null);
                    }
                    if (rocket.isLive() && rocket.getY() != 0) {
                        canvas.drawBitmap(rocket.getBitmap(), null, rocket.getDstRectF(), null);
                    }
                    if (rocketEnemy != null) {
                        if (rocketEnemy.isLive()) {
                            canvas.drawBitmap(rocketEnemy.getBitmap(), null, rocketEnemy.getDstRectF(), null);
                        }
                    }
                    for (GameObject ship : arrayShip) {
                        if (ship.isLive()) {
                            canvas.drawBitmap(ship.getBitmap(), null, ship.getDstRectF(), null);
                        }
                    }
                    if (arrayShipEnemy.size() == 4 /*&& drawShipEnemy*/) {
                        //System.out.println("Tau chien dich co so luong: " + arrayShipEnemy.size());
                        for (GameObject ship : arrayShipEnemy) {
                            if (ship.isLive()) {
                                canvas.drawBitmap(ship.getBitmap(), null, ship.getDstRectF(), null);
                            }
                        }
                        drawShipEnemy = false;
                    }
                    if (btnHomeDiscovery.isLive()) {
                        canvas.drawBitmap(btnHomeDiscovery.getBitmap(), null, btnHomeDiscovery.getDstRectF(), null);
                    }

                    //System.out.println(rocket.getY());
                    //}while (rocket.run() >= YDes);

//                canvas.drawBitmap(rocket.getBitmap(), null, rocket.getDstRectF(), null);
//                if (rocket.run() < 0)
//                    rocket.setCheckLock(false);
                    break;
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        switch (stepGame) {
            case 0:
                if (btnDiscovery.checkIsCollitionPoint(x, y) && btnDiscovery.isLive()) {
                    eventButtonDiscovery();
                    btnDiscovery.setLive(false);
                    arrayButtonDevice.clear();
                }
                for (int i = 0; i < arrayButtonDevice.size(); i++) {
                    if (arrayButtonDevice.get(i).checkIsCollitionPoint(x, y) && arrayButtonDevice.get(i).isLive()) {
                        connectDevice(i);
                        arrayButtonDevice.get(i).setLive(false);
                        break;
                    }
                }
                break;
            case 1:
                winer = false;
                loser = false;
                fireWall = false;
                btnHomeDiscovery.setLive(false);
                for (GameObject ship : arrayShip) {
                    ship.setLive(true);
                    ship.setCheckLock(false);
                }
                if (btnStart.checkIsCollitionPoint(x, y)) {
                    stepGame = 2;
                    gameState = 2;
                    rocket.setLive(true);
                    rocket.setCheckLock(false);
                    for (GameObject ship : arrayShip) {
                        ship.setCheckLock(true);
                    }
                    System.out.println("STEP GAME = " + stepGame);
                }
//                if (serverClass != null){
//                    if (serverClass.getIsConnectedToClient()){
//                        serverClass.getSendReceive().write("hi toi la host".getBytes());
//                        System.out.println("Đã gửi đi một gói tin từ Server");
//                        serverClass.setIsConnectedToClient(false);
//                    }
//                }
//                if (clientClass != null){
//                    if (clientClass.getIsConnectedToServer()){
//                        clientClass.getSendReceive().write(SendData().getBytes());
//                        System.out.println("Đã gửi đi một gói tin từ Client " + SendData());
//                        clientClass.setIsConnectedToServer(false);
//                    }
//                }
                break;
            case 2:
                if (clientClass != null && clientClass.getIsConnectedToServer()) {
                    if (clientClass.getIsConnectedToServer() && clientPlay) {
                        clientClass.getSendReceive().write(SendData().getBytes());
                        //System.out.println("Đã gửi đi một gói tin từ Client " + SendData());
                        //clientClass.setIsConnectedToServer(false);
                        //clientPlay = false;
                    }
                }
                if (serverClass != null && serverClass.getIsConnectedToClient()) {
                    if (serverClass.getIsConnectedToClient() && hostPlay) {
                        serverClass.getSendReceive().write(SendData().getBytes());
                        //System.out.println("Đã gửi đi một gói tin từ Server" + SendData());
                        //serverClass.setIsConnectedToClient(false);
                        //hostPlay = false;
                    }
                }
                if (winer) {
                    rocket.setLive(false);
                    for (GameObject ship : arrayShip) {
                        ship.setLive(false);
                    }
//                    for (GameObject shipE : arrayShipEnemy) {
//                        shipE.setLive(false);
//                    }
                    arrayShipEnemy.clear();
                    rocketEnemy.setLive(false);
                    btnFeature1.setLive(false);
                    btnFeature2.setLive(false);
                    backgroundGame.setBitmap(backgroundWinner);
                    btnHomeDiscovery.setLive(true);
                    disconnect();
                    if (serverClass != null)
                        serverClass.CloseSocket();
                    if (clientClass != null)
                        clientClass.CloseSocket();
                }
                if (loser) {
                    rocket.setLive(false);
                    for (GameObject ship : arrayShip) {
                        ship.setLive(false);
                    }
//                    for (GameObject shipE : arrayShipEnemy) {
//                        shipE.setLive(false);
//                    }
                    arrayShipEnemy.clear();
                    rocketEnemy.setLive(false);
                    btnFeature1.setLive(false);
                    btnFeature2.setLive(false);
                    backgroundGame.setBitmap(backgroundLoser);
                    btnHomeDiscovery.setLive(true);
                    disconnect();
                    if (serverClass != null)
                        serverClass.CloseSocket();
                    if (clientClass != null)
                        clientClass.CloseSocket();
                }
                //clientPlay = !hostPlay;
                // Bật tính năng 1: Qua tường lửa thì không thấy tên lửa.
                if (btnFeature1.checkIsCollitionPoint(x, y) && btnFeature1.isLive()) {
                    btnFeature1.setLive(false);
                    fireWall = true;
                    if (play == "host"){
                        hostSwitchFireWall = true;
                        clientSwitchFireWall = false;
                    }
                    if (play == "client"){
                        hostSwitchFireWall = false;
                        clientSwitchFireWall = true;
                    }
                }
                if (fireWall == true){

                    if (!rocketEnemy.isLive() && (hostSwitchFireWall == true || clientSwitchFireWall == true)){
                        fireWall = false;
                        changBackground = false;
                        TurnOnFireWall(changBackground);
                        if (play == "host"){
                            hostSwitchFireWall = false;
                            clientSwitchFireWall = false;
                        }
                        if (play == "client"){
                            hostSwitchFireWall = false;
                            clientSwitchFireWall = false;
                        }
                    }
                    else if (!rocket.isLive() && hostSwitchFireWall == false && clientSwitchFireWall == false){
                        fireWall = false;
                        changBackground = false;
                        TurnOnFireWall(changBackground);
                        if (play == "host" && hostSwitchFireWall == true){
                            hostSwitchFireWall = false;
                            clientSwitchFireWall = false;
                        }
                        if (play == "client" && clientSwitchFireWall == true){
                            hostSwitchFireWall = false;
                            clientSwitchFireWall = false;
                        }
                    }
                    else if (!rocketEnemy.isLive() && hostSwitchFireWall == false && clientSwitchFireWall == false){
                        fireWall = false;
                        changBackground = false;
                        TurnOnFireWall(changBackground);
                        if (play == "host" && hostSwitchFireWall == true){
                            hostSwitchFireWall = false;
                            clientSwitchFireWall = false;
                        }
                        if (play == "client" && clientSwitchFireWall == true){
                            hostSwitchFireWall = false;
                            clientSwitchFireWall = false;
                        }
                    }

                    else if (fireWall == true){
                        changBackground = true;
                        TurnOnFireWall(changBackground);
                    }
                }
                if (btnFeature2.checkIsCollitionPoint(x, y) && btnFeature2.isLive()) {
                    if (sound){
                        sound = false;
                        Bitmap feature2 = getBitmapFromSvg(getContext(), R.drawable.btn_soundoff);
                        btnFeature2.setBitmap(feature2);
                    }
                    else{
                        sound = true;
                        Bitmap feature2 = getBitmapFromSvg(getContext(), R.drawable.btn_soundon);
                        btnFeature2.setBitmap(feature2);
                    }
                    x = rocket.getX();
                }
                if (rocket.isLive() && !rocket.isCheckLock()) {
//                    rocket.setX(x);
//                    rocket.setY(y);
//                    rocket.setCheckLock(true);
//                    YDes = getYOfPointSymmetry(YDes);
                    if (YDes == 0.0f && y != 0.0f) {
                        YDes = y;
                        YDes = getYOfPointSymmetry(YDes);
                        rocket.setyDead(YDes);
                        //System.out.println(YDes);
                    }
                    if (y >= YDes && y != 0.0f && y > heightScreen / 2.0f) {
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
                } else {
                    /*if (serverClass != null && hostPlay)*/
                    if(sound){
                        soundPool.play(firingSoundId, 1, 1, 0, 0, 1);
                    }
                    rocket.run();
//                    if (clientClass != null && clientPlay)
//                        rocket.run();
                    for (GameObject ship : arrayShipEnemy) {
                        if (rocket.isLive() && ship.isLive()){
                            if (rocket.checkIsCollition(ship) && rocket.getY() >= YDes - 10 && rocket.getY() <= YDes + 10) {
                                System.out.println("VA CHAM");
                                if(sound){
                                    soundPool.play(hitSoundId, 1, 1, 0, 0, 1);
                                }
                                rocket.setLive(false);
                                ship.setLive(false);
                                shipEnemyLive--;
                                YDes = 0.0f;
//                            if (hostPlay){
//                                play = "client";
//                                hostPlay = false;
//                                clientPlay = true;
//                            }
//                            else{
//                                play = "host";
//                                hostPlay = true;
//                                clientPlay = false;
//                            }
                                if (shipEnemyLive == 0){
                                    winer = true;
                                    loser = false;
                                }
                            }
                        }

                    }
                    if (rocket.getY() <= 0) {
                        rocket.setLive(false);
                        YDes = 0.0f;
                    }
                }
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            switch (gameState) {
                case 0:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        x = event.getX();
                        y = event.getY();
                        System.out.println("TouchEvent button Discovery");
                    }
                    break;
                case 1://Welcome Screen
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        x = event.getX();
                        y = event.getY();
                        checkDrag = false;
                        System.out.println("ACTION_UP");
                        break;
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN && (stepGame == 1 || stepGame == 2)) {
                        for (GameObject ship : arrayShip) {
                            if (ship.checkIsCollitionPoint(event.getX(), event.getY()) && ship.isCheckLock() == false) {
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
                    if (event.getAction() == MotionEvent.ACTION_MOVE && checkDrag && (stepGame == 1 || stepGame == 2)
                            && shipp != null && shipp.isCheckLock() == false && event.getY() > heightScreen / 2.0f + 50) {
                        System.out.println("ACTION_MOVE");
                        shipp.setX(event.getX());
                        shipp.setY(event.getY());
                    }
                    break;
                case 2:// Play game
                    //ArrayList<GameObject> arrayShip = gamePlayState.getArrayShip();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        x = event.getX();
                        y = event.getY();
//                        checkDrag = false;
                        if (!rocket.isLive()) {
                            rocket.setLive(true);
                            if (rocket.isCheckLock()) {
                                rocket.setCheckLock(false);
                            }
                        }
//                        for (GameObject ship : arrayShip) {
//                            ship.setCheckLock(true);
//                        }
                        if (btnHomeDiscovery.checkIsCollitionPoint(x, y) && btnHomeDiscovery.isLive()) {
                            eventButtonHomeDiscovery();
                        }
                        System.out.println("ACTION_UP");
                        break;
                    }

                    if (stepGame == 2) {
                        x = 0;
                        y = 0;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    // Bật tường lửa.
    private void TurnOnFireWall(boolean valueFireWall) {
        if (valueFireWall) {
            backgroundGame.setBitmap(background2);
            if(!hostSwitchFireWall && !clientSwitchFireWall)
            {
                rocket.setyDead(this.heightScreen / 2);
            }
            if (!btnFeature1.isLive()){
                btnFeature1.setCheckLock(true);
            }
        } else {
            backgroundGame.setBitmap(background);
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
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this.activity, this);
        this.activity.registerReceiver(mReceiver, mIntentFilter);
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            this.activity.unregisterReceiver(mReceiver);
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        //System.out.println("Đang chạy nè");
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    public Bitmap textToBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        float baseline = -paint.ascent(); // ascent() is negative
//        int width = (int) (paint.measureText(text) + 0.5f); // round
//        int height = (int) (baseline + paint.descent() + 0.5f);
        int width = discovery.getWidth();
        int height = discovery.getHeight();
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, width / 2, baseline, paint);
        return image;
    }

    /*----------------------------------------- CONNECT WIFI AND SYNC DATA -----------------------------------------*/
    private void exqListener() {

        wifiManager = (WifiManager) this.activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) this.activity.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this.context, this.activity.getMainLooper(), null);

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this.activity, this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String msg = writeMsg.getText().toString();
//                byte[] ms = msg.getBytes();
//                sendReceive.write(ms);
//            }
//        });
//        final WifiP2pDevice device = deviceArray[0];
//        WifiP2pConfig config = new WifiP2pConfig();
//        config.deviceAddress = device.deviceAddress;
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CHANGE_WIFI_MULTICAST_STATE}, 1);
//        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);
//
//
//        if (ActivityCompat.checkSelfPermission(this.context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(context.getApplicationContext(), "Connected to " + arrayButtonDevice.size(), Toast.LENGTH_SHORT).show();
//                //checkGamePlay = true;
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Toast.makeText(context.getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    private void initialWork() {
//        btnOnOff = (Button) findViewById(R.id.onOff);
//        btnDiscovery = (Button) findViewById(R.id.discover);
//        btnSend = (Button) findViewById(R.id.sendButton);
//        listView = (ListView) findViewById(R.id.peerListView);
//        read_msg_box = (TextView) findViewById(R.id.readMsg);
//        connectionStatus = (TextView) findViewById(R.id.connectionStatus);
//        writeMsg = (EditText) findViewById(R.id.writeMsg);
//        writeMsg.setText("asdasd");
//
//
//        btnOnOff.setEnabled(false);
//
//        wifiManager = (WifiManager) this.activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        mManager = (WifiP2pManager) this.activity.getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this.context, this.activity.getMainLooper(), null);
//
//        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this.activity);
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//    }

    private void eventButtonDiscovery() {
        System.out.println("Event button Discovery");
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this.context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    //connectionStatus.setText("Discovery Started");
                    btnDiscovery.setBitmap(discoveryStart);
                }

                @Override
                public void onFailure(int i) {
                    //connectionStatus.setText("Discovery Starting Failed");
                    btnDiscovery.setBitmap(discoveryFailed);
                }
            });
        }
    }

    private void eventButtonHomeDiscovery() {
        backgroundGame.setBitmap(background);
        btnDiscovery.setLive(true);
        gameState = 0;
        stepGame = 0;
    }

    private void connectDevice(int index) {
        final WifiP2pDevice device = deviceArray[index];
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        System.out.println("Đang cố kết nối với: " + device.deviceName);

        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CHANGE_WIFI_MULTICAST_STATE}, 1);
        ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 1);


        if (ActivityCompat.checkSelfPermission(this.activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity.getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                //checkGamePlay = true;
//                        if(checkGamePlay){
//                            gameView = new GameView(getApplicationContext(), MainActivity.this);
//                            setContentView(gameView);
//                            btnSend.callOnClick();
//                            System.out.println("Qua man choi");
//
//                        }

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(activity.getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void disconnect() {
        //if (mManager != null && mChannel != null) {
        if (ActivityCompat.checkSelfPermission(this.activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                if (group != null && mManager != null && mChannel != null) {
                    mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "removeGroup onSuccess", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "removeGroup onSuccess -");
                        }

                        @Override
                        public void onFailure(int reason) {
                            Toast.makeText(context, "removeGroup onFailure", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "removeGroup onFailure -" + reason);
                        }
                    });
                }
            }
        });
        //}
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
                System.out.println("Vào hàm");
            if (!peerList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());
                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                arrayButtonDevice.clear();
                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    System.out.println("Đã thấy: " + device.deviceName);
                    Bitmap newDeviceName = textToBitmap(device.deviceName, 35, 22);
                    GameObject buttonDevice = new GameObject(500, 500, newDeviceName);
                    arrayButtonDevice.add(buttonDevice);
                    index++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getApplicationContext(),
                        android.R.layout.simple_list_item_1, deviceNameArray);
                //listView.setAdapter(adapter);
            }

            if (peers.size() == 0) {
                Toast.makeText(context.getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT);
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener()
    {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                //connectionStatus.setText("Host");
                serverClass = new ServerClass(handler);
                serverClass.start();
                gameState = 1;
                stepGame = 1;
                for (GameObject buttonDevice : arrayButtonDevice){
                    buttonDevice.setLive(false);
                }
                Toast.makeText(context, "HOST", Toast.LENGTH_SHORT).show();
                play = "host";
                //btnOnOff.setEnabled(true);

            } else if (info.groupFormed) {
                //connectionStatus.setText("Client");
                clientClass = new ClientClass(groupOwnerAddress, handler);
                clientClass.start();
                gameState = 1;
                stepGame = 1;
                for (GameObject buttonDevice : arrayButtonDevice){
                    buttonDevice.setLive(false);
                }
                Toast.makeText(context, "CLIENT", Toast.LENGTH_SHORT).show();
                play = "client";
                //clientClass.getSendReceive().write("hi toi la client".getBytes());
                //btnOnOff.setEnabled(true);
            }
        }
    };

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what)
            {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    //read_msg_box.setText(tempMsg);
                    //gameView.setxMsg(Float.valueOf(tempMsg));
                    //Toast.makeText(context, tempMsg, Toast.LENGTH_SHORT).show();
                    String[] gameObject = tempMsg.split(";");
//                    System.out.println("So luong gui qua " + gameObject.length);
//                    for (int i = 0; i < gameObject.length; i ++){
//                        System.out.println("Noi dung gui qua " + gameObject[i]);
//                    }
                    System.out.println("GameObject length: "  + gameObject.length);
                    if (gameObject.length == 17 || gameObject.length == 29/*&& !drawShipEnemy*/){

                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inMutable = true;
                        // Lấy tàu.
                        int indexShip = 1;
                        int indexShipE = 0;
                        Bitmap shipEnemy = getBitmapFromSvg(getContext(), R.drawable.green_ship);
//                        Bitmap shipEnemy = BitmapFactory.decodeResource(getResources(), R.drawable.green_ship, option);
                        for (int i = 0; i < 12;){
//                            String[] detailShip = gameObject[i].split("`;`");
//                            System.out.println("So luong detail " + detailShip.length);
//                            System.out.println("Noi dung detail " + detailShip[0] + " --- " + detailShip[1] + " --- " + detailShip[2]);
                            if (arrayShipEnemy.size() == 4){
                                arrayShipEnemy.get(indexShipE).setX(Float.parseFloat(gameObject[i]));
                                arrayShipEnemy.get(indexShipE).setY(getYOfPointSymmetry(Float.parseFloat(gameObject[i + 1])));
                                if (arrayShipEnemy.get(indexShipE).isLive())
                                    arrayShipEnemy.get(indexShipE).setLive(Boolean.parseBoolean(gameObject[i + 2]));
                            }
                            else{
                                boolean isLive = Boolean.parseBoolean(gameObject[i + 2]);
                                String nameShip = "ship_enemy" + (indexShip);
//                                int resourceId = getResources().getIdentifier(nameShip, "drawable", context.getPackageName());
//                                Bitmap shipEnemy = BitmapFactory.decodeResource(getResources(), resourceId, option);
//                            System.out.println("ship x: " + Float.parseFloat(detailShip[0]) + "y: " + Float.parseFloat(detailShip[1]) + "live: " + isLive);
                                GameObject ship = new GameObject(Float.parseFloat(gameObject[i]), getYOfPointSymmetry(Float.parseFloat(gameObject[i + 1])), shipEnemy);
                                ship.setLive(isLive);
                                arrayShipEnemy.add(ship);
                            }
                            i += 3;
                            indexShip++;
                            indexShipE++;
                        }
                        // Lấy rocket.
//                        String[] rocketEnemyString = gameObject[gameObject.length - 1].split("`;`");
//                        System.out.println("lay rocket string" + gameObject[gameObject.length - 1]);
//                        System.out.println("x: " + rocketEnemyString[0] + "y: " + rocketEnemyString[1] + "live: " + rocketEnemyString[2]);
                        for (int i = 12; i < 15; i += 3){
                            try{
                                float xRocketEnemy = Float.parseFloat(gameObject[i]);
//                            System.out.println("xRocketEnemy" + xRocketEnemy);
                                float yRocketEnemy = Float.parseFloat(gameObject[i + 1]);
//                            System.out.println("yRocketEnemy" + yRocketEnemy);
                                boolean isLive = Boolean.valueOf(gameObject[i + 2]);
                                if (rocketEnemy != null){
                                    rocketEnemy.setX(xRocketEnemy);
                                    rocketEnemy.setY(heightScreen - yRocketEnemy);
                                    rocketEnemy.setLive(isLive);
                                }
                                else{
                                    Bitmap rocket100Enemy = getBitmapFromSvg(getContext(), R.drawable.rocket_green);
//                                    Bitmap rocket100Enemy = BitmapFactory.decodeResource(getResources(), R.drawable.rocket_100_enemy, option);
                                    rocketEnemy = new GameObject(xRocketEnemy, heightScreen - yRocketEnemy, rocket100Enemy);
                                    rocketEnemy.setLive(isLive);
                                }
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                        }

                        indexShipE = 0;
                        if (gameObject.length > 17){
                            for (int i = 15; i < 27; i += 3){
                                if (!Boolean.valueOf(gameObject[i + 2])){
                                    arrayShip.get(indexShipE).setLive(Boolean.valueOf(gameObject[i + 2]));
                                    if (Boolean.valueOf(gameObject[i + 2]) == false)
                                        shipLive--;
                                }
                                indexShipE++;
                            }
//                            hostPlay = Boolean.valueOf(gameObject[27]);
//                            clientPlay = !hostPlay;
                            loser = Boolean.valueOf(gameObject[27]);
                            if (!fireWall)
                                fireWall = Boolean.valueOf(gameObject[28]);
                            System.out.println(fireWall);
                        }
                        else
                        {
//                            hostPlay = Boolean.valueOf(gameObject[15]);
//                            clientPlay = !hostPlay;
                            loser = Boolean.valueOf(gameObject[15]);
                            if (!fireWall)
                                fireWall = Boolean.valueOf(gameObject[16]);
                        }
                    }
                    break;
            }
            return true;
        }
    });

    private String SendData() {
        String data = "";
        // Lấy tàu hiện tại.
        for (GameObject ship : arrayShip){
            data += ship.getX() + ";" + ship.getY() + ";" + ship.isLive() + ";";
        }
        // Lấy rocket hiện tại.
        if (arrayShipEnemy.size() == 4){
            data += rocket.getX() + ";" + rocket.getY() + ";" + rocket.isLive() + ";";
        }
        else{
            data += rocket.getX() + ";" + rocket.getY() + ";" + rocket.isLive() + ";";
            data += winer + ";";
            data += fireWall;
        }
        // Lấy tàu đội địch.
        if (arrayShipEnemy.size() == 4){
            for (GameObject ship : arrayShipEnemy){
                data += ship.getX() + ";" + ship.getY() + ";" + ship.isLive() + ";";
            }
            data += winer + ";";
            data += fireWall;
        }

        return data;
    }

    public Bitmap getBitmapFromSvg(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public boolean isWiner() {
        return winer;
    }

    public void setWiner(boolean winer) {
        this.winer = winer;
    }

    public boolean isLoser() {
        return loser;
    }

    public void setLoser(boolean loser) {
        this.loser = loser;
    }

    public int getGameState(){
        return gameState;
    }

    public int getStepGame(){
        return stepGame;
    }

    public int getShipLive() {
        return shipLive;
    }

    public int getShipEnemyLive() {
        return shipEnemyLive;
    }

    public boolean isFireWall() {
        return fireWall;
    }
}
