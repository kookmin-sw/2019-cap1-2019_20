package display;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
    private Button[] temp_btn;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        temp_btn = new Button[2];
        temp_btn[0] = (Button) findViewById(R.id.temp_btn1);
        temp_btn[1] = (Button) findViewById(R.id.temp_btn2);
        actionButton = (FloatingActionButton)findViewById(R.id.actionButton); //동그라미

        temp_btn[0].setOnClickListener(this);
        temp_btn[1].setOnClickListener(this);
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

    public class NetworkTask extends AsyncTask<Void, Void, String[]> {

        private String url, result;
        private Event event;
        private Gson gson;
        private String events[];

        public NetworkTask(String _url) {
            url = _url;
            gson = new Gson();
            event = new Event();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            RequestHttpConnection connection = new RequestHttpConnection();
            result = connection.request(url);
            events = result.split("\n");
            return events;
        }

        @Override
        protected void onPostExecute(String[] aVoid) {
            super.onPostExecute(aVoid);

            for(int i = 0; i < events.length; i++) {
                event = gson.fromJson(events[i], Event.class);
                temp_btn[i].setText(event.getName());
            }
        }
    }
}
