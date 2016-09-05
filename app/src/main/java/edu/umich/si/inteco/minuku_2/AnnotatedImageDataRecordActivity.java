/*
 * Copyright (c) 2016.
 *
 * DReflect and Minuku Libraries by Shriti Raj (shritir@umich.edu) and Neeraj Kumar(neerajk@uci.edu) is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 * Based on a work at https://github.com/Shriti-UCI/Minuku-2.
 *
 *
 * You are free to (only if you meet the terms mentioned below) :
 *
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 *
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 *
 * Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * NonCommercial — You may not use the material for commercial purposes.
 * ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.
 */

package edu.umich.si.inteco.minuku_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;

import java.io.ByteArrayOutputStream;

import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;
import edu.umich.si.inteco.minuku.model.UserSubmissionStats;
import edu.umich.si.inteco.minuku_2.manager.InstanceManager;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minukucore.dao.DAOException;
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
        requestPermission();

        setContentView(R.layout.add_image_activity);
        imageView = (ImageView) findViewById(R.id.image);
        mText = (EditText) findViewById(R.id.image_annotation);

        photoTypeValue = getIntent().getExtras().getString(photoTypeKey);
        showToast("Checking type of photo : " + photoTypeValue);


        // Add click listeners for buttons
        ImageView acceptButton = (ImageView) findViewById(R.id.acceptButton);
        ImageView rejectButton = (ImageView) findViewById(R.id.rejectButton);

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
        UserSubmissionStats userSubmissionStats = InstanceManager
                .getInstance(getApplicationContext()).getUserSubmissionStats();

        switch (photoTypeValue) {
            case ApplicationConstants.IMAGE_TYPE_GLUCOSE_READIMG:
                GlucoseReadingImage glucoseReadingImage = new GlucoseReadingImage(base64ImageData,
                        annotation, photoTypeValue);
                try {
                    StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                            getStreamGeneratorFor(GlucoseReadingImage.class);
                    Log.d(TAG, "Saving results to the database");
                    streamGenerator.offer(glucoseReadingImage);
                    Log.d(TAG, "increment glucose reading image count");

                    if(userSubmissionStats != null)
                        userSubmissionStats.incrementGlucoseReadingCount();
                    else
                        Log.d(TAG, "mUSerSubmissionStats is null");
                } catch (StreamNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "The photo stream does not exist on this device.");
                }
                break;
            case ApplicationConstants.IMAGE_TYPE_INSULIN_SHOT:
                InsulinAdminImage insulinAdminImage = new InsulinAdminImage(base64ImageData,
                        annotation, photoTypeValue);
                try {
                    StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                            getStreamGeneratorFor(InsulinAdminImage.class);
                    Log.d(TAG, "Saving results to the database");
                    streamGenerator.offer(insulinAdminImage);
                    Log.d(TAG, "increment insulin shot image count");
                    userSubmissionStats.incrementInsulinCount();
                } catch (StreamNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "The photo stream does not exist on this device.");
                }
                break;
            case ApplicationConstants.IMAGE_TYPE_FOOD:
                FoodImage foodImage = new FoodImage(base64ImageData, annotation, photoTypeValue);
                try {
                    StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                            getStreamGeneratorFor(FoodImage.class);
                    Log.d(TAG, "Saving results to the database");
                    streamGenerator.offer(foodImage);
                    Log.d(TAG, "increment food image count");
                    userSubmissionStats.incrementFoodCount();
                } catch (StreamNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "The photo stream does not exist on this device.");
                }
                break;
            default:
                AnnotatedImageDataRecord annotatedImageDataRecord = new AnnotatedImageDataRecord(
                        base64ImageData,
                        annotation, photoTypeValue);
                try {
                    StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                            getStreamGeneratorFor(AnnotatedImageDataRecord.class);
                    Log.d(TAG, "Saving results to the database");
                    streamGenerator.offer(annotatedImageDataRecord);
                    Log.d(TAG, "increment other image count");
                    userSubmissionStats.incrementOtherImagesCount();
                } catch (StreamNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "The photo stream does not exist on this device.");
                }
        }
        Log.d(TAG, "increment total image count");
        userSubmissionStats.incrementTotalImageCount();
        InstanceManager
                .getInstance(getApplicationContext())
                .setUserSubmissionStats(userSubmissionStats);

        showToast("Starting the image stream annotated photo.");

        finish();
    }

    protected String getBase64FromBitmap(Bitmap b) {
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

    public void requestPermission() {
        mLayout = findViewById(R.id.main_layout);

        Log.d(TAG, "Requesting camera permission");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else {
            Log.d(TAG, "Permission for camera activity already granted. Starting activity.");
            Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
            startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "On request permission result called.");
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
                    startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
                    Log.d(TAG, "Successfully received permissions and started camera activity.");
                } else {
                    finish();
                }
                return;
        }
    }
}
