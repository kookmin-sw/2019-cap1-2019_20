package toolbar_menu.mypage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.real_visittogether.R;

public class CompletedEventList extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_event_list);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("완료된 이벤트 목록") ;
    }
}
