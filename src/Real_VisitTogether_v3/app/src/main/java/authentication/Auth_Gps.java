package authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.example.real_visittogether.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

import data_fetcher.RequestHttpConnection;
import event.Event1;
import login.Register;
import vt_object.Imply;
import vt_object.Place;

//public class Auth_Gps extends FragmentActivity implements OnMapReadyCallback {
public class Auth_Gps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button1,button2;
    private TextView txtResult;
    private int place_num;
    private Event1.NetworkTask fetchPlaces;
    private Event1.NetworkTask registerParticipation;
    private boolean check = true;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2) ;
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        place_num = intent.getIntExtra("place_num", 0);


        button1 = (Button)findViewById(R.id.mylocation);
        button2 = (Button)findViewById(R.id.auth);
        txtResult = (TextView)findViewById(R.id.txtResult);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( Auth_Gps.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    /*
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    txtResult.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + latitude + "\n" +
                            "경도 : " + longitude + "\n" +
                            "고도  : " + altitude);
                    */

                    //처음 버튼 누르고 시간이 100초이상 흐르거나 거리가 100m이상 차이날때 다시 좌표를 잡아줌
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            100000,
                            1000,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            100000,
                            1000,
                            gpsLocationListener);

                }
            }
        });

        //인증버튼 누를시
        //이 부분은 DB연동되고 event중 해당 장소 좌표만 알면 바로 구현 가능함!


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean check = true;//전역으로 설정

                //수정중
                /////////////////////////////////////////////////////////////////////


                //////////////////////////////////////////////////////////////////////


                //인증실패시
                if(check)
                {
                    Toast.makeText(getApplicationContext(),"인증실패하셨습니다." , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = getIntent();
                    int place_num = intent.getIntExtra("place_num", 0);
                    intent = new Intent(Auth_Gps.this, Event1.class);
                    intent.putExtra("place_num", place_num);
                    intent.putExtra("authenticated", check);
                    intent.putExtra("joined", true);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"인증성공!" , Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    //DB연동 및 인증
// 네트워크 연결을 수행하는 이너클래스
    // AsyncTask: 비동기로 백그라운드 작업을 할 수 있도록 도와주는 클래스
    public class NetworkTask extends AsyncTask<String, Void, String> {

        final private String url_p = "place/";
        final private String url_imply = "imply/";

        private String place_str, relation_str;
        private String[] place_dict, relation_dict;

        private Place temp_place;
        private Vector<Place> places;

        private Imply temp_imply;
        private Vector<Imply> implyVector;


        private Gson gson;

        // NetworkTask의 execute 메소드가 호출된 후 실행되는 메소드
        // doin 메소드로 파라미터 전달
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gson = new Gson();
            temp_imply = new Imply();
            places = new Vector<Place>();
            implyVector = new Vector<Imply>();
        }

        // 백그라운드 스레드에서 처리되는 부분
        @Override
        protected String doInBackground(String... strings) {

            if(strings[0] == "fetchPlaces") {

                // 네트워크 연결
                RequestHttpConnection connection = new RequestHttpConnection();

                // 리턴된 "{..}\n{..} ... {..}" 값들을 split
                place_str = connection.request(url_p);
                place_dict = place_str.split("\n");

                relation_str = connection.request(url_imply);
                relation_dict = relation_str.split("\n");

                return strings[0];
            }else if(strings[0] == "participate"){

                Intent intent = getIntent();
                int event_id = intent.getIntExtra("event_id", 0);
                String user_id = intent.getStringExtra("user_id");

                Register connection = new Register();
                connection.participate(user_id, event_id);

                return strings[0];
            }

            return null;
        }

        // 백그라운드 작업 결과 반영
        // doin 메소드로 파라미터를 받는다
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            //이부분을 수정해야 될텐데
            if(string == "fetchPlaces") {
                // 관계 엔티티에서 이벤트 1인 경우만 뽑아냄
                for (int i = 0; i < relation_dict.length; i++) {
                    temp_imply = gson.fromJson(relation_dict[i], Imply.class);
                    if (temp_imply.getEvent_id() == 1)
                        implyVector.add(temp_imply);
                }

                // 뽑아낸 데이터와 Place 데이터 매칭
                for (int i = 0; i < implyVector.size(); i++) {
                    for (int j = 0; j < place_dict.length; j++) {
                        temp_place = gson.fromJson(place_dict[j], Place.class);
                        if (implyVector.elementAt(i).getPlace_id() == temp_place.getId())
                            places.add(temp_place);
                    }
                }

                // 매칭된 데이터의 name으로 setText()
                for (int i = 0; i < place_text.length; i++) {
                    place_text[i].setText(places.elementAt(i).getName());
                }
            }else if(string == "participate")
                participate_button.setBackgroundColor(Color.rgb(100,100,100));
        }
    }
}




    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////


    //지도 하단에 위치정보 출력
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + latitude + "\n" +
                    "경도 : " + longitude + "\n" +
                    "고도  : " + altitude);

            //현위치를 기본위치로 설정
            LatLng DEFAULT_LOCATION = new LatLng(latitude, longitude);

            //현위치로 카메라 이동
            CameraUpdate update = CameraUpdateFactory.newLatLng(DEFAULT_LOCATION);
            mMap.moveCamera(update);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.animateCamera(zoom);



            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions
                    .position(DEFAULT_LOCATION)
                    .title("현위치")
                    .snippet("위도 : "+ latitude +" 경도 :" + longitude);
            mMap.addMarker(markerOptions);

            /////////////////////////////////////////////////////////////////////////////////////////
            //인증을 위한 수정부분
            //일단 intent로 받아오게될 장소n에 대한 위치정보 위도 경도를 각각 alpha , beta라 하자

            double alpha , beta ; //intent로 받아올 특정 장소의 위도 경도
            int cal_x , cal_y;  //위도경도 계산을 위한 변수


            cal_x = (alpha - latitude)*100000;
            cal_y = (beta - longitude)*100000;

            if(cal_x <=15 && cal_y <=15)
            {
                check =  false ; //15미터 이내에서 장소인증 버튼 클릭시 check값 바꿔줌
            }

            /////////////////////////////////////////////////////////////////////////////////////////


        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


    //    지도부분 구현
