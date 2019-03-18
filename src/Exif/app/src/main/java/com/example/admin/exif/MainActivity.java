package com.example.admin.exif;

import java.io.IOException;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.widget.Button;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    static final String TAG = "MainActivity";

    private static final int ID_JPGDIALOG = 0;
    private String exifAttribute;
    private Dialog dlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtImgName = (TextView) findViewById(R.id.jpgname);
        ImageView jpgView = (ImageView) findViewById(R.id.imageView);
        jpgView.setOnClickListener(popupDlgOnClickListener);

        Intent intent = getIntent();
        if (intent != null) {
            String path = Environment.getExternalStorageState();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            jpgView.setImageBitmap(bm);

            try {
                ExifInterface exif = new ExifInterface(path);
                exifAttribute = getExif(exif);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener popupDlgOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            createdDialog(ID_JPGDIALOG).show(); // Instead of showDialog(0);
        }
    };

    protected Dialog createdDialog(int id) {
        dlg = null;
        TextView content;

        switch (id) {
            case ID_JPGDIALOG:

                Context mContext = this;
                dlg = new Dialog(mContext);

                dlg.setContentView(R.layout.selectimage);
                content = (TextView) dlg.findViewById(R.id.dlgImageName);
                content.setText(exifAttribute);

                Button okDialogButton = (Button) dlg.findViewById(R.id.btnOk);
                okDialogButton.setOnClickListener(okDialogButtonOnClickListener);

                break;
            default:
                break;
        }
        return dlg;
    }

    private Button.OnClickListener okDialogButtonOnClickListener =
            new Button.OnClickListener() {
                public void onClick(View v) {
                    dlg.dismiss();
                }
            };

    private String getExif(ExifInterface exif) {
        String myAttribute = "";
        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif);
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif);
        return myAttribute;
    }

    private String getTagString(String tag, ExifInterface exif) {
        return (tag + " : " + exif.getAttribute(tag) + "\n");
    }

    //로드버튼 클릭시 실행
    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //이미지 선택작업을 후의 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                imgView = (ImageView) findViewById(R.id.imageView);
                imgView.setImageBitmap(scaled);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


}


