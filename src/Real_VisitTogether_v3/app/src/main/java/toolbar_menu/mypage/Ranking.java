package toolbar_menu.mypage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import data_fetcher.RequestHttpConnection;
import display.event_btn;
import event.Event1;
import login.Register;
import user.User;
import user.UserAdaptor;
import user.UserRanking;
import vt_object.Event;

import com.example.real_visittogether.R;
import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;






public class Ranking extends AppCompatActivity {

    ArrayList<UserRanking> userRanking = new ArrayList<>();
    private Context mcontext;
    ListView listView;
    int event_id;
    String result;
    String rank_message = "";
    String parse = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        mcontext = this;
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("랭킹") ;
        event_id = getIntent().getIntExtra("event_id",-1);
        NetworkTask end = new NetworkTask();
        end.execute();

    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            result = new Register().ranking(event_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Parsing_Rank().execute();
        }
    }
    public class Parsing_Rank extends AsyncTask<Void, Void, Void> {

        final private String url = "event/";
        private UserRanking user;
        private String event_str;
        private String[] event_dict;
        private Gson gson;
        private Context mContext;



        // NetworkTask의 execute 메소드가 호출된 후 실행되는 메소드
        // doin 메소드로 파라미터 전달
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gson = new Gson();

        }
        // 백그라운드 스레드에서 처리되는 부분
        @Override
        protected Void doInBackground(Void... voids) {
            event_dict = result.split("\n");
            // 배열의 원소들을 json 인코딩 후 각 버튼 setText()
            for(int i = 0; i < event_dict.length; i++) {
                user = gson.fromJson(event_dict[i], UserRanking.class);
                userRanking.add(user);

            }
            return null;
        }

        // 백그라운드 작업 결과 반영
        // doin 메소드로 파라미터를 받는다
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            makeRank();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) findViewById(R.id.rank);
                    textView.setText(parse);

                }
            });
            //new UpdateRank().execute();
        }

    }



    public void makeRank()
    {
        int idx = 1;

        for(UserRanking k : userRanking)
        {
            parse += (idx+". "+k.getUser_id()+" : "+k.getNumber_of_visits()+"\n");

            ////////////////////////////////////////////////



            ///////////////////////////////////////////////


            idx++;
        }
    }


}