package display;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;
import login.login;

import com.example.real_visittogether.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.nhn.android.naverlogin.OAuthLogin;

import com.example.real_visittogether.R;
import com.google.gson.Gson;


import data_fetcher.RequestHttpConnection;
import event.Event1;
import event.Event2;

import toolbar_menu.Help;
import toolbar_menu.MyPage;

import vt_object.Event;

public class Display extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    Button mainBtn;
    int cnt = 1;
    LinearLayout display_layout;

    private NetworkTask networkTask;
    private Button[] temp_btn;
    private FloatingActionButton actionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        display_layout = (LinearLayout) findViewById(R.id.display_layout);
        FloatingActionButton button4 = (FloatingActionButton)findViewById(R.id.ActionButton); //동그라미
/*
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Eventregistration.class);
               // startActivityForResult(intent,1000);
            }
        });

*/

    }

    public void onClick(View view) {



        if(view.getId() == R.id.button2){

        temp_btn = new Button[2];
        temp_btn[0] = (Button) findViewById(R.id.temp_btn1);
        temp_btn[1] = (Button) findViewById(R.id.temp_btn2);
        actionButton = (FloatingActionButton)findViewById(R.id.actionButton); //동그라미

        temp_btn[0].setOnClickListener(this);
        temp_btn[1].setOnClickListener(this);
        actionButton.setOnClickListener(this);

        networkTask = new NetworkTask();
        networkTask.execute();
    }

    public void onClick(View view) {
//        if(view.getId() == R.id.button1){
//            intent = new Intent(Display.this, Menu.class);
//            startActivity(intent);
//        }
        if(view.getId() == R.id.temp_btn1){

            intent = new Intent(Display.this, Event1.class);
            startActivity(intent);
        }
        if(view.getId() == R.id.temp_btn2){
            intent = new Intent(Display.this, Event2.class);
            startActivity(intent);
        }


        if(view.getId() == R.id.ActionButton){

            Button btn = new Button(getApplicationContext());
            btn.setText("test"+ String.valueOf(cnt));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"test"+ String.valueOf(cnt),Toast.LENGTH_LONG).show();
                }
            });

            display_layout.addView(btn);


            cnt++;

        if(view.getId() == R.id.actionButton){
            intent = new Intent(getApplicationContext(), Eventregistration.class);
            startActivity(intent);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.logout:
            {
                Toast.makeText(this,"로그아웃 되었습니다",Toast.LENGTH_LONG).show();
                Intent login =  new Intent(getApplicationContext(),login.class);
                startActivity(login);
                return true;
            }
            case R.id.help:
            {
                Intent help_intent = new Intent(getApplicationContext(),Help.class);
                startActivity(help_intent);
                return true;
            }
            case R.id.mypage:
            {
                Intent mypage_intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(mypage_intent);
                return true;
            }
        }
        return true;

    // 네트워크 연결을 수행하는 이너클래스
    // AsyncTask: 비동기로 백그라운드 작업을 할 수 있도록 도와주는 클래스
    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        final private String url = "event/";
        private Event event;
        private String event_str;
        private String[] event_dict;
        private Gson gson;

        // NetworkTask의 execute 메소드가 호출된 후 실행되는 메소드
        // doin 메소드로 파라미터 전달
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gson = new Gson();
            event = new Event();
        }

        // 백그라운드 스레드에서 처리되는 부분
        @Override
        protected Void doInBackground(Void... voids) {

            // 네트워크 연결
            RequestHttpConnection connection = new RequestHttpConnection();
            event_str = connection.request(url);

            // 리턴된 "{..}\n{..} ... {..}" 값들을 split
            event_dict = event_str.split("\n");
            return null;
        }

        // 백그라운드 작업 결과 반영
        // doin 메소드로 파라미터를 받는다
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // 배열의 원소들을 json 인코딩 후 각 버튼 setText()
            for(int i = 0; i < temp_btn.length; i++) {
                event = gson.fromJson(event_dict[i], Event.class);
                temp_btn[i].setText(event.getName());
            }
        }

    }
}
