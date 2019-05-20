package authentication;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.os.Environment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.real_visittogether.R;

import data_fetcher.CameraGallery;


public class Auth_Exif extends AppCompatActivity {

    private final int PICK_FROM_ALBUM = 1;
    private final int PICK_FROM_CAMERA = 2;
    private static final String TAG = "VisiTogether";
    private Boolean isPermission = true;

    private Intent intent;
    public String currentPhotoPath;//실제 사진 파일 경로
    public static Context context;
    //private File tempFile;

    private ImageView imageView;
    private CameraGallery cameraGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exif);

        imageView = findViewById(R.id.exif_image);
        cameraGallery = new CameraGallery(this);

        cameraGallery.tedPermission();

        findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if (cameraGallery.getIsPermission()) {
                    cameraGallery.goToAlbum();
                }
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

            }
        });

        findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if (cameraGallery.getIsPermission()){
                    cameraGallery.takePhoto();
                }
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

            }
        });

        findViewById(R.id.exif_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();
                int place_num = intent.getIntExtra("place_num", 0);
                intent = new Intent(Auth_Exif.this, SelectImage.class);
                intent.putExtra("place_num", place_num);
                startActivity(intent);
                System.out.printf("\n<Auth_Exif>\nplace_num = %d\nauthenticated = %b\n", place_num, true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        File tempFile = cameraGallery.getTempFile();
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
        }//else if (requestCode == PICK_FROM_CAMERA) {}
        setImage(tempFile);
    }

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage(File tempFile) {

        ImageView imageView = findViewById(R.id.exif_image);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
        System.out.println("setImage : " + tempFile.getAbsolutePath());
        currentPhotoPath = tempFile.getAbsolutePath();
        context = Auth_Exif.this;

        imageView.setImageBitmap(originalBm);
    }

    public void onClickAuth(View view) {
        if (view.getId() == R.id.exif_image) {
            intent = getIntent();
            int place_num = intent.getIntExtra("place_num", 0);
            intent = new Intent(Auth_Exif.this, SelectImage.class);
            intent.putExtra("place_num", place_num);
            startActivity(intent);
            //System.out.printf("\n<Auth_Exif>\nplace_num = %d\nauthenticated = %b\n", place_num, true);
        }
    }
}