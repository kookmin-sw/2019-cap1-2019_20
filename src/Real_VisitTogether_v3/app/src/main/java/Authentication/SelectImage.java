package Authentication;

import android.content.Intent;
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
import Authentication.Auth_Exif;


import com.example.real_visittogether.R;


public class SelectImage extends AppCompatActivity {

    private Intent intent;
    private TextView photo_gps;
    private TextView db_gps;
    private boolean valid = false;
    private String latitude, longitude, db_latitude, db_longtitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image);

        photo_gps = (TextView) findViewById(R.id.photo_gps);
        db_gps = (TextView) findViewById(R.id.db_gps);

        photo_gps.setText(
                "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n");

        String imagePath =((Auth_Exif)Auth_Exif.context).currentPhotoPath;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

    }


    public void onClickBack(View view) {

        if (view.getId() == R.id.btnOK) {
            intent = new Intent(SelectImage.this, SelectAuth.class);
            startActivity(intent);
        }

    }
    public void onClickAuth(View view) {

        if (view.getId() == R.id.btnAuth) {
            if ((latitude == db_latitude) && (longitude == db_longtitude)) {
                Toast.makeText(SelectImage.this, "인증성공", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SelectImage.this, "인증실패", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
