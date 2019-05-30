package toolbar_menu.mypage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.capstone.real_visittogether.R;

public class ApplyReward extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_reward);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("보상 요청") ;
    }
}
