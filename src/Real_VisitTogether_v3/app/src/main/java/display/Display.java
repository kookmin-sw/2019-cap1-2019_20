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
import android.widget.TextView;

import com.example.real_visittogether.R;

import org.json.JSONObject;

import connector.RequestHttpConnection;
import event.Event1;
import event.Event2;

public class Display extends AppCompatActivity implements View.OnClickListener {

    final private String url = "";
    private Intent intent;
    private NetworkTask networkTask;
    private Button button2, button3;
    private FloatingActionButton actionButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        actionButton = (FloatingActionButton)findViewById(R.id.actionButton); //동그라미

        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        actionButton.setOnClickListener(this);

        networkTask = new NetworkTask(url);
        networkTask.execute();
    }

    public void onClick(View view) {

//        if(view.getId() == R.id.button1){
//            intent = new Intent(Display.this, Menu.class);
//            startActivity(intent);
//        }

        if(view.getId() == R.id.button2){
            intent = new Intent(Display.this, Event1.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.button3){
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

    public class NetworkTask extends AsyncTask<Void, Void, Void>{

        String url;
        private String result;

        public NetworkTask(String _url) {
            url = _url;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RequestHttpConnection connection = new RequestHttpConnection();
            result = connection.request(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            button2.setText(result);
        }
    }
}
