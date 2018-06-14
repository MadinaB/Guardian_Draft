package com.madinabektayeva.theguardian;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.*;
import android.media.AudioManager;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SensorEventListener, RecognitionListener {

    private static final int REQUEST_RECORD_PERMISSION = 100;


    private Button settings;
    private Button user_info;
    private Button info;
    private Button location;
    private Button contact;
    private Button mic;
    private Button call;
    private Button alarm;

    private String mailLogin;
    private String mailPass;

    private String name;
    private String surname;
    private String phone;
    private String command1;
    private String description1;
    private String mail1;
    private String command2;
    private String description2;
    private String mail2;


    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    private SharedPreferences prefs;

    private boolean location_pressed;
    private boolean contact_pressed;
    private boolean mic_pressed;
    private boolean call_pressed;
    private boolean alarm_pressed;
    private boolean mic_enabled;
    private boolean speechReconitionOn;

    private AudioManager audio;
    private boolean isAudioMuted;

    private TextView returnedText;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        returnedText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        speechReconitionOn = false;

        mailLogin = "theGuardianInforms@gmail.com";
        mailPass = "dummyPass";


        settings = (Button) findViewById(R.id.settings);
        user_info = (Button) findViewById(R.id.user_info);
        info = (Button) findViewById(R.id.info);
        location = (Button) findViewById(R.id.location);
        contact = (Button) findViewById(R.id.contact);
        mic = (Button) findViewById(R.id.mic);
        call = (Button) findViewById(R.id.call);
        alarm = (Button) findViewById(R.id.onoff);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        prefs = getSharedPreferences("TheGuardianData", MODE_PRIVATE);

        location_pressed = false;
        contact_pressed = false;
        mic_pressed = false;
        call_pressed = false;
        alarm_pressed = false;

        updateName();
        updateSurname();
        updatePhone();
        updateCommand1();
        updateCommand2();
        updateDescription1();
        updateDescription2();
        updateMail1();
        updateMail2();

        mic_enabled = true;
        mic_pressed = true;
        mic.setBackgroundResource(R.drawable.mic_button_yellow);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();

                updateCommand1();
                updateCommand2();
                updateDescription1();
                updateDescription2();
                updateMail1();
                updateMail2();
            }
        });

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserInfoActivity();

                updateName();
                updateSurname();
                updatePhone();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoActivity();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_pressed = (!location_pressed);
                if (location_pressed) {
                    location.setBackgroundResource(R.drawable.location_button_yellow);
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            if (location_pressed) {
                                location.setBackgroundResource(R.drawable.location_button_white);
                                sendMsg(locationMessage(), locationSubject(), mail1, mail1);// TODO locationMessage()
                                returnedText.setText("Location message was sent");
                                (new Handler()).postDelayed(new Runnable() {
                                    public void run() {
                                        returnedText.setText(" ");
                                    }
                                }, 3000);
                            }
                        }
                    }, 3000);
                } else {
                    location.setBackgroundResource(R.drawable.location_button_white);
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_pressed = (!contact_pressed);
                if (contact_pressed) {
                    contact.setBackgroundResource(R.drawable.contact_button_yellow);
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            if (contact_pressed) {
                                sendMsg(contactMessage(), contactSubject(), mail1, mail1);// TODO contatcmessage
                                contact.setBackgroundResource(R.drawable.contact_button_white);
                                returnedText.setText("Request for contact was sent");
                                (new Handler()).postDelayed(new Runnable() {
                                    public void run() {
                                        returnedText.setText(" ");
                                    }
                                }, 3000);
                            }
                        }
                    }, 3000);
                } else {
                    contact.setBackgroundResource(R.drawable.contact_button_white);
                }
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic_pressed = (!mic_pressed);
                if (mic_pressed) {
                    mic_enabled = true;
                    mic.setBackgroundResource(R.drawable.mic_button_yellow);
                } else {
                    mic_enabled = false;
                    if (speechReconitionOn) {
                        stopSpeechRecognition();
                    }
                    mic.setBackgroundResource(R.drawable.mic_button_white);
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_pressed = (!call_pressed);
                if (call_pressed) {
                    call.setBackgroundResource(R.drawable.call_button_yellow);
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            if (call_pressed) {
                                sendMsg(callMessage(), callSubject(),  mail1, mail1);
                                call.setBackgroundResource(R.drawable.call_button_white);
                                returnedText.setText("Call was requested");
                                (new Handler()).postDelayed(new Runnable() {
                                    public void run() {
                                        returnedText.setText(" ");
                                    }
                                }, 3000);
                            }
                        }
                    }, 3000);
                } else {
                    call.setBackgroundResource(R.drawable.call_button_white);
                }
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_pressed = (!alarm_pressed);
                if (alarm_pressed) {
                    alarm.setBackgroundResource(R.drawable.onoff_button_yellow);
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            if (alarm_pressed) {
                                sendMsg(alarmMessage(), alarmSubject(), mail1, mail1);
                                openAlarmActivity();
                                alarm.setBackgroundResource(R.drawable.onoff_button_white);
                                returnedText.setText("Alarm situation");
                                (new Handler()).postDelayed(new Runnable() {
                                    public void run() {
                                        returnedText.setText(" ");
                                    }
                                }, 3000);
                            }

                        }
                    }, 3000);
                } else {
                    alarm.setBackgroundResource(R.drawable.onoff_button_white);
                }
            }
        });


    }

    private void sendMail() {

    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void openInfoActivity() {
        Intent intent = new Intent(this, Info.class);
        startActivity(intent);
    }

    private void openUserInfoActivity() {
        Intent intent = new Intent(this, UserInfo.class);
        startActivity(intent);
    }

    private void openAlarmActivity() {
        Intent intent = new Intent(this, Alarm.class);
        startActivity(intent);
    }

    private void updateName() {
        name = prefs.getString("name", "");
    }

    private void updateSurname() {
        surname = prefs.getString("surname", "");
    }

    private void updatePhone() {
        phone = prefs.getString("phone", "");
    }

    private void updateCommand1() {
        command1 = prefs.getString("command1", "");
    }

    private void updateDescription1() {
        description1 = prefs.getString("description1", "");
    }

    private void updateMail1() {
        mail1 = prefs.getString("mail1", "");
    }

    private void updateCommand2() {
        command2 = prefs.getString("command2", "");
    }

    private void updateDescription2() {
        description2 = prefs.getString("description2", "");
    }

    private void updateMail2() {
        mail2 = prefs.getString("mail2", "");
    }

    private String getLoctionOnMap() {
        return "";
    }

    private String locationMessage() {

        StringBuilder message = new StringBuilder();


        message.append("Following user shares with you their location: \n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Location: ");
        message.append(getLoctionOnMap());
        message.append(". \n");

        return message.toString();
    }

    private String locationSubject(){

        StringBuilder subject = new StringBuilder();

        subject.append("TheGuardian: ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);
        subject.append("shared her location ");

        return subject.toString();
    }

    private String contactMessage() {

        StringBuilder message = new StringBuilder();

        message.append("Following user asks you to contact them right now: \n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Location: ");
        message.append(getLoctionOnMap());
        message.append(". \n");

        return message.toString();
    }

    private String contactSubject(){

        StringBuilder subject = new StringBuilder();

        subject.append("TheGuardian: Contact Request from ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);

        return subject.toString();
    }

    private String alarmMessage() {

        StringBuilder message = new StringBuilder();

        message.append("Following user is in danger \n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Location: ");
        message.append(getLoctionOnMap());
        message.append(". \n");

        return message.toString();
    }

    private String alarmSubject(){

        StringBuilder subject = new StringBuilder();

        subject.append("TheGuardian: Alarm Situation. ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);

        return subject.toString();
    }

    private String callMessage() {

        StringBuilder message = new StringBuilder();

        message.append("Following user asks you to call them right now: \n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Phone: ");
        message.append(phone);
        message.append(". \n");

        return message.toString();
    }

    private String callSubject(){

        StringBuilder subject = new StringBuilder();

        subject.append("TheGuardian: Call Request from ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);

        return subject.toString();
    }

    private boolean sendMsg(String msg, String subject, String mail1, String mail2) {

        ArrayList<String> recipients = new ArrayList<>();

        recipients.add(mail1);

        if (!mail1.equals(mail2)) {
            recipients.add(mail2);
        }

        Log.d("mail1", "_" + mail1 + "_");
        Log.d("mail1", "_" + mail2 + "_");
        Log.d("mailLogin", "_" + mailLogin + "_");
        Log.d("mailPass", "_" + mailPass + "_");

        sendMessage(mailLogin, mailPass, subject, msg, recipients);

        return true;
    }

    private static boolean sendMessage(String from, String pass, String subject, String message, ArrayList<String> recipients) {
        Mail mailService = new Mail();
        String[] to = recipients.toArray(new String[recipients.size()]);
        mailService.sendFromGMail(from, pass, to, subject, message);
        return true;

    }

    private boolean sendMessageForCommand1(){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(mail1);

        StringBuilder subject = new StringBuilder();
        StringBuilder message = new StringBuilder();

        message.append("Following user requests" + command1 +"\n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Location: ");
        message.append(getLoctionOnMap());
        message.append("Additional Information: ");
        message.append(description1);
        message.append(". \n");

        subject.append("TheGuardian: "+ command1 +" Request from ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);



        Log.d("mail1", "_" + mail1 + "_");
        Log.d("mailLogin", "_" + mailLogin + "_");
        Log.d("mailPass", "_" + mailPass + "_");

        sendMessage(mailLogin, mailPass, subject.toString(), message.toString(), recipients);
        return true;
    }

    private boolean sendMessageForCommand2(){

        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(mail2);

        StringBuilder subject = new StringBuilder();
        StringBuilder message = new StringBuilder();

        message.append("Following user requests" + command2 +"\n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Location: ");
        message.append(getLoctionOnMap());
        message.append("Additional Information: ");
        message.append(description2);
        message.append(". \n");

        subject.append("TheGuardian: "+ command2 +" Request from ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);



        Log.d("mail1", "_" + mail1 + "_");
        Log.d("mailLogin", "_" + mailLogin + "_");
        Log.d("mailPass", "_" + mailPass + "_");

        sendMessage(mailLogin, mailPass, subject.toString(), message.toString(), recipients);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCountSensor != null) {
            sensorManager.registerListener((SensorEventListener) this, stepCountSensor, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this, "works", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onPause() {
        super.onPause();
        // sensorManager.unregisterListener((SensorListener) stepCounter);
    }

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

        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            currentStepCount = (int) sensorEvent.values[0];
        }

        if (lastDate == null) {
            lastDate = currentDate;
        }

        if (lastStepCount < 1) {
            lastStepCount = currentStepCount;
        }

        timepassed = (int) ((lastDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (1000 * 60) % 60);

        if (timepassed < 3) {
            if (currentStepCount > lastStepCount + stepDistance) {
                Toast.makeText(this, "timepassed " + timepassed + "'/n' currentStepCount " + currentStepCount + ". lastStepCount  " + lastStepCount, Toast.LENGTH_SHORT).show();
                if (!speechReconitionOn) {
                    lastDate = currentDate;
                    lastStepCount = currentStepCount;
                    Log.v(" ", "Speech recognition start");
                    startSpeechRecognition();
                }
            }
        } else {
            lastDate = currentDate;
            lastStepCount = currentStepCount;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        speechReconitionOn = false;
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
        returnedText.setText("");
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                unmuteAudio();
            }
        }, 5000);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        //  toggleButton.setChecked(false);
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        String bestMatch = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        for (String result : matches)
            text += result + "\n";

        if (bestMatch.contains(command1.toLowerCase().trim())) {
            Toast.makeText(this, command1 + " detected!", Toast.LENGTH_SHORT).show();
            stopSpeechRecognition();
            sendMessageForCommand1();
            openAlarmActivity();
        }

        if (bestMatch.contains(command2.toLowerCase().trim())) {
            Toast.makeText(this, command2 + " detected!", Toast.LENGTH_SHORT).show();
            stopSpeechRecognition();
            sendMessageForCommand2();
            openAlarmActivity();
        }

        returnedText.setText(bestMatch);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public void startSpeechRecognition() {
        speechReconitionOn = true;
        if (mic_enabled & speechReconitionOn) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            if (!isAudioMuted) {
                muteAudio();
            }
            ActivityCompat.requestPermissions
                    (MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_PERMISSION);
        }
    }

    public void stopSpeechRecognition() {
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
        speech.stopListening();
        speechReconitionOn = false;
    }

    private void muteAudio() {
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 1);
        isAudioMuted = true;
    }

    private void unmuteAudio() {
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 1);
        isAudioMuted = false;
    }




}


// https://maps.google.com/?q=<lat>,<lng>
//https://www.google.com/maps/?q=-15.623037,18.388672