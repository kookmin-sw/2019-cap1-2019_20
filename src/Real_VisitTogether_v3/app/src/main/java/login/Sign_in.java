
package login;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.real_visittogether.R;

import data_fetcher.RequestHttpConnection;

public class Sign_in extends AppCompatActivity {
    private String id;
    private String password;
    private String confirm_password;
    private String information;
    private EditText email;
    private  EditText confirm_email_password;
    private  EditText email_password;
    private  Context mcontext = this;
    String result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);

        Button confirm = (Button)findViewById(R.id.btnDone);
        Button btnCancel = (Button)findViewById(R.id.btnCancel) ;
        email = (EditText)findViewById(R.id.etEmail);
        email_password = (EditText)findViewById(R.id.etPassword);
        confirm_email_password = (EditText)findViewById(R.id.etPasswordConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password_check check =  new password_check();
                check.execute();

                try{
                    Thread.sleep(100);
                }catch (Exception e){}
                if(result.equals("ok"))
                    new NetworkTask(mcontext).execute();
                else{
                    Toast.makeText(mcontext,"아이디가 중복 되었습니다.",Toast.LENGTH_LONG).show();
                    email.setText("");

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mcontext,login.class));
            }
        });

    }
    public class id_duplicate_check extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... voids) {
            Register connection = new Register();
            String result = connection.idDuplicateCheck(email.getText().toString());
            return null;
        }





    }
    public class password_check extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Register connection = new Register();
           result = connection.idDuplicateCheck(email.getText().toString());
           // 가져온 문자들은 공백제거 해주어야함
           result = result.trim();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!email_password.getText().toString().equals(confirm_email_password.getText().toString())){
                        Toast.makeText(mcontext,"암호가 일치하지 않습니다",Toast.LENGTH_LONG).show();
                        email_password.setText("");
                        confirm_email_password.setText("");
                    }

                }
            });
        }


    }
    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        public NetworkTask(Context context){
            this.mContext = context;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            id = email.getText().toString();
            password = email_password.getText().toString();
            information = "direct";
            Register connection = new Register();

           connection.registerUser(id,password,information.toString());
           startActivity(new Intent(mContext,login.class));
            return null;
        }
    }
}
