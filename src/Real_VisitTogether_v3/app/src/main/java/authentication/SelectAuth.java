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

import event.Event1;

public class SelectAuth extends AppCompatActivity {
    //인증 성공 유무에따라 부여되는값
    private int success = 10000;
    private int fail = 10001;
    private int place_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_auth);

        Intent intent = getIntent();
        place_num = intent.getIntExtra("place_num", 0);
    }

    public void Select_Auth(View v) {
        if (v.getId() == R.id.auth_exif) {
            Intent intent = new Intent(SelectAuth.this, Auth_Exif.class);
            intent.putExtra("place_num", place_num);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_qr) {
            new IntentIntegrator(SelectAuth.this).initiateScan();

        } else if (v.getId() == R.id.auth_bicorn) {
            Intent intent = new Intent(SelectAuth.this, Auth_Exif.class);
            intent.putExtra("place_num", place_num);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_gps) {
            Intent intent = new Intent(SelectAuth.this, Auth_Gps.class);
            intent.putExtra("place_num", place_num);
            startActivity(intent);
        }
        System.out.printf("\n<SelectAuth>\nplace_num = %d\nauthenticated = %b\n", place_num, true);
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
                String QR_Info = null;
                if (true) {
                    //if (QR_Info == result.getContents()) {
                    Toast.makeText(SelectAuth.this, "인증성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectAuth.this, "인증실패", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(SelectAuth.this, Event1.class);
                intent.putExtra("place_num", place_num);
                intent.putExtra("authenticated", true);
                intent.putExtra("joined", true);
                startActivity(intent);
            }
        }
    }
}
