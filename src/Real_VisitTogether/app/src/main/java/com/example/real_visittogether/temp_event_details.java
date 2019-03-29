//이벤트 디테일 페이지 만들어지면 지울 임시 페이지
package com.example.real_visittogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class temp_event_details extends AppCompatActivity implements Button.OnClickListener{

    Button qr_auth_btn;
    final String temp_code = "이효준짱";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_event_details);

        qr_auth_btn = (Button) findViewById(R.id.qr_auth_btn);
        qr_auth_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.qr_auth_btn){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.initiateScan();
        }
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result == null)
                Toast.makeText(this, "취소됨", Toast.LENGTH_LONG).show();
            // QR코드가 있을 경우
            else
                if(result.getContents() == temp_code)
                    //완료된 후 이벤트 더 넣어야함
                    Toast.makeText(this, "인증완료!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "인증실패ㅠ_ㅠ", Toast.LENGTH_LONG).show();
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
