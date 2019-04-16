package Login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Display.Display;
import com.example.real_visittogether.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

public class login extends AppCompatActivity {

    private static final String TAG = "OAuthSampleActivity";

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "7siNFLSF2Xv3a3zmbtlg";
    private static String OAUTH_CLIENT_SECRET = "XZvII8lJxW";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인 테스트";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

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


        mContext = this;
        Button faceBook = (LoginButton) findViewById(R.id.login_button);
        OAuthLoginButton naver = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        SignInButton  buttonGoogle = findViewById(R.id.btn_googleSignIn);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, google.class));

            }
        });
        if(faceBook.isClickable()) init(); //로그인버튼을 클릭하면 facebook 로그인
        if(naver.isClickable()){
            initData();
            initView();

        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        boolean checkNaver =mOAuthLoginInstance.getAccessToken(mContext)==null;
        //토큰을 가지고있으면(로그인 유지시) 로그인 하지않고 메뉴에 접근가능
        if(isLoggedIn || !checkNaver) {
            startActivity(new Intent(login.this, Display.class));
        }
        final Button sign_in = (Button)findViewById(R.id.sign);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(login.this, Login.Sign_in.class),Sign_in);

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

        logoutbtn = (Button)findViewById(R.id.logout);  //로그아웃 버튼
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그아웃 하였습니다", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance (). logOut ();
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

                                // Application code

                            }
                        });
                /*
                Bundle parameters = new Bundle();
                parameters.putString("status", "ok");//데이터를 전부 받아오지 않고 email만 받아온다. "email,name,age" 의 형식으로 받아올수 있음.
                request.setParameters(parameters);
                request.executeAsync();
                */
                startActivity(new Intent(login.this,Display.class));
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

                startActivity(new Intent(login.this,Display.class));
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
        @Override
        protected void onPreExecute() {
            mApiResultText.setText((String) "");
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            Object ob2 = mOAuthLoginInstance.requestApi(mContext, at, url);
            Log.v("success",ob2.toString());//프로필 정보
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }

        protected void onPostExecute(String content) {
            mApiResultText.setText((String) content);
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
    public void LogOut(View v)
    {
        Toast.makeText(getApplicationContext(), "로그아웃 하였습니다", Toast.LENGTH_SHORT).show();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        boolean checkNaver =mOAuthLoginInstance.getAccessToken(mContext)==null;
        LoginManager.getInstance (). logOut ();
        if(isLoggedIn ) {
            LoginManager.getInstance (). logOut ();
        }
        else if(!checkNaver) {
            mOAuthLoginInstance.logout(mContext);
        }
    }

}

