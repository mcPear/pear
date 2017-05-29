package com.lab.gruszczynski.pear;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private GameLogic gameLogic;
    private PaintView paintView;
    private RelativeLayout content_main_layout;
    private final String sharedPrefs = "sharedPrefs";
    private final String highScoreKey = "highScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paintView = new PaintView(this, this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveHighScore(int highScore) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(highScoreKey, highScore);
        preferencesEditor.commit();
    }

    public int loadHighScore() {
        return preferences.getInt(highScoreKey, 0);
    }
}
