package com.lab.gruszczynski.pear;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import java.lang.Thread;

public class GameLogic extends Thread {

    private SurfaceHolder surfaceHolder;
    private PaintView paintView;
    protected int gameState;
    // gameState
    // 1 (PIERWSZA STRZAŁA JUŻ UDERZYŁA)
    // 0 (PĘTLA DZIAŁA NORMALNIE)
    // -1 (KONIEC PĘTLI, STRZAŁY POOPADAŁY)
    // 2 (EKRAN STARTOWY)
    int result;
    int resultToShow;
    private int s1; //speed of first arrow
    private int s2;
    private int s3;
    private int s4;
    private int s5; //speed of fifth arrow
    private int canvasHeight;
    private int canvasWidth;
    private MainActivity context;
    int intensityPLAYAGAIN; //przezroczystość PLAY AGAIN
    private double intensityA; //pomocnicza canvasWidth liczeniu intensityPLAYAGAIN(Angle)
    private double intensityB; //pomocnicza canvasWidth liczeniu intensityPLAYAGAIN(Angle)
    private double intensityC; //pomocnicza canvasWidth liczeniu intensityPLAYAGAIN(Angle)
    int intensityTAP; //przezroczystość "TAP"
    int intensityTO; //przezroczystość "TO"
    int intensityPLAY; //przezroczystość "PLAY"
    int intensitySCORE; //przezroczystość WYNIKU
    private boolean[] levels = new boolean[16];
    private int[] steps = new int[19];
    int bestScore;
    boolean again=false;
    GameLevel gameLevel;



    //POTRZEBNE DO OBSŁUGI W PaintView
    public GameLogic(SurfaceHolder surfaceHolder, PaintView paintView, MainActivity mainActivity, GameLevel initialLevel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.paintView = paintView;
        this.context = mainActivity;
        this.gameState = 2;
        gameLevel = initialLevel;
    }

