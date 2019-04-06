package Display;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;


import com.example.real_visittogether.Eventregistration;
import com.example.real_visittogether.R;

import Event.Event1;
import Event.Event2;

public class Display extends AppCompatActivity {

    //private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        Button button1 = (Button)findViewById(R.id.event1);
        Button button2 = (Button)findViewById(R.id.event2);
        //Button button3 = (Button)findViewById(R.id.event3);
        FloatingActionButton button4 = (FloatingActionButton)findViewById(R.id.ActionButton); //동그라미


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Event1.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Event2.class);
                startActivity(intent);
            }
        });

        //do next
        /*button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Event3.class);
                startActivity(intent);
            }
        });*/

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Eventregistration.class);
                startActivity(intent);
            }
        });



    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }

}
