package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;


public class Square {



    private Bitmap image;
    private Rect hitbox;
    public int xPosition;
    public int yPosition;
    public int width;
    private int speed;
    private ArrayList<Rect> bullets = new ArrayList<Rect>();
    private final int BULLET_WIDTH = 15;

    public Square(Context context, int x, int y, int width, int speed) {
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.speed = speed;


        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_ship);

        // 3. Set the default hitbox - all enemies have same hitbox
        this.hitbox = new Rect(
                this.xPosition,
                this.yPosition,
                this.xPosition + this.image.getWidth(),
                this.yPosition + this.image.getHeight()
        );
    }


    public int getSpeed() {
        return this.speed;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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


    public void updateHitbox() {
        this.hitbox.left = this.xPosition;
        this.hitbox.top = this.yPosition;
        this.hitbox.right = this.xPosition + this.image.getWidth();
        this.hitbox.bottom = this.yPosition + this.image.getHeight();
    }

    public ArrayList<Rect> getBullets() {
        return bullets;
    }
    public void spawnBullet() {
        // make bullet come out of middle of enemty
        Rect bullet = new Rect(this.xPosition + image.getWidth(),
                this.yPosition + this.image.getHeight() / 2,
                this.xPosition + BULLET_WIDTH + image.getWidth(),
                this.yPosition + this.image.getHeight() / 2 + BULLET_WIDTH
        );

        this.bullets.add(bullet);
    }
    public void setBullets(ArrayList<Rect> bullets) {
        this.bullets = bullets;
    }


    public int getBulletWidth() {
        return BULLET_WIDTH;
    }

}

