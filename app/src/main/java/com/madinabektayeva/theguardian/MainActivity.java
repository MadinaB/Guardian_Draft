package com.madinabektayeva.theguardian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

    private SharedPreferences prefs;

    private boolean location_pressed;
    private boolean contact_pressed;
    private boolean mic_pressed;
    private boolean call_pressed;
    private boolean alarm_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            location.setBackgroundResource(R.drawable.location_button_white);
                        }
                    }, 5000);
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
                            contact.setBackgroundResource(R.drawable.contact_button_white);
                        }
                    }, 5000);
                }
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic_pressed = (!mic_pressed);
                if (mic_pressed) {
                    mic.setBackgroundResource(R.drawable.mic_button_yellow);
                } else {
                    mic.setBackgroundResource(R.drawable.mic_button_white);
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_pressed = (!call_pressed);
                if (call_pressed) {
                    requestCall();
                    call.setBackgroundResource(R.drawable.call_button_yellow);
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            call.setBackgroundResource(R.drawable.call_button_white);
                        }
                    }, 5000);
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
                            alarm.setBackgroundResource(R.drawable.onoff_button_white);
                        }
                    }, 5000);
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

    private boolean requestCall(){

        StringBuilder subject = new StringBuilder();
        StringBuilder message = new StringBuilder();

        ArrayList<String> recipients = new ArrayList<>();

        subject.append("Notice from TheGuardian: Call request from ");
        subject.append(name);
        subject.append(" ");
        subject.append(surname);

        message.append("Following user asks you to call them right now: \n");
        message.append(name);
        message.append(" ");
        message.append(surname);
        message.append(". \n");
        message.append("Phone: ");
        message.append(phone);
        message.append(". \n");

        recipients.add(mail1);
        recipients.add(mail2);

        Log.d("mail1","_"+mail1+"_");
        Log.d("mail1", "_"+mail2+"_");
        Log.d("mailLogin","_"+mailLogin+"_");
        Log.d("mailPass", "_"+mailPass+"_");

        sendMessage(mailLogin, mailPass, subject.toString(), message.toString(), recipients);

        return true;
    }

    private static boolean sendMessage(String from, String pass, String subject, String message, ArrayList<String> recipients){
        Mail mailService = new Mail();
        String[] to = recipients.toArray(new String[recipients.size()]);
        mailService.sendFromGMail(from, pass, to, subject, message);
        return true;

    }

}
