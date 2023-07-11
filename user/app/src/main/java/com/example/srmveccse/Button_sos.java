package com.example.srmveccse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class Button_sos extends AppCompatActivity {
    private static final int REQUEST_CALL =1;
    private Button button1,button2,button3,button4;
    TextToSpeech t1;
    String voice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_sos);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
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


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call("8610859572");
                    }
                }, 2000);
                int speech = t1.speak("Calling control room", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call("100");
                    }
                }, 2000);
                int speech = t1.speak("Calling police station", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call("108");
                    }
                }, 2000);
                int speech = t1.speak("Calling ambulance", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call("102");
                    }
                }, 2000);
                int speech = t1.speak("Calling fire station", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    private void call(String number) {
        if(ContextCompat.checkSelfPermission(Button_sos.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Button_sos.this,
                    new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else{
            String dial ="tel:"+number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                call("8610859572");
            } else {
                Toast.makeText(getApplicationContext(),"PERMISSION DENIED",Toast.LENGTH_LONG).show();
            }
        }
    }
}
