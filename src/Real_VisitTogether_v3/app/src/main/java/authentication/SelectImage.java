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
import authentication.Geodegree;


import com.example.real_visittogether.R;


public class SelectImage extends AppCompatActivity {

    private Intent intent;
    private TextView photo_gps;
    private TextView db_gps;
    private boolean valid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image);

        String path = ((Auth_Exif)Auth_Exif.context).currentPhotoPath;

        photo_gps = (TextView) findViewById(R.id.photo_gps);
        db_gps = (TextView) findViewById(R.id.db_gps);


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

        if (view.getId() == R.id.btnAuth) {
                Toast.makeText(SelectImage.this, "인증성공", Toast.LENGTH_SHORT).show();
        }

    }


}
