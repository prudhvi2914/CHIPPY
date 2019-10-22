package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.tappyspaceship01.R.drawable.alien_laser;
import static com.example.tappyspaceship01.R.drawable.eye;
import static com.example.tappyspaceship01.R.drawable.leg;
import static com.example.tappyspaceship01.R.drawable.newalien;
import static com.example.tappyspaceship01.R.drawable.player_laser;

public class GameEngine extends SurfaceView implements Runnable {
    // Android debug variables
    final static String TAG = "TAPPY-SPACESHIP";
     int yPosition;
    int xPosition;


    int min = 0;
    int max = this.screenWidth;


    //
    Random r = new Random();
    int fpsEnemyCount = 0;
    int fpsLifeCount = 0;
    int hp;
    int score;
    int highScore = 0;
    boolean enemyUp = true;
    int yPosEnemy = 250;
    int newPlayerY;
    //===========SOUND EFFECTS



    //==================


//
//    List<Item> items = new ArrayList<Item>();
//            this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.eye);
//
//    this.item1 = new Item(getContext(), random.xposition, yposition,image ,  )
//    items.add(item1);

   //-----------------------------------
    // screen size
    int screenHeight;
    int screenWidth;
//List<Item> sprites;
    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;
    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;
    List<Enemy> enemyList = new ArrayList<Enemy>();

    Bitmap image;
    ArrayList<Rect> bullets = new ArrayList<Rect>();

    Enemy mainenemy;
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

    Bitmap picture;
    Enemy item1;
    Enemy item2;
    Enemy item3;
    Bitmap background;
    int bgXPosition  = 1;
    int backgroundRightSide = 0;

    int lives = 10;
    private int bullet;

    int xPos = 1600;
    int yPos1 ;
    int yPos2;
    int yPos3;

    // ------------------------------------
    // Sound Effects
    // ------------------------------------
    SoundPool soundPool;
    SoundPool.Builder soundPoolBuilder;

    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;

    int soundID_bulletSound;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameEngine(Context context, int w, int h) {
        super(context);

        // Sound Effects:
        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes = attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        soundPoolBuilder.setMaxStreams(30);
        soundPool = soundPoolBuilder.build();

        soundID_bulletSound = soundPool.load(context, R.raw.bulletsound, 1);

        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();


        int SQUARE_WIDTH = 100;


        //======================
//        this.sprites = new ArrayList<>();
        this.hp = 5;
        this.score = 0;
        //=====================
        // @TODO: Add your sprites


        this.player = new Player(context, 100, 300, SQUARE_WIDTH, 2);


        // put the initial starting position of your player and enemies
//        this.player = new Player(getContext(), 100, 300,SQUARE_WIDTH,2);

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.eye);
        this.enemy1 = new Enemy(getContext(), 1350, 300, image , new Rect(1350,140,1414,204)
        );

        this.image = BitmapFactory.decodeResource(context.getResources(),newalien );
        this.enemy2 = new Enemy(getContext(), 1350, 140, image,new Rect(1350,140,1414,204 ));

        this.image = BitmapFactory.decodeResource(context.getResources(), newalien);
        this.enemy3 = new Enemy(getContext(), 1350, 450,image,new Rect(1350,450,1414,514 ));

        this.image = BitmapFactory.decodeResource(context.getResources(),newalien );
        this.enemy4 = new Enemy(getContext(), 1500, 140,image,new Rect(1500,140,1564,204));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy5 = new Enemy(getContext(), 1500, 300,image,new Rect(1500,300,1564,364));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy6 = new Enemy(getContext(), 1500, 450,image,new Rect(1500,450,1564,514));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy7 = new Enemy(getContext(), 1200, 140,image,new Rect(1200,140,1264,204));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy8 = new Enemy(getContext(), 1200, 300,image,new Rect(1200,300,1264,364));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy9 = new Enemy(getContext(), 1200, 450,image,new Rect(1200,450,1264,514));

