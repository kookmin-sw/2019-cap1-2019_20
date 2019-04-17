package authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.real_visittogether.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SelectAuth extends AppCompatActivity {
    //인증 성공 유무에따라 부여되는값
    private int success = 10000;
    private int fail = 10001;
    private String QR_Info;
    private String place;
    private Intent intent;

    private Intent temp_intent;
    private int temp_place_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_auth);
        intent = getIntent();
        place = intent.getStringExtra("place");

        temp_intent = getIntent();
        temp_place_num = temp_intent.getIntExtra("place_num", 0);
        System.out.printf("SelectAuth 화면에서 place_num = %d 받음\n", temp_place_num);
    }

    public void Select_Auth(View v) {
        if (v.getId() == R.id.auth_exif) {
            intent = new Intent(SelectAuth.this, Auth_Exif.class);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_qr) {
            new IntentIntegrator(SelectAuth.this).initiateScan();

        } else if (v.getId() == R.id.auth_bicorn) {
            intent = new Intent(SelectAuth.this, Auth_Exif.class);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_gps) {
            intent = new Intent(SelectAuth.this, Auth_Gps.class);
            startActivity(intent);
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
                temp_intent = new Intent(this, event.Event1.class);
                QR_Info = result.getContents();
                place = getIntent().getStringExtra("place");
                Toast.makeText(SelectAuth.this, place.toString(), Toast.LENGTH_SHORT).show();
                if (QR_Info.equals(place)) {
                    Toast.makeText(SelectAuth.this, "인증성공", Toast.LENGTH_SHORT).show();
                    temp_intent.putExtra("authenticated", true);
                } else {
                    Toast.makeText(SelectAuth.this, "인증실패", Toast.LENGTH_SHORT).show();
                    temp_intent.putExtra("authenticated", true);
                }
                temp_intent.putExtra("place_num", temp_place_num);
                startActivity(temp_intent);
                finish();
            }
        }
    }
}
