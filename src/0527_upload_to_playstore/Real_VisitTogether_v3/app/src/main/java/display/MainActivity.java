package display;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.capstone.real_visittogether.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(MainActivity.this, Display.class));
    }
}