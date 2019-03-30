package Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.real_visittogether.R;

public class SelectAuth extends AppCompatActivity {
    //인증 성공 유무에따라 부여되는값
    private int success = 10000;
    private int fail = 10001;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_auth);
    }
    public void Select_Auth(View v){
        if(v.getId() == R.id.auth_exif){
            intent = new Intent(SelectAuth.this, Exif.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.auth_qr){
            intent = new Intent(SelectAuth.this, Exif.class);
            startActivity(intent);

        }else if(v.getId() == R.id.auth_bicorn){
            intent = new Intent(SelectAuth.this, Exif.class);
            startActivity(intent);
        }
    }

}
