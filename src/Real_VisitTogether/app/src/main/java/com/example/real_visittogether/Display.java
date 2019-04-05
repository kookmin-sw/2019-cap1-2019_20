package com.example.real_visittogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Display extends AppCompatActivity implements Button.OnClickListener{

    //임시버튼
    Button temp_hj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        temp_hj = (Button) findViewById(R.id.temp_hj);
        temp_hj.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //임시
        if(view.getId() == R.id.temp_hj)
            startActivity(new Intent(this, temp_event_details.class));
    }
}
