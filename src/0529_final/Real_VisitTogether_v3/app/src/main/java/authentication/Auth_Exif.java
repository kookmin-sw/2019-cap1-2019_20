package authentication;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.os.Environment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.capstone.real_visittogether.R;

import event.Event1;
import login.Register;


public class Auth_Exif extends AppCompatActivity {

    private static final String TAG = "VisiTogether";
    private double longitude,latitude;
    private Boolean isPermission = true;
    private Context mContext;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private static final int ID_JPGDIALOG = 0;
    private String exifAttribute;
    private Intent intent;
    public String currentPhotoPath;//실제 사진 파일 경로
    public static Context context;
    private int place_id;
    private Register Reg;
    private int auth_num;
    private  int event_id;
    private  String user_id;
    private Boolean isCamera = false;
    private File tempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exif);
        user_id = getIntent().getStringExtra("user_id");
        place_id = getIntent().getIntExtra("place_id",-1);
        event_id = getIntent().getIntExtra("event_id",-1);
        ImageView jpgView = (ImageView) findViewById(R.id.exif_image);
        mContext = this;
        tedPermission();



        findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if (isPermission) takePhoto();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {
            try {
                currentPhotoPath = tempFile.getAbsolutePath();
                ExifInterface exif = new ExifInterface(currentPhotoPath);
                Geodegree geoDegree = new Geodegree(exif);
                longitude = geoDegree.getLongitude();
                latitude = geoDegree.getLatitude();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
             new gps_check().execute();
            //setImage();

        }
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     * 카메라를 이미지 가져오기
     */
    private void takePhoto() {
        isCamera = true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            /**
             *  안드로이드 OS 누가 버전 이후부터는 file:// URI 의 노출을 금지로 FileUriExposedException 발생
             *  Uri 를 FileProvider 도 감싸 주어야 합니다.
             *
             *  참고 자료 http://programmar.tistory.com/4 , http://programmar.tistory.com/5
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "authentication.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    /**
     * 폴더 및 파일 만들기
     */
    private File createImageFile() throws IOException {

        // 이미지 파일 이름
        String timeStamp_day = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String timeStamp_time = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = timeStamp_day + "_" + timeStamp_time;

        // 이미지가 저장될 파일 주소
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {

        ImageView imageView = findViewById(R.id.exif_image);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        context = Auth_Exif.this;

        imageView.setImageBitmap(originalBm);

    }

    /**
     * 권한 설정
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }


    public class tranlate_exif extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new gps_check().execute();
        }
    }
    public class gps_check extends AsyncTask<Void, Void, Void> {

        String save;

        @Override
        protected Void doInBackground(Void... voids) {

            Register r = new Register();
            double x,y;
            x = latitude;
            y = longitude;
            auth_num = 3;
            System.out.println("place_id              "+place_id);
            save = r.auth_info(place_id,3,x,y,user_id,place_id);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("pictrue :   "+save);
                    if("error".equals(save) || latitude ==0 || longitude ==0) {
                        {
                            Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                            Intent select_auth = new Intent(mContext, SelectAuth.class);
                            select_auth.putExtra("user_id",user_id);
                            select_auth.putExtra("event_id",event_id);
                            select_auth.putExtra("place_id",place_id);
                            startActivity(select_auth);
                        }
                    }
                    else
                    {Toast.makeText(getApplicationContext(),"인증성공! " , Toast.LENGTH_SHORT).show();
                        Intent event = new Intent(mContext, Event1.class);
                        event.putExtra("user_id",user_id);
                        event.putExtra("event_id",event_id);
                        startActivity(event);
                    }
                    //{Toast.makeText(getApplicationContext(), save.toString(), Toast.LENGTH_LONG).show();}

                }
            });
        }


    }
}