package com.example.real_visittogether;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Event.Event1;
import Event.Event2;

public class Eventregistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventregistration);

        Button button5 = (Button)findViewById(R.id.place);


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), placeAdd.class);
                startActivity(intent);
            }
        });


    }
}
