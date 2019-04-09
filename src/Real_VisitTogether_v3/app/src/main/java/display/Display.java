package display;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import com.example.real_visittogether.R;
import com.google.gson.Gson;

import data_fetcher.RequestHttpConnection;
import event.Event1;
import event.Event2;
import vt_object.Event;

public class Display extends AppCompatActivity implements View.OnClickListener {

    final private String url = "";
    private Intent intent;
    private NetworkTask networkTask;
    private Button temp_btn1, temp_btn2;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        temp_btn1 = (Button) findViewById(R.id.temp_btn1);
        temp_btn2 = (Button) findViewById(R.id.temp_btn2);
        actionButton = (FloatingActionButton)findViewById(R.id.actionButton); //동그라미

        temp_btn1.setOnClickListener(this);
        temp_btn2.setOnClickListener(this);
        actionButton.setOnClickListener(this);

        networkTask = new NetworkTask(url);
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


    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        private String url;
        private String result;
        private Event event;

        public NetworkTask(String _url) {
            url = _url;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RequestHttpConnection connection = new RequestHttpConnection();
            result = connection.request(url);

            Gson gson = new Gson();
            event = new Event();
            Log.d("제이슨메세지???", result);
            result = "{'id': 1, 'name': '정릉맛집'}";
            event = gson.fromJson(result, Event.class);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            temp_btn1.setText(event.getName());
        }
    }
}
