package display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    public Bitmap stringToBitmap(String imageString){
        /*
        // DB에서 이미지 다시 가져올 때 패딩 오류 해결
        String[] tempArray = imageString.split(" ");
        String result = "";
        for(int i = 0; i < tempArray.length; i++){
            result += tempArray[i];
            if (i != tempArray.length - 1)
                result += "+";
        }
        byte[] bytes = Base64.decode(result, Base64.DEFAULT);
        */
        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }
}
