package com.androbro.mypowernotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText usernameMain;
    TextView forgotPassword;
    EditText passMain;
    String usernameEntered;
    String passwordEntered;
    SharedPreferences myPrefs;
    String username;
    String password;
    Button login;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;

        forgotPassword = (TextView) findViewById(R.id.passForgot);
        usernameMain = (EditText) findViewById(R.id.usernameMain);
        passMain = (EditText) findViewById(R.id.passMain);

        myPrefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        username = myPrefs.getString("namestr", null);
        password = myPrefs.getString("pass1str", null);

        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameEntered = usernameMain.getText().toString();
                passwordEntered = passMain.getText().toString();

                    if (passwordEntered.equals(password) && usernameEntered.equals(username)) {

                        usernameMain.setText("");
                        passMain.setText("");

                        Intent intent = new Intent(LoginActivity.this, NoteListActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Check your credentials", Toast.LENGTH_LONG).show();
                    }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RestorePassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
