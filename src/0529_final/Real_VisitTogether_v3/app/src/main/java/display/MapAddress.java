package display;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.real_visittogether.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import authentication.Geodegree;
import login.Register;

public class MapAddress extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button button1;
    private TextView txtResult;
    private Register Reg ;
    private int place_id;
    private Geodegree Geo;
    private int auth_num;
    private double latitude, longitude;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_address);

        Intent intent = getIntent();
        place_id = intent.getIntExtra("place_id", 0);

        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2) ;
        mapFragment.getMapAsync(this);



        button1 = (Button)findViewById(R.id.place_choice);
        txtResult = (TextView)findViewById(R.id.txtResult);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);




    }//end onCreate()







    final LocationListener gpsLocationListener = new LocationListener() {


        public void onLocationChanged(Location location) {


           longitude = location.getLongitude();
           latitude = location.getLatitude();



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
        //latitude = Geo.getLatitude();
        //longitude = Geo.getLongitude();
        // 기본위치값 국민대학교로
        //LatLng DEFAULT_LOCATION = new LatLng(latitude, longitude);

        LatLng DEFAULT_LOCATION = new LatLng(37.611099, 126.997182);


        MarkerOptions makerOptions = new MarkerOptions();


        mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                //MarkerOptions.remove();
                //////////////////////////////////////////////////////////////////
                //if (currentMarker != null) currentMarker.remove();

                MarkerOptions markerOptions = new MarkerOptions();
                String markerTitle = "위치정보 가져올 수 없음";
                String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

                //if (markerOptions != null) markerOptions.remove();

                mMap.clear();
                markerOptions.title(markerTitle);
                markerOptions.snippet(markerSnippet);
                markerOptions.draggable(true);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //currentMarker = mGoogleMap.addMarker(markerOptions);

                markerOptions
                        .position(point)
                        //.title(places.elementAt(i).getName())
                        .snippet("test")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .alpha(0.5f);
                mMap.addMarker(markerOptions);

//                Marker m = map.addMarker( MarkerOption())...  -> Marker 객체가 리턴됨
//
//
//
//                        (2) m.remove() -> (1)에서 그려진 마커가 삭제됨




                String text = "[단시간 클릭시 이벤트] latitude ="
                        + point.latitude + ", longitude ="
                        + point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();
            }

        });



    }


    //지도클릭 이벤트
    /////////////////////////////////////////////////////////////////////////////////


}


