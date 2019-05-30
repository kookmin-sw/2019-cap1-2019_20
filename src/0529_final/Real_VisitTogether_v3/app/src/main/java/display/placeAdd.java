package display;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.real_visittogether.R;

import java.util.ArrayList;

public class placeAdd extends AppCompatActivity {

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private EditText placeName;
    private TextView addressText;
    private EditText information;
    private Button addAddressButton;
    private Intent intent;
    //DB로 넘기기위한 파라미터들
    private double latitude,longitude;
    private String address ;
    private int lat_int, long_int, lat_dec, long_dec;
    private int check_exif =0 ,check_gps =0, check_beacon =0 , check_qr =0;
    private double final_latitude , final_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("장소 추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);

        //////////////////////////////////////////////////////////////////////////////
        //효준아 이부분에 위도 경도 주소 넣어둘테니깐 DB로 넘길때 참고해서 해줘
        address =getIntent().getStringExtra("address");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        System.out.println("%%%%%%%%%%%%%%%%%%%%placeadd로 intent한 위도 경도 주소\n");

        lat_int = (int) latitude;
        long_int = (int) longitude;
        lat_dec = (int) ((latitude - lat_int) * 10000000);
        long_dec = (int) ((longitude - long_int) * 10000000);

        //////////////////////////////////////////////////////////////////////////////


        placeName = (EditText) findViewById(R.id.inputPlace);
        addressText = (TextView) findViewById(R.id.addressText);
        information = (EditText) findViewById(R.id.inputInformation);
        addressText.setHint("[주소찾기]버튼을 클릭하면 자동으로 입력됩니다.");

        //EditText editText =(EditText)findViewById(R.id.editText);
//        if(address.length() == 0)
//            {addressText.setHint("[주소찾기]버튼을 클릭하면 자동으로 입력됩니다.");}
//        else
            addressText.setText(address);





        arrayList = new ArrayList<>();
        arrayList.add("사진촬영(Exif)");
        arrayList.add("사진촬영(Text)");
        arrayList.add("GPS");
        arrayList.add("Beacon");
        arrayList.add("QR_Code");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        //spinner = (Spinner)findViewById(R.id.spinner);
        //spinner.setAdapter(arrayAdapter);
        ///////////////////////////////////////////////////////////////
        //spinner 지우고 check박스로
        //각 버튼 눌렀을때 쿼리 수정해야됨
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.exif) ;
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.gps) ;
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.beacon) ;
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.qr_code) ;

        //////////////////////////////////////////////////////////////////////////////////////////
        /////////////효준 여기야 여기 여길 고쳐줘야됨///////////////////////
        checkBox1.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox)v).isChecked()) {
                    check_exif =1;
                    //이거 체크되면 DB place테이블의 auth_exif 값 1로 해줌.
                } else {
                    check_exif =0;
                    //이거 체크되지 않으면 DB place 테이블의 auth_exif 값 건드리지 않아야해.
                }
            }
        });
        checkBox2.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    check_gps =1;
                    //이거 체크되면 DB place테이블의 auth_gps 값 1로 해줌.
                } else {
                    check_gps =0;
                    //이거 체크되지 않으면 DB place 테이블의 auth_gps 값 건드리지 않아야해.
                }
            }
        });
        checkBox3.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    Toast.makeText(getApplicationContext(), "비콘의정보를 입력해주세요!", 2000).show();
                    check_beacon =1;
                    //이거 체크되면 DB place테이블의 beacon_distance 값 1로 해줌.
                } else {
                    check_beacon =0;
                    //이거 체크되지 않으면 DB place 테이블의 beacon_distance 값 건드리지 않아야해.
                }

            }
        });
        checkBox4.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    Toast.makeText(getApplicationContext(), "qr메세지를 입력해주세요!", 2000).show();
                    check_qr =1;
                    //이거 체크되면 DB place테이블의 auth_qr 값 1로 해줌.
                } else {
                    check_qr =0;
                    //이거 체크되지 않으면 DB place 테이블의 auth_qr 값 건드리지 않아야해.
                }

            }
        });

        //체크박스 선택됐을때 db로 그 정보보내는거 해줘야됨 , 이때 check_exif check_gps 등의 int값 0,1 보내는데 이에따라서 나중에 인증들어갈때 1이면 인증창으로 넘어가고 0이면 해당 장소는 이런 인증 불가능이라고뜸



///////////////////////////////////////////////////////추가부분//////////////////////
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


    //뒤로가기 버튼 누를경우
    @Override public void onBackPressed()
    {
        Intent eventRegisteration = new Intent(getApplicationContext(), Eventregistration.class);
        startActivity(eventRegisteration);
    }

    public void onClick(View v){

        if(v.getId() == R.id.regist){

            SharedPreferences places_pref = getSharedPreferences("temp_places", MODE_PRIVATE);
            SharedPreferences.Editor editor = places_pref.edit();

            int temp_places_size = places_pref.getInt("temp_places_size",0);
            editor.putString("temp_places_name" + temp_places_size, placeName.getText().toString());
            editor.putString("temp_places_address" + temp_places_size, addressText.getText().toString());
            editor.putString("temp_places_information" + temp_places_size, information.getText().toString());

            editor.putInt("temp_places_lat_int" + temp_places_size, lat_int);
            editor.putInt("temp_places_lat_dec" + temp_places_size, lat_dec);
            editor.putInt("temp_places_long_int" + temp_places_size, long_int);
            editor.putInt("temp_places_long_dec" + temp_places_size, long_dec);
            editor.putInt("temp_places_check_exif" + temp_places_size, check_exif);
            //이거 체크되면 DB place테이블의 auth_exif 값 1로 해줌.
            editor.putInt("temp_places_check_gps" + temp_places_size, check_gps);
            //이거 체크되면 DB place테이블의 auth_gps 값 1로 해줌.
            editor.putInt("temp_places_check_beacon" + temp_places_size, check_beacon);
            //이거 체크되면 DB place테이블의 beacon_distance 값 1로 해줌.
            editor.putInt("temp_places_check_qr" + temp_places_size, check_qr);
            //이거 체크되면 DB place테이블의 auth_qr 값 1로 해줌.


            editor.putInt("temp_places_size", temp_places_size + 1);
            editor.commit();

            Intent eventRegisteration = new Intent(getApplicationContext(), Eventregistration.class);
            startActivity(eventRegisteration);
        }
    }
}



