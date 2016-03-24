package com.androbro.mypowernotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Context context;
    EditText name;
    EditText email;
    EditText pass1;
    EditText pass2;
    Button registerBtn;
    SharedPreferences myPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;

        myPrefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        boolean regComplete = myPrefs.getBoolean("registered", false);

        if (regComplete) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            name = (EditText) findViewById(R.id.nameRegister);
            email = (EditText) findViewById(R.id.emailRegister);
            pass1 = (EditText) findViewById(R.id.passRegister);
            pass2 = (EditText) findViewById(R.id.confirmPassRegister);
            registerBtn = (Button) findViewById(R.id.registerButton);

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameStr = name.getText().toString();
                    String emailStr = email.getText().toString();
                    String pass1Str = pass1.getText().toString();
                    String pass2Str = pass2.getText().toString();

                    myPrefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();

                    if (!pass1Str.equals(pass2Str)) {
                        Toast.makeText(RegisterActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putString("namestr", nameStr);
                        editor.putString("emailstr", emailStr);
                        editor.putString("pass1str", pass1Str);
                        editor.putBoolean("registered", true);
                        editor.apply();
                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
