package display;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.real_visittogether.R;

public class placeAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("장소 추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);
    }
}
