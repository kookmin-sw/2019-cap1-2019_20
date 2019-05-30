package authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.real_visittogether.R;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.UUID;

import display.Eventregistration;
import event.Event1;
import login.Register;

public class SelectAuth extends AppCompatActivity {
    //인증 성공 유무에따라 부여되는값
    private int success = 10000;
    private int fail = 10001;
    private int place_id;
    private String user_id;
    private Register Reg;
    private int auth_num;
    private int event_id;
    static String QR_Info;
    private int poss_exif, poss_qr, poss_gps, poss_beacon;
    private BeaconManager beaconManager;
    private Region region;
    private TextView rssi;
    private Intent intent;
    private int beacon_rssi, beacon_power;
    private double ratio,distance;
    private int distance_check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_auth);

        Intent intent = getIntent();
        place_id = intent.getIntExtra("place_id", 0);
        user_id = getIntent().getStringExtra("user_id");
        event_id = getIntent().getIntExtra("event_id", -1);
        //place_id = intent.getIntExtra().getInt("place_id");
        new NetworkTask().execute();

        ///////////////////////////////////////
        rssi = (TextView) findViewById(R.id.rssi);

        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = (Beacon) list.get(0);
                    beacon_rssi = nearestBeacon.getRssi();
                    beacon_power = nearestBeacon.getMeasuredPower();
                    ratio = beacon_rssi*1.0/beacon_power;
                    distance = (0.3)*Math.pow(ratio,6);
                    //rssi.setText("비콘과의 거리 : 약 " + String.format("%, .3f", distance) + "m");
                }

                if(distance <15)
                    distance_check =1;
                else
                    distance_check = -1;
            }
        });

        region = new Region("ranged region", UUID.fromString("74278BDA-B644-4520-8f0C-720EAF059935"), 40001, 25627);



        ////////////////////////


    }

    @Override
    protected void onResume() {
        super.onResume(); // 블루투스 권한 및 활성화 코드
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    public class beacon_check extends AsyncTask<Void, Void, Void> {

        String save;

        @Override
        protected Void doInBackground(Void... voids) {
            Register r = new Register();

            auth_num = 2;
            save = r.auth_info(place_id,2,distance_check,user_id,event_id);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (distance_check ==1) {
                            Intent intent = new Intent(SelectAuth.this, Event1.class);
                            intent.putExtra("place_id", place_id);
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("event_id",event_id);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "비콘과의 거리" +distance + "\n인증성공! ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "비콘과의 거리" +distance +"인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){System.out.println(e);
                        Toast.makeText(getApplicationContext(), "비콘과의 거리" +distance +"인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        String result;
        String auth_info[];

        @Override
        protected Void doInBackground(Void... voids) {
            Register r = new Register();
            result = r.select_auth_method(place_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result = result.replaceAll("[^0-9]", "");

            for (int i = 0; i < result.length(); i++) {

                if (i == 0 && result.charAt(i) == '1') {
                    poss_qr = 1;

                } else if (i == 1 && result.charAt(i) == '1') {
                    poss_beacon = 1;

                } else if (i == 2 && result.charAt(i) == '1') {
                    poss_exif = 1;

                } else if (i == 3 && result.charAt(i) == '1') {
                    poss_gps = 1;

                }
            }

            Button auth_gps = (Button) findViewById(R.id.auth_gps);
            Button auth_qr = (Button) findViewById(R.id.auth_qr);
            Button auth_exif = (Button) findViewById(R.id.auth_exif);
            Button auth_beacon = (Button) findViewById(R.id.auth_bicorn);

            if (poss_gps != 1)
                auth_gps.setVisibility(View.GONE);
            if (poss_qr != 1)
                auth_qr.setVisibility(View.GONE);
            if (poss_exif != 1)
                auth_exif.setVisibility(View.GONE);
            if (poss_beacon != 1)
                auth_beacon.setVisibility(View.GONE);

        }
    }


    public void Select_Auth(View v) {

        if (v.getId() == R.id.auth_exif) {
            Intent intent = new Intent(SelectAuth.this, Auth_Exif.class);
            intent.putExtra("place_id", place_id);
            intent.putExtra("user_id", user_id);
            intent.putExtra("event_id", event_id);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_qr) {
            new IntentIntegrator(SelectAuth.this).initiateScan();

        } else if (v.getId() == R.id.auth_bicorn) {
            beacon_check check = new beacon_check();
            check.execute();
            //임의로 99로 해놓은거고 비콘 사용하는 곳에서만

            /*
            if (place_id != 0) {

                Intent intent = new Intent(SelectAuth.this, Auth_Beacon.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("event_id", event_id);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "인증완료! \n 비콘과의 거리 3.231782 m", 3000).show();
            } else {
                Toast.makeText(getApplicationContext(), "해당 장소는 비콘인증이 등록되어있지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            */


        } else if (v.getId() == R.id.auth_gps) {
            Intent intent = new Intent(SelectAuth.this, Auth_Gps.class);
            intent.putExtra("place_id", place_id);
            intent.putExtra("user_id", user_id);
            intent.putExtra("event_id", event_id);
            startActivity(intent);
        }
        System.out.printf("\n<SelectAuth>\nplace_id = %d\nauthenticated = %b\n", place_id, true);
    }


    public class qr_check extends AsyncTask<Void, Void, Void> {

        String save;

        @Override
        protected Void doInBackground(Void... voids) {
            Register r = new Register();

            String qr;

            auth_num = 1;
            save = r.auth_info(place_id, 1, QR_Info, user_id, event_id);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (save.equals("ok")) {
                            Toast.makeText(getApplicationContext(), "인증성공! ", Toast.LENGTH_SHORT).show();

                            //  Intent event1 = new Intent(getApplicationContext(), event1.class);
                            // startActivity(event1);

                        } else {
                            Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //QR코드가 없을 경우
            if (result.getContents() == null) {
                Toast.makeText(SelectAuth.this, "취소", Toast.LENGTH_SHORT).show();
            }
            //QR코드가 있을 경우
            else {
                //String QR_Info = null;
                QR_Info = result.getContents();


                qr_check check = new qr_check();
                check.execute();

            }
        }
    }


    @Override public void onBackPressed()
    {
        Intent intent = new Intent(SelectAuth.this, Event1.class);
        intent.putExtra("place_id", place_id);
        intent.putExtra("user_id",user_id);
        intent.putExtra("event_id",event_id);
        startActivity(intent);
    }
}