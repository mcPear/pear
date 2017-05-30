package com.lab.gruszczynski.pear;
import android.content.Context;
        import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
        import android.view.*;
        import java.util.Random;

public class PaintView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    Paint paint = new Paint();
    Bitmap pear;
    Bitmap pearBit;
    Bitmap arr1;
    Bitmap arr2;
    Bitmap arr3;
    Bitmap arr4;
    Bitmap arr5;
    private Bitmap s1Bit;
    private Bitmap pearStart;
    private Bitmap pearStartBit;
    float[] pearXY =new float[2];
    float[] arrowsXY =new float[10];
    private MainActivity mainActivity;
    int height;
    int width;
    private int [] digits = new int[3];
    boolean b1=true;
    boolean b2=true;
    boolean b3=true;
    boolean b4=true;
    boolean b5=true;
    boolean b6=true;
    boolean b7=true;
    boolean b8=true;
    boolean b9=true;
    boolean b10=true;
    boolean stroboscope;
    private SensorManager sensorManager;
    Sensor rotationVectorSensor;
    private float[] mRotationVector;
    private final float MAX_ROLL = 0.6f;
    GameTheme gameTheme;

    private SurfaceHolder holder=getHolder();
    GameLogic gameLogic;


    public PaintView(Context context, MainActivity mainActivity, SensorManager sensorManager, GameTheme initialTheme, GameLevel initialLevel) {

        super(context);
        this.mainActivity =mainActivity;
        gameLogic =new GameLogic(holder,this, this.mainActivity, initialLevel);

        paint = new Paint();
        pearStartBit =BitmapFactory.decodeResource(getResources(), R.drawable.start);

        getHolder().addCallback(this);
        setFocusable(true);

        this.sensorManager = sensorManager;
        rotationVectorSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        gameTheme = initialTheme;
    }


    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        gameLogic.loadScreenDimensions();
        pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pgreen);
        pear = Bitmap.createScaledBitmap(pearStartBit, (int)(width /4.64), (int)(width *0.353), true); //magic numbers - są sprawdzone i tyle
        s1Bit=BitmapFactory.decodeResource(getResources(), R.drawable.fullarrow);
        arr1 =Bitmap.createScaledBitmap(s1Bit, (int)(width /14.29), (int)(width *0.17), true);
        arr5 = arr4 = arr3 = arr2 = arr1;
        if(gameLogic.getState() == Thread.State.NEW){ gameLogic.start(); }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        gameLogic.gameState =-1;
    }

    public boolean onTouchEvent(MotionEvent event) {

        // OBSŁUGA KLIKNIĘCIA PLAY AGAIN
        if(gameLogic.gameState ==-1&&event.getY()< height /2-10){
            gameLogic.again=true; //ZNACZY, ZE KLIKNIĘTO PLAY AGAIN, POTRZEBNE DO CHOWANIA WYNIKU POWOLI
            gameLogic.gameState =0; }

        //OBSŁUGA KLIKNIĘCIA NA EKRAN STARTOWY
        else if(gameLogic.gameState ==2){
            gameLogic.gameState =0; }


        return true;
    }

    protected void onDraw(Canvas canvas) {

        height = canvas.getHeight();
        width = canvas.getWidth();

        //EKRAN STARTOWY
        if (gameLogic.gameState == 2) {

            paint.setTextAlign(Paint.Align.CENTER);
            //paint.setSubpixelText(true);

            //TŁO
            canvas.drawColor(Color.WHITE);

            //TAP TO PLAY
            if(width < height){
                pearStart = Bitmap.createScaledBitmap(pearStartBit, width, width, true);
                paint.setARGB(200, 1, 1, 1);
            canvas.drawBitmap(pearStart, 0,((height - width)/2), paint);
                paint.setTextSize((float) (height / 12));
                paint.setARGB(gameLogic.intensityTAP, 1, 1, 1);
                canvas.drawText("TAP", width / 2, (float) (height / 2), paint);
                paint.setARGB(gameLogic.intensityTO, 1, 1, 1);
                canvas.drawText("TO", width / 2, (float) (height / 1.75), paint);
                paint.setARGB(gameLogic.intensityPLAY, 1, 1, 1);
                canvas.drawText("PLAY", width / 2, (float) (height / 1.55), paint);

                //BEST STARTOWY
                paint.setARGB(30, 1, 1, 1);
                paint.setTextSize((float) (height / 5));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(""+ gameLogic.bestScore, width /2, (float) (height / 6), paint);
                paint.setTextSize((float) (height / 8));
                canvas.drawText("BEST", width /2, (float) (height /1.03), paint);
            }
            else{
                pearStart = Bitmap.createScaledBitmap(pearStartBit, height, height, true);
                paint.setARGB(200, 1, 1, 1);
            canvas.drawBitmap(pearStart,((width - height)/2), 0, paint);
                paint.setTextSize((float) (height / 12));
                paint.setARGB(gameLogic.intensityTAP, 1, 1, 1);
                canvas.drawText("TAP", width / 2, (float) (height /2), paint);
                paint.setARGB(gameLogic.intensityTO, 1, 1, 1);
                canvas.drawText("TO", width / 2, (float) (height / 1.75), paint);
                paint.setARGB(gameLogic.intensityPLAY, 1, 1, 1);
                canvas.drawText("PLAY", width / 2, (float) (height / 1.55), paint);
            }

        }
        else {

            //ZMIANA KOLORU
            if (gameLogic.result < 30){if(b1){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pgreen);
            pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
                s1Bit=BitmapFactory.decodeResource(getResources(), R.drawable.fullarrow);
                arr1 =Bitmap.createScaledBitmap(s1Bit, (int)(width /14.29), (int)(width *0.17), true);
                arr2 = arr1; arr3= arr1; arr4 = arr1; arr5 = arr1;
             }
                b1=false;}
            else if (gameLogic.result < 80)
            {if(b2){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.porange);
                pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
            }b2=false;}
            else if (gameLogic.result < 120)
            {if(b3){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pred);
                pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
            }b3=false;}
            else if (gameLogic.result < 180)
            {if(b4){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pblack);
                pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
            }b4=false;}
            else if (gameLogic.result < 210)
            {if(b5){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pkontur);
                pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
            }b5=false;}
            else if (gameLogic.result < 240)
            {if(b6){
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                pear = Bitmap.createBitmap(1,1,conf);
            }b6=false;}
            else if (gameLogic.result < 290)
            {if(b7){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.ppink);
                pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
            }b7=false;}
            else if (gameLogic.result < 340)
            {if(b8){
                pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pglasses);
                pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
            }b8=false;}
            else if (gameLogic.result < 420){
                if(b9){
                    pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.ppastela);
                    pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
                    s1Bit=BitmapFactory.decodeResource(getResources(), R.drawable.arrred);
                    arr1 =Bitmap.createScaledBitmap(s1Bit, (int)(width /14.29), (int)(width *0.17), true);
                    arr2 = arr1; arr3= arr1; arr4 = arr1; arr5 = arr1;
                }b9=false;}
            else if (gameLogic.result < 490)
                {if(b10){
                    pearBit = BitmapFactory.decodeResource(getResources(), R.drawable.pviolet);
                    pear = Bitmap.createScaledBitmap(pearBit, (int)(width /4.64), (int)(width *0.353), true);
                }b10=false;}

            if(gameLogic.result ==30|| gameLogic.result ==80)
                canvas.drawColor(Color.BLACK);
                else if(gameLogic.result ==120|| gameLogic.result ==141|| gameLogic.result ==131|| gameLogic.result ==146)
                canvas.drawColor(Color.YELLOW);
                else if(gameLogic.result ==180|| gameLogic.result ==210)
                canvas.drawColor(Color.RED);
                else if(gameLogic.result >240&&!stroboscope)
                canvas.drawColor(Color.RED);

            else
            canvas.drawColor(gameTheme.getBackgroundColor()); //// Tło
            stroboscope =!stroboscope;
            paint.setColor(Color.BLACK);

            /*//BLOKOWANIE CHOWANIA SIĘ GRUSZKI
            if (pearXY[0] > width - pear.getWidth()) pearXY[0] = width - pear.getWidth();
            else if (pearXY[0] < 0) pearXY[0] = 0;*/

            //RYSOWANIE GRUSZKI I STRZAŁ
            canvas.drawBitmap(pear, pearXY[0], pearXY[1], paint);
            canvas.drawBitmap(arr1, arrowsXY[0], arrowsXY[1], paint);
            canvas.drawBitmap(arr2, arrowsXY[2], arrowsXY[3], paint);
            canvas.drawBitmap(arr3, arrowsXY[4], arrowsXY[5], paint);
            canvas.drawBitmap(arr4, arrowsXY[6], arrowsXY[7], paint);
            canvas.drawBitmap(arr5, arrowsXY[8], arrowsXY[9], paint);

            //WYPISANIE WYNIKU NA BIEŻĄCO
            if (gameLogic.result < 340) {
                paint.setTextSize((float) (height / 9));
                paint.setTextAlign(Paint.Align.LEFT);
                if (gameLogic.gameState == 0) {
                    paint.setARGB(50, 0, 10, 0);
                    canvas.drawText(gameLogic.result + "", 10, height / 11, paint);
                    paint.setColor(Color.BLACK);
                }
            }
            //WYPISANIE EPIC
            else {
                paint.setTextSize((float) (height / 9));
                paint.setTextAlign(Paint.Align.LEFT);
                if (gameLogic.gameState == 0) {
                    paint.setARGB(50, 0, 10, 0);
                    canvas.drawText("EPIC!!!!!", 10, height / 11, paint);
                    paint.setColor(Color.BLACK);
                }
            }

            // WYPISANIE WYNIKU ZARAZ PO ZDERZENIU
            if (gameLogic.gameState != 0 || gameLogic.gameState == 0 && gameLogic.again == true) {

                //paint.setARGB(gameLogic.intensitySCORE, 100, 170, 0);
                paint.setColor(gameTheme.getTextColor());
                paint.setAlpha(gameLogic.intensitySCORE);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setSubpixelText(true);

                paint.setTextSize((float) (height / 3.2));
                canvas.drawText("" + gameLogic.resultToShow, width / 2, (float) (height / 3.5), paint);

                //paint.setARGB(gameLogic.intensitySCORE, 100, 190, 0);
                paint.setTextSize((float) (height / 10));
                if (gameLogic.result == 1) canvas.drawText("ARROW", width / 2, (float) (height / 2.7), paint);
                else canvas.drawText("ARROWS", width / 2, (float) (height / 2.7), paint);
                //canvas.drawText("BEST: "+context.loadHighScore(),10,150,paint); //BEST

                //WYPISANIE BESTA ROZPROSZONEGO
                //paint.setARGB(gameLogic.intensitySCORE, 0, 200, 0);
                paint.setTextSize((float) (height / 16));
                paint.setTextAlign(Paint.Align.RIGHT);
                spreadBestText();
                if (gameLogic.bestScore < 10)
                    canvas.drawText("" + gameLogic.bestScore, width - 5, (float) (height / 20), paint);
                else if (gameLogic.bestScore < 100) {
                    canvas.drawText("" + digits[1], width - 5, (float) (height / 20), paint);
                    canvas.drawText("" + digits[0], width - 5, (float) (height / 7), paint);
                } else if (gameLogic.bestScore < 1000) {
                    canvas.drawText("" + digits[2], width - 5, (float) (height / 20), paint);
                    canvas.drawText("" + digits[1], width - 5, (float) (height / 7), paint);
                    canvas.drawText("" + digits[0], width - 5, (float) (height / 2.5), paint);
                }

                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("B", 5, (float) (height / 20), paint);
                canvas.drawText("E", 5, (float) (height / 10), paint);
                canvas.drawText("S", 5, (float) (height / 5), paint);
                canvas.drawText("T", 5, (float) (height / 2.5), paint);


            }

            //WYPISANIE PLAY AGAIN
            if (gameLogic.gameState == -1) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize((float) (height / 15));
                //paint.setARGB(gameLogic.intensityPLAYAGAIN, 100, 190, 0);
                paint.setColor(gameTheme.getTextColor());
                paint.setAlpha(gameLogic.intensityPLAYAGAIN);
                canvas.drawText("PLAY AGAIN", width / 2, height / 2, paint);
            }

        }
    }



    int ran(Canvas c){
        int x; Random y=new Random();
        for(x=y.nextInt(2000); x>c.getWidth()- arr1.getWidth(); x-=c.getWidth()) Log.d("x"," "+x);
        if(x<0) x+= arr1.getWidth();  Log.d("x"," "+x);
        Log.d("x"," koniec");
        return x;
    }

    void spreadBestText(){
        int w= gameLogic.bestScore;
        digits[0]=w%10;
        w/=10;
        if(w!=0) digits[1]=w%10;
        w/=10;
        if(w!=0) digits[2]=w%10;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float R[] = new float[9];
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            mRotationVector = event.values;
            SensorManager.getRotationMatrixFromVector(R, mRotationVector);
        }

        float orientation[] = new float[9];
        SensorManager.getOrientation(R, orientation);

        float azimuth = orientation[0];
        float pitch = orientation[1];
        float roll = orientation[2];
        //Log.w("AZIMUTH, PITCH, ROLL", String.valueOf(azimuth) + " " + String.valueOf(pitch) + " " + String.valueOf(roll));

        float pearPosition = width/2 + roll/ MAX_ROLL *width/2;

        if(gameLogic.gameState ==0){
            if(roll> MAX_ROLL || pearPosition > width-pear.getWidth()){pearXY[0]=width-pear.getWidth();}
            else if(roll<-MAX_ROLL){pearXY[0]=0;}
            else{
                pearXY[0] = pearPosition;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void start() {
        int sensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
        sensorManager.registerListener(this, rotationVectorSensor, sensorDelay);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

}


