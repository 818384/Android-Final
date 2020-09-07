package edu.hcmus.playwithfens;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Map;

public class GameView extends SurfaceView implements Runnable {
    private Map params;
    private int gameState = 1;
    private int delay = 1;
    private Thread thread = null;
    private boolean running = true;
    private SurfaceHolder holder;
    public GameView(Context context) {
        super(context);
        setWillNotDraw(false);
        holder = getHolder();
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        doDraw(canvas);
//        switch (gameState) {
//            case 1: //Splash State
//                doDraw(canvas);
//                // tap to start.
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
//                doDraw(canvas);
//                break;
//            default:
//                gameState = 1;
//                break;
//        }
    }

    private void doDraw(Canvas canvas) {
        if (getParams() != null) {
            canvas.drawBitmap((Bitmap) params.get("BITMAP"), null, (RectF) params.get("RECT"), null);
            setParams(null);
        }
    }

    @Override
    public void run() {
        while (running) {
            if(!holder.getSurface().isValid()){
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            doDraw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        thread = null;
    }

    public void resume() {
        running = true;
        thread = new Thread();
        thread.start();
    }
}
