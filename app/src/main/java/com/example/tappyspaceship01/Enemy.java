package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

public class Enemy {

    // PROPERTIES:
    // Image
    // Hitbox
    private Bitmap image;
    private Bitmap bullet;
    private Rect hitbox;
    private Bitmap image2;
    private Bitmap image3;

    public int xPosition;
    public int yPosition;



    ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
//bitmapArray.add(myBitMap); // Add a bitmap
//bitmapArray.get(0); // Get first bitmap

    // Give your enemy some bullets
    // I am using a Rect object because I am lazy and
    // I don't want to manage another set of hitbox
    // I will be using the Rect object's built in interect functions
    private ArrayList<Rect> bullets = new ArrayList<Rect>();
    private final int BULLET_WIDTH = 35;

    public Enemy(Context context, int x, int y, Bitmap image, Rect hitbox ) {
        // 1. set up the initial position of the Enemy
        this.xPosition = x;
        this.yPosition = y;
        this.image = image;
        this.hitbox = hitbox;


    }

    // Getter and setters
    // Autogenerate this by doing Right Click --> Generate --> Getter&Setter

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

    public ArrayList<Rect> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Rect> bullets) {
        this.bullets = bullets;
    }


    public int getBulletWidth() {
        return BULLET_WIDTH;
    }

    public void updateHitbox() {
        this.hitbox.left = this.xPosition;
        this.hitbox.top = this.yPosition;
        this.hitbox.right = this.xPosition + this.image.getWidth();
        this.hitbox.bottom = this.yPosition + this.image.getHeight();
    }


    // Make a new bullet
    public void spawnBullet() {
        // make bullet come out of middle of enemty
        Rect bullet = new Rect(this.xPosition,
                this.yPosition + this.image.getHeight() / 2,
                this.xPosition + BULLET_WIDTH,
                this.yPosition + this.image.getHeight() / 2 + BULLET_WIDTH
        );




        this.bullets.add(bullet);
    }




}
