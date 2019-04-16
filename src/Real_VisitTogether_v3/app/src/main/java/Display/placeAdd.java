package Display;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("장소 추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);

        //먼저, 스피너에 넣을 ArrayList를 만든다.
        arrayList = new ArrayList<>();
        arrayList.add("사진촬영(Exif)");
        arrayList.add("사진촬영(Text)");
        arrayList.add("GPS");
        arrayList.add("Beacon");
        arrayList.add("QR_Code");


        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);       //ArrayAdapter에 ArrayList를 넣어준다.

        spinner = (Spinner)findViewById(R.id.spinner); //스피너를 선언
        spinner.setAdapter(arrayAdapter); // 마지막으로 스피너에 .setAdapter()를 이용하여 adapter를 넣어준다.








      //  final EditText inputPlace = (EditText) findViewById(R.id.inputPlace);
      //  final EditText inputInformation= (EditText) findViewById(R.id.inputInformation);
       // final EditText inputExplanation = (EditText) findViewById(R.id.inputExplanation);



     //   Button button7= (Button)findViewById(R.id.guestbutton);

       // button7.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
         //       Intent intent = new Intent(getApplicationContext(), guest2.class);
           //     intent.putExtra("place", inputPlace.getText().toString());
                //   intent.putExtra("information",inputInformation.getText().toString());
                //  intent.putExtra("explanation",inputExplanation.getText().toString());
             //   startActivity(intent);
        //    }
        // });

    }

}
