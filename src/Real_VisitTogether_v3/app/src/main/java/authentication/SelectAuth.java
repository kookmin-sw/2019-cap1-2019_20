package authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    private String user_id;
    private Register Reg;
    private int auth_num;
    private int event_id;
    static String QR_Info ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_auth);

        Intent intent = getIntent();
        place_id = intent.getIntExtra("place_id", 0);
        user_id = getIntent().getStringExtra("user_id");
        event_id = getIntent().getIntExtra("event_id",-1);
        //place_id = intent.getIntExtra().getInt("place_id");

    }

    public void Select_Auth(View v) {
        if (v.getId() == R.id.auth_exif) {
            Intent intent = new Intent(SelectAuth.this, Auth_Exif.class);
            intent.putExtra("place_id", place_id);
            intent.putExtra("user_id",user_id);
            intent.putExtra("event_id",event_id);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_qr) {
            new IntentIntegrator(SelectAuth.this).initiateScan();

        } else if (v.getId() == R.id.auth_bicorn) {
            Intent intent = new Intent(SelectAuth.this, Auth_Beacon.class);
            intent.putExtra("place_id", place_id);
            intent.putExtra("user_id",user_id);
            intent.putExtra("event_id",event_id);
            startActivity(intent);
        } else if (v.getId() == R.id.auth_gps) {
            Intent intent = new Intent(SelectAuth.this, Auth_Gps.class);
            intent.putExtra("place_id", place_id);
            intent.putExtra("user_id",user_id);
            intent.putExtra("event_id",event_id);
            startActivity(intent);
        }
        System.out.printf("\n<SelectAuth>\nplace_id = %d\nauthenticated = %b\n", place_id, true);
    }



    public class qr_check extends AsyncTask<Void, Void, Void> {

        String save;

        @Override
        protected Void doInBackground(Void... voids) {
            Register r = new Register();

            String qr;

            auth_num = 1;
            save = r.auth_info(place_id, 1, QR_Info,user_id,event_id);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (save.equals("ok")) {
                            Toast.makeText(getApplicationContext(), "인증성공! ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){System.out.println(e);
                        Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
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
                    //String QR_Info = null;
                    QR_Info = result.getContents();
                    System.out.println("##################################QR_INFO :"+QR_Info);

                    qr_check check = new qr_check();
                    check.execute();

                /*
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
                */
                }
            }
        }
    }

