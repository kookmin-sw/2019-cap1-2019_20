package display;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.real_visittogether.R;

import data_fetcher.CameraGallery;

public class SelectCG extends Dialog {

    private TextView cameraText;
    private TextView galleryText;
    private Button cancelButton;
    private CameraGallery cameraGallery;
    private SelectCGListener selectCGListener;

    public SelectCG(final placeAdd context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.select_cg_dialog);

        cameraText = (TextView) findViewById(R.id.cameraText);
        galleryText = (TextView) findViewById(R.id.galleryText);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        cameraGallery = new CameraGallery(context);
        cameraGallery.tedPermission();



        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        cameraText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cameraGallery.getIsPermission()){
                    cameraGallery.takePhoto();
                    selectCGListener.onClicked(cameraGallery);
                }
                else
                    Toast.makeText(view.getContext(), context.getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        galleryText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cameraGallery.getIsPermission()) {
                    cameraGallery.goToAlbum();
                    selectCGListener.onClicked(cameraGallery);
                }
                else
                    Toast.makeText(view.getContext(), context.getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }
    
    interface SelectCGListener{
        void onClicked(CameraGallery cameraGallery);
    }

    public void setSelectCGListener(SelectCGListener selectCGListener){
        this.selectCGListener = selectCGListener;
    }
    
}
