package Event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import Authentication.SelectAuth;
import Display.Menu;
import com.example.real_visittogether.R;

public class Event1 extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event1);


        ImageView image1 = (ImageView) findViewById(R.id.imageView) ;
        image1.setImageResource(R.drawable.drink) ;

        ImageView image2 = (ImageView) findViewById(R.id.imageView2) ;
        image2.setImageResource(R.drawable.sabal) ;

        ImageView image3 = (ImageView) findViewById(R.id.imageView3) ;
        image3.setImageResource(R.drawable.oven) ;
    }
    public void onClickEvent1(View view) {


        if(view.getId() == R.id.imageView){
            intent = new Intent(Event1.this, Authentication.SelectAuth.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.imageView2){
            intent = new Intent(Event1.this, Authentication.SelectAuth.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.imageView3){
            intent = new Intent(Event1.this, Authentication.SelectAuth.class);
            startActivity(intent);
        }



    }
}
