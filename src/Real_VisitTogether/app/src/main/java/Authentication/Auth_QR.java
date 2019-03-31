package Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Auth_QR extends AppCompatActivity {
    private String QR_Info;
    private String place;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IntentIntegrator(Auth_QR.this).initiateScan();
        /*
        Intent intent = getIntent();
        place = intent.getStringExtra("place");
        QR_Info = "imageView";
        if(QR_Info == place) {
            Toast.makeText(Auth_QR.this,"인증완료",Toast.LENGTH_LONG);
        }
        else{
            Toast.makeText(Auth_QR.this,"인증실패",Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            //QR코드가 없을 경우
            if(result.getContents() == null) {
                Toast.makeText(Auth_QR.this, "취소", Toast.LENGTH_SHORT).show();
            }
            //QR코드가 있을 경우
            else {
                Toast.makeText(Auth_QR.this, "스캔완료", Toast.LENGTH_SHORT).show();
                //json코드
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    QR_Info = obj.toString();

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(Auth_QR.this, "읽기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
    }
}
