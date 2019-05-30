package display;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.real_visittogether.R;

import login.Register;

public class Eventregistration extends AppCompatActivity {

    private String user_id;
    private Button addPlaceButton;
    private EditText nameText;
    private EditText rewardText;
    private Intent intent;
    private SharedPreferences places_pref;
    private SharedPreferences event_pref;
    private SharedPreferences placeInfo_pref;
    private SharedPreferences.Editor placeInfo_editor;
    private SharedPreferences.Editor place_editor;
    private SharedPreferences.Editor event_editor;
    private int temp_places_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventregistration);

        addPlaceButton = (Button)findViewById(R.id.place);
        nameText = (EditText) findViewById(R.id.inputevent);
        rewardText = (EditText) findViewById(R.id.inputReward);
        LinearLayout placesLayout = (LinearLayout) findViewById(R.id.placesLayout);

        user_id = getIntent().getStringExtra("user_id");

        event_pref = getSharedPreferences("event_info", MODE_PRIVATE);
        nameText.setText(event_pref.getString("event_name", ""));
        rewardText.setText(event_pref.getString("event_reward", ""));

        places_pref = getSharedPreferences("temp_places", MODE_PRIVATE);
        temp_places_size = places_pref.getInt("temp_places_size", 0);
        for(int i = 0; i < temp_places_size; i++){

            LinearLayout placeInfoLayout = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            placeInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
            placeInfoLayout.setLayoutParams(layoutParams);

            ImageView placeImage = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);
            placeImage.setImageResource(R.drawable.plain);
            placeImage.setLayoutParams(imageParams);

            TextView placeText = new TextView(this);
            placeText.setText("<" + places_pref.getString("temp_places_name" + i, "") + ">\n" + "주소 : " + places_pref.getString("temp_places_address" + i, "") + "\n설명 : " + places_pref.getString("temp_places_information" + i, ""));
            placeText.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
            placeText.setTextSize(15);
            placeText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/dogimayu_ttf.ttf"));
            placeInfoLayout.addView(placeImage);
            placeInfoLayout.addView(placeText);
            placeInfoLayout.setBackgroundResource(R.drawable.button_event);

            placesLayout.addView(placeInfoLayout);
        }

        event_editor = event_pref.edit();
        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.printf("이벤트이름: %s\n", nameText.getText().toString());
                event_editor.putString("event_name", nameText.getText().toString());
                event_editor.putString("event_reward", rewardText.getText().toString());
                event_editor.commit();

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
        placeInfo_pref = getSharedPreferences("place_info", MODE_PRIVATE);
        placeInfo_editor = placeInfo_pref.edit();
        placeInfo_editor.clear();
        placeInfo_editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        place_editor = places_pref.edit();
        place_editor.clear();
        place_editor.commit();

        event_editor = event_pref.edit();
        event_editor.clear();
        event_editor.commit();
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            Register connection = new Register();
            connection.registerEvent(nameText.getText().toString(), rewardText.getText().toString(), user_id);

            for(int i = 0; i < temp_places_size; i++) {

                double latitude = places_pref.getInt("temp_places_lat_int" + i, 0) + (double) places_pref.getInt("temp_places_lat_dec" + i, 0) / 10000000;
                double longitude = places_pref.getInt("temp_places_long_int" + i, 0) + (double) places_pref.getInt("temp_places_long_dec" + i, 0) / 10000000;

                connection.registerPlace(
                        places_pref.getString("temp_places_name" + i, ""),
                        places_pref.getString("temp_places_address" + i, ""),
                        places_pref.getString("temp_places_information" + i, ""),
                        latitude,
                        longitude
                );
            }

            return null;
        }
    }
}