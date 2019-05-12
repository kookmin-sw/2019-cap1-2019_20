
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        Button confirm = (Button)findViewById(R.id.btnDone);
        email = (EditText)findViewById(R.id.etEmail);
        email_password = (EditText)findViewById(R.id.etPassword);
        confirm_email_password = (EditText)findViewById(R.id.etPasswordConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email_password.toString().equals(confirm_email_password.toString())){
                    Toast.makeText(getApplicationContext(),"암호가 일치하지 않습니다",Toast.LENGTH_LONG);
                    email_password.setText("");
                    confirm_email_password.setText("");
                }
                else {
                    new id_duplicate_check().execute();
                   // new NetworkTask(getApplicationContext()).execute();
                }
            }
        });
    }
    public class id_duplicate_check extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        @Override
        protected Void doInBackground(Void... voids) {

            Register connection = new Register();
            connection.idDuplicateCheck(email.getText().toString());
            RequestHttpConnection read_connection = new RequestHttpConnection();
            String message = read_connection.request("id_duplicate_check/");
            System.out.println(message);
            return null;
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
