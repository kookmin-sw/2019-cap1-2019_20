package display;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.real_visittogether.R;

import data_fetcher.Register;
import data_fetcher.RequestHttpConnection;

public class Eventregistration extends AppCompatActivity {

    private EditText nameText;
    private EditText rewardText;
    private Intent intent;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventregistration);

        Button addPlaceButton = (Button)findViewById(R.id.place);
        nameText = (EditText) findViewById(R.id.inputevent);
        rewardText = (EditText) findViewById(R.id.inputReward);

        LinearLayout placesLayout = (LinearLayout) findViewById(R.id.placesLayout);
        preferences = getSharedPreferences("temp_places", MODE_PRIVATE);
        int temp_places_size = preferences.getInt("temp_places_size", 0);
        for(int i = 0; i < temp_places_size; i++){
            TextView placeText = new TextView(this);
            placeText.setText(preferences.getString("temp_places_name" + i, ""));
            placesLayout.addView(placeText);
        }

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
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
