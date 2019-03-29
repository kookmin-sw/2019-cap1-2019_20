package com.example.capston1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class detail2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);


        ImageView image1 = (ImageView) findViewById(R.id.imageView) ;
        image1.setImageResource(R.drawable.drink) ;

        ImageView image2 = (ImageView) findViewById(R.id.imageView2) ;
        image2.setImageResource(R.drawable.sabal) ;

        ImageView image3 = (ImageView) findViewById(R.id.imageView3) ;
        image3.setImageResource(R.drawable.oven) ;
    }
}
