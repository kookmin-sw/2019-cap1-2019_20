package authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.real_visittogether.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import event.Event1;
import login.Register;

public class SelectAuth extends AppCompatActivity {
    //인증 성공 유무에따라 부여되는값
    private int success = 10000;
    private int fail = 10001;
    private int place_id;
    private Register Reg;
    private int auth_num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_auth);

        place_id = getIntent().getIntExtra("place_id",-1);
        Intent intent = getIntent();
        place_id = intent.getIntExtra("place_id", 0);
    }

    public void Select_Auth(View v) {
        if (v.getId() == R.id.auth_exif) {
            Intent intent = new Intent(SelectAuth.this, Auth_Exif.class);
            intent.putExtra("place_id", place_id);
            //auth_num = 4;
            //Reg.auth_info(place_id,4);

        } else if (v.getId() == R.id.auth_qr) {
            //auth_num = 1;
            //Reg.auth_info(place_id,1);
            new IntentIntegrator(SelectAuth.this).initiateScan();

        } else if (v.getId() == R.id.auth_bicorn) {

            Intent intent = new Intent(SelectAuth.this, Auth_Beacon.class);
            intent.putExtra("place_id", place_id);
            //auth_num = 2;
            //Reg.auth_info(place_id,2);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_gps) {
            Intent intent = new Intent(SelectAuth.this, Auth_Gps.class);
            intent.putExtra("place_id", place_id);
            //startActivityForResult(intent,3000);
            //auth_num = 3;
            // Reg.auth_info(place_id,3);

            startActivity(intent);
        }
        System.out.printf("\n<SelectAuth>\nplace_id = %d\nauthenticated = %b\n", place_id, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


       /* if(resultCode ==RESULT_OK){
            switch (requestCode){
                case 3000:
                    Reg.auth_info(place_id,3,);
                    break;

            }

        }

          */
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
                    intent.putExtra("place_id", place_id);
                    intent.putExtra("authenticated", true);
                    startActivity(intent);
                }
            }
    }

}
