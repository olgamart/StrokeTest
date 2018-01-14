package com.stroketest.olgamart.stroketest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.SurfaceHolder;


/**
 * Created by olgamart on 01/11/2017.
 */

public class UpdateThread extends Thread {

    private double xPos;
    private double yPos;

    private double xVel;
    private double yVel;

    private int width;
    private int height;
    int fr_size; // size of frame
    int set_fr; //position of frame

    private int circleRadius;

    private SurfaceHolder mSurfaceHolder;
    private SensorSimulation mAccelerometer;

    private long time;

    final private long timeTestlimited = 18000; // Time limit for test in milliseconds
    final private long timeSleep = 2000;
    private long timeStart;
    private boolean fl_time = false;  //Time out for test
    private boolean fl_test = false;  //Test executed / failed
    private boolean cross_frame = false;
    Resources res;
    public Patient p;


    private final int fps = 20;
    private boolean toRun;


    public UpdateThread(Context context, SurfaceHolder surfaceHolder, Patient pt, Resources rs) {
        mSurfaceHolder = surfaceHolder;
        toRun = false;
        mAccelerometer = new SensorSimulation(context);
        res = rs;
        p = pt;
    }


    public void setRunning(boolean run) {

        toRun = run;
    }

    @Override
    public void run() {

        Canvas c = null;

        Rect surfaceFrame = mSurfaceHolder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();
        circleRadius = width / 15;
        xPos = width / 2;
        yPos = height / 2;
        if (p.testing == 0) p.count = 0;
        p.result_test = 0;
        if (p.testing != 0) set_fr = p.set_fr_test;
        else set_fr = p.set_fr_training;

// pause before the movement begins
        try {
            c = mSurfaceHolder.lockCanvas();
            draw(c);

        } finally {
            if (c != null) {
                mSurfaceHolder.unlockCanvasAndPost(c);
            }
        }

        try {
            if (time == 0) sleep(timeSleep);
        } catch (Exception e) {
        }
// end pause

        timeStart = System.currentTimeMillis(); // start time

        while (toRun) {

            long cTime = System.currentTimeMillis();
            if (p.testing != 0) p.time_test = (int) (cTime + timeSleep - timeStart);
            else p.time_training = (int) (cTime - timeStart);


            if ((cTime - time) <= (1000 / fps)) {
                c = null;

                xVel = xVel * 0.98 + mAccelerometer.getAcsX() / 10;
                yVel = yVel * 0.98 + mAccelerometer.getAcsY() / 10;
                try {

                    c = mSurfaceHolder.lockCanvas();
                    if (c == null) break;
                    synchronized (mSurfaceHolder) {
                        updatePhysics();
                        draw(c);
// Hit to frame

                        if (setVin() && p.testing == 0) {
                            p.count++;
                            try {
                                sleep(timeSleep);
                            } catch (Exception e) {
                            }
                        }
                        if (setVin() && p.testing == 1) {
                            fl_test = true;
                            break;
                        }

//Determining the time limit
                        if (p.testing == 1 && cTime >= (timeStart + timeTestlimited)) {
                            p.time_test = (int) timeTestlimited;
                            fl_time = true;
                            break;
                        }
                    }
                } finally {

                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            time = cTime;

        }
//If time is out

        if (fl_time) {
            try {
                c = mSurfaceHolder.lockCanvas();
                mDrawText(c, res.getString(R.string.draw_timeTest));

            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }

//If the test is executed
        if (fl_test) {
            try {
                c = mSurfaceHolder.lockCanvas();
                mDrawText(c, res.getString(R.string.draw_testExecuted));
                p.result_test = 1;

            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }


    public void draw(Canvas canvas) {

        canvas.drawColor(res.getColor(R.color.fon_color));
        Paint paint = new Paint();

        paint.setColor(res.getColor(R.color.frame_color));

        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        fr_size = canvas.getWidth() / 5;

        switch (set_fr) {
            case 1:
                canvas.drawRect(0, 0, fr_size, fr_size, paint);
                break;
            case 2:
                canvas.drawRect(canvas.getWidth() - fr_size, 0, canvas.getWidth(), fr_size, paint);
                break;
            case 3:
                canvas.drawRect(canvas.getWidth() - fr_size, canvas.getHeight() - fr_size, canvas.getWidth(), canvas.getHeight(), paint);
                break;
            case 4:
                canvas.drawRect(0, canvas.getHeight() - fr_size, fr_size, canvas.getHeight(), paint);
                break;
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(res.getColor(R.color.mode_color));

        paint.setTextSize(32);

        if (p.testing == 1) canvas.drawText(res.getString(R.string.draw_test), 200, 40, paint);
        else canvas.drawText(res.getString(R.string.draw_training), 175, 40, paint);


        Shader radialGradientShader = new RadialGradient(
                (int) xPos, (int) yPos, (float) (circleRadius * 1.4),
                res.getColor(R.color.gradient_light), res.getColor(R.color.gradient_dark),
                Shader.TileMode.MIRROR);

        Paint circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setShader(radialGradientShader);
        canvas.drawCircle((int) xPos, (int) yPos, circleRadius, circlePaint);

    }

    public void updatePhysics() {

        xPos -= xVel;
        yPos += yVel;

        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {
            if (yPos - circleRadius < 0) {
                yPos = circleRadius;
            } else {
                yPos = height - circleRadius;
            }
            yVel *= -1;
        }
        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {
            if (xPos - circleRadius < 0) {
                xPos = circleRadius;
            } else {
                xPos = width - circleRadius;
            }
            xVel *= -1;
        }
    }



    public boolean setVin() {
        boolean goal = false;
        switch (set_fr) {
            case 1:
                if (xPos < (fr_size - circleRadius - 10) && yPos < (fr_size - circleRadius - 10)) {
                    if (p.testing == 0) {
                        if (!cross_frame) {
                            cross_frame = true;
                            goal = true;
                        }
                    } else goal = true;
                } else if (p.testing == 0) cross_frame = false;
                break;
            case 2:
                if (xPos > (width - fr_size + circleRadius + 10) && yPos < (fr_size - circleRadius - 10)) {
                    if (p.testing == 0) {
                        if (!cross_frame) {
                            cross_frame = true;
                            goal = true;
                        }
                    } else goal = true;
                } else if (p.testing == 0) cross_frame = false;
                break;
            case 3:
                if (xPos > (width - fr_size + circleRadius + 10) && yPos > (height - fr_size + circleRadius + 10)) {
                    if (p.testing == 0) {
                        if (!cross_frame) {
                            cross_frame = true;
                            goal = true;
                        }
                    } else goal = true;
                } else if (p.testing == 0) cross_frame = false;
                break;
            case 4:
                if (xPos < (fr_size - circleRadius - 10) && yPos > (height - fr_size + circleRadius + 10)) {
                    if (p.testing == 0) {
                        if (!cross_frame) {
                            cross_frame = true;
                            goal = true;
                        }
                    } else goal = true;
                } else if (p.testing == 0) cross_frame = false;
                break;
        }

        return goal;
    }

    public void mDrawText(Canvas canvas, String text) {

        Paint paint;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(res.getColor(R.color.result_test));
        paint.setTextSize(40);
        canvas.drawText(text, 50, 140, paint);

    }

}

