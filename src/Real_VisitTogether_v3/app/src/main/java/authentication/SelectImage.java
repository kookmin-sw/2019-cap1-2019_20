package authentication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import java.io.IOException;

import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;
import authentication.Auth_Exif;
import event.Event1;
import login.Register;


import com.example.real_visittogether.R;


public class SelectImage extends AppCompatActivity {

    private Intent intent;
    private TextView photo_gps;
    private TextView db_gps;
    private boolean valid = false;
    private String exifAttribute;
    private Register Reg ;
    private int place_id;
    private Geodegree Geo;
    private int auth_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image);

        photo_gps = (TextView) findViewById(R.id.photo_gps);
        db_gps = (TextView) findViewById(R.id.db_gps);

        String path = ((Auth_Exif) Auth_Exif.context).currentPhotoPath;

        try {
            ExifInterface exif = new ExifInterface(path);
            Geodegree geoDegree = new Geodegree(exif);
            photo_gps = (TextView) findViewById(R.id.photo_gps);
            photo_gps.setText(geoDegree.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(SelectImage.this, "Error", Toast.LENGTH_SHORT).show();
        }

        db_gps.setText("db gps값");
    }


    public void onClickBack(View view) {

        if (view.getId() == R.id.btnOK) {
            intent = new Intent(SelectImage.this, authentication.SelectAuth.class);
            startActivity(intent);
        }

    }

    public void onClickAuth(View view) {
        float x,y;
        // 현위치 좌표 x,y받아옴.
        x = Geo.getLatitude();
        y = Geo.getLongitude();
        auth_num = 4;
        Reg.auth_info(place_id,4,x,y);

        ///////////////////////////////////////////////////////////////////////////////////////////
        //여기 아래로 DB에서 인증 성공 실패 값이 0인지 아닌지등으로 판단해서
        //성공이면 성공이라띄우고 아님 실패라고 띄우도록 수정할예정 금방바꾸니 일단 놔둘께
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (view.getId() == R.id.btnAuth) {
            if (photo_gps == db_gps) {
                intent = getIntent();
                int place_id = intent.getIntExtra("place_id", 0);
                intent = new Intent(SelectImage.this, Event1.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("authenticated", true);
                intent.putExtra("joined", true);
                startActivity(intent);
                System.out.printf("\n<SelectImage>\nplace_id = %d\nauthenticated = %b\n", place_id, true);
                Toast.makeText(SelectImage.this, "인증성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SelectImage.this, "인증실패", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
