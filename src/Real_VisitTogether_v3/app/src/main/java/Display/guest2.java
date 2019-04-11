package Display;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.real_visittogether.R;

public class guest2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest2);



        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        TextView txt_next = (TextView) findViewById(R.id.txt_next);
                            txt_next.setText(name);



        String year = intent.getStringExtra("year");

        TextView eventyear = (TextView) findViewById(R.id.eventyear);
                            eventyear.setText(year);



        String month = intent.getStringExtra("month");

        TextView eventmonth = (TextView) findViewById(R.id.eventmonth);
        eventmonth.setText(month);



        String day = intent.getStringExtra("day");

        TextView eventday = (TextView) findViewById(R.id.eventday);
        eventday.setText(day);



        String reward = intent.getStringExtra("reward");

        TextView eventreward = (TextView) findViewById(R.id.eventreward);
        eventreward.setText(reward);


    }
}
