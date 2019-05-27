package toolbar_menu.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.real_visittogether.R;
import com.google.gson.Gson;

import java.util.Vector;

import data_fetcher.RequestHttpConnection;
import display.event_btn;
import event.Event1;
import login.Register;
import vt_object.Event;

public class Participation extends AppCompatActivity {
    private String user_id;
    private Vector<Button> btn = new Vector<Button>();
    private String result;
    LinearLayout layout1;
    LinearLayout layout2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participation);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("참여중인 이벤트 목록") ;
        user_id = getIntent().getStringExtra("user_id");
        layout1 =  (LinearLayout) findViewById(R.id.participating_layout1);
        layout2 =  (LinearLayout) findViewById(R.id.participating_layout2);
        new NetworkTask(this).execute();


    }
    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        private Gson gson;
        private Context mContext;
        private  String [] event_dict;
        private  Event event;
        public NetworkTask (Context context){
            this.mContext = context;
            gson = new Gson();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            Register conn = new Register();
            result = conn.participating_event(user_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (result == ""){
                TextView t = new TextView(mContext);
                t.setText("참여중인 이벤트가 존재하지 않습니다");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                t.setGravity(Gravity.CENTER);
                t.setLayoutParams(params);
                t.setTextSize(25);
                LinearLayout parent = (LinearLayout) findViewById(R.id.parent_participating_layout);
                parent.removeView(layout2);
                parent.removeView(layout1);
                parent.addView(t);
            }
                else{
                event_dict = result.split("\n");
                // 배열의 원소들을 json 인코딩 후 각 버튼 setText()
                for (int i = 0; i < event_dict.length; i += 2) {

                    btn.addElement(new event_btn(mContext));
                    event = gson.fromJson(event_dict[i], Event.class);
                    btn.lastElement().setText(event.getName());
                    btn.lastElement().setBackgroundResource(R.drawable.buttoncolor_4);
                    btn.lastElement().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    final int event_id = event.getEvent_id();
                    btn.lastElement().setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent event_intent = new Intent(mContext, Event1.class);
                            event_intent.putExtra("event_id", event_id);
                            //intent = getIntent();
                            event_intent.putExtra("user_id", user_id);

                            startActivity(event_intent);
                        }
                    });
                    layout1.addView(btn.lastElement());
                }
                for (int i = 1; i < event_dict.length; i += 2) {

                    btn.addElement(new event_btn(mContext));
                    event = gson.fromJson(event_dict[i], Event.class);
                    btn.lastElement().setText(event.getName());
                    btn.lastElement().setBackgroundResource(R.drawable.buttoncolor_4);
                    btn.lastElement().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    final int event_id = event.getEvent_id();
                    btn.lastElement().setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent event_intent = new Intent(mContext, Event1.class);
                            event_intent.putExtra("event_id", event_id);
                            //intent = getIntent();
                            event_intent.putExtra("user_id", user_id);

                            startActivity(event_intent);
                        }
                    });
                    layout2.addView(btn.lastElement());
                }

            }
        }
    }
    }



