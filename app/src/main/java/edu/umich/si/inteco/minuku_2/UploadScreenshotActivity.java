package edu.umich.si.inteco.minuku_2;

import android.content.Intent;
import android.os.Bundle;

import edu.umich.si.inteco.minuku.logger.Log;

/**
 * Created by shriti on 8/20/16.
 */
public class UploadScreenshotActivity extends AnnotatedImageDataRecordActivity {

    private String TAG = "UploadScreenshotActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void requestPermission() {
        Log.d(TAG, "Requestin gallery permission");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }
}
