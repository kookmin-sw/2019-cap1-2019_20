package authentication;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
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
    private double longitude,latitude;
    private boolean valid = false;
    private String exifAttribute;
    private int place_id;
    private Register Reg;
    private int auth_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image);

        Intent intent = getIntent();
        place_id = intent.getIntExtra("place_id", 0);


        photo_gps = (TextView) findViewById(R.id.photo_gps);
        db_gps = (TextView) findViewById(R.id.db_gps);

        String path = ((Auth_Exif) Auth_Exif.context).currentPhotoPath;

        try {
            ExifInterface exif = new ExifInterface(path);
            Geodegree geoDegree = new Geodegree(exif);
            photo_gps = (TextView) findViewById(R.id.photo_gps);
            photo_gps.setText(geoDegree.toString());
            longitude = geoDegree.getLongitude();
            latitude = geoDegree.getLatitude();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(SelectImage.this, "Error", Toast.LENGTH_SHORT).show();
        }

        db_gps.setText("db gps값 ");
    }


    public void onClickBack(View view) {

        if (view.getId() == R.id.btnOK) {
            intent = new Intent(SelectImage.this, authentication.SelectAuth.class);
            startActivity(intent);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////
    public class gps_check extends AsyncTask<Void, Void, Void> {

        String save;

        @Override
        protected Void doInBackground(Void... voids) {
            Register r = new Register();
            double x,y;
            x = latitude;
            y = longitude;
            auth_num = 3;
            save = r.auth_info(place_id,3,x,y);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if("error".equals(save))
                    {Toast.makeText(getApplicationContext(),"인증실패하셨습니다." , Toast.LENGTH_SHORT).show();}
                    else
                    {Toast.makeText(getApplicationContext(),"인증성공! " , Toast.LENGTH_SHORT).show();}
                    //{Toast.makeText(getApplicationContext(), save.toString(), Toast.LENGTH_LONG).show();}

                }
            });
        }


    }


    public void onClickAuth(View view) {

        if (view.getId() == R.id.btnAuth) {

            gps_check check = new gps_check();
            check.execute();


/*
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
*/


        }

    }

}
