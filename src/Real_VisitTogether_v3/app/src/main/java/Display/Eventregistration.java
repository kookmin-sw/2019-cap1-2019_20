package Display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.real_visittogether.R;

public class Eventregistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");  // 타이틀바 텍스트 변경
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


        final EditText inputevent = (EditText) findViewById(R.id.inputevent);
        final EditText inputYear = (EditText) findViewById(R.id.inputYear);
        final EditText inputMonth = (EditText) findViewById(R.id.inputMonth);
        final EditText inputDay = (EditText) findViewById(R.id.inputDay);
        final EditText inputReward = (EditText) findViewById(R.id.inputReward);


        Button button6= (Button)findViewById(R.id.guestbutton);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), guest2.class);
                intent.putExtra("name",inputevent.getText().toString());
                intent.putExtra("year",inputYear.getText().toString());
                intent.putExtra("month",inputMonth.getText().toString());
                intent.putExtra("day",inputDay.getText().toString());
                intent.putExtra("reward",inputReward.getText().toString());
                startActivity(intent);
        }
    });






    }
}
