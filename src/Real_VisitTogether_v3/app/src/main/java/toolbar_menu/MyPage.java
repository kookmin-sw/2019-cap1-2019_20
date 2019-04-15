package toolbar_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.real_visittogether.R;

import authentication.SelectAuth;
import toolbar_menu.mypage.ApplyReward;
import toolbar_menu.mypage.CompletedEventList;
import toolbar_menu.mypage.Ranking;
import toolbar_menu.mypage.RegistedEventList;
import toolbar_menu.mypage.SelectReward;

public class MyPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
    }
    public void select_list(View v){
      int id = v.getId();
      switch(id){
          case R.id.apply_reward:{
              Intent apply_reward = new Intent(getApplicationContext(), ApplyReward.class);
              startActivity(apply_reward);
          }
          case R.id.registed_event_list:{
              Intent registed_event_list = new Intent(getApplicationContext(), RegistedEventList.class);
              startActivity(registed_event_list);
          }
          case R.id.ranking:{
              Intent ranking = new Intent(getApplicationContext(), Ranking.class);
              Toast.makeText(getApplicationContext(),"랭킹",Toast.LENGTH_LONG).show();
              startActivity(ranking);
          }
          case R.id.completedeventlist:{
            Intent completedeventlist = new Intent(getApplicationContext(), CompletedEventList.class);
            startActivity(completedeventlist);
          }
      }
    }
}
