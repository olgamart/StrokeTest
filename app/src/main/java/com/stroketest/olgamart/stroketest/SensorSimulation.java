package com.stroketest.olgamart.stroketest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;


import java.util.List;

/**
 * Created by olgamart on 05/11/2017.
 */

public class SensorSimulation implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mAccelerometerSensor;

    private float x;
    private float y;
    private float z;



    public SensorSimulation(Context context) {

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL); // list of sensors

        if (sensors.size() > 0) {

            for (Sensor sensor : sensors) {
                switch (sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        if (mAccelerometerSensor == null)
                            mAccelerometerSensor = sensor;
                        break;
                    default:
                        break;
                }
            }
        }
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;


        float[] values = event.values;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public double getAcsX(){
        return x / 9.8 * 4;
    }

    public double getAcsY(){
        return y / 9.8 * 4;
    }

}
