package display;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.real_visittogether.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button search;
    private Button regist;
    private EditText editText;
    private Geocoder mgeocoder = new Geocoder(MapsActivity.this);
    private double final_latitude , final_longitude;
    private String final_address;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editText = (EditText) findViewById(R.id.editText);
        editText.setHint("등록하고싶은 장소를 입력하세요");

        search=(Button)findViewById(R.id.search);
        regist=(Button)findViewById(R.id.regist);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String text = "장소를 검색하거나 \n지도상에서 터치해주세요.";
        Toast.makeText(getApplicationContext(), text, 2000)
                .show();
    }


    /*
    private void String getAddress(double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        //Geocoder geocoder = new Geocoder(, Locale.KOREA);
        List <Address> address;
        try {
                address = mgeocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;
                }
        } catch (IOException e) {
            //Toast.makeText(baseContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowAddress;
    }

*/

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);


        // 버튼 이벤트
        search.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                mMap.clear();
                String str = editText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String[] splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                System.out.println("#################################" + address);
                System.out.println("#################################" + latitude);
                System.out.println("#################################" + longitude);

                if (address == null) {
                    String text = "입력하신 장소를 찾을수 없습니다.";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                            .show();
                } else {
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

                    String text = "이곳을 장소로 지정합니다. \n주소"
                            + address;
                    Toast.makeText(getApplicationContext(), text, 2000)
                            .show();

                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14));

                    final_latitude = Double.parseDouble(latitude);
                    final_longitude = Double.parseDouble(longitude);
                    final_address = address;
                }

            }

        });
        //등록버튼 클릭시 이전 화면으로 이동하면서 주소와 위도 경도를 넘겨준다.

        regist.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                System.out.println("#################################" + final_address);
                System.out.println("#################################" + final_latitude);
                System.out.println("#################################" + final_longitude);
//////////////////////////////
                Intent intent = new Intent(MapsActivity.this, placeAdd.class);
                intent.putExtra("address", final_address);
                intent.putExtra("latitude",final_latitude);
                intent.putExtra("longitude",final_longitude);
                startActivity(intent);

//////////////////////////////
                //Intent placeAdd = new Intent(getApplicationContext(), placeAdd.class);
                //startActivity(placeAdd);
            }
            });


        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                String place_address;
                // 마커 타이틀
                mMap.clear();

                //currentMarker = mGoogleMap.addMarker(markerOptions);

                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도

                List<Address> address = null ;

               try{
                   address = geocoder.getFromLocation(latitude, longitude, 1);
               }
               catch (IOException e)
               {
                   e.printStackTrace();
               }

                System.out.println("#################################################place_Address:"+ address);

               place_address = address.get(0).getAddressLine(0).toString();


                mOptions
                        .position(new LatLng(latitude, longitude))
                        .title("이곳을 장소로 지정합니다.")
                        .snippet("주소 : "+place_address)
                        .draggable(true);
                mMap.addMarker(mOptions);



                //mGeoCoder.getFromLocation(latitude, longitude, 1);


                String text = "이곳을 장소로 지정합니다. \n주소"
                        + place_address;
                Toast.makeText(getApplicationContext(), text, 2000)
                        .show();

                final_latitude = latitude;
                final_longitude = longitude;
                final_address = place_address ;

            }
        });


        LatLng KMU = new LatLng(37.611099, 126.997182);
       // mMap.addMarker(new MarkerOptions().position(KMU).title("국민대학교"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(KMU));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(KMU, 14);
        mMap.moveCamera(cameraUpdate);
    }

    @Override public void onBackPressed()
    {
        Intent placeAdd = new Intent(getApplicationContext(), placeAdd.class);
        startActivity(placeAdd);
    }


}