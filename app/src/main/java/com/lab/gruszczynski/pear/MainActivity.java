package com.lab.gruszczynski.pear;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private GameLogic gameLogic;
    private PaintView paintView;
    private RelativeLayout content_main_layout;
    private final String sharedPrefs = "sharedPrefs";
    private final String highScoreKey = "highScore";
    private SensorManager sensorManager;
    private Map<String, GameTheme> gameThemes;
    private Map<String, GameLevel> gameLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createGameThemes();
        createGameLevels();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        paintView = new PaintView(this, this, sensorManager, gameThemes.get(getString(R.string.theme_main)), gameLevels.get(getString(R.string.level_medium)));
        preferences = this.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE);
        gameLogic = paintView.gameLogic;

        content_main_layout = (RelativeLayout) findViewById(R.id.content_main);
        paintView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        content_main_layout.addView(paintView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.theme_blue:{
                    paintView.gameTheme=gameThemes.get(getString(R.string.theme_blue));
                break;
            }
            case R.id.theme_main:{
                    paintView.gameTheme=gameThemes.get(getString(R.string.theme_main));
                break;
            }
            case R.id.level_sandbox:{
                gameLogic.gameLevel=gameLevels.get(getString(R.string.level_sandbox));
                break;
            }
            case R.id.level_medium:{
                gameLogic.gameLevel=gameLevels.get(getString(R.string.level_medium));
                break;
            }
            case R.id.level_russia:{
                gameLogic.gameLevel=gameLevels.get(getString(R.string.level_russia));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //gameLogic.notify();
        paintView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //try{gameLogic.wait();} catch(InterruptedException e) {e.printStackTrace();}
        paintView.stop();
    }

    public void saveHighScore(int highScore) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(highScoreKey, highScore);
        preferencesEditor.commit();
    }

    public int loadHighScore() {
        return preferences.getInt(highScoreKey, 0);
    }

    private void createGameThemes(){
        gameThemes=new HashMap<>();
        gameThemes.put(getString(R.string.theme_blue), new GameTheme(Color.RED, Color.BLUE));
        gameThemes.put(getString(R.string.theme_main), new GameTheme(Color.argb(255, 100, 170,0), Color.WHITE));
    }

    private void createGameLevels(){
        gameLevels=new HashMap<>();
        gameLevels.put(getString(R.string.level_medium), new GameLevel(1));
        gameLevels.put(getString(R.string.level_sandbox), new GameLevel(0.4));
        gameLevels.put(getString(R.string.level_russia), new GameLevel(2.5));
    }
}
