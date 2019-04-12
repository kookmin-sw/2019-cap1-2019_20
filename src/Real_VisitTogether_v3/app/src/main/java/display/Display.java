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

        final private String url = "event/";
        private String event_str;
        private Event event;
        private Gson gson;
        private String[] event_dict;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gson = new Gson();
            event = new Event();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            RequestHttpConnection connection = new RequestHttpConnection();
            event_str = connection.request(url);
            event_dict = event_str.split("\n");
            return event_dict;
        }

        @Override
        protected void onPostExecute(String[] aVoid) {
            super.onPostExecute(aVoid);

            for(int i = 0; i < temp_btn.length; i++) {
                event = gson.fromJson(event_dict[i], Event.class);
                temp_btn[i].setText(event.getName());
            }
        }
    }
}