        enemyList.add(enemy1);
        enemyList.add(enemy2);
        enemyList.add(enemy3);
        enemyList.add(enemy4);
        enemyList.add(enemy5);
        enemyList.add(enemy6);
        enemyList.add(enemy7);
        enemyList.add(enemy8);
        enemyList.add(enemy9);


        // Creating items:
        this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand);
        this.item1 = new Enemy(getContext(), xPos, yPos1, picture , new Rect(1350,140,1414,204)
        );
        this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.body);
        this.item2 = new Enemy(getContext(), xPos, yPos2, picture , new Rect(1350,140,1414,204)
        );
        this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        this.item3 = new Enemy(getContext(), xPos, yPos3, picture , new Rect(1350,140,1414,204)
        );
        enemyList.add(item1);
        enemyList.add(item2);
        enemyList.add(item3);



        // setup the backgroun
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
//public int addlist(){
//
//
//        return
//}



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


public int getrand(int min, int max){

        return (int)(Math.random() * max + min);

}


//

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
        if(this.highScore < this.score) this.highScore = this.score;
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void restartGame()
    {
        this.score = 0;
        this.hp = 5;
        gameIsRunning = true;
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

    final int BOTTOM_OF_SCREEN = 500;
    final int TOP_OF_SCREEN = 200;

    Direction bulletDirection = Direction.DOWN;

    int numLoops = 0;


    public void updatePositions() {

        //==================================
        this.fpsEnemyCount += 1;
        this.fpsLifeCount += 1;

        //=================================
        // UPDATE BACKGROUND POSITION
        // 1. Move the background
        this.bgXPosition = this.bgXPosition - 50;

        backgroundRightSide = this.bgXPosition + this.background.getWidth();
        // 2. Background collision detection
        if (backgroundRightSide < 0) {
            this.bgXPosition = 0;
        }

        numLoops = numLoops + 1;


        player.setxPosition(player.x);
        player.setyPosition(player.y);
        player.updateHitbox();

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 20 == 0) {
            this.enemy1.spawnBullet();
        }

        if(numLoops % 100 == 0) {
            yPos1 = getrand(100,900);
            yPos2 = getrand(100,900);
            yPos3 = getrand(100,900);
        }
//
//        if (bulletDirection == Direction.DOWN) {
//            enemy1.yPosition = enemy1.yPosition + 10;
//            enemy1.updateHitbox();
//            enemy2.yPosition = enemy2.yPosition + 10;
//            enemy2.updateHitbox();
//            enemy3.yPosition = enemy3.yPosition + 10;
//            enemy3.updateHitbox();
//            enemy4.yPosition = enemy4.yPosition + 10;
//            enemy4.updateHitbox();
//            enemy5.yPosition = enemy5.yPosition + 10;
//            enemy5.updateHitbox();
//            enemy6.yPosition = enemy6.yPosition + 10;
//            enemy6.updateHitbox();
//            enemy7.yPosition = enemy7.yPosition + 10;
//            enemy7.updateHitbox();
//            enemy8.yPosition = enemy8.yPosition + 10;
//            enemy8.updateHitbox();
//            enemy9.yPosition = enemy9.yPosition + 10;
//            enemy9.updateHitbox();
//
//
//            if (enemy1.yPosition > BOTTOM_OF_SCREEN) {
//                bulletDirection = Direction.UP;
//            }
//        }
//
//        if (bulletDirection == Direction.UP) {
//            enemy1.yPosition = enemy1.yPosition - 10;
//            enemy1.updateHitbox();
//            enemy2.yPosition = enemy2.yPosition - 10;
//            enemy2.updateHitbox();
//
//            enemy3.yPosition = enemy3.yPosition - 10;
//            enemy3.updateHitbox();
//
//            enemy4.yPosition = enemy4.yPosition - 10;
//            enemy4.updateHitbox();
//
//            enemy5.yPosition = enemy5.yPosition - 10;
//            enemy5.updateHitbox();
//
//            enemy6.yPosition = enemy6.yPosition - 10;
//            enemy6.updateHitbox();
//
//            enemy7.yPosition = enemy7.yPosition - 10;
//            enemy7.updateHitbox();
//
//            enemy8.yPosition = enemy8.yPosition - 10;
//            enemy8.updateHitbox();
//
//            enemy9.yPosition = enemy9.yPosition - 10;
//            enemy9.updateHitbox();
//
//            if (enemy1.yPosition < TOP_OF_SCREEN) {
//                bulletDirection = Direction.DOWN;
//            }
//        }



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
         //   soundPool.play(soundID_bulletSound, 1, 1, 1, 0, 1);
            // For each bullet, check if teh bullet touched the wall
            if (bullet.right < 0) {
                this.enemy1.getBullets().remove(bullet);
            }

        }

        // COLLISION DETECTION BETWEEN BULLET AND PLAYER
