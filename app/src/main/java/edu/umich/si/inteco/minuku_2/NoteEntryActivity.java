package edu.umich.si.inteco.minuku_2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.NoteDataRecord;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.streamgenerator.StreamGenerator;
import edu.umich.si.inteco.minuku.logger.Log;

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
