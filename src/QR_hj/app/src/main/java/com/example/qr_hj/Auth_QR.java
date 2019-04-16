package com.example.qr_hj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Auth_QR extends AppCompatActivity implements Button.OnClickListener {

    private  Button read_qr, make_qr, next_page;
    private TextView textview;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        read_qr = (Button) findViewById(R.id.read_qr);
        read_qr.setOnClickListener(this);

        textview = (TextView) findViewById(R.id.textView);

        next_page = (Button) findViewById(R.id.db_page);
        next_page.setOnClickListener(this);

        make_qr = (Button) findViewById(R.id.make_qr);
        make_qr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        if(view.getId() == R.id.read_qr)
            new IntentIntegrator(this).initiateScan();

        if(view.getId() == R.id.db_page){
            intent = new Intent(Auth_QR.this, DB_Access.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.make_qr){
            intent = new Intent(Auth_QR.this, Make_QR_Activity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultConde, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultConde, data);
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
                    textview.setText(obj.toString());

                }catch (JSONException e){
                    e.printStackTrace();
                    textview.setText(result.getContents());
                }
            }
        }
    }
}
