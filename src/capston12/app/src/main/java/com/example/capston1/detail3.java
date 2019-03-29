package com.example.capston1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class detail3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail3);

        ImageView image1 = (ImageView) findViewById(R.id.image1) ;
        image1.setImageResource(R.drawable.남대문) ;

        ImageView image2 = (ImageView) findViewById(R.id.image2) ;
        image2.setImageResource(R.drawable.동대문) ;

        ImageView image3 = (ImageView) findViewById(R.id.image3) ;
        image3.setImageResource(R.drawable.서대문) ;
    }
}
