package display;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.real_visittogether.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;
    private double last_latitude, last_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editText = (EditText) findViewById(R.id.editText);
        editText.setHint("등록하고싶은 장소를 입력하세요");
        //editText.setBackground(null);


        button=(Button)findViewById(R.id.button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);



        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                mMap.clear();
                String str=editText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println("#################################"+latitude);
                System.out.println("#################################"+longitude);

//                if(address == )
//                {
//                    String text = "입력하신 장소를 찾을수 없습니다." ;
//                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
//                            .show();
//                }

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2
                            .title(str)
                            .snippet(address + "\n" + latitude + "," + longitude)
                            .position(point)
                            .draggable(true);

                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌 
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

            }

        });
        ////////////////////

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mMap.clear();

                /////////////////////////////////////////////////////////////////

                //currentMarker = mGoogleMap.addMarker(markerOptions);

                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도

                mOptions
                        .position(new LatLng(latitude, longitude))
                        .title("이곳을 장소로 지정합니다.")
                        .snippet(latitude.toString() + ", " + longitude.toString())
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        //.alpha(0.5f)
                        .draggable(true);
                mMap.addMarker(mOptions);

                String text = "이곳을 장소로 지정합니다. \nlatitude ="
                        + point.latitude + ", longitude ="
                        + point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();

                ////////////////////////////////////////////////////////////////////
                /*
                mOptions.title("여기를 장소로 지정합니다");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));

                mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
                */
            }
        });
        ////////////////////

        // Add a marker in Sydney and move the camera
        LatLng KMU = new LatLng(37.611099, 126.997182);
       // mMap.addMarker(new MarkerOptions().position(KMU).title("국민대학교"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(KMU));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(KMU, 14);
        mMap.moveCamera(cameraUpdate);
    }
}