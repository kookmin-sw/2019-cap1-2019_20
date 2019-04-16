package Display;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import com.example.real_visittogether.R;

import Event.Event1;
import Event.Event2;

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
                Intent intent = new Intent(getApplicationContext(), Eventregistration.class); // 현재 화면의 제어권자 // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
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
        // 메뉴버튼이 처음 눌러졌을 때 실행되는 콜백메서드
        // 메뉴버튼을 눌렀을 때 보여줄 menu 에 대해서 정의

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);  //menu_1.xml 파일을 java 객체로 인플레이트(inflate)해서 menu객체에 추가

        return true;
    }
}
