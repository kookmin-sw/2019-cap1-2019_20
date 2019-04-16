package com.example.qr_hj;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.AsyncTask;

public class DB_Access extends AppCompatActivity {

    private String url = "http://13.124.201.137:50156/";
    private TextView json_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        json_text = (TextView) findViewById(R.id.json_text);

        NetworkTask neworkTask = new NetworkTask(url, null);
        neworkTask.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            URLConnector urlConnector = new URLConnector();
            result = urlConnector.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            json_text.setText(s);
        }
    }
}
