package display;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.capstone.real_visittogether.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.nhn.android.naverlogin.OAuthLogin;

import java.util.Vector;

import data_fetcher.RequestHttpConnection;
import event.Event1;

import login.login;
import toolbar_menu.Help;
import toolbar_menu.MyPage;
import vt_object.Event;

public class Display extends AppCompatActivity implements View.OnClickListener {
    String id;
    private Intent intent;
    private NetworkTask networkTask;
    private Vector<Button> btn = new Vector<Button>();
    private FloatingActionButton actionButton;
    LinearLayout display_layout;
    LinearLayout layout1, layout2;
    String email ;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("같이가자!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        actionButton = (FloatingActionButton)findViewById(R.id.actionButton); //동그라미
        display_layout = (LinearLayout) findViewById(R.id.display_layout);
        layout1 =  (LinearLayout) findViewById(R.id.layout1);
        layout2 =  (LinearLayout) findViewById(R.id.layout2);
        actionButton.setOnClickListener(this);
        id = getIntent().getStringExtra("user_id");
        networkTask = new NetworkTask(this);
        networkTask.execute();

    }
    public void onClick(View view) {

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
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.logout: {
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                boolean checkNaver =OAuthLogin.getInstance().getAccessToken(this) != null;
                //토큰을 가지고있으면(로그인 유지시) 로그인 하지않고 메뉴에 접근가능
                if(isLoggedIn) {
                    LoginManager.getInstance().logOut();
                }else if(checkNaver){
                    OAuthLogin.getInstance().logout(this);
                }
                startActivity(new Intent(this, login.class));
                return true;
            }
            case R.id.help: {
                Intent help_intent = new Intent(getApplicationContext(), Help.class);
                startActivity(help_intent);
                return true;
            }
            case R.id.mypage: {
                Intent mypage_intent = new Intent(getApplicationContext(), MyPage.class);
                mypage_intent.putExtra("user_id",id);

                startActivity(mypage_intent);
                return true;
            }
        }
        return true;
    }

    // 네트워크 연결을 수행하는 이너클래스
    // AsyncTask: 비동기로 백그라운드 작업을 할 수 있도록 도와주는 클래스
    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        final private String url = "event/";
        private Event event;
        private String event_str;
        private String[] event_dict;
        private Gson gson;
        private Context mContext;

        public NetworkTask (Context context){
            this.mContext = context;
        }

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
            for(int i = 0; i < event_dict.length; i+=2) {

                btn.addElement(new event_btn(mContext));
                event = gson.fromJson(event_dict[i], Event.class);
                btn.lastElement().setText(event.getName());
                btn.lastElement().setBackgroundResource(R.drawable.buttoncolor_4);
                btn.lastElement().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final int event_id = event.getEvent_id();
                btn.lastElement().setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent event_intent = new Intent(mContext,Event1.class);
                        event_intent.putExtra("event_id", event_id);
                        //intent = getIntent();
                        event_intent.putExtra("user_id", id);

                        startActivity(event_intent);
                    }
                }) ;
                layout1.addView(btn.lastElement());
            }
            for(int i = 1; i < event_dict.length; i+=2) {

                btn.addElement(new event_btn(mContext));
                event = gson.fromJson(event_dict[i], Event.class);
                btn.lastElement().setText(event.getName());
                btn.lastElement().setBackgroundResource(R.drawable.buttoncolor_4);
                btn.lastElement().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final int event_id = event.getEvent_id();
                btn.lastElement().setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent event_intent = new Intent(mContext,Event1.class);
                        event_intent.putExtra("event_id", event_id);
                        //intent = getIntent();
                        event_intent.putExtra("user_id", id);

                        startActivity(event_intent);
                    }
                }) ;
                layout2.addView(btn.lastElement());
            }

        }
    }
}


