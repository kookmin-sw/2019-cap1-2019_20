package authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import event.Event1;

//public class Auth_Gps extends FragmentActivity implements OnMapReadyCallback {
public class Auth_Gps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button1,button2;
    private TextView txtResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2) ;
        mapFragment.getMapAsync(this);



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
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    txtResult.setText("위치정보 : " + provider + "\n" +
                            "위도 : " + latitude + "\n" +
                            "경도 : " + longitude + "\n" +
                            "고도  : " + altitude);


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
                boolean check = true;

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

    }//end onCreate()





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


        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


//    지도부분 구현

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        // 기본위치값 국민대학교로
        LatLng DEFAULT_LOCATION = new LatLng(37.611099, 126.997182);

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



