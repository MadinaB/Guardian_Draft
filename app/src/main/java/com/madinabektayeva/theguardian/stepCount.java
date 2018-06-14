package com.madinabektayeva.theguardian;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by madina on 14.06.18.
 */

import java.util.Calendar;

import static android.hardware.SensorManager.SENSOR_DELAY_UI;

public class stepCount extends Activity implements SensorEventListener {

    private Calendar lastDate;
    private Calendar currentDate;
    private int currentStepCount = 0;
    private int lastStepCount = 0;
    private int initialStepCount = -1;
    private int stepDistance = 15;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        int timepassed;

        currentDate = Calendar.getInstance();

        if (lastDate == null) {
            lastDate = currentDate;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            currentStepCount = (int) sensorEvent.values[0];
        }

        timepassed = (int) ((lastDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (1000 * 60) % 60);

        if (timepassed < 3) {
            if (currentStepCount > lastStepCount + stepDistance) {
                //if (!speechReconitionOn) {
                    //  recognizespeech
                    lastDate = currentDate;
                    lastStepCount = currentStepCount;
                    Log.v(" ", "Speech recognition start");
                    // startSpeechRecognition();
               // } else {
                    //hamdle gps update
              //  }
            }
        } else {
            lastDate = currentDate;
            lastStepCount = currentStepCount;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
