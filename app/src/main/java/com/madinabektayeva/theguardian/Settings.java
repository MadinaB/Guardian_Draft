package com.madinabektayeva.theguardian;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    private String command1;
    private String description1;
    private String mail1;
    private String command2;
    private String description2;
    private String mail2;

    private EditText command1Field;
    private EditText description1Field;
    private EditText mail1Field;
    private EditText command2Field;
    private EditText description2Field;
    private EditText mail2Field;

    private Button saveButton;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("TheGuardianData", MODE_PRIVATE);
        editor = getSharedPreferences("TheGuardianData", MODE_PRIVATE).edit();

        saveButton = (Button)findViewById(R.id.saveButton);

        command1Field = (EditText)findViewById(R.id.command1EditText);
        command2Field = (EditText)findViewById(R.id.command2EditText);
        description1Field = (EditText)findViewById(R.id.description1EditText);
        description2Field = (EditText)findViewById(R.id.description2EditText);
        mail1Field = (EditText)findViewById(R.id.mail1EditText);
        mail2Field = (EditText)findViewById(R.id.mail2EditText);

        updateCommand1();
        updateCommand2();
        updateDescription1();
        updateDescription2();
        updateMail1();
        updateMail2();

        command1Field.setText(command1);
        command2Field.setText(command2);
        description1Field.setText(description1);
        description2Field.setText(description2);
        mail1Field.setText(mail1);
        mail2Field.setText(mail2);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command1 = command1Field.getText().toString();
                description1 = description1Field.getText().toString();
                mail1 = mail1Field.getText().toString();
                command2 = command2Field.getText().toString();
                description2 = description2Field.getText().toString();
                mail2 = mail2Field.getText().toString();

                editor.putString("command1", command1);
                editor.putString("command2", command2);
                editor.putString("description1", description1);
                editor.putString("description2", description2);
                editor.putString("mail1", mail1);
                editor.putString("mail2", mail2);

                editor.apply();

                updateCommand1();
                updateCommand2();
                updateDescription1();
                updateDescription2();
                updateMail1();
                updateMail2();

                finish();
            }
        });
    }

    private void updateCommand1(){
        command1 = prefs.getString("command1", "");
    }

    private void updateDescription1(){
        description1 = prefs.getString("description1", "");
    }

    private void updateMail1(){
        mail1 = prefs.getString("mail1", "");
    }

    private void updateCommand2(){
        command2 = prefs.getString("command2", "");
    }

    private void updateDescription2(){
        description2 = prefs.getString("description2", "");
    }

    private void updateMail2(){
        mail2 = prefs.getString("mail2", "");
    }
}
