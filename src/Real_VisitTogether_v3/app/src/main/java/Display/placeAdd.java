package Display;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.real_visittogether.R;

import java.util.ArrayList;

public class placeAdd extends AppCompatActivity {

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("장소 추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);


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

    }

}