//        for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
//            Rect bullet = this.enemy1.getBullets().get(i);
//
//            if (this.player.getHitbox().intersect(bullet)) {
//                this.player.setxPosition(100);
//                this.player.setyPosition(600);
//                this.player.updateHitbox();
//                lives = lives - 1;
//            }
//
//        }


        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (bullet.intersect(this.enemy1.getHitbox()) ) {


                enemyList.remove(enemy1);
                enemy1.setHitbox(new Rect(0, 0, 0, 0));

//                Log.d(TAG, "++++++BLAH!");
//                System.out.print("HITBOX ENEMY1: ");
                if (enemy1.getHitbox() == null) {
                    Log.d(TAG, "HITBOX REMOVED");
                }
                else {
                    Log.d(TAG, "HITBOX DOESN'T REMOVED");
                }
                this.player.getBullets().remove(bullet);

            }
            if (this.enemy2.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy2);
                enemy2.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);


            }
            if (this.enemy3.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy3);
                enemy3.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);

            }
            if (this.enemy4.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy4);
                enemy4.setHitbox(new Rect(0, 0, 0, 0));

                this.player.getBullets().remove(this.bullet);

            }
            if (this.enemy5.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy5);
                enemy5.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);

            }

            if (this.enemy6.getHitbox().intersect(bullet)) {
                enemyList.remove(enemy6);
                enemy6.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);

            }

            if (bullet.intersect(this.enemy7.getHitbox()) ) {
                enemyList.remove(enemy7);
                enemy7.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);
                this.player.updateHitbox();

            }
            if (bullet.intersect(this.enemy8.getHitbox()) ) {
                enemyList.remove(enemy8);
                enemy8.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);

            }
            if (bullet.intersect(this.enemy9.getHitbox()) ) {

                enemyList.remove(enemy9);
                enemy9.setHitbox(new Rect(0, 0, 0, 0));
                this.player.getBullets().remove(bullet);

            }


        }

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 5 == 0) {
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
//            Log.d(TAG, "++++++ENEMY AND PLAYER COLLIDING!");

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


            // draw the player's hitbox
            canvas.drawRect(player.getHitbox(), paintbrush);

            //drawing the array of eneimes
            for (int i = 0; i < enemyList.size(); i++) {
                Enemy b = enemyList.get(i);
                canvas.drawRect(b.getHitbox(), paintbrush);
                canvas.drawBitmap(b.getImage(), b.getxPosition(), b.getyPosition(), paintbrush);


            }

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
                    100,
                    200,
                    paintbrush
            );


            canvas.drawText("Bullets: " + this.enemy1.getBullets().size(),
                    100,
                    400,
                    paintbrush
            );

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
            case MotionEvent.ACTION_MOVE :
                player.x = (int) event.getX();
                player.y = (int) event.getY();

//                Log.d("PUSH", "PERSON CLICKED AT: (" + event.getX() + "," + event.getY() + ")");
                break;
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return true;
    }

}
