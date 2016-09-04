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

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.NoteDataRecord;
import edu.umich.si.inteco.minuku.model.UserSubmissionStats;
import edu.umich.si.inteco.minuku_2.manager.InstanceManager;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.streamgenerator.StreamGenerator;

/**
 * Created by shriti on 8/20/16.
 */
public class NoteEntryActivity extends BaseActivity {

    private String TAG = "NoteEntryActivity";

    private NoteDataRecord mNote;
    private EditText mNoteData;
    private ImageView acceptButton;
    private ImageView rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_entry_activity);
        mNoteData = (EditText) findViewById(R.id.note_data);

        // Add click listeners for buttons
        acceptButton = (ImageView) findViewById(R.id.acceptButton);
        rejectButton = (ImageView) findViewById(R.id.rejectButton);

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

        Bundle extras = getIntent().getExtras();
        if(extras !=null && extras.getBoolean("FROM_NOTIFICATION")) {
            for(String key:extras.keySet()) {
                Log.d("NoteEntryActivity", key);
            }
            showToast("This screen was started from a notification with type:" + getIntent().getExtras().getString("PROMPT_TYPE", ""));
        }
    }


    /**
     * This is called when the user pressed "Tick" button on the screen.
     */
    public void acceptResults() {

        // Make sure that there is some data entered by the user in the note field.
        String noteData = mNoteData.getText().toString();
        if (noteData == null || noteData.equals("") || noteData.trim().isEmpty()) {
            showToast("Cannot add note without any content.");
            return;
        } else {
            mNote = new NoteDataRecord(noteData);
            try {
                StreamGenerator streamGenerator = MinukuStreamManager.getInstance().
                        getStreamGeneratorFor(NoteDataRecord.class);
                Log.d(TAG, "Saving results to the database");
                streamGenerator.offer(mNote);
            } catch (StreamNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "The note stream does not exist on this device.");
            }
            showToast("Your note has been recorded");
            finish();
        }
    }

    /**
     * This is called when the user presses the "X" button the screen.
     */
    public void rejectResults() {
        showToast("Going back to home screen");
        finish();
    }
}
