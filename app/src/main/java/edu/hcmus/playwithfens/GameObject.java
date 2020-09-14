package edu.hcmus.playwithfens;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class GameObject {
    private float x = 0;
    private float y = 0;
    private boolean isLive = true;
    private boolean checkLock = false;
    private float dt;
    private float yDead;
    private Bitmap bitmap;

    public GameObject(float x, float y, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }

    public GameObject(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean checkIsCollition(GameObject gameObject)
    {
        if(this.x < gameObject.getX() + gameObject.getBitmap().getWidth() &&
                        this.x + this.bitmap.getWidth() > gameObject.getX() &&
                        this.y + this.bitmap.getHeight() > gameObject.getY() &&
                        this.y < gameObject.getY() + gameObject.getBitmap().getHeight())
        {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean checkIsCollitionPoint(float x, float y)
    {
        if(this.x <= x &&
                this.x + this.bitmap.getWidth() >= x &&
                this.y + this.bitmap.getHeight() >= y &&
                this.y <= y )
        {
            return true;
        }
        else {
            return false;
        }
    }

    public RectF getDstRectF(){
        float left = (x - bitmap.getWidth() / 2.0f);
        float top = (y - bitmap.getHeight() / 2.0f);
        RectF dst = new RectF(left, top, left + bitmap.getWidth(), top + bitmap.getHeight());
        return dst;
    }

    public float run(){
        if (this.y <= yDead){
            isLive = false;
        }
        return this.y = this.y - 10;
    }

    public void setyDead(float ydie){
        this.yDead = ydie;
    }

    public boolean isCheckLock() {
        return checkLock;
    }

    public void setCheckLock(boolean checkLock) {
        this.checkLock = checkLock;
    }

    public void setDt(float dt) {
        this.dt = dt;
    }

    public float getDt() {
        return dt;
    }
}
