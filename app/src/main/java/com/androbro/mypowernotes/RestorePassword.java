package com.androbro.mypowernotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RestorePassword extends AppCompatActivity {

    Session session;
    ProgressDialog pdialog;
    Context context;
    SharedPreferences myPrefs;
    String username;
    String password;
    String email;
    EditText emailField;
    Button sendEmailBtn;
    String userEnteredEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        context = RestorePassword.this;

        emailField = (EditText) findViewById(R.id.emailField);
        sendEmailBtn = (Button) findViewById(R.id.sendEmail);

        myPrefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        username = myPrefs.getString("namestr", "");
        password = myPrefs.getString("pass1str", "");
        email = myPrefs.getString("emailstr", "");

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userEnteredEmail = emailField.getText().toString();
                if (!userEnteredEmail.equals(email)){

                    Toast.makeText(context, "Wrong email, check it again...", Toast.LENGTH_LONG).show();
                } else {

                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");

                    session = Session.getDefaultInstance(props, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("vectraincorporated@gmail.com", "warcraft3ft");
                        }
                    });

                    pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

                    RetreiveFeedTask task = new RetreiveFeedTask();
                    task.execute();

                }
            }
        });
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("vectraincorporated@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEnteredEmail));
                message.setSubject("PowerNote password restoration");
                message.setContent("Username: " + username + " , password: " + password, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            emailField.setText("");
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RestorePassword.this, LoginActivity.class);
        startActivity(intent);
    }
}
