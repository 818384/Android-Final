package edu.hcmus.playwithfens;

import android.graphics.Bitmap;

public class GameObject {
    private float x = 0;
    private float y = 0;
    private boolean isLive = true;
    private Bitmap bitmap;

    public GameObject(int x, int y, Bitmap bitmap) {
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
        if(this.x < x &&
                this.x + this.bitmap.getWidth() > x &&
                this.y + this.bitmap.getHeight() > y &&
                this.y < y )
        {
            return true;
        }
        else {
            return false;
        }
    }
}
