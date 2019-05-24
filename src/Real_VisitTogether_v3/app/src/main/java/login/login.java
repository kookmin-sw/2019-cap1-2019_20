package login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import display.Display;

import com.example.real_visittogether.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

public class login extends AppCompatActivity {

    private static final String TAG = "OAuthSampleActivity";
    final private String strURL = "http://ec2-13-209-22-178.ap-northeast-2.compute.amazonaws.com:8888/";
    private String postData;
    /**
     * client 정보를 넣어준다.
     */
   private EditText etEmail;
    private EditText etPassword;
    private String id;
    private String information;
    private String email="";
    private String email2;
    private String password;
    private static String OAUTH_CLIENT_ID = "7siNFLSF2Xv3a3zmbtlg";
    private static String OAUTH_CLIENT_SECRET = "XZvII8lJxW";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인 테스트";
    String result;
    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;
    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;

    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 구글  로그인 버튼
    private SignInButton buttonGoogle;
    /**
     * UI 요소들
     */
    private TextView mApiResultText;
    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;

    private OAuthLoginButton mOAuthLoginButton;
    CallbackManager callbackManager;
    LoginButton loginButton;
    Button loginbtn,logoutbtn;

    /*
        Intent 요청 변수
     */
    public static final int display = 1001;
    public static final int Sign_in = 1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        mContext = this;
        Button faceBook = (LoginButton) findViewById(R.id.login_button);
        OAuthLoginButton naver = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);

        if(faceBook.isClickable()) init(); //로그인버튼을 클릭하면 facebook 로그인
        if(naver.isClickable()){
            initData();
            initView();

        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        boolean checkNaver =mOAuthLoginInstance.getAccessToken(mContext)==null;
        //토큰을 가지고있으면(로그인 유지시) 로그인 하지않고 메뉴에 접근가능
        if(isLoggedIn || !checkNaver || firebaseAuth.getCurrentUser() !=null ) {
            new RequestApiTask().execute();
            //startActivity(new Intent(login.this, display.Display.class));
        }
        final Button sign_in = (Button)findViewById(R.id.sign);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(login.this, Sign_in.class),Sign_in);

            }
        });

        Button login_access = (Button)findViewById(R.id.login_access);
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        login_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new direct_login().execute();


            }
        });

    }
    //여기서부터 페이스북 로그인
    private void init(){
        callbackManager = CallbackManager.Factory.create();//페이스북의 로그인 콜백을 담당하는 클래스.

        loginButton = (LoginButton) findViewById(R.id.login_button);//로그인 버튼. 실제 기능 다수가 이 안에 담겨있다.
        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 성공 후 메뉴창으로 이동

            }
        });


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {//로그인 성공

                Toast.makeText(getApplicationContext(),"로그인 성공", Toast.LENGTH_SHORT).show();

                GraphRequest request = GraphRequest.newMeRequest(//데이터를 받아내기 위해서
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {//데이터를 받아낸 후(네트워크 연결 후)의 콜백
                            @Override
                            public void onCompleted(//완료되었을때 실행된다.
                                                    JSONObject object,
                                                    GraphResponse response) {
                                Log.d("Success", String.valueOf(Profile.getCurrentProfile().getId()));
                                Log.d("Success", String.valueOf(Profile.getCurrentProfile().getName()));
                                Log.d("Success", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));
                                id = String.valueOf(Profile.getCurrentProfile().getId());
                                information = "facebook";
                                // Application code

                            }
                        });

                request.executeAsync();
                new NetworkTask().execute();
                //여기서 데이터베이스와 통신

               // startActivity(new Intent(login.this,display.Display.class));
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(),"로그인 에러", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                System.out.print("check");
            } catch (ApiException e) {

            }
        }
    }
    ////여기까지 FACEBOOK 로그인 부분

    //여기서부터 NAVER로그인
    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);


        // if(OAuthLogin.getInstance().getState(mContext) == );
        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

    private void initView() {


        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);


    }




    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                new RequestApiTask().execute();
                new NetworkTask().execute();
                //원래 여기
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }

    };

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }


    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        private Gson gson;
        JsonParser parser;
        @Override
        protected void onPreExecute() {
            gson = new Gson();
           parser = new JsonParser();



        }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            Object ob2 = mOAuthLoginInstance.requestApi(mContext, at, url);
            JsonElement rootObejct = parser.parse(ob2.toString())
                    .getAsJsonObject().get("response").getAsJsonObject().get("id");

            Log.v("success",rootObejct.toString());//프로필 정보
            email = parser.parse(ob2.toString())
                    .getAsJsonObject().get("response").getAsJsonObject().get("email").toString();
            id = rootObejct.toString();
            id = id.replaceAll("\"","");
            information = "naver";
            new NetworkTask().execute();
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }

        protected void onPostExecute(String content) {

        }
    }

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }

        protected void onPostExecute(String res) {

        }
    }

    //여기까지 네이버 로그인

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(login.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this,display.Display.class));
                        } else {
                            // 로그인 실패
                            Toast.makeText(login.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            if(information !=null) {
                Register connection = new Register();
                connection.registerUser(id, information);
            }
            Intent login = new Intent(mContext,Display.class);
            login.putExtra("user_id",id);
            System.out.println("login : "+id);
            startActivity(login);
            return null;
        }
    }

    public class direct_login extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            email2 = etEmail.getText().toString();
            id = email2;
            password = etPassword.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(etEmail.length() ==0 || etPassword.length() == 0) result = "error";
            else result = new Register().login(email2,password);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(result.equals("ok")){
                        new NetworkTask().execute();
                    }
                    else{
                        Toast.makeText(mContext,"아이디 또는 비빌먼호가 일치하지 않습니다",Toast.LENGTH_LONG).show();
                        etEmail.setText("");
                        etPassword.setText("");
                    }
                }
            });
        }
    }
}