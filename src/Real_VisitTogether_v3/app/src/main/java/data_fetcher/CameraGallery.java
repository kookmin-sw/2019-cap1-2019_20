package data_fetcher;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.real_visittogether.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraGallery extends AppCompatActivity {

    private static final String TAG = "VisiTogether";
    private final int PICK_FROM_ALBUM = 1;
    private final int PICK_FROM_CAMERA = 2;

    private File tempFile;
    private boolean isPermission;
    private Context context;

    public CameraGallery(Context context){
        this.context = context;
        this.isPermission = false;
    }

    /**
     * 권한 설정
     */
    public void tedPermission() {

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
        new TedPermission(context)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(context.getResources().getString(R.string.permission_2))
                .setDeniedMessage(context.getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    public void goToAlbum() {

        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        //startActivityForResult(intent, PICK_FROM_ALBUM);
        ((Activity)context).startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 카메라를 통해 이미지 가져오기
     */
    public void takePhoto() {

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
            Uri photoUri;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                photoUri = FileProvider.getUriForFile(this,"authentication.provider", tempFile);
            else
                photoUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            ((Activity)context).startActivityForResult(intent, PICK_FROM_CAMERA);
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

    public boolean getIsPermission(){
        return isPermission;
    }

    public File getTempFile(){
        return tempFile;
    }
}
