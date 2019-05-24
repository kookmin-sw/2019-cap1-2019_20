package toolbar_menu.mypage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.real_visittogether.R;

public class Participation extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participation);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("참여중인 이벤트 목록") ;
    }
}
