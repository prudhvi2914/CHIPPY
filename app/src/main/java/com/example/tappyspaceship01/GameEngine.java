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

import static com.example.tappyspaceship01.R.drawable.eye;

public class GameEngine extends SurfaceView implements Runnable {


    // Android debug variables
    final static String TAG = "TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;
    int hp;
    int score;
    int highScore = 0;
    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;
    List<Enemy> enemyList = new ArrayList<Enemy>();

    ArrayList<Rect> bullets = new ArrayList<Rect>();

    //------------------------------------------------------
    // Array of Bullets
    ArrayList<Square> bull = new ArrayList<Square>();
    Square enemy;

    //------------------------------------------------
    Player player;
    Enemy enemy1;
    Enemy enemy2;
    Enemy enemy3;
    Enemy enemy4;
    Enemy enemy5;
    Enemy enemy6;
    Enemy enemy7;
    Enemy enemy8;
    Enemy enemy9;

    Bitmap background;
    int bgXPosition = 0;
    int backgroundRightSide = 0;

    int lives = 10;
    //  int SQUARE_WIDTH = 50;


    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

//----------------------------------------------------------------------
        int SQUARE_WIDTH = 100;
//        // initalize sprites
//        // lets make 6 bullets
//        int bulletStartX = 100;
//
//        // make 10 bullets
//        int baseSpeed = 2;
//
//        for (int j = 1; j <= 1; j++) {
//            Square b = new Square(context, 100, 300, SQUARE_WIDTH, 20*j);
//            this.bull.add(b);
//        }
//
//        this.enemy = new Square(context, 1350, 300, SQUARE_WIDTH, 0);
        //----------------------------------------------------------------

        // @TODO: Add your sprites


        this.player = new Player(context, 100, 300, SQUARE_WIDTH, 2);


        // put the initial starting position of your player and enemies
//        this.player = new Player(getContext(), 100, 300,SQUARE_WIDTH,2);

        this.enemy1 = new Enemy(getContext(), 1350, 300);
        this.enemy2 = new Enemy(getContext(), 1350, 140);
        this.enemy3 = new Enemy(getContext(), 1350, 450);

        this.enemy4 = new Enemy(getContext(), 1500, 140);
        this.enemy5 = new Enemy(getContext(), 1500, 300);
        this.enemy6 = new Enemy(getContext(), 1500, 450);

        this.enemy7 = new Enemy(getContext(), 1200, 140);
        this.enemy8 = new Enemy(getContext(), 1200, 300);
        this.enemy9 = new Enemy(getContext(), 1200, 450);

        enemyList.add(enemy1);
        enemyList.add(enemy2);
        enemyList.add(enemy3);
        enemyList.add(enemy4);
        enemyList.add(enemy5);
        enemyList.add(enemy6);
        enemyList.add(enemy7);
        enemyList.add(enemy8);
        enemyList.add(enemy9);


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


//---------------------------===============================================================
//public void moveBulletToTarget(Square bullet) {
//    // @TODO:  Move the square
//    // 1. calculate distance between bullet and square
//    double a = (enemy.xPosition - bullet.xPosition);
//    double b = (enemy.yPosition - bullet.yPosition);
//    double distance = Math.sqrt((a*a) + (b*b));
//
//    // 2. calculate the "rate" to move
//    double xn = (a / distance);
//    double yn = (b / distance);
//
//    // 3. move the bullet
//    bullet.xPosition = bullet.xPosition + (int)(xn * bullet.getSpeed());
//    bullet.yPosition = bullet.yPosition + (int)(yn * bullet.getSpeed());
//}
//
//
//    float mouseX;
//    float mouseY;
//
//    public void moveBulletToMouse(Square bullet, float mouseXPos, float mouseYPos) {  //player,player
//        // @TODO:  Move the square
//        // 1. calculate distance between bullet and square
//        double a = (mouseXPos - bullet.xPosition);
//        double b = (mouseYPos - bullet.yPosition);
//        double distance = Math.sqrt((a*a) + (b*b));
//
//        // 2. calculate the "rate" to move
//        double xn = (a / distance);
//        double yn = (b / distance);
//
//        // 3. move the bullet
//        bullet.xPosition = bullet.xPosition + (int)(xn * bullet.getSpeed());
//        bullet.yPosition = bullet.yPosition + (int)(yn * bullet.getSpeed());
//    }
    //---------------------------


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

    int count = 0;

