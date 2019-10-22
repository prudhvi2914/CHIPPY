package com.example.tappyspaceship01;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    MediaPlayer music;

    Display display;
    Point size;
    int screenHeight;
    int screenWidth;


    GameEngine tappySpaceship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //make full screnn
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Get size of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // =============================================
        // MUSIC
        // =============================================
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        music = new MediaPlayer();

        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("music.mp3");
            music.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            music.prepare();
            music.setLooping(true);
        }
        catch (IOException e) {
            System.out.print( "Music IS NOT WORKING: " + e.getMessage());
            music = null;
        }
        // =============================================

        // Initialize the GameEngine object
        // Pass it the screen size (height & width)
        tappySpaceship = new GameEngine(this, screenWidth, screenHeight);

        // Make GameEngine the view of the Activity
        setContentView(tappySpaceship);
    }

    // Android Lifecycle function
    @Override
    protected void onResume() {
        super.onResume();
        if (music != null) {
            music.start();
        }
        tappySpaceship.startGame();
    }

    // Stop the thread in snakeEngine
    @Override
    protected void onPause() {
        super.onPause();
        music.pause();
        tappySpaceship.pauseGame();
    }



}

