package authentication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import com.capstone.real_visittogether.R;

import java.util.*;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.*;
import login.Register;


public class Auth_Beacon extends AppCompatActivity {
    private BeaconManager beaconManager;
    private Region region;
    private TextView rssi;
    private Intent intent;
    private int beacon_rssi, beacon_power;
    private double ratio,distance;
    private Register Reg ;
    private int place_id;
    private Geodegree Geo;
    private int auth_num;
    private String user_id;
    private int event_id;
    private int distance_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        Intent intent = getIntent();
        place_id = intent.getIntExtra("place_id", 0);
        user_id = getIntent().getStringExtra("user_id");
        event_id = getIntent().getIntExtra("event_id",-1);
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
                    rssi.setText("비콘과의 거리 : 약 " + String.format("%, .3f", distance) + "m");
                }

                if(distance <15)
                    distance_check =1;
                else
                    distance_check = -1;
            }
        });

        region = new Region("ranged region", UUID.fromString("74278BDA-B644-4520-8f0C-720EAF059935"), 40001, 25627);
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

    public void onClickBack(View view) {

        if (view.getId() == R.id.btnOK) {
            intent = new Intent(Auth_Beacon.this, SelectAuth.class);
            startActivity(intent);
        }

    }


    /////////////////////////////////////////////////////////////////////////////////
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
                            Toast.makeText(getApplicationContext(), "인증성공! ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){System.out.println(e);
                        Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }



    public void onClickAuth(View view) {

        beacon_check check = new beacon_check();
        check.execute();

    }
}
