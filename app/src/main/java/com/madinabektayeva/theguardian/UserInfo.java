package com.madinabektayeva.theguardian;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInfo extends AppCompatActivity {

    private String name;
    private String surname;
    private String phone;

    private EditText nameField;
    private EditText surnameField;
    private EditText phoneField;

    private Button saveButton;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        prefs = getSharedPreferences("TheGuardianData", MODE_PRIVATE);
        editor = getSharedPreferences("TheGuardianData", MODE_PRIVATE).edit();

        saveButton = (Button)findViewById(R.id.saveButton);

        nameField = (EditText)findViewById(R.id.nameEditText);
        surnameField = (EditText)findViewById(R.id.surnameEditText);
        phoneField = (EditText)findViewById(R.id.phoneEditText);

        updateName();
        updateSurname();
        updatePhone();

        nameField.setText(name);
        surnameField.setText(surname);
        phoneField.setText(phone);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameField.getText().toString();
                surname = surnameField.getText().toString();
                phone = phoneField.getText().toString();

                editor.putString("name", name);
                editor.putString("surname", surname);
                editor.putString("phone", phone);

                editor.apply();

                updateName();
                updateSurname();
                updatePhone();

                finish();
            }
        });
    }

    private void updateName(){
        name = prefs.getString("name", "");
    }

    private void updateSurname(){
        surname = prefs.getString("surname", "");
    }

    private void updatePhone(){
        phone = prefs.getString("phone", "");
    }

}
