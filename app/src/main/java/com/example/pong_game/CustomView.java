package com.example.pong_game;


import android.content.Context;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
//import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class CustomView extends View {

    private Rect lRect,rRect,bRect,tRect;
    private Paint rectPaint, bPaint, textPaint, textPaint1;
    private float bCircleX;
    private float bCircleY;

    int directionY=1, directionX, max,score, sScore, rDirection,directionXTemp,DimenX,DimenY;
    float x,y,speed=1,xCoordinate,xCoordinateTemp;
    public boolean toStart=true;
    CustomView CustomView;
    Timer bTimer = new Timer();
    Timer hardTimer = new Timer();

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            moveBallCases();

        }
    };
    TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            speed = (speed*5)/4;
        }
    };

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int highScoreEP, highScoreEC, highScoreHP, highScoreHC;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init(){
        lRect = new Rect();
        rRect = new Rect();
        bRect = new Rect();
        tRect = new Rect();

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);

        CustomView = findViewById(R.id.CustomView);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        highScoreEP= sharedPreferences.getInt("highScoreEP",0);
        highScoreEC= sharedPreferences.getInt("highScoreEC",0);
        highScoreHP= sharedPreferences.getInt("highScoreHP",0);
        highScoreHC= sharedPreferences.getInt("highScoreHC",0);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (toStart) {
            directionX = -1 + 2 * ((int) Math.round(Math.random() * (1 - 0)));
            if (MainActivity.order.equals("Hard")) {
                if (MainActivity.mode.equals("CM")) {
                    tRect.left = getWidth() / 2 - 100;
                    tRect.right = getWidth() / 2 + 100;


                    hardTimer.scheduleAtFixedRate(task1, 15000, 15000);
                }
                if (MainActivity.mode.equals("Practice")) {
                    tRect.left = 15;
                    tRect.right = getWidth() - 15;


                    hardTimer.scheduleAtFixedRate(task1, 10000, 15000);
                }
            }




            if (MainActivity.order.equals("Easy")) {
                if (MainActivity.mode.equals("CM")) {
                    tRect.left = getWidth() / 2 - 100;
                    tRect.right = getWidth() / 2 + 100;
                    max = randBetween(3, 8);
                }
                if (MainActivity.mode.equals("Practice")) {
                    tRect.left = 15;
                    tRect.right = getWidth() - 15;
                }

            }
            bTimer.scheduleAtFixedRate(task, 0, 2);
            toStart = false;
            bCircleX = randBetween(135, getWidth() - 135);
            bCircleY = getHeight() / 2f;

            bRect.left = getWidth() / 2 - 100;
            bRect.right = getWidth() / 2 + 100;
        }




        if (bTimer == null && !toStart) {
            if (bCircleY  <= 260) {
                textPaint1.setTextSize(150);
                textPaint1.setColor(Color.parseColor("#ffffff"));
                canvas.drawText("You Won!", getWidth() / 2f - 290, getHeight() / 2f - 50, textPaint1);
                canvas.drawText("Score: " + score, getWidth() / 2f - 270, getHeight() / 2f + 110, textPaint1);
            } else {
                textPaint1.setTextSize(150);
                textPaint1.setColor(Color.parseColor("#ffffff"));
                canvas.drawText("You Lost!", getWidth() / 2f - 295, getHeight() / 2f - 50, textPaint1);
                canvas.drawText("Score: " + score, getWidth() / 2f - 270, getHeight() / 2f + 110, textPaint1);
            }


        } else {
            lRect.left = 0;

            lRect.right = 15;
            lRect.bottom = getHeight() - 170;

            textPaint.setTextSize(70);
            textPaint.setColor(Color.WHITE);
            rectPaint.setColor(Color.WHITE);


            bPaint.setColor(Color.parseColor("#e7d2cc"));

            canvas.drawRect(lRect, rectPaint);


            rRect.left = getWidth() - 15;

            rRect.right = getWidth();
            rRect.bottom = getHeight() - 170;

            canvas.drawRect(rRect, rectPaint);

            bRect.top = getHeight() - 200;
            bRect.bottom = getHeight() - 170;


            if (bRect.right >= (getWidth() - 15)) {
                bRect.right = (getWidth() - 15);
                bRect.left = getWidth() - 215;
            }
            if (bRect.left <= 15) {
                bRect.left = 15;
                bRect.right = 215;
            }

            canvas.drawRect(bRect, rectPaint);
            if (MainActivity.mode.equals("Practice")) {
                tRect.top = 235;
                rRect.top = 235;
                lRect.top = 235;
            } else {
                tRect.top = 220;
                rRect.top = 220;
                lRect.top = 220;
            }
            tRect.bottom = 250;



            canvas.drawRect(tRect, rectPaint);


            float bRadius = 20f;
            canvas.drawCircle(bCircleX, bCircleY, bRadius, bPaint);

            canvas.drawText("Your Score: " + score, getWidth() - 550, getHeight() - 60, textPaint);

            if (MainActivity.mode.equals("CM")) {
                textPaint1.setTextSize(80);
                textPaint1.setColor(Color.parseColor("#e7d2cc"));
                canvas.drawText("Computer Score:" + sScore, getWidth() - 630, 190, textPaint);
                canvas.drawText("COMPUTER MODE", getWidth() / 2f - 340, 100, textPaint1);
            }
            if (MainActivity.mode.equals("Practice")) {
                canvas.drawText("PRACTICE MODE", getWidth() / 2f - 350, 150, textPaint1);
                textPaint1.setTextSize(100);
                textPaint1.setColor(Color.parseColor("#e7d2cc"));
            }

        }




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                return true;
            }

            case MotionEvent.ACTION_MOVE:{
                x = event.getX();
                y = event.getY();

                if ((bRect.left<= x && bRect.right>= x) && ((getHeight() - 450) <= y && y<= getHeight())) {
                    bRect.left = (int) (x - 100);
                    bRect.right = (int) (x + 100);

                    postInvalidate();

                    return true;
                }

                return value;
            }
        }

        return value;
    }

    private void moveBallCases(){
        if (directionY==1){
            if (bCircleX >= (getWidth() - 35) && bCircleY <= (getHeight() - 220)){
                directionX = -1;
                moveBall();

            }
            else if (bCircleX <= 35 && bCircleY <= (getHeight() - 220)){
                directionX = 1;
                moveBall();

            }
            else if ((getHeight()-200) >= bCircleY && bCircleY >= (getHeight()-220)){
                if ((bCircleX + 14) >=bRect.left && (bCircleX - 14) <= bRect.right) {

                    directionY = -1;
                    score += 1;
                    xCoordinateTemp = bCircleX-15;

                    directionXTemp = directionX;

                } moveBall();
            }
            else if ((bCircleX >= (getWidth() +15) || bCircleX + 15 <= 0) && bCircleY >= (getHeight() - 220)){

                bTimer.cancel();
                bTimer = null;

                highScore();
            } else if (bCircleY - 15 >= getHeight()){
                if (MainActivity.order.equals("Hard")){
                    hardTimer.cancel();
                }

                bTimer.cancel();
                bTimer = null;

                highScore();


            } else moveBall();

        }
        else if (directionY == -1) {
            if (bCircleX >= (getWidth() - 35) && bCircleY != 270) {
                directionX = -1;

                moveBall();

            } else if (bCircleX <= 35 && bCircleY != 270) {
                directionX = 1;
                moveBall();

            } else if (bCircleY <= 270 && bCircleX != (getWidth() - 35) && bCircleX != 35) {

                if ((MainActivity.order.equals("Easy") && MainActivity.mode.equals("CM")) && score == max){
                    directionY = -1;
                    if (bCircleY <= 100) {

                        directionY=0;
                        directionX=0;
                        bTimer.cancel();
                        bTimer = null;
                        highScore();
                    }
                }else {
                    directionY = 1;
                    sScore += 1;

                }
                moveBall();
            } else if (bCircleX >= (getWidth() - 35) && bCircleY <= 270) {

                directionX = -1;
                directionY = 1;
                moveBall();
                sScore += 1;

            } else if (bCircleX <= 35 && bCircleY <= 270) {
                directionX = 1;
                directionY = 1;
                sScore += 1;
                moveBall();

            } else {
                moveBall();
            }
            if (MainActivity.mode.equals("CM")){
                moveBarCases();
            }
        }

    }

    private void moveBall(){
        bCircleX = bCircleX + speed*directionX;
        bCircleY = bCircleY + speed*directionY;
        invalidate();

    }
    private static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));

    }

    private void moveBarCases(){
        DimenX = getWidth() - 30;
        DimenY = getHeight() - 450;
        if (directionXTemp == 1){
            if ((DimenY - (DimenX -xCoordinateTemp)) > DimenX){
                xCoordinate = ((DimenY- (DimenX -xCoordinateTemp))% DimenX)+30;

            } else {
                xCoordinate = (DimenX - (DimenY - (DimenX -xCoordinateTemp))) + 15 ;

            }
        }
        if(directionXTemp == -1) {
            if ((DimenY - xCoordinateTemp) < DimenX){
                xCoordinate = (DimenY - xCoordinateTemp) + 15;

            } else {
                xCoordinate = (DimenX - ((DimenY-xCoordinateTemp)%DimenX));

            }
        }
        easyCase();
    }
    private void  moveBar(){
        if (xCoordinate<=115){
            if (tRect.left>15){
                rDirection = -1;
                barSpeed();
            } else rDirection = 0;
        }
        else if (xCoordinate>= getWidth() - 115){
            if (tRect.right< getWidth() - 15){
                rDirection = 1;
                barSpeed();
            } else rDirection = 0;
        }
        else if (xCoordinate> 115 && xCoordinate < getWidth() - 115){
            if (xCoordinate < tRect.left + 100){
                rDirection = -1;
                barSpeed();
            }
            else if (xCoordinate > tRect.left + 100){
                rDirection = 1;
                barSpeed();
            } else rDirection = 0;
        }
    }
    private void barSpeed(){
        tRect.left = tRect.left + (((int) speed))*rDirection;
        tRect.right = tRect.right + ((int) speed)*rDirection;
        postInvalidate();
    }
    public void easyCase(){
        if (MainActivity.order.equals("Easy") && MainActivity.mode.equals("CM")) {
            if (score == max && (xCoordinate < (tRect.right + 10) && xCoordinate > (tRect.left - 10))) {
                if (xCoordinate < getWidth() / 2f) {
                    rDirection = 1;
                    barSpeed();
                }else if (xCoordinate > getWidth() / 2f) {
                    rDirection = -1;
                    barSpeed();
                }
            }else if (score == max && (xCoordinate > (tRect.right + 10) || xCoordinate < (tRect.left - 10))) {
                rDirection = 0;
                barSpeed();
            }
            else moveBar();
        }
        else moveBar();
    }

    public void highScore(){
        if (MainActivity.mode.equals("Practice")){
            if (MainActivity.order.equals("Easy")){
                if (score>highScoreEP){
                    editor.putInt("highScoreEP",score);
                    editor.commit();
                }
            }
            if (MainActivity.order.equals("Hard")){
                if (score>highScoreHP){
                    editor.putInt("highScoreHP",score);
                    editor.commit();
                }
            }
        }
        if (MainActivity.mode.equals("CM")){
            if (MainActivity.order.equals("Easy")){
                if (score>highScoreEC){
                    editor.putInt("highScoreEC",score);
                    editor.commit();
                }
            }
            if (MainActivity.order.equals("Hard")){
                if (score>highScoreHC){
                    editor.putInt("highScoreHC",score);
                    editor.commit();
                }
            }
        }
    }

}
