package display;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.real_visittogether.R;

import java.io.File;
import java.util.ArrayList;

import data_fetcher.CameraGallery;
import login.Register;

public class placeAdd extends AppCompatActivity {

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private Button addPictureButton;
    private EditText placeName;
    private EditText addressText;
    private EditText information;
    private SelectCG selectCG_dialog;
    private ImageView placeImage;

    private CameraGallery cameraGallery;
    private Bitmap originalBm;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("장소 추가");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);

        addPictureButton = (Button) findViewById(R.id.AddPicture);
        placeName = (EditText) findViewById(R.id.inputPlace);
        addressText = (EditText) findViewById(R.id.addressText);
        information = (EditText) findViewById(R.id.inputInformation);
        placeImage = (ImageView) findViewById(R.id.placeImage);

        arrayList = new ArrayList<>();
        arrayList.add("사진촬영(Exif)");
        arrayList.add("사진촬영(Text)");
        arrayList.add("GPS");
        arrayList.add("Beacon");
        arrayList.add("QR_Code");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        System.out.println("onCreate 메소드");

        //다이얼로그 생성
        selectCG_dialog = new SelectCG(this);
        WindowManager.LayoutParams windowManager = selectCG_dialog.getWindow().getAttributes();
        windowManager.copyFrom(selectCG_dialog.getWindow().getAttributes());

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        windowManager.width = width * 2 / 3;
        windowManager.height = height / 3;
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("버튼 눌렀을 경우");

                selectCG_dialog.setSelectCGListener(new SelectCG.SelectCGListener() {
                    @Override
                    public void onClicked(CameraGallery _cameraGallery) {
                        cameraGallery = _cameraGallery;
                    }
                });
                selectCG_dialog.show();
            }
        });
    }

    public void onClick(View v) {
        System.out.println("onClick 메소드");
        if (v.getId() == R.id.regist) {
            /*
            SharedPreferences places_pref = getSharedPreferences("temp_places", MODE_PRIVATE);
            SharedPreferences.Editor editor = places_pref.edit();

            int temp_places_size = places_pref.getInt("temp_places_size",0);
            editor.putString("temp_places_name" + temp_places_size, placeName.getText().toString());
            editor.putString("temp_places_address" + temp_places_size, addressText.getText().toString());
            editor.putString("temp_places_information" + temp_places_size, information.getText().toString());
            editor.putInt("temp_places_size", temp_places_size + 1);
            editor.commit();
            */
            NetworkTask networkTask = new NetworkTask();
            networkTask.execute();

            Intent eventRegisteration = new Intent(getApplicationContext(), Eventregistration.class);
            startActivity(eventRegisteration);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("placeAdd의 onActivityResult메소드 진입");

        tempFile = cameraGallery.getTempFile();
        final String TAG = "VisiTogether";
        final int PICK_FROM_ALBUM = 1;
        final int PICK_FROM_CAMERA = 2;

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
        }
        setImage(tempFile);

    }

    private void setImage(File tempFile) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        placeImage.setImageBitmap(originalBm);
    }



    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            System.out.println("PlaceAdd doInbackground!!");

            Register connection = new Register();
            String result = connection.registerPlace(placeName.getText().toString(), addressText.getText().toString(), information.getText().toString());
            if(tempFile != null){
                ImageConverter imageConverter = new ImageConverter();
                String result2 = connection.registerImage(imageConverter.bitmapToString(originalBm));
                System.out.println("사진 string 값:  " + imageConverter.bitmapToString(originalBm));
                System.out.println("Response 데이터: " + result2);
            }
            //System.out.println("Register Place Result: " + result);

            // 임시 저장 장소 개수
            // 임시 저장 장소ID 추가하고 개수 +1
            // 저장
            SharedPreferences places_pref = getSharedPreferences("temp_places", MODE_PRIVATE);
            SharedPreferences.Editor editor = places_pref.edit();
            int temp_places_size = places_pref.getInt("temp_places_size",0);
            editor.putInt("temp_places_id" + temp_places_size, Integer.parseInt(result));
            editor.putInt("temp_places_size", temp_places_size + 1);
            editor.commit();
            //System.out.println("temp_places_size = " + places_pref.getInt("temp_places_size", 0));

            return null;
        }
    }
}