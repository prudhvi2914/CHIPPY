package com.example.tappyspaceship01;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.widget.Toast.*;
import static com.example.tappyspaceship01.R.drawable.alien_laser;
import static com.example.tappyspaceship01.R.drawable.eye;
import static com.example.tappyspaceship01.R.drawable.heart;
import static com.example.tappyspaceship01.R.drawable.leg;
import static com.example.tappyspaceship01.R.drawable.newalien;
import static com.example.tappyspaceship01.R.drawable.player_laser;
import static com.example.tappyspaceship01.R.drawable.power;

public class GameEngine extends SurfaceView implements Runnable {
    // Android debug variables
    final static String TAG = "TAPPY-SPACESHIP";
    int yPosition;
    int xPosition;

    int min = 2;
    int max = 5;
    int fpsEnemyCount = 0;
    int fpsLifeCount = 0;
    int hp;
    int score = 0;
    int highScore = 0;
    boolean enemyUp = true;
    int yPosEnemy = 250;
    int newPlayerY;
    //score and lives
    int playerlives =  3;
    int enemylives = 10;
    int armylives = 5;
    int BULLET_SPEED = 20;

    List<Item> items = new ArrayList<Item>();

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
    List<Enemy> enemyList = new ArrayList<Enemy>();

    Bitmap image;
    ArrayList<Rect> bullets = new ArrayList<Rect>();
    int random = new Random().nextInt((max - min) + 1) + min;
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
    Item item1;
    Item item2;
    Item item3;
    Item itempowerUp;
    Item health;

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

        int SQUARE_WIDTH = 100;
        this.hp = 5;
        this.score = 0;


