package Event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.real_visittogether.R;

public class Event1 extends AppCompatActivity {
   private String place;
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

        TextView t;
        if(view.getId() == R.id.imageView){
            intent = new Intent(Event1.this, Authentication.SelectAuth.class);
            t =(TextView)findViewById(R.id.taste1);
            place = t.getText().toString();
            intent.putExtra("place",place);
           startActivity(intent);
        }

        if(view.getId() == R.id.imageView2){
            intent = new Intent(Event1.this, Authentication.SelectAuth.class);
            t =(TextView)findViewById(R.id.taste2);
            place = t.getText().toString();
            intent.putExtra("place",place);
           startActivity(intent);
        }

        if(view.getId() == R.id.imageView3){
            intent = new Intent(Event1.this, Authentication.SelectAuth.class);
            t =(TextView)findViewById(R.id.taste3);
            place = t.getText().toString();
            intent.putExtra("place",place);
           startActivity(intent);
        }

    }

}