    public enum Direction {
        UP,
        DOWN
    }

    final int BOTTOM_OF_SCREEN = 700;
    final int TOP_OF_SCREEN = 100;

    Direction bulletDirection = Direction.DOWN;

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

//        // @TODO: Update position of player based on mouse tap
//        if (this.fingerAction == "mousedown") {
//            // if mousedown, then move player up
//            // Make the UP movement > than down movement - this will
//            // make it look like the player is moving up alot
////            player.getxPosition(player.setxPosition()  = (int)tx );
//
//          player.setyPosition(player.getyPosition() - 15);
//
////            player.setxPosition(player.getxPosition() + 15);
//           // player.getxPosition(player.setxPosition() = tx);
//
//            if (this.player.getyPosition() >= this.screenHeight) {
//                Log.d(TAG, "BALL HIT THE FLOOR / OUT OF BOUNDS");
////                directionBallIsMoving = "up";
////                player.setyPosition(player.getyPosition() - 15);
//            }
//            player.updateHitbox();
//        }
//        else if (this.fingerAction == "mouseup") {
//            // if mouseup, then move player down
////            player.setyPosition(player.getyPosition() - 15);
//            player.setyPosition(player.getyPosition() + 15);
//            player.updateHitbox();
//        }


        //-----------
///player.setxpos(player.x)
//==========
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
        player.setxPosition(player.x);
        player.setyPosition(player.y);
        player.updateHitbox();

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 20 == 0) {
            this.enemy1.spawnBullet();
        }
        //----------------------------------------------------------------------------------
        if (bulletDirection == Direction.DOWN) {
            enemy1.yPosition = enemy1.yPosition + 10;
            enemy1.updateHitbox();
            enemy2.yPosition = enemy2.yPosition + 10;
            enemy2.updateHitbox();
            enemy3.yPosition = enemy3.yPosition + 10;
            enemy3.updateHitbox();
            enemy4.yPosition = enemy4.yPosition + 10;
            enemy4.updateHitbox();
            enemy5.yPosition = enemy5.yPosition + 10;
            enemy5.updateHitbox();
            enemy6.yPosition = enemy6.yPosition + 10;
            enemy6.updateHitbox();
            enemy7.yPosition = enemy7.yPosition + 10;
            enemy7.updateHitbox();
            enemy8.yPosition = enemy8.yPosition + 10;
            enemy8.updateHitbox();
            enemy9.yPosition = enemy9.yPosition + 10;
            enemy9.updateHitbox();


            if (enemy1.yPosition > BOTTOM_OF_SCREEN) {
                bulletDirection = Direction.UP;
            }
        }

        if (bulletDirection == Direction.UP) {
            enemy1.yPosition = enemy1.yPosition - 10;
            enemy1.updateHitbox();
            enemy2.yPosition = enemy2.yPosition - 10;
            enemy2.updateHitbox();

            enemy3.yPosition = enemy3.yPosition - 10;
            enemy3.updateHitbox();

            enemy4.yPosition = enemy4.yPosition - 10;
            enemy4.updateHitbox();

            enemy5.yPosition = enemy5.yPosition - 10;
            enemy5.updateHitbox();

            enemy6.yPosition = enemy6.yPosition - 10;
            enemy6.updateHitbox();

            enemy7.yPosition = enemy7.yPosition - 10;
            enemy7.updateHitbox();

            enemy8.yPosition = enemy8.yPosition - 10;
            enemy8.updateHitbox();

            enemy9.yPosition = enemy9.yPosition - 10;
            enemy9.updateHitbox();

            if (enemy1.yPosition < TOP_OF_SCREEN) {
                bulletDirection = Direction.DOWN;
            }
        }
