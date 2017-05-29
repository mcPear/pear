/*package com.lab.gruszczynski.pear;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MyActivity3 extends Activity {

    private SharedPreferences preferences;
    private GameLogic gl;
    //private PaintView pvv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaintView pv=new PaintView(this, this);
        setContentView(pv);
        preferences = this.getSharedPreferences("HSsave", Context.MODE_PRIVATE);
        gl=pv.gl;
    }

    @Override
    public void onStop() {
        super.onStop();
        //gl.zderzenie=-1;
        gl.interrupt();
        System.exit(0);
        //try{Thread.sleep(1000);} catch(Exception e){;}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //gl.zderzenie=-1;
        gl.interrupt();
        System.exit(0);
        //try{Thread.sleep(1000);} catch(Exception e){;}
    }

    public void zapis(int wynik) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt("klucz", wynik);
        preferencesEditor.commit();
    }

    public int odczyt() {
        return preferences.getInt("klucz", 0);
    }


}*/
