package Display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import com.example.real_visittogether.R;

import Event.Event1;
import Event.Event2;

public class Display extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
    }

    public void onClick(View view) {


        if(view.getId() == R.id.event1){
            intent = new Intent(Display.this, Event1.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.event2){
            intent = new Intent(Display.this, Event2.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.event3){
            intent = new Intent(Display.this, Event1.class);
            startActivity(intent);
        }



    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }
}
