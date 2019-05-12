package display;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.real_visittogether.R;

import java.util.ArrayList;

import login.Register;

public class placeAdd extends AppCompatActivity {

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private EditText placeName;
    private EditText addressText;
    private EditText information;

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

    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.regist){

            NetworkTask networkTask = new NetworkTask();
            networkTask.execute();

            Intent display =new Intent(getApplicationContext(),Display.class);
            display.putExtra("check",1);
            startActivity(display);
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            Register connection = new Register();
            connection.registerPlace(placeName.getText().toString(), addressText.getText().toString(), information.getText().toString());

            return null;
        }
    }
}