        this.player = new Player(context, 100, 300, SQUARE_WIDTH, 2);

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.eye);
        this.enemy1 = new Enemy(getContext(), 1350, 300, image , new Rect(1350,300,1350+image.getWidth(),300+image.getHeight())
        );

        this.image = BitmapFactory.decodeResource(context.getResources(),newalien );
        this.enemy2 = new Enemy(getContext(), 1350, 140, image,new Rect(1350,140,1350+image.getWidth(),140+image.getHeight() ));

        this.image = BitmapFactory.decodeResource(context.getResources(), newalien);
        this.enemy3 = new Enemy(getContext(), 1350, 450,image,new Rect(1350,450,1350+image.getWidth(),450+image.getHeight() ));

        this.image = BitmapFactory.decodeResource(context.getResources(),newalien );
        this.enemy4 = new Enemy(getContext(), 1500, 140,image,new Rect(1500,140,1500+image.getWidth(),140+image.getHeight()));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy5 = new Enemy(getContext(), 1500, 300,image,new Rect(1500,300,1500+image.getWidth(),300+image.getHeight()));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy6 = new Enemy(getContext(), 1500, 450,image,new Rect(1500,450,1500+image.getWidth(),450+image.getHeight()));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy7 = new Enemy(getContext(), 1200, 140,image,new Rect(1200,140,1200+image.getWidth(),140+image.getHeight()));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy8 = new Enemy(getContext(), 1200, 300,image,new Rect(1200,300,1200+image.getWidth(),300+image.getHeight()));

        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.newalien);
        this.enemy9 = new Enemy(getContext(), 1200, 450,image,new Rect(1200,450,1200+image.getWidth(),450+image.getHeight()));
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
        this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.three);


        this.item1 = new Item(getContext(),random*1000, random*200,picture);

        this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.three);

        this.item2 =new Item(getContext(),random*1100, random*370,picture);

        this.picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.rck);
        this.item3 = new Item(getContext(),random*1200, random*60,picture);

        this.picture = BitmapFactory.decodeResource(context.getResources(), power);
        this.itempowerUp = new Item(getContext(),random*1200, random*60,picture);

        this.picture = BitmapFactory.decodeResource(context.getResources(), heart);
        this.health = new Item(getContext(),random*1200, random*60,picture);

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(itempowerUp);
        items.add(health);


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


    public int getrand(int min, int max){

        return (int)(Math.random() * max + min);

    }

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

        this.item1.setxPosition(this.item1.getxPosition()-25);
        this.item1.updateHitbox();
        this.item2.setxPosition(this.item2.getxPosition()-5);
        this.item2.updateHitbox();
        this.item3.setxPosition(this.item3.getxPosition()-15);
        this.item3.updateHitbox();
        this.itempowerUp.setxPosition(this.itempowerUp.getxPosition()-8);
        this.itempowerUp.updateHitbox();
        this.health.setxPosition(this.health.getxPosition()-13);
        this.health.updateHitbox();


         int rand = new Random().nextInt((max - min) + 1) + min;

        if(this.item1.getxPosition() < 0){


            this.picture = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.three);

            this.item1 = new Item(getContext(),rand*1000, rand*60,picture);
            items.add(item1);


        }
        if(this.item2.getxPosition()<0 ){
            this.picture = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.three);

            this.item2 =new Item(getContext(),rand*1400, rand*370,picture);
            items.add(item2);

        }
        if(this.item3.getxPosition()<0 ){
            this.picture = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rck);

            this.item3 =new Item(getContext(),rand*1400, rand*370,picture);
            items.add(item3);

        }

        if(this.itempowerUp.getxPosition()<0 ){
            this.picture = BitmapFactory.decodeResource(getContext().getResources(), power);

            this.itempowerUp =new Item(getContext(),rand*1400, rand*370,picture);
            items.add(itempowerUp);

        }

        if(this.health.getxPosition()<0 ){
            this.picture = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.heart);

            this.health =new Item(getContext(),rand*1400, rand*370,picture);
            items.add(health);

        }
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
        for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
            Rect bullet = this.enemy1.getBullets().get(i);

            if (this.player.getHitbox().intersect(bullet)) {
                this.player.updateHitbox();
                this.enemy1.getBullets().remove(bullet);

                playerlives = playerlives - 1;

            }

        }
        if (playerlives == 0) {
            getContext().startActivity(new Intent(getContext(),LooseActivity.class));

        }



        // COLLISION DETECTION BETWEEN BULLET AND enemy
        for (int i = 0; i < this.player.getBullets().size(); i++) {
            Rect bullet = this.player.getBullets().get(i);

            if (bullet.intersect(this.enemy1.getHitbox()) ) {
                this.player.getBullets().remove(bullet);
                enemylives = enemylives -1 ;
                score = score + 1;

            }
            if (this.enemy2.getHitbox().intersect(bullet)) {
                armylives = armylives -1 ;
                if (armylives == 0) {
                    enemyList.remove(enemy2);
                    enemy2.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }
                this.player.getBullets().remove(bullet);

            }
            if (this.enemy3.getHitbox().intersect(bullet)) {

                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy3);
                    enemy3.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }

            }
            if (this.enemy4.getHitbox().intersect(bullet)) {
                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy4);
                    enemy4.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }

            }
            if (this.enemy5.getHitbox().intersect(bullet)) {
                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy5);
                    enemy5.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }
            }

            if (this.enemy6.getHitbox().intersect(bullet)) {
                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy6);
                    enemy6.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }

            }

            if (bullet.intersect(this.enemy7.getHitbox()) ) {
                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy7);
                    enemy7.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }
            }
            if (bullet.intersect(this.enemy8.getHitbox()) ) {
                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy8);
                    enemy8.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }

            }
            if (bullet.intersect(this.enemy9.getHitbox()) ) {

                this.player.getBullets().remove(bullet);
                armylives = armylives -1;
                if (armylives == 0) {
                    enemyList.remove(enemy9);
                    enemy9.setHitbox(new Rect(0, 0, 0, 0));
                    score = score + 1;

                }

            }


        }
        if (enemylives == 0){
            enemyList.removeAll(enemyList);
            enemy1.setHitbox(new Rect(0, 0, 0, 0));

            getContext().startActivity(new Intent(getContext(),WinScreenActivity.class));

        }

        // Shoot a bullet every (5) iterations of the loop
        if (numLoops % 20 == 0) {
            this.player.spawnBullet();
        }

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


        // @TODO:  Check collisions between enemy and player
        if (this.player.getHitbox().intersect(this.enemy1.getHitbox()) == true) {

            playerlives = playerlives - 1;
        }


        // @TODO:  Check collisions between enemy2 and player
        if (this.player.getHitbox().intersect(this.enemy2.getHitbox()) == true) {


            playerlives = playerlives - 1;

        }
        if (this.player.getHitbox().intersect(this.item1.getHitbox()) == true) {

            playerlives = 0;
        }
        if (this.player.getHitbox().intersect(this.item2.getHitbox()) == true) {

            playerlives = 0;
        }
        if (this.player.getHitbox().intersect(this.item3.getHitbox()) == true) {

            playerlives = 0;
        }

        if (this.player.getHitbox().intersect(this.itempowerUp.getHitbox()) == true) {
              int BULLET_SPEED = 50;
            player.spawnBullet();

        }
        if (this.player.getHitbox().intersect(this.health.getHitbox()) == true) {
            playerlives = playerlives + 1;

        }


    }