//        for (int  j= 0; j < this.bull.size();j++) {
//            Square b = this.bull.get(j);
//            moveBulletToMouse(b, this.mouseX, this.mouseY);
//
//        }
        //-----------------------------------------------------------------------------

        // MOVING THE BULLETS
        int BULLET_SPEED = 20;
        for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
            Rect bullet = this.enemy1.getBullets().get(i);
            bullet.left = bullet.left - BULLET_SPEED;
            bullet.right = bullet.right - BULLET_SPEED;
        }

        // COLLISION DETECTION ON THE BULLET AND WALL
        for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
            Rect bullet = this.enemy1.getBullets().get(i);

            // For each bullet, check if teh bullet touched the wall
            if (bullet.right < 0) {
                this.enemy1.getBullets().remove(bullet);
            }

        }

        // COLLISION DETECTION BETWEEN BULLET AND PLAYER
        for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
            Rect bullet = this.enemy1.getBullets().get(i);

            if (this.player.getHitbox().intersect(bullet)) {
                this.player.setxPosition(100);
                this.player.setyPosition(600);
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy


        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy1.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy1);
                this.player.getBullets().remove(bullet);

                this.enemy1.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }


        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy2.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy2);
                this.player.getBullets().remove(bullet);
                this.enemy2.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
                this.enemy1.getBullets().remove(bullet);

            }

        }

        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy3.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy3);
                this.player.getBullets().remove(bullet);
                this.enemy3.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy4.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy4);
                this.player.getBullets().remove(bullet);
                this.enemy4.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy5.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy5);
                this.player.getBullets().remove(bullet);
                this.enemy5.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy6.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy6);
                this.player.getBullets().remove(bullet);
                this.enemy6.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy7.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy7);
                this.player.getBullets().remove(bullet);
//                this.enemy7.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy8.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy8);
                this.player.getBullets().remove(bullet);
//                this.enemy8.removeHitbox();
                this.player.updateHitbox();
                lives = lives - 1;
            }

        }
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (this.enemy9.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy9);
                this.player.getBullets().remove(bullet);
//                this.enemy9.removeHitbox();
//                this.player.updateHitbox();
                lives = lives - 1;
            }

        }


//        movePlayerToMouse(player,this.mouseX,this.mouseY);


        // DEAL WITH player BULLETS

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 20 == 0) {
            this.player.spawnBullet();
        }


        // MOVING THE BULLETS
//        int BULLET_SPEED = 20;
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            bullet.right = bullet.right + BULLET_SPEED;
            bullet.left = bullet.left + BULLET_SPEED;
        }

        // COLLISION DETECTION ON THE BULLET AND WALL
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            // For each bullet, check if teh bullet touched the wall
            if (bullet.right < 0) {
                this.player.getBullets().remove(bullet);
            }

        }


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
            this.canvas.drawColor(Color.argb(255, 255, 255, 255));
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

            paintbrush.setColor(Color.RED);

//
//            // draw the player's hitbox
            canvas.drawRect(player.getHitbox(), paintbrush);

            //drawing the array of eneimes

//            for (int i = 0; i < enemyList.size(); i++) {
//                Enemy b = enemyList.get(i);
//                canvas.drawRect(b.getHitbox(), paintbrush);
//                canvas.drawBitmap(b.getImage(), b.getxPosition(), b.getyPosition(), paintbrush);
//
//
//            }


            // draw bullet on screen
            for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
                Rect bullet = this.enemy1.getBullets().get(i);
                canvas.drawRect(bullet, paintbrush);
            }

            for (int i = 0; i < this.player.getBullets().size(); i++) {
                Rect bullet = this.player.getBullets().get(i);
                canvas.drawRect(bullet, paintbrush);
            }


            // draw bullet==================================================
//            paintbrush.setColor(Color.YELLOW);
//
//            for (int i = 0; i < this.bull.size();i++) {
//                Square b = this.bull.get(i);
////                canvas.drawRect(
////                        b.getxPosition(),
////                        b.getyPosition(),
////                        b.getxPosition() + b.getWidth(),
////                        b.getyPosition() + b.getWidth(),
////                        paintbrush
////                );
//                canvas.drawBitmap(b.getImage(), b.getxPosition()+b.getWidth() , b.getyPosition()+b.getWidth(), paintbrush);
//
//            }
//
//            paintbrush.setColor(Color.RED);
//
//
//            // draw the player's hitbox
//            canvas.drawRect(player.getHitbox(), paintbrush);
//
//            // draw enemy
//            paintbrush.setColor(Color.MAGENTA);

//===============================================================================


            // DRAW GAME STATS
            // -----------------------------
            paintbrush.setColor(Color.YELLOW);
            paintbrush.setTextSize(60);
            canvas.drawText("Lives remaining: " + lives,
                    100,
                    200,
                    paintbrush
            );


            canvas.drawText("Bullets: " + this.enemy1.getBullets().size(),
                    100,
                    400,
                    paintbrush
            );


            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        } catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.x = (int) event.getX();
                player.y = (int) event.getY();

                Log.d("PUSH", "PERSON CLICKED AT: (" + event.getX() + "," + event.getY() + ")");
                break;
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return true;
    }

}