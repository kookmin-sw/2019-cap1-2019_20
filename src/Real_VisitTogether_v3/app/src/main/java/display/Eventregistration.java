package display;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.real_visittogether.R;
import com.google.gson.Gson;

import java.util.Vector;

import data_fetcher.RequestHttpConnection;
import login.Register;
import vt_object.Place;

public class Eventregistration extends AppCompatActivity {

    private Button addPlaceButton;
    private EditText nameText;
    private EditText rewardText;
    private Intent intent;
    private SharedPreferences places_pref;
    private SharedPreferences event_pref;
    private SharedPreferences.Editor place_editor;
    private SharedPreferences.Editor event_editor;
    private int temp_places_size;
    private LinearLayout placesLayout;
    private Vector<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventregistration);

        System.out.println("EventRegistration onCreate!!");

        addPlaceButton = (Button)findViewById(R.id.place);
        nameText = (EditText) findViewById(R.id.inputevent);
        rewardText = (EditText) findViewById(R.id.inputReward);
        placesLayout = (LinearLayout) findViewById(R.id.placesLayout);

        places = new Vector<Place>();

        event_pref = getSharedPreferences("event_info", MODE_PRIVATE);
        nameText.setText(event_pref.getString("event_name", ""));
        rewardText.setText(event_pref.getString("event_reward", ""));


        places_pref = getSharedPreferences("temp_places", MODE_PRIVATE);
        temp_places_size = places_pref.getInt("temp_places_size", 0);
        if(temp_places_size > 0) {
            NetworkTask registerTask = new NetworkTask(this);
            registerTask.execute("fetchPlaces");
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

        final Context context = this;
        Button registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameText.getText().toString() != null && rewardText.getText().toString() != null){
                    NetworkTask registerTask = new NetworkTask(context);
                    registerTask.execute("register");

                    intent = new Intent(Eventregistration.this, Display.class);
                    startActivity(intent);
                }
            }
        });
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

    public class NetworkTask extends AsyncTask<String, Void, String> {

        private final Context context;
        private final String url_p = "place/";
        private String place_str;
        private String[] place_dict;
        private Place temp_place;
        //private Vector<Place> places;
        private Gson gson;

        public NetworkTask(Context context){
            this.context = context;
            gson = new Gson();
            temp_place = new Place();
            //places = new Vector<Place>();
        }

        @Override
        protected String doInBackground(String... strings) {

            if(strings[0] == "register") {
                Register connection = new Register();
                connection.registerEvent(nameText.getText().toString(), rewardText.getText().toString(), "testID2");

            }else if(strings[0] == "fetchPlaces"){
                System.out.println("EventRegistration fetchPlaces doInBackground!!");
                RequestHttpConnection connection = new RequestHttpConnection();
                place_str = connection.request(url_p);
                place_dict = place_str.split("\n");

                return strings[0];
            }
            /*
            for(int i = 0; i < temp_places_size; i++) {
                connection.registerPlace(places_pref.getString("temp_places_name" + i, ""), places_pref.getString("temp_places_address" + i, ""), places_pref.getString("temp_places_information" + i, ""));
            }
            */

            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            if(string == "fetchPlaces") {
                System.out.println("EventRegistration fetchPlaces onPostExecute!!");

                temp_places_size = places_pref.getInt("temp_places_size", 0);
                System.out.println("temp_places_size = " + temp_places_size);
                for(int j = 0; j < temp_places_size; j++){
                    //System.out.println(j + "번째 place_id = " + places_pref.getInt("temp_places_id" + j, 0));
                }

                System.out.println("place_dict.length = " + place_dict.length);
                for(int i = 0; i < place_dict.length; i++){
                    temp_place = gson.fromJson(place_dict[i], Place.class);

                    for(int j = 0; j < temp_places_size; j++){

                        System.out.println(temp_place.getId());
                        System.out.println(places_pref.getInt("temp_places_id" + j, 0));

                        if(temp_place.getId() == places_pref.getInt("temp_places_id" + j, 0)){
                            System.out.println("양쪽 placeID가 모두 " + temp_place.getId() + "이므로 ");
                            places.add(temp_place);
                        }
                    }
                }
                System.out.println("임시 place 개수 = " + places.size());
                for (int i = 0; i < places.size(); i++) {

                    LinearLayout placeInfoLayout = new LinearLayout(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
                    placeInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
                    placeInfoLayout.setLayoutParams(layoutParams);

                    ImageView placeImage = new ImageView(context);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);

                    //if(places.elementAt(i).getPicture() == null || places.elementAt(i).getPicture().length() == 0 || places.elementAt(i).getPicture() == "None" || places.elementAt(i).getPicture() == "") {
                    if(places.elementAt(i).getPicture().contains("None")){
                        System.out.println("장소 이미지가 null");
                        placeImage.setImageResource(R.drawable.rabbit);
                    }else {
                        System.out.println("");
                        System.out.println("장소 이미지가 null이 아님");
                        System.out.println("getPicture() 출력: " + places.elementAt(i).getPicture());
                        placeImage.setImageBitmap(stringToBitmap(places.elementAt(i).getPicture()));
                    }

                    placeImage.setLayoutParams(imageParams);

                    TextView placeText = new TextView(context);
                    placeText.setText(places.elementAt(i).getName());

                    placeInfoLayout.addView(placeImage);
                    placeInfoLayout.addView(placeText);

                    placesLayout.addView(placeInfoLayout);
                }
            }
        }

        public Bitmap stringToBitmap(String imageString){
            byte[] bytes = Base64.decode(imageString, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }
    }
}