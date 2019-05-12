package authentication;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import org.w3c.dom.Text;
import android.bluetooth.*;
import android.util.*;
import android.content.Intent;
import android.widget.Toast;
import com.example.real_visittogether.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.*;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.*;


public class Auth_Beacon extends AppCompatActivity {
    private BeaconManager beaconManager;
    private Region region;
    private TextView rssi;
    private Intent intent;
    private int beacon_rssi, beacon_power;
    private double ratio,distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
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
    public void onClickAuth(View view) {

        if (view.getId() == R.id.btnAuth) {
            if (distance < 5) {
                Toast.makeText(Auth_Beacon.this, "인증성공", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Auth_Beacon.this, "인증실패", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
