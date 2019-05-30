package authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.capstone.real_visittogether.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
        new NetworkTask().execute();
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
            //임의로 99로 해놓은거고 비콘 사용하는 곳에서만

            if(place_id != 0) {

                Intent intent = new Intent(SelectAuth.this, Auth_Beacon.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("event_id", event_id);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "인증완료! \n 비콘과의 거리 3.231782 m", 3000).show();
            }
            else
            {Toast.makeText(getApplicationContext(), "해당 장소는 비콘인증이 등록되어있지 않습니다.", Toast.LENGTH_SHORT).show();}

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

                    System.out.println("####################################QR_info:"+QR_Info +"\n");
                    System.out.println("####################################place_id:"+place_id +"\n");
                    System.out.println("####################################user_id:"+user_id +"\n");
                    System.out.println("####################################event_id:"+event_id +"\n");

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
    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        String result;
        String auth_info[];
        @Override
        protected Void doInBackground(Void... voids) {
            Register r = new Register();
            result = r.select_auth_method(place_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result = result.replaceAll("[^0-9]", "");

            for(int i =0; i<result.length(); i++)
            {

                if(i ==0 &&result.charAt(i) == '1'){

                    System.out.println("##################################################qr");
                }
                else if(i ==1 &&result.charAt(i) == '1'){
                    System.out.println("##################################################becoan");
                }
                else if(i ==2 &&result.charAt(i) == '1'){
                    System.out.println("##################################################exif");
                }
                else if(i ==3 &&result.charAt(i) == '1'){
                    System.out.println("##################################################gps");
                }
            }

        }
    }
    }

