package display;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.real_visittogether.R;

import data_fetcher.RequestHttpConnection;

public class Eventregistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("이벤트 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventregistration);

        Button button5 = (Button)findViewById(R.id.place);


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), placeAdd.class);
                startActivity(intent);
            }
        });

        Button registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkTask networkTask = new NetworkTask();
                networkTask.execute();
            }
        });
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            RequestHttpConnection connection = new RequestHttpConnection();

            EditText nameText = (EditText)findViewById(R.id.inputevent);
            connection.sendData(nameText.getText().toString(), "testID");

            return null;
        }
    }
}
