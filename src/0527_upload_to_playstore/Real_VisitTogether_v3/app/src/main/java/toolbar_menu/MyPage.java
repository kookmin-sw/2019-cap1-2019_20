package toolbar_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.capstone.real_visittogether.R;

import toolbar_menu.mypage.ApplyReward;
import toolbar_menu.mypage.CompletedEventList;
import toolbar_menu.mypage.Participation;

public class MyPage extends AppCompatActivity {
    private  String user_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
       user_id = getIntent().getStringExtra("user_id");
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("My Page") ;
    }
    public void select_list(View v){
      int id = v.getId();
      switch(id){
          case R.id.apply_reward:{
              Intent apply_reward = new Intent(getApplicationContext(), ApplyReward.class);
              startActivity(apply_reward);
              break;
          }
          case R.id.participation_event_list:{
              Intent registed_event_list = new Intent(getApplicationContext(), Participation.class);
              registed_event_list.putExtra("user_id",user_id);
              startActivity(registed_event_list);
              break;
          }
//          case R.id.ranking:{
//              Intent ranking = new Intent(getApplicationContext(), Ranking.class);
//              startActivity(ranking);
//              break;
//          }
          case R.id.completedeventlist:{
            Intent completedeventlist = new Intent(getApplicationContext(), CompletedEventList.class);
            startActivity(completedeventlist);
              break;
          }
      }
    }
}
