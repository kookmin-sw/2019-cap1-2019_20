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
//import static authentication.Geodegree.latitude;
//import static authentication.Geodegree.longitude;


import com.example.real_visittogether.R;


public class SelectImage extends AppCompatActivity {

    private Intent intent;
    private TextView photo_gps;
    private TextView db_gps;
    private Geodegree Geo;
    private boolean valid = false;
    private String exifAttribute;


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

        boolean check = true;
        //x,y에 각각 현재 좌표 입력시킴
        float x,y;
        x = Geo.getLatitude();
        y = Geo.getLongitude();

        double alpha , beta ; //intent로 받아올 특정 장소의 위도 경도
        int cal_x , cal_y;  //위도경도 계산을 위한 변수


        cal_x = (alpha - x)*100000;
        cal_y = (beta - y)*100000;

        if(cal_x <=15 && cal_y <=15)
        {
            check =  false ; //15미터 이내에서 장소인증 버튼 클릭시 check값 바꿔줌
        }





        if (view.getId() == R.id.btnAuth) {

            //DB정보 끌어오는거하고 아래 코드를 다음과 같이만 바꾸면돌아감
            //if()
            if (photo_gps == db_gps) {
                intent = getIntent();
                int place_num = intent.getIntExtra("place_num", 0);
                intent = new Intent(SelectImage.this, Event1.class);
                intent.putExtra("place_num", place_num);
                intent.putExtra("authenticated", true);
                intent.putExtra("joined", true);
                startActivity(intent);
                System.out.printf("\n<SelectImage>\nplace_num = %d\nauthenticated = %b\n", place_num, true);
                Toast.makeText(SelectImage.this, "인증성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SelectImage.this, "인증실패", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
