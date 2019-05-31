package event;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.real_visittogether.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import authentication.SelectAuth;
import data_fetcher.RequestHttpConnection;
import login.Register;
import toolbar_menu.mypage.Ranking;
import vt_object.Auth_place;
import vt_object.Event;
import vt_object.Imply;
import vt_object.Place;

//import com.google.android.gms.location.places.Place;


public class Event1 extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private String []auth_info;
    private Vector<Auth_place> auth_places;
    private NetworkTask fetchPlaces;
    private NetworkTask registerParticipation;
    private String user_id;
    private int event_id;
    private double place_latitude , place_longitude;
    private TextView[] place_text;
    private String participate_check;
    private Button participate_button;
    private String auth_place_id;
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;

    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.event1);
        user_id = getIntent().getStringExtra("user_id");
        event_id = getIntent().getIntExtra("event_id",-1);
        participate_button = (Button) findViewById(R.id.Participation);
        //new Participate_check().execute();

        auth_places = new Vector<>();

        Log.d(TAG, "onCreate");
        mActivity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        new place_auth_info().execute();

        new finish_auth_check().execute();
    }

    public void onClickEvent1(View view) {

        if(view.getId() == R.id.Rank){
            Intent rank = new Intent(this, Ranking.class);
            rank.putExtra("event_id",event_id);
            startActivity(rank);
        }

        //participate_button = (Button) findViewById(R.id.Participation);
        if(view.getId() == R.id.Participation){
            registerParticipation = new NetworkTask();
            registerParticipation.execute("participate");
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }

        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);
        }
    }



    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        mGoogleMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치로 이동
        setDefaultLocation();

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d( TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates){

                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;

            }
        });

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        currentPosition
                = new LatLng( location.getLatitude(), location.getLongitude());

        Log.d(TAG, "onLocationChanged : ");

        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);

        mCurrentLocatiion = location;
    }

    @Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }

    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;

        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = mGoogleMap.addMarker(markerOptions);

        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude() ) ;
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    //이부분 수정들어간다
    public void setDefaultLocation() {

        mMoveMapByUser = false;

        //디폴트 위치, 국민대학교
        LatLng DEFAULT_LOCATION = new LatLng(37.611099, 126.997182);
        //노가리 , 송백식당 , 주당끼리 주소 일단입력해놨음
        LatLng Place1 = new LatLng(37.607640, 127.001356);
        LatLng Place2 = new LatLng(37.610819, 126.994235);
        LatLng Place3 = new LatLng(37.607918, 126.999681);

        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        //event1에 나오는 미션장소3군데
        /*
        markerOptions
                .position(Place1)
                .title("노가리")
                .snippet("위도 : 37.607640 경도 :127.001356")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(0.5f);
        mGoogleMap.addMarker(markerOptions);
        markerOptions
                .position(Place2)
                .title("송백식당")
                .snippet("위도 : 37.610819 경도 :126.994235")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(0.5f);
        mGoogleMap.addMarker(markerOptions);
        markerOptions
                .position(Place3)
                .title("주당끼리")
                .snippet("위도 : 37.607918 경도 :126.999681")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(0.5f);
        mGoogleMap.addMarker(markerOptions);
        //여기까지 미션장소들
        */
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if ( mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }
            } else {

                checkPermissions();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Event1.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Event1.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Event1.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");

                        if ( mGoogleApiClient.isConnected() == false ) {

                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }
                break;
        }
    }

    // 네트워크 연결을 수행하는 이너클래스
    // AsyncTask: 비동기로 백그라운드 작업을 할 수 있도록 도와주는 클래스
    public class NetworkTask extends AsyncTask<String, Void, String> {

        final private String url_p = "place/";
        final private String url_imply = "imply/";

        private String place_str, relation_str;
        private String[] place_dict, relation_dict;

        private vt_object.Place temp_place;
        private Vector<Place> places;

        private Imply temp_imply;
        private Vector<Imply> implyVector;

        private Gson gson;

        private Intent intent;
        private int event_id;
        private String user_id;

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

            intent = getIntent();
            event_id = intent.getIntExtra("event_id", 0);
            user_id = intent.getStringExtra("user_id");


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

                Register connection = new Register();
                //connection.participate(user_id, event_id);
                participate_check = connection.participate(user_id, event_id);
                return strings[0];
            }

            return null;
        }



        // 백그라운드 작업 결과 반영
        // doin 메소드로 파라미터를 받는다
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            if(string == "fetchPlaces") {

                // 관계 엔티티에서 이벤트 event_id 인 경우만 뽑아냄
                for (int i = 0; i < relation_dict.length; i++) {
                    temp_imply = gson.fromJson(relation_dict[i], Imply.class);
                    if (temp_imply.getEvent_id() == event_id)
                        implyVector.add(temp_imply);

                }



                // 뽑아낸 데이터와 Place 데이터 매칭
                for (int i = 0; i < implyVector.size(); i++) {
                    for (int j = 0; j < place_dict.length; j++) {
                        //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + place_dict[j]);
//                        String a;
//                        a = places.elementAt(i).getLatitude();
//                        //b = places.elementAt(i).getLongitude();
//                        System.out.println(a+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+j+"@@@@@@@@@@@@@@@@@");


                        temp_place = gson.fromJson(place_dict[j], Place.class);

                        if (implyVector.elementAt(i).getPlace_id() == temp_place.getId())
                            places.add(temp_place);
                    }
                }

                LinearLayout places_layout = (LinearLayout) findViewById(R.id.places_layout);

                // 매칭된 데이터의 name으로 setText()
                for (int i = 0; i < places.size(); i++) {

                    LinearLayout placeInfoLayout = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
                    placeInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
                    placeInfoLayout.setLayoutParams(layoutParams);

                    ImageView placeImage = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);

                    //일단 기본장소들같은경우 drawble에 넣어놓자
                    //일단은 서울의4대문부터

                    if(event_id ==1 ){
                        if (i == 0) {
                            placeImage.setImageResource(R.drawable.nogari);
                        }else if (i == 1) {
                            placeImage.setImageResource(R.drawable.songbaek);
                        }else if (i == 2) {
                            placeImage.setImageResource(R.drawable.jakma);
                        }
                    }

                    else if(event_id ==2) {
                        if (i == 0) {
                            placeImage.setImageResource(R.drawable.westdoor);
                        } else if (i == 1) {
                            placeImage.setImageResource(R.drawable.southdoor);
                        } else if (i == 2) {
                            placeImage.setImageResource(R.drawable.eastdoor);
                        }
                    }
                    else if(event_id ==3) {
                        if (i == 0) {
                            placeImage.setImageResource(R.drawable.seven);
                        } else if (i == 1) {
                            placeImage.setImageResource(R.drawable.dragon);
                        } else if (i == 2) {
                            placeImage.setImageResource(R.drawable.cafenamu);
                        }else if (i == 3) {
                            placeImage.setImageResource(R.drawable.concert);
                        }else if (i == 4) {
                            placeImage.setImageResource(R.drawable.momstouch);
                        }else if (i == 5) {
                            placeImage.setImageResource(R.drawable.gongcha);
                        }else if (i == 6) {
                            placeImage.setImageResource(R.drawable.sunggok);
                        }else if (i == 7) {
                            placeImage.setImageResource(R.drawable.chunghyang);
                        }else if (i == 8) {
                            placeImage.setImageResource(R.drawable.bockzi);
                        }else if (i == 9) {
                            placeImage.setImageResource(R.drawable.science);
                        }else if (i == 10) {
                            placeImage.setImageResource(R.drawable.gym);
                        }else if (i == 11) {
                            placeImage.setImageResource(R.drawable.gukje);
                        }

                    }
                    else if(event_id ==4) {
                        if (i == 0) {
                            placeImage.setImageResource(R.drawable.cap_1);
                        } else if (i == 1) {
                            placeImage.setImageResource(R.drawable.cap_2);
                        } else if (i == 2) {
                            placeImage.setImageResource(R.drawable.cap_3);
                        }else if (i == 3) {
                            placeImage.setImageResource(R.drawable.cap_4);
                        }else if (i == 4) {
                            placeImage.setImageResource(R.drawable.cap_5);
                        }else if (i == 5) {
                            placeImage.setImageResource(R.drawable.cap_6);
                        }else if (i == 6) {
                            placeImage.setImageResource(R.drawable.cap_7);
                        }else if (i == 7) {
                            placeImage.setImageResource(R.drawable.cap_8);
                        }else if (i == 8) {
                            placeImage.setImageResource(R.drawable.cap_9);
                        }else if (i == 9) {
                            placeImage.setImageResource(R.drawable.cap_10);
                        }else if (i == 10) {
                            placeImage.setImageResource(R.drawable.cap_11);
                        }else if (i == 11) {
                            placeImage.setImageResource(R.drawable.cap_12);
                        }else if (i == 12) {
                            placeImage.setImageResource(R.drawable.cap_13);
                        }else if (i == 13) {
                            placeImage.setImageResource(R.drawable.cap_14);
                        }else if (i == 14) {
                            placeImage.setImageResource(R.drawable.cap_15);
                        }else if (i == 15) {
                            placeImage.setImageResource(R.drawable.cap_16);
                        }else if (i == 16) {
                            placeImage.setImageResource(R.drawable.cap_17);
                        }else if (i == 17) {
                            placeImage.setImageResource(R.drawable.cap_18);
                        }else if (i == 18) {
                            placeImage.setImageResource(R.drawable.cap_19);
                        }else if (i == 19) {
                            placeImage.setImageResource(R.drawable.cap_20);
                        }else if (i == 20) {
                            placeImage.setImageResource(R.drawable.cap_21);
                        }else if (i == 21) {
                            placeImage.setImageResource(R.drawable.cap_22);
                        }else if (i == 22) {
                            placeImage.setImageResource(R.drawable.cap_23);
                        }else if (i == 23) {
                            placeImage.setImageResource(R.drawable.cap_24);
                        }

                    }
                    else
                        placeImage.setImageResource(R.drawable.plain);//이부분도 처리해야되는데,장소별 이미지는?


                    placeImage.setLayoutParams(imageParams);
                    TextView placeText = new TextView(getApplicationContext());
                    //placeText.setText("    "+ places.elementAt(i).getName());
                    placeText.setText("< "+ places.elementAt(i).getName()+" >\n"+"주소 : "+places.elementAt(i).getAddress()+"\n설명 : "+places.elementAt(i).getExplanation());
                    //////////////////////////////////////////////////////////
                    //
                    placeText.setBackgroundColor(Color.rgb(255,255,255));
                    placeText.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
                    // placeText.setBackgroundColor(#0);
                    placeText.setTextSize(15);
                    placeText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/dogimayu_ttf.ttf"));


                    placeInfoLayout.addView(placeImage);
                    placeInfoLayout.addView(placeText);

                    for(Auth_place t : auth_places)
                    {
                        try {
                            if (t.getAuth_place_id() == places.elementAt(i).getId()) {
                                placeText.setBackgroundColor(Color.GRAY);
                                placeInfoLayout.setBackgroundColor(Color.GRAY);
                            }
                        } catch (Exception e){}
                    }

                    places_layout.addView(placeInfoLayout);

                    //5/20일 수정들어간부분
                    ////////////////////////////////////////////////////////
                    ////////////////////////////////////////////////////////

                    double a,b;
                    a = Double.parseDouble(places.elementAt(i).getLatitude());
                    b = Double.parseDouble(places.elementAt(i).getLongitude());


                    LatLng Place = new LatLng(a,b);

                    if (currentMarker != null) currentMarker.remove();

                    MarkerOptions markerOptions = new MarkerOptions();
                    String markerTitle = "위치정보 가져올 수 없음";
                    String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

                    markerOptions.title(markerTitle);
                    markerOptions.snippet(markerSnippet);
                    markerOptions.draggable(true);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    //currentMarker = mGoogleMap.addMarker(markerOptions);

                    markerOptions
                            .position(Place)
                            .title(places.elementAt(i).getName())
                            .snippet(places.elementAt(i).getLatitude() +"/"+places.elementAt(i).getLongitude())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .alpha(0.5f);
                    mGoogleMap.addMarker(markerOptions);


                    ////////////////////////////////////////////////////////
                    ////////////////////////////////////////////////////////

                    final int finalI = i;
                    placeInfoLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Event1.this, authentication.SelectAuth.class);
                            intent.putExtra("place_id", places.elementAt(finalI).getId());
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("event_id",event_id);
                            //이부분 수정
                            //intent.putExtra("latitude", place_latitude);
                            //intent.putExtra("longitude", place_latitude);
                            startActivity(intent);
                        }
                    });
                }
            }else if(string == "participate")
                if(participate_check.equals("duplicated"))
                    participate_button.setBackgroundColor(Color.rgb(100,100,100));
        }
    }

    @Override public void onBackPressed()
    {

        Intent intent1 = new Intent(Event1.this, display.Display.class);
        intent1.putExtra("user_id",user_id);
        intent1.putExtra("event_id",event_id);
        startActivity(intent1);
    }

    public class Participate_check extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            Register con = new Register();
            participate_check = con.participate(user_id,event_id);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println(user_id +"..............."+event_id);
            System.out.println("participaton check : "+participate_check);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(participate_check.equals("duplicated"))
                        participate_button.setBackgroundColor(Color.rgb(100,100,100));
                }
            });
        }
    }

    public class finish_auth_check extends AsyncTask<Void,Void,Void>{
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            Register con = new Register();
            result = con.finish_auth_check(user_id,event_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("auth_check: "+result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(result.equals("ok")){
                        //Toast.makeText(mActivity,"모든 장소에대한 인증 성공",Toast.LENGTH_LONG).show();
                    }
                    else{

                    }
                }
            });
        }

    }
    public class  place_auth_info extends AsyncTask<Void, Void, Void>{


        private Gson gson;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gson = new Gson();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Register con = new Register();

            auth_place_id = con.place_auth_info(user_id,event_id);
            // 리턴된 "{..}\n{..} ... {..}" 값들을 split
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            auth_info = auth_place_id.split("\n");

            for (int i = 0; i < auth_info.length; i++){

                auth_places.add(gson.fromJson(auth_info[i],Auth_place.class));
            }

            new NetworkTask().execute("fetchPlaces");
        }
    }
    

}