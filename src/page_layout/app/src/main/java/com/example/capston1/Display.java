package com.example.capston1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Display extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
    }

    public void onClick(View view) {


        if(view.getId() == R.id.button1){
            intent = new Intent(Display.this, Menu.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.button2){
            intent = new Intent(Display.this, Event1.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.button3){
            intent = new Intent(Display.this, Event2.class);
            startActivity(intent);
        }





        /* 버튼의 id를 가져온다.
        switch(view.getId())
        {
            case button1:

            // btn1을 눌렀을 때 처리
                break;

            case button2:
                break;

            case button3:
                break;
        }
        */



    }
}
