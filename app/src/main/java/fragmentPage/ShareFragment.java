package fragmentPage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.linyunchen.voicerecipe.R;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;

public class ShareFragment extends Fragment {
    public static final int REQUEST_CAPTURE = 1;
    public static final int PICK_IMAGE = 100;

    public final static int CAMERA_RESULT = 8888;
    public final static String TAG = "xx";
    private File mPhotoFile;
    private String mPhotoPath;

    public ImageView imageView;
    private ImageButton btnCamera;
    private ImageButton btnShare;
    private Button btnGallery;
    Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View shareView = inflater.inflate(R.layout.fragment_share,container,false);

        imageView = (ImageView) shareView.findViewById(R.id.imageView);
        btnCamera = (ImageButton) shareView.findViewById(R.id.imageBtn_camera);
        btnShare = (ImageButton) shareView.findViewById(R.id.imageBtn_share);
        btnGallery = (Button) shareView.findViewById(R.id.imageBtn_gallery);

        return shareView;
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        public void onClick(View v) {
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                mPhotoPath = "mnt/sdcard/DCIM/Camera/" + getPhotoFileName();
                mPhotoFile = new File(mPhotoPath);
                if (!mPhotoFile.exists()) {
                    mPhotoFile.createNewFile();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, CAMERA_RESULT);
            } catch (Exception e) {}
        }

    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "'IMG'_yyyyMMdd_HHmmss");
            return dateFormat.format(date) + ".jpg";
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CAMERA_RESULT) {
                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
