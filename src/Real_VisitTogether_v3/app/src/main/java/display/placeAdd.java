package display;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.real_visittogether.R;

import java.util.ArrayList;

public class placeAdd extends AppCompatActivity {

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private EditText placeName;
    private EditText addressText;
    private EditText information;
    private Button addAddressButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("장소 추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);

        placeName = (EditText) findViewById(R.id.inputPlace);
        addressText = (EditText) findViewById(R.id.addressText);
        information = (EditText) findViewById(R.id.inputInformation);


        arrayList = new ArrayList<>();
        arrayList.add("사진촬영(Exif)");
        arrayList.add("사진촬영(Text)");
        arrayList.add("GPS");
        arrayList.add("Beacon");
        arrayList.add("QR_Code");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);

        Button b = (Button)findViewById(R.id.FindAddress);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MapsActivity.class);
                //startActivity(mapAddress); // 다음 화면으로 넘어간다

                startActivity(intent);
            }
        });


    }




    public void onClick(View v){

        if(v.getId() == R.id.regist){

            SharedPreferences places_pref = getSharedPreferences("temp_places", MODE_PRIVATE);
            SharedPreferences.Editor editor = places_pref.edit();

            int temp_places_size = places_pref.getInt("temp_places_size",0);
            editor.putString("temp_places_name" + temp_places_size, placeName.getText().toString());
            editor.putString("temp_places_address" + temp_places_size, addressText.getText().toString());
            editor.putString("temp_places_information" + temp_places_size, information.getText().toString());
            editor.putInt("temp_places_size", temp_places_size + 1);
            editor.commit();

            Intent eventRegisteration = new Intent(getApplicationContext(), Eventregistration.class);
            startActivity(eventRegisteration);
        }
    }
}