//
//    public void itemstoplyer(Rect bullet, float mouseXPos, float mouseYPos) {
//
//        // @TODO:  Move the square
//        // 1. calculate distance between bullet and square
//        double a = (player.x - enemy1.);
//        double b = (player.y - player.getyPos());
//        double distance = Math.sqrt((a * a) + (b * b));
//
//        // 2. calculate the "rate" to move
//        double xn = (a / distance);
//        double yn = (b / distance);
//
//        // 3. move the bullet
//        player.setxPos(player.getxPos() + (int) (xn * 15));
//        player.setyPos(player.getyPos() + (int) (yn * 15));
//
//        player.getHitbox().left = player.getxPos();
//        player.getHitbox().right = player.getxPos() + player.getImage().getWidth();
//        player.getHitbox().top = player.getyPos();
//        player.getHitbox().bottom = player.getyPos() + player.getImage().getHeight();
//
//    }
    public void redrawSprites() {
             if (this.holder.getSurface().isValid()) {
              this.canvas = this.holder.lockCanvas();

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255, 255, 255, 255));
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

            //drawing the array of eneimes
            for (int i = 0; i < enemyList.size(); i++) {
                Enemy b = enemyList.get(i);
//                canvas.drawRect(b.getHitbox(), paintbrush);
                canvas.drawBitmap(b.getImage(), b.getxPosition(), b.getyPosition(), paintbrush);
            }
//                 for (int i = 0; i < items.size(); i++) {
//                     Item c = items.get(i);
////                canvas.drawRect(b.getHitbox(), paintbrush);
//                     canvas.drawBitmap(c.getImage(), c.getxPosition(), c.getyPosition(), paintbrush);
//                 }

                 canvas.drawBitmap(item1.getImage(), item1.getxPosition(), item1.getyPosition(), paintbrush);
                 canvas.drawRect(item1.getHitbox(), paintbrush);

                 canvas.drawBitmap(item2.getImage(), item2.getxPosition(), item2.getyPosition(), paintbrush);
                 canvas.drawRect(item2.getHitbox(), paintbrush);

                 canvas.drawBitmap(item3.getImage(), item3.getxPosition(), item3.getyPosition(), paintbrush);
                 canvas.drawRect(item3.getHitbox(), paintbrush);

                 canvas.drawBitmap(itempowerUp.getImage(), itempowerUp.getxPosition(), itempowerUp.getyPosition(), paintbrush);
                 canvas.drawRect(itempowerUp.getHitbox(), paintbrush);

                 canvas.drawBitmap(health.getImage(), health.getxPosition(), health.getyPosition(), paintbrush);
                 canvas.drawRect(health.getHitbox(), paintbrush);


                 // draw bullet on screen
                 paintbrush.setColor(Color.RED);
            for (int i = 0; i < this.enemy1.getBullets().size(); i++) {
                Rect bullet = this.enemy1.getBullets().get(i);
                canvas.drawRect(bullet, paintbrush);
            }
                 paintbrush.setColor(Color.YELLOW);
            for (int i = 0; i < this.player.getBullets().size(); i++) {
                Rect bullet = this.player.getBullets().get(i);
                canvas.drawRect(bullet,paintbrush);
            }


            // DRAW GAME STATS
            // -----------------------------
            paintbrush.setColor(Color.RED);
            paintbrush.setTextSize(60);
            canvas.drawText("Lives remaining: " + playerlives,
                    100,
                    200,
                    paintbrush
            );
                 paintbrush.setColor(Color.GREEN);
                 paintbrush.setTextSize(60);

              //drawing player bullets
                 canvas.drawText("SCORE: " + score,
                         100,
                         300,
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
