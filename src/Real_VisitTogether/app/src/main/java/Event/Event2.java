package Event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.real_visittogether.R;

public class Event2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event2);

        ImageView image1 = (ImageView) findViewById(R.id.image1) ;
        image1.setImageResource(R.drawable.southdoor) ;

        ImageView image2 = (ImageView) findViewById(R.id.image2) ;
        image2.setImageResource(R.drawable.eastdoor) ;

        ImageView image3 = (ImageView) findViewById(R.id.image3) ;
        image3.setImageResource(R.drawable.westdoor) ;
    }
}
