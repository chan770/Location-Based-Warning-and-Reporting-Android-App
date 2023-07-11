package com.example.admincse1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Addspot extends AppCompatActivity  {
    private Button button;
    CheckBox c1,c2,c3,c4,c5;
    Spinner spinner1;
    String place,text="";
    int dam=0,num=0;
    String[] loc = {"Select the place","VEC New block","VEC First-year block","VEC Admin block","VEC Student parking","VEC Canteen","UB Bulding","SRM Tech-Park","Java-Green Food Court"};

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("spot");
    DatabaseReference times = database.getReference("times");


    TextToSpeech t1;
    String voice = "Creating new Data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addspot);

        button = findViewById(R.id.button);
        spinner1 = findViewById(R.id.spinner1);
        c1=findViewById(R.id.checkBox1);
        c2=findViewById(R.id.checkBox2);
        c3=findViewById(R.id.checkBox3);
        c4=findViewById(R.id.checkBox4);
        c5=findViewById(R.id.checkBox5);
        times.child("num")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        num=dataSnapshot.getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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

        getspinner1();

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked()){
                    dam=dam+5;
                    text=text+c1.getText().toString()+" , ";
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c2.isChecked()){
                    dam=dam+4;
                    text=text+c2.getText().toString()+" , ";
                }
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c3.isChecked()){
                    dam=dam+3;
                }
                text=text+c3.getText().toString()+" , ";
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c4.isChecked()){
                    dam=dam+3;
                    text=text+c4.getText().toString()+" , ";
                }
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c5.isChecked()){
                    dam=dam+2;
                    text=text+c5.getText().toString()+" , ";
                }
            }
        });


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    place = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String damstring=Integer.toString(dam);
                 if(!place.equals(loc[0])){
                     int speech = t1.speak("Record added successfully", TextToSpeech.QUEUE_FLUSH, null);
                    Fire fire=new Fire(text,place,damstring);
                    ref.child(String.valueOf(num)).setValue(fire);
                    num++;
                    times.child("num").setValue(num);
                    Toast.makeText(Addspot.this,"Record added successfully",Toast.LENGTH_LONG).show();
                    dam=0;
                    text="";
                     c1.setChecked(false);
                     c2.setChecked(false);
                     c3.setChecked(false);
                     c4.setChecked(false);
                     c5.setChecked(false);
                     spinner1.setSelection(0);
                }else{
                    Toast.makeText(Addspot.this,"Please enter all fields",Toast.LENGTH_LONG).show();
                }

            }
        });
    }





    private void getspinner1() {
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,loc);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(aa);
    }
}

