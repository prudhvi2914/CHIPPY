package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {





    // Android debug variables
    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;



    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------
    List<Enemy> enemyList = new ArrayList<Enemy>();
    // ----------------------------
    // ## SPRITES
    // ----------------------------
    ArrayList<Rect> bullets = new ArrayList<Rect>();
    // represent the TOP LEFT CORNER OF THE GRAPHIC
//for toch event
    Player playerpoint;
    Player player;
    Enemy enemy1;
    Enemy enemy2;
    Enemy enemy3;

    Bitmap background;
    int bgXPosition = 0;
    int backgroundRightSide = 0;
    // ----------------------------
    // ## GAME STATS
    // ----------------------------

    int lives = 10;


    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();



        // @TODO: Add your sprites

//

        // put the initial starting position of your player and enemies
        this.player = new Player(getContext(), 100, 600);
        this.enemy1 = new Enemy(getContext(), 1350, 140);
        this.enemy2 = new Enemy(getContext(), 970, 140);
        this.enemy3 = new Enemy(getContext(), 1350, 550);
        enemyList.add(enemy1);
        enemyList.add(enemy2);
        enemyList.add(enemy3);



        // setup the background
        this.background = BitmapFactory.decodeResource(context.getResources(), R.drawable.back1);
        // dynamically resize the background to fit the device
        this.background = Bitmap.createScaledBitmap(
                this.background,
                this.screenWidth,
                this.screenHeight,
                false
        );

        this.bgXPosition = 0;

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    int numLoops = 0;


    public void updatePositions() {

        // UPDATE BACKGROUND POSITION
        // 1. Move the background
        this.bgXPosition = this.bgXPosition - 50;

        backgroundRightSide = this.bgXPosition + this.background.getWidth();
        // 2. Background collision detection
        if (backgroundRightSide < 0) {
            this.bgXPosition = 0;
        }



        numLoops = numLoops + 1;

        // @TODO: Update position of player based on mouse tap
        if (this.fingerAction == "mousedown") {
            // if mousedown, then move player up
            // Make the UP movement > than down movement - this will
            // make it look like the player is moving up alot
            player.setyPosition(player.getyPosition() + 10);
            player.updateHitbox();
        }
        else if (this.fingerAction == "mouseup") {
            // if mouseup, then move player down
            player.setyPosition(player.getyPosition() - 10);
            player.updateHitbox();
        }



        // MAKE ENEMY MOVE
        // - enemy moves left forever
        // - when enemy touches LEFT wall, respawn on RIGHT SIDE
//        this.enemy1.setyPosition(this.enemy1.getyPosition()-20);
//
//        // MOVE THE HITBOX (recalcluate the position of the hitbox)
//        this.enemy1.updateHitbox();
//
//        if (this.enemy1.getyPosition() <= 0) {
//            // restart the enemy in the starting position
//            this.enemy1.setxPosition(1300);
//            this.enemy1.setyPosition(1000);
//
//            this.enemy1.updateHitbox();
//        }




        // DEAL WITH BULLETS

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 20  == 0) {
            this.enemy1.spawnBullet();
        }


        // MOVING THE BULLETS
        int BULLET_SPEED = 20;
        for (int i = 0; i < this.enemy1.getBullets().size();i++) {
            Rect bullet = this.enemy1.getBullets().get(i);
            bullet.left = bullet.left - BULLET_SPEED;
            bullet.right = bullet.right - BULLET_SPEED;
        }

        // COLLISION DETECTION ON THE BULLET AND WALL
        for (int i = 0; i < this.enemy1.getBullets().size();i++) {
            Rect bullet = this.enemy1.getBullets().get(i);

            // For each bullet, check if teh bullet touched the wall
            if (bullet.right < 0) {
                this.enemy1.getBullets().remove(bullet);
            }

        }

        // COLLISION DETECTION BETWEEN BULLET AND PLAYER
        for (int i = 0; i < this.enemy1.getBullets().size();i++) {
            Rect bullet = this.enemy1.getBullets().get(i);

            if (this.player.getHitbox().intersect(bullet)) {
                this.player.setxPosition(100);
                this.player.setyPosition(600);
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size();i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy1.getHitbox().intersect(bullet)) {
              enemyList.remove(enemy1);
              bullets.remove(bullet);
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }



        // DEAL WITH player BULLETS

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 20  == 0) {
            this.player.spawnBullet();
        }


        // MOVING THE BULLETS
//        int BULLET_SPEED = 20;
        for (int i = 0; i < this.player.getBullets().size();i++) {
            Rect bullet = this.player.getBullets().get(i);

            bullet.right = bullet.right + BULLET_SPEED;
            bullet.left = bullet.left + BULLET_SPEED;
        }

        // COLLISION DETECTION ON THE BULLET AND WALL
        for (int i = 0; i < this.player.getBullets().size();i++) {
            Rect bullet = this.player.getBullets().get(i);

            // For each bullet, check if teh bullet touched the wall
            if (bullet.right < 0) {
                this.player.getBullets().remove(bullet);
            }

        }

//        // COLLISION DETECTION BETWEEN BULLET AND PLAYER
//        for (int i = 0; i < this.player.getBullets().size();i++) {
//            Rect bullet = this.player.getBullets().get(i);
//
//            if (this.player.getHitbox().intersect(bullet)) {
//                this.player.setxPosition(100);
//                this.player.setyPosition(600);
//                this.player.updateHitbox();
//                lives = lives - 1;
//            }
//
//        }




        // MAKE ENEMY2 MOVE
        // MAKE ENEMY MOVE
        // - enemy moves left forever
        // - when enemy touches LEFT wall, respawn on RIGHT SIDE
//        this.enemy2.setxPosition(this.enemy2.getxPosition()-25);
//
//        // MOVE THE HITBOX (recalcluate the position of the hitbox)
//        this.enemy2.updateHitbox();
//
//        if (this.enemy2.getxPosition() <= 0) {
//            // restart the enemy in the starting position
//            this.enemy2.setxPosition(1500);
//            this.enemy2.setyPosition(500);
//            this.enemy2.updateHitbox();
//        }


        // @TODO:  Check collisions between enemy and player
        if (this.player.getHitbox().intersect(this.enemy1.getHitbox()) == true) {
            // the enemy and player are colliding
            Log.d(TAG, "++++++ENEMY AND PLAYER COLLIDING!");

            // @TODO: What do you want to do next?

            // RESTART THE PLAYER IN ORIGINAL POSITION
            // -------
            // 1. Restart the player
            // 2. Restart the player's hitbox
            this.player.setxPosition(100);
            this.player.setyPosition(600);
            this.player.updateHitbox();

            // decrease the lives
            lives = lives - 1;

        }


        // @TODO:  Check collisions between enemy2 and player
//        if (this.player.getHitbox().intersect(this.enemy2.getHitbox()) == true) {
////            // the enemy and player are colliding
////            Log.d(TAG, "++++++ENEMY 2 AND PLAYER COLLIDING!");
//
//            // @TODO: What do you want to do next?
//
//            // RESTART THE PLAYER IN ORIGINAL POSITION
//            // -------
//            // 1. Restart the player
//            // 2. Restart the player's hitbox
//            this.player.setxPosition(100);
//            this.player.setyPosition(600);
//            this.player.updateHitbox();
//
//            // decrease the lives
//            lives = lives - 1;
//
//        }





    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------



            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.YELLOW);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);


            // DRAW THE BACKGROUND
            // -----------------------------
            canvas.drawBitmap(this.background,
                    this.bgXPosition,
                    0,
                    paintbrush);

            canvas.drawBitmap(this.background,
                    backgroundRightSide,
                    0,
                    paintbrush);



            // draw player graphic on screen
            canvas.drawBitmap(player.getImage(), player.getxPosition(), player.getyPosition(), paintbrush);
            // draw the player's hitbox
            canvas.drawRect(player.getHitbox(), paintbrush);

