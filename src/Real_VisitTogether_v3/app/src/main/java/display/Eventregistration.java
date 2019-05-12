package display;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.real_visittogether.R;

import login.Register;

public class Eventregistration extends AppCompatActivity {

    EditText nameText;
    EditText rewardText;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventregistration);

        Button button5 = (Button)findViewById(R.id.place);
        nameText = (EditText) findViewById(R.id.inputevent);
        rewardText = (EditText) findViewById(R.id.inputReward);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), placeAdd.class);
                startActivity(intent);
            }
        });


        Button registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameText.getText().toString() != null && rewardText.getText().toString() != null){
                    NetworkTask networkTask = new NetworkTask();
                    networkTask.execute();

                    intent = new Intent(Eventregistration.this, Display.class);
                    startActivity(intent);
                }
            }
        });
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            Register connection = new Register();
            connection.registerEvent(nameText.getText().toString(), rewardText.getText().toString(), "testID2");

            return null;
        }
    }
}
