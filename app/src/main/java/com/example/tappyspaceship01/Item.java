package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;





public class Item {

    private Bitmap image;
    private int xPosition;
    private int yPosition;
    private Bitmap picture;
    private Rect hitbox;



    public Item(Context context, int x, int y, Bitmap image, Rect hitbox ) {
        // 1. set up the initial position of the Enemy
        this.xPosition = x;
        this.yPosition = y;
        this.picture = image;
        this.hitbox = hitbox;


    }


    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }



    }