//drawing the array of eneimes

            for (int i = 0; i < enemyList.size(); i++) {
                Enemy b = enemyList.get(i);
                canvas.drawRect(b.getHitbox(), paintbrush);
                canvas.drawBitmap(b.getImage(), b.getxPosition(), b.getyPosition(), paintbrush);



            }



            // draw enemy 2 on the screen
            // draw the enemy graphic on the screen
//            canvas.drawBitmap(enemy2.getImage(), enemy2.getxPosition(), enemy2.getyPosition(), paintbrush);
//            // 2. draw the enemy's hitbox
//            canvas.drawRect(enemy2.getHitbox(), paintbrush);

            // draw bullet on screen
            for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
                Rect bullet = this.enemy1.getBullets().get(i);
                canvas.drawRect(bullet, paintbrush);
            }

            for (int i = 0; i < this.player.getBullets().size(); i++) {
                Rect bullet = this.player.getBullets().get(i);
                canvas.drawRect(bullet, paintbrush);
            }


            // DRAW GAME STATS
            // -----------------------------
            paintbrush.setColor(Color.YELLOW);
            paintbrush.setTextSize(60);
            canvas.drawText("Lives remaining: " + lives,
                    1100,
                    800,
                    paintbrush
            );


            canvas.drawText("Bullets: " + this.enemy1.getBullets().size(),
                    1100,
                    720,
                    paintbrush
            );



            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    String fingerAction = "";








    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        // Answer - PRESS DOWN ON SCREEN --> PLAYER MOVES UP
        // RELEASE FINGER --> PLAYER MOVES DOWN
        if (userAction == MotionEvent.ACTION_DOWN) {
            //Log.d(TAG, "Person tapped the screen");
            // User is pressing down, so move player up
            fingerAction = "mousedown";


        }
        else if (userAction == MotionEvent.ACTION_UP) {
            //Log.d(TAG, "Person lifted finger");
            // User has released finger, so move player down
            fingerAction = "mouseup";
        }




//
//            // TODO Auto-generated method stub
//        float tx = event.getX();
//          float  ty = event.getY();
//
//            int action = event.getAction();
//            switch(action){
//                case MotionEvent.ACTION_DOWN:
//                    tx = event.getX();
//                    ty = event.getY();
//                    player.touchDown(tx,ty);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    tx = event.getX();
//                    ty = event.getY();
//                    player.touchMove(tx,ty);
//
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    player.touchRelease(tx,ty);
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//
//                    break;
//                case MotionEvent.ACTION_OUTSIDE:
//
//                    break;
//                default:
//            }
            return true; //processed
        }



}