    public void loadScreenDimensions(){
        Canvas canvas;
        canvas= surfaceHolder.lockCanvas();
        paintView.height = canvas.getHeight();
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        paintView.width = canvas.getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        try {
            Canvas canvas;
            this.bestScore = context.loadHighScore();

            //WYŚWIETLANIE EKRANU STARTOWEGO
            while (gameState == 2) {
                if (intensityA > Math.PI - 0.02) intensityA = 0;
                if (intensityB > Math.PI - 0.02) intensityB = 0;
                if (intensityC > Math.PI - 0.02) intensityC = 0;
                intensityTAP = (int) ((Math.sin(intensityA)) * 160);
                intensityTO = (int) ((Math.sin(intensityB)) * 160);
                intensityPLAY = (int) ((Math.sin(intensityC)) * 160);
                canvas = surfaceHolder.lockCanvas();
                paintView.onDraw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e){e.printStackTrace();}
                intensityA += 0.08;
                intensityB += 0.1;
                intensityC += 0.11;
            }

            while (true) {

                //paintView.pearXY[0]=10; // X GRUSZKI NA STARCIE
                paintView.pearXY[1] = canvasHeight - paintView.pear.getHeight() - 30; // WYSOKOŚĆ GRUSZKI NA STARCIE
                paintView.arrowsXY[0] = 200; // X PIERWSZEJ STRZAŁY



                //ZEROWANIE
                result = 0;
                intensityPLAYAGAIN = 0;
                intensityA = 0;
                paintView.b1=true;
                paintView.b2=true;
                paintView.b3=true;
                paintView.b4=true;
                paintView.b5=true;
                paintView.b6=true;
                paintView.b7=true;
                paintView.b8=true;
                paintView.b9=true;
                paintView.b10=true;

                //STRZAŁY STARTUJĄ Z PONAD PŁÓTNA
                paintView.arrowsXY[1] = -paintView.arr1.getHeight();
                paintView.arrowsXY[3] = -paintView.arr1.getHeight();
                paintView.arrowsXY[5] = -paintView.arr1.getHeight();
                paintView.arrowsXY[7] = -paintView.arr1.getHeight();
                paintView.arrowsXY[9] = -paintView.arr1.getHeight();

                while (gameState != -1) {
                    canvas = surfaceHolder.lockCanvas();

                    //OBLICZENIE KROKU STRZAŁY (eksperymentalnie)
                    steps[0]= canvasWidth /68;
                    steps[1]= canvasWidth /60;
                    steps[2]= canvasWidth /53;
                    steps[3]= canvasWidth /48;
                    steps[4]= canvasWidth /43;
                    steps[5]= canvasWidth /40;
                    steps[6]= canvasWidth /37;
                    steps[7]= canvasWidth /34;
                    steps[8]= canvasWidth /32;
                    steps[9]= canvasWidth /30;
                    steps[10]= canvasWidth /28;
                    steps[11]= canvasWidth /27;
                    steps[12]= canvasWidth /25;
                    steps[13]= canvasWidth /24;
                    steps[14]=(int)((double) canvasWidth /22.85);
                    steps[15]=(int)((double) canvasWidth /21.81);
                    steps[16]=(int)((double) canvasWidth /20.86);
                    steps[17]= canvasWidth /20;
                    steps[18]=(int)((double) canvasWidth /19.2);

                    //FIT ARROW STEPS TO GAME LEVEL (excluding the slowest one)
                    for(int i=1; i<steps.length; i++){
                        steps[i] *= gameLevel.getArrowStepRatio();
                    }

                    //PRĘDKOŚCI STRZAŁ NA KOLEJNYCH LVLACH
                    if (gameState == 0) {
                        if (result < 1) {
                            s1 = steps[3]; s2 = 0; s3 = 0; s4 = 0; s5 = 0;
                        } else if (result < 5) {
                            s1 = steps[6]; s2 = 0; s3 = 0; s4 = 0; s5 = 0;
                        } else if (result < 30) {
                            s1 = steps[6]; s2 = steps[3]; s3 = 0; s4 = 0; s5 = 0;
                        } else if (result < 80) {
                            s1 = steps[1]; s2 = steps[3]; s3 = steps[6]; s4 = 0; s5 = 0;
                        } else if (result < 120) {
                            s1 = steps[1]; s2 = steps[3]; s3 = steps[4]; s4 = steps[6]; s5 = 0;
                        } else if (result < 131) {
                            s1 = steps[1]; s2 = steps[3]; s3 = steps[4]; s4 = steps[6]; s5 = steps[0];
                        } else if (result < 141) {
                            if (levels[0]) s1 = steps[13];
                            s2 = steps[3];
                            s3 = steps[4];
                            s4 = steps[6];
                            s5 = steps[0];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[0] = true;
                            if (paintView.arrowsXY[3] == -paintView.arr1.getHeight()) s2 = 0;
                            if (paintView.arrowsXY[5] == -paintView.arr1.getHeight()) s3 = 0;
                            if (paintView.arrowsXY[7] == -paintView.arr1.getHeight()) s4 = 0;
                            if (paintView.arrowsXY[9] == -paintView.arr1.getHeight()) s5 = 0;
                        } else if (result < 146) {
                            if (levels[1]) s1 = steps[16]; s2 = 0; s3 = 0; s4 = 0; s5 = 0;
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) ;levels[1] = true;
                        } else if (result < 180) {
                            if (levels[2]) s1 = steps[1]; s2 = steps[7]; s3 = steps[8]; s4 = steps[2]; s5 = steps[0];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[2] = true;
                        } else if (result < 210) {
                            if (levels[3]) s1 = steps[13];
                            if (levels[4]) s2 = steps[15];
                            s3 = steps[8]; s4 = steps[2]; s5 = steps[0];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[3] = true;
                            if (paintView.arrowsXY[3] == -paintView.arr1.getHeight()) levels[4] = true;
                            if (paintView.arrowsXY[5] == -paintView.arr1.getHeight()) s3 = 0;
                            if (paintView.arrowsXY[7] == -paintView.arr1.getHeight()) s4 = 0;
                            if (paintView.arrowsXY[9] == -paintView.arr1.getHeight()) s5 = 0;
                        } else if (result < 240) {
                            if (levels[5]) s1 = steps[5]; s2 = steps[15]; s3 = 0; s4 = 0; s5 = 0;
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[5] = true;
                            if (paintView.arrowsXY[3] == -paintView.arr1.getHeight()) s2 = 0;
                        } else if (result < 290) {
                            if (levels[6]) s1 = steps[1]; s2 = steps[7]; s3 = steps[8]; s4 = steps[9]; s5 = steps[0];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[6] = true;
                        } else if (result < 340) {
                            if (levels[3]) s1 = steps[13];
                            if (levels[4]) s2 = steps[12];
                            if (levels[11]) s3 = steps[11];
                            s4 = steps[9]; s5 = steps[0];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[3] = true;
                            if (paintView.arrowsXY[3] == -paintView.arr1.getHeight()) levels[4] = true;
                            if (paintView.arrowsXY[5] == -paintView.arr1.getHeight()) levels[11] = true;
                            if (paintView.arrowsXY[7] == -paintView.arr1.getHeight()) s4 = 0;
                            if (paintView.arrowsXY[9] == -paintView.arr1.getHeight()) s5 = 0;
                        } else if (result < 420) {
                            if(levels[7]) s1 = steps[5]; if(levels[12]) s2 = steps[7]; if(levels[14]) s3 = steps[8]; s4 = steps[9]; s5 = steps[0];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[7] = true;
                            if (paintView.arrowsXY[3] == -paintView.arr1.getHeight()) levels[12] = true;
                            if (paintView.arrowsXY[5] == -paintView.arr1.getHeight()) levels[14] = true;
                        } else if (result < 490) {
                            s1 = steps[5]; s2 = steps[7]; s3 = steps[8];
                            if (levels[8]) s4 = steps[13];
                            if (levels[9]) s5 = steps[6];
                            if (paintView.arrowsXY[7] == -paintView.arr1.getHeight()) levels[8] = true;
                            if (paintView.arrowsXY[9] == -paintView.arr1.getHeight()) levels[9] = true;
                        } else if (result < 1000) {
                            if(levels[15]) s1 = steps[11]; s2 = steps[7]; s3 = steps[8];
                            if (levels[10]) s4 = steps[13];
                            if (levels[13]) s5 = steps[15];
                            if (paintView.arrowsXY[1] == -paintView.arr1.getHeight()) levels[15] = true;
                            if (paintView.arrowsXY[7] == -paintView.arr1.getHeight()) levels[10] = true;
                            if (paintView.arrowsXY[9] == -paintView.arr1.getHeight()) levels[13] = true;
                        }
                    }


                    //RUCH STRZAŁ
                    paintView.arrowsXY[1] += s1;
                    paintView.arrowsXY[3] += s2;
                    paintView.arrowsXY[5] += s3;
                    paintView.arrowsXY[7] += s4;
                    paintView.arrowsXY[9] += s5;

                    //PODNOSZENIE STRZAŁ, JAK KTÓRAŚ UDERZYŁA TO JUŻ NIE PODNOSI ŻADNEJ
                    if (paintView.arrowsXY[1] > canvas.getHeight() && gameState == 0) {
                        paintView.arrowsXY[1] = -paintView.arr1.getHeight();
                        paintView.arrowsXY[0] = paintView.ran(canvas);
                        result += 1;
                    }
                    if (paintView.arrowsXY[3] > canvas.getHeight() && gameState == 0) {
                        paintView.arrowsXY[3] = -paintView.arr2.getHeight();
                        paintView.arrowsXY[2] = paintView.ran(canvas);
                        result += 1;
                    }
                    if (paintView.arrowsXY[5] > canvas.getHeight() && gameState == 0) {
                        paintView.arrowsXY[5] = -paintView.arr3.getHeight();
                        paintView.arrowsXY[4] = paintView.ran(canvas);
                        result += 1;
                    }
                    if (paintView.arrowsXY[7] > canvas.getHeight() && gameState == 0) {
                        paintView.arrowsXY[7] = -paintView.arr4.getHeight();
                        paintView.arrowsXY[6] = paintView.ran(canvas);
                        result += 1;
                    }
                    if (paintView.arrowsXY[9] > canvas.getHeight() && gameState == 0) {
                        paintView.arrowsXY[9] = -paintView.arr5.getHeight();
                        paintView.arrowsXY[8] = paintView.ran(canvas);
                        result += 1;
                    }

                    //WYKRYCIE ZDERZENIA - ZERUJE PRĘDKOŚĆ DANEJ STRZAŁY
                    if ((paintView.arrowsXY[0] > paintView.pearXY[0] && paintView.arrowsXY[0] < paintView.pearXY[0] + paintView.pear.getWidth() - paintView.arr1.getWidth()
                            && paintView.arrowsXY[1] > paintView.pearXY[1] - paintView.arr1.getHeight() + 15 && paintView.arrowsXY[1] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 86 - paintView.arr1.getHeight()) ||
                            (paintView.arrowsXY[0] > paintView.pearXY[0] - paintView.arr1.getWidth() + 5 && paintView.arrowsXY[0] < paintView.pearXY[0] + paintView.pear.getWidth() - 5
                                    && paintView.arrowsXY[1] > paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() && paintView.arrowsXY[1] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() + 26)) {
                        gameState = 1;
                        s1 = 0;
                        resultToShow = result;
                    }

                    if ((paintView.arrowsXY[2] > paintView.pearXY[0] && paintView.arrowsXY[2] < paintView.pearXY[0] + paintView.pear.getWidth() - paintView.arr1.getWidth()
                            && paintView.arrowsXY[3] > paintView.pearXY[1] - paintView.arr1.getHeight() + 15 && paintView.arrowsXY[3] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 86 - paintView.arr1.getHeight()) ||
                            (paintView.arrowsXY[2] > paintView.pearXY[0] - paintView.arr1.getWidth() + 5 && paintView.arrowsXY[2] < paintView.pearXY[0] + paintView.pear.getWidth() - 5
                                    && paintView.arrowsXY[3] > paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() && paintView.arrowsXY[3] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() + 26)) {
                        gameState = 1;
                        s2 = 0;
                        resultToShow = result;
                    }

                    if ((paintView.arrowsXY[4] > paintView.pearXY[0] && paintView.arrowsXY[4] < paintView.pearXY[0] + paintView.pear.getWidth() - paintView.arr1.getWidth()
                            && paintView.arrowsXY[5] > paintView.pearXY[1] - paintView.arr1.getHeight() + 15 && paintView.arrowsXY[5] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 86 - paintView.arr1.getHeight()) ||
                            (paintView.arrowsXY[4] > paintView.pearXY[0] - paintView.arr1.getWidth() + 5 && paintView.arrowsXY[4] < paintView.pearXY[0] + paintView.pear.getWidth() - 5
                                    && paintView.arrowsXY[5] > paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() && paintView.arrowsXY[5] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() + 26)) {
                        gameState = 1;
                        s3 = 0;
                        resultToShow = result;
                    }
                    if ((paintView.arrowsXY[6] > paintView.pearXY[0] && paintView.arrowsXY[6] < paintView.pearXY[0] + paintView.pear.getWidth() - paintView.arr1.getWidth()
                            && paintView.arrowsXY[7] > paintView.pearXY[1] - paintView.arr1.getHeight() + 15 && paintView.arrowsXY[7] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 86 - paintView.arr1.getHeight()) ||
                            (paintView.arrowsXY[6] > paintView.pearXY[0] - paintView.arr1.getWidth() + 5 && paintView.arrowsXY[6] < paintView.pearXY[0] + paintView.pear.getWidth() - 5
                                    && paintView.arrowsXY[7] > paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() && paintView.arrowsXY[7] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() + 26)) {
                        gameState = 1;
                        s4 = 0;
                        resultToShow = result;
                    }
                    if ((paintView.arrowsXY[8] > paintView.pearXY[0] && paintView.arrowsXY[8] < paintView.pearXY[0] + paintView.pear.getWidth() - paintView.arr1.getWidth()
                            && paintView.arrowsXY[9] > paintView.pearXY[1] - paintView.arr1.getHeight() + 15 && paintView.arrowsXY[9] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 86 - paintView.arr1.getHeight()) ||
                            (paintView.arrowsXY[8] > paintView.pearXY[0] - paintView.arr1.getWidth() + 5 && paintView.arrowsXY[8] < paintView.pearXY[0] + paintView.pear.getWidth() - 5
                                    && paintView.arrowsXY[9] > paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() && paintView.arrowsXY[9] < paintView.pearXY[1] + (int) paintView.pear.getHeight() * 59 / 85 - paintView.arr1.getHeight() + 26)) {
                        gameState = 1;
                        s5 = 0;
                        resultToShow = result;
                    }

                    //CHOWA POWOLI WYŚWIETLONY WCZEŚNIEJ WYNIK
                    if (intensitySCORE > 0 && gameState == 0) intensitySCORE -= 4;
                    if (intensitySCORE < 0 && gameState == 0) intensitySCORE = 0;

                    // SPRAWDZA CZY WSZYSTKIE POZA EKRANEM LUB WBITE
                    if (gameState != 0) {
                        if ((s1 == 0 || paintView.arrowsXY[1] > canvas.getHeight()) && (s2 == 0 || paintView.arrowsXY[3] > canvas.getHeight()) && (s3 == 0 || paintView.arrowsXY[5] > canvas.getHeight()) && (s4 == 0 || paintView.arrowsXY[7] > canvas.getHeight()) && (s5 == 0 || paintView.arrowsXY[9] > canvas.getHeight()))
                            gameState = -1;

                        //ZAPIS REKORDU
                        if (result >= context.loadHighScore()) context.saveHighScore(result);
                        bestScore = context.loadHighScore();

                        //ROZPOCZYNA POWOLNE RYSOWANIE WYNIKU
                        if (intensitySCORE < 180) intensitySCORE += 4;
                    }


                    //(CZEKANIE) I RYSOWANIE
                   // try {
                    //    Thread.sleep(1);
                    //} catch (InterruptedException e) {
                    //}
                    synchronized (surfaceHolder) {
                        paintView.onDraw(canvas);
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                //ZAPIS REKORDU
                if (result >= context.loadHighScore()) context.saveHighScore(result);

                //KOŃCZY POWOLNE WYŚWIETLANIE WYNIKU
                for (; intensitySCORE < 200; intensitySCORE += 4) {
                    canvas = surfaceHolder.lockCanvas();
                    paintView.onDraw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                }


                //WYŚWIETLANIE PLAY AGAIN (-1) PO KOLIZJI, OPADNIĘCIU I POKAZANIU WYNIKU
                while (gameState == -1) {
                    if (intensityA > Math.PI - 0.02) intensityA = 0;
                    intensityPLAYAGAIN = (int) ((Math.sin(intensityA)) * 170);
                    try {
                        Thread.sleep(45);
                    } catch (InterruptedException e) {
                        ;
                    }
                    canvas = surfaceHolder.lockCanvas();
                    paintView.onDraw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    intensityA += 0.1;
                }

            }//while
        } catch(Exception e) {
            if (result >= context.loadHighScore()) context.saveHighScore(result);
            bestScore = context.loadHighScore();
        }
    }//ran()


}
