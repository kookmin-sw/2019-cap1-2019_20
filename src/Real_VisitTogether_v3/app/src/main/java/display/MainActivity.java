package display;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.real_visittogether.R;

<<<<<<< HEAD:src/Real_VisitTogether_v3/app/src/main/java/Display/MainActivity.java
import login.login;

=======
>>>>>>> ff17a21a33a3794f85ea9ff086dcf4ddfeb31cac:src/Real_VisitTogether_v3/app/src/main/java/display/MainActivity.java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, Display.class));
    }
}