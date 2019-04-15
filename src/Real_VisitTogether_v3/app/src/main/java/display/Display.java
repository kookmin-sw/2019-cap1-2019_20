package display;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;
import login.login;

import com.example.real_visittogether.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.nhn.android.naverlogin.OAuthLogin;

import event.Event1;
import event.Event2;
import toolbar_menu.Help;
import toolbar_menu.MyPage;

public class Display extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        FloatingActionButton button4 = (FloatingActionButton)findViewById(R.id.ActionButton); //동그라미

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Eventregistration.class);
                startActivity(intent);
            }
        });



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

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.logout:
            {
                Toast.makeText(this,"로그아웃 되었습니다",Toast.LENGTH_LONG).show();
                Intent login =  new Intent(getApplicationContext(),login.class);
                startActivity(login);
                return true;
            }
            case R.id.help:
            {
                Intent help_intent = new Intent(getApplicationContext(),Help.class);
                startActivity(help_intent);
                return true;
            }
            case R.id.mypage:
            {
                Intent mypage_intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(mypage_intent);
                return true;
            }
        }
        return true;
    }
}