//  이부분  EVENT1에 대한 내용만 띄워주기때문에 이렇게 하면 안됨
    //화면에 EVENT1에 대한 정보가 아닌 해당 EVENT에 대한 내용을 동적으로 띄워줘야하는데 그부분을 고려하지못했음
    //어떤 이벤트,장소에서 인증하기를 선택할때 그 장소에 대한 인증을 완료해야함.
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        // 기본위치값 국민대학교로
        LatLng DEFAULT_LOCATION = new LatLng(37.611099, 126.997182);

        //이부분이 동적으로 생성되어야함
        //(이벤트 등록 장소등록시 해당 장소에 대한 )
        //노가리 , 송백식당 , 주당끼리 주소 일단입력해놨음
        LatLng Place1 = new LatLng(37.607640, 127.001356);
        LatLng Place2 = new LatLng(37.610819, 126.994235);
        LatLng Place3 = new LatLng(37.607918, 126.999681);

        MarkerOptions makerOptions = new MarkerOptions();

        /*
        makerOptions
                .position(DEFAULT_LOCATION)
                .title("기본값");
        mMap.addMarker(makerOptions);
        */

        //event1에 나오는 미션장소3군데
        makerOptions
                .position(Place1)
                .title("노가리")
                .snippet("위도 : 37.607640 경도 :127.001356")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(0.5f);
        mMap.addMarker(makerOptions);

        makerOptions
                .position(Place2)
                .title("송백식당")
                .snippet("위도 : 37.610819 경도 :126.994235")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(0.5f);
        mMap.addMarker(makerOptions);

        makerOptions
                .position(Place3)
                .title("주당끼리")
                .snippet("위도 : 37.607918 경도 :126.999681")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(0.5f);

        mMap.addMarker(makerOptions);
        //여기까지 미션장소들

        mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);



    }


}






