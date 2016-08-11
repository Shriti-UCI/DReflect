package edu.umich.si.inteco.minuku_2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;

import java.io.ByteArrayOutputStream;

import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.streamgenerator.StreamGenerator;

/**
 * Created by shriti on 7/19/16.
 */
public class AnnotatedImageDataRecordActivity extends BaseActivity {

    private static final String TAG = "AnotatdImgDataRecActvty";

    protected ImageView imageView;
    protected EditText mText;
    protected String base64ImageData;
    private String photoTypeValue;
    protected String photoTypeKey = "photoType";

    protected static final int REQUEST_CAMERA = 101;
    protected static final int REQUEST_GALLERY = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_image_activity);
        imageView = (ImageView) findViewById(R.id.image);
        mText = (EditText) findViewById(R.id.image_annotation);

        photoTypeValue = getIntent().getExtras().getString(photoTypeKey);

        // Add click listeners for buttons
        ImageView acceptButton = (ImageView) findViewById(R.id.acceptButton);
        ImageView rejectButton = (ImageView) findViewById(R.id.rejectButton);

        requestCameraPermission();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptResults();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectResults();
            }
        });

    }

    private void rejectResults() {
        showToast("Back to home screen");
        finish();
    }

    private void acceptResults() {
        //create a new image data record once submitted/accepted by user
        String annotation = mText.getText().toString();

            switch (photoTypeValue) {
                case "GLUCOSE_READING":
                    GlucoseReadingImage glucoseReadingImage = new GlucoseReadingImage(base64ImageData,
                            annotation);
                    try {
                        StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                                getStreamGeneratorFor(GlucoseReadingImage.class);
                        Log.d(TAG, "Saving results to the database");
                        streamGenerator.offer(glucoseReadingImage);
                    } catch (StreamNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "The photo stream does not exist on this device.");
                    }
                    break;
                case "INSULIN_SHOT":
                    InsulinAdminImage insulinAdminImage = new InsulinAdminImage(base64ImageData,
                            annotation);
                    try {
                        StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                                getStreamGeneratorFor(InsulinAdminImage.class);
                        Log.d(TAG, "Saving results to the database");
                        streamGenerator.offer(insulinAdminImage);
                    } catch (StreamNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "The photo stream does not exist on this device.");
                    }
                    break;
                case "FOOD":
                    FoodImage foodImage = new FoodImage(base64ImageData, annotation);
                    try {
                        StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                                getStreamGeneratorFor(FoodImage.class);
                        Log.d(TAG, "Saving results to the database");
                        streamGenerator.offer(foodImage);
                    } catch (StreamNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "The photo stream does not exist on this device.");
                    }
                    break;
                default:
                    AnnotatedImageDataRecord annotatedImageDataRecord = new AnnotatedImageDataRecord(
                            base64ImageData,
                            annotation);
                    try {
                        StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                                getStreamGeneratorFor(AnnotatedImageDataRecord.class);
                        Log.d(TAG, "Saving results to the database");
                        streamGenerator.offer(annotatedImageDataRecord);
                    } catch (StreamNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "The photo stream does not exist on this device.");
                    }
            }

        showToast("Starting the image stream annotated photo.");

        finish();
    }

    private String getBase64FromBitmap(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
        }
        // There are two types of media that are requested, either from Camera, or from Gallery.
        //Each of them have their own ways of rendering the selected/taken image.
        if (requestCode == REQUEST_CAMERA
                && resultCode == RESULT_OK
                && null != data) {
            Uri photoUri = data.getData();
            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), 300, 300);
            imageView.setImageBitmap(bitmap);
            base64ImageData = getBase64FromBitmap(bitmap);
        } else if(requestCode == REQUEST_GALLERY
                && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
            imageView.setImageBitmap(bitmap);
            base64ImageData = getBase64FromBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestCameraPermission() {
        mLayout = findViewById(R.id.main_layout);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                Log.i(TAG,
                        "Displaying camera permission rationale to provide additional context.");

                Snackbar.make(mLayout, R.string.permission_location_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(AnnotatedImageDataRecordActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA);
                            }
                        })
                        .show();
            } else {
                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        }

        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

}
