package com.stroketest.olgamart.stroketest;


import android.content.Context;

import android.content.res.Resources;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by olgamart on 01/11/2017.
 */

public class MovementView  extends SurfaceView implements SurfaceHolder.Callback {


    private SurfaceHolder mSurfaceHolder;
    private UpdateThread updateThread;
    Resources rs = getResources();

    public MovementView(Context context, Patient p) {

        super(context);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        updateThread = new UpdateThread(context, mSurfaceHolder, p, rs);

    }

    public void surfaceCreated(SurfaceHolder holder) {

       updateThread.setRunning(true);
       updateThread.start();

    }

   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

   }

    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;

        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
}

