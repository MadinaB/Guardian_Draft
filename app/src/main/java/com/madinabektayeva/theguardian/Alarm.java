package com.madinabektayeva.theguardian;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Alarm extends AppCompatActivity implements
        TextToSpeech.OnInitListener {
    private Button yes_button;
    private Button no_button;
    private String alarm;
    private SharedPreferences prefs;
    private boolean alarmSituation;
    private TextToSpeech tts;
    private AudioManager audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        tts = new TextToSpeech(this, this);

        yes_button = (Button) findViewById(R.id.yes_button);
        no_button = (Button) findViewById(R.id.no_button);

        prefs = getSharedPreferences("TheGuardianData", MODE_PRIVATE);
        alarm = prefs.getString("alarm", (prefs.getString("name", "user")+" is in danger. Police was informed. "));

        unmuteAudio();
        increaseAlarm();
        speakOut();

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
                finish();
            }
        });

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
                speakOut();
            }
        });


    }
    private void increaseAlarm(){
        int i =0;
        while(i<100){
            alarm+="! ";
            alarm+=alarm;
            i++;
        }
    }

    private void stopAlarm() {
        alarmSituation = false;
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {
        alarmSituation = true;
        tts.speak(alarm, TextToSpeech.QUEUE_FLUSH, null);

    }

    private void unmuteAudio() {
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 1);
    }
}

