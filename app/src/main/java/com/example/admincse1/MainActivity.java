package com.example.admincse1;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    TextToSpeech t1;
    String voice = "please enter user name and password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);

        t1 = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.SUCCESS) {
                            int lang = t1.setLanguage(Locale.ENGLISH);
                        }
                        int speech = t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });


        Info.setText("No of attempts remaining: 5");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(String userName, String userPassword){
        if((userName.equals("root")) && (userPassword.equals("toor"))){
            int speech = t1.speak("Access Granted", TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(MainActivity.this, PostListActivity.class);
            startActivity(intent);
        }else{
            int speech = t1.speak("access denied", TextToSpeech.QUEUE_FLUSH, null);

            counter--;

            Info.setText("No of attempts remaining: " + String.valueOf(counter));

            if(counter == 0){
                Login.setEnabled(false);
            }
        }
    }

}