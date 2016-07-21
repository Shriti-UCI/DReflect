package edu.umich.si.inteco.minuku_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

import edu.umich.si.inteco.minuku.dao.ImageDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.LocationDataRecordDAO;
import edu.umich.si.inteco.minuku.dao.MoodDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.model.ImageDataRecord;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.streamgenerator.ImageStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.LocationStreamGenerator;
import edu.umich.si.inteco.minuku.streamgenerator.MoodStreamGenerator;
import edu.umich.si.inteco.minuku_2.service.BackgroundService;
import edu.umich.si.inteco.minuku_2.view.helper.ActionObject;
import edu.umich.si.inteco.minuku_2.view.helper.StableArrayAdapter;
import edu.umich.si.inteco.minukucore.user.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeActionList();
        startService(new Intent(getBaseContext(), BackgroundService.class));

        User dummyUser = new User("dummy", "user", "dummyuser@gmail,com");
        UUID dummyUUID = UUID.randomUUID();

        // DAO initialization stuff
        MinukuDAOManager daoManager = MinukuDAOManager.getInstance();
        //For location
        LocationDataRecordDAO locationDataRecordDAO = new LocationDataRecordDAO();
        locationDataRecordDAO.setDevice(dummyUser, dummyUUID);
        daoManager.registerDaoFor(LocationDataRecord.class, locationDataRecordDAO);

        //For image
        ImageDataRecordDAO imageDataRecordDAO = new ImageDataRecordDAO();
        imageDataRecordDAO.setDevice(dummyUser, dummyUUID);
        daoManager.registerDaoFor(ImageDataRecord.class, imageDataRecordDAO);

        //For mood
        MoodDataRecordDAO moodDataRecordDAO = new MoodDataRecordDAO();
        moodDataRecordDAO.setDevice(dummyUser, dummyUUID);
        daoManager.registerDaoFor(MoodDataRecord.class, moodDataRecordDAO);

        // Create corresponding stream generators. Only to be created once in Main Activity
        //creating a new stream registers it with the stream manager
        LocationStreamGenerator locationStreamGenerator =
                new LocationStreamGenerator(getApplicationContext());
        ImageStreamGenerator imageStreamGenerator =
                new ImageStreamGenerator(getApplicationContext());
        MoodStreamGenerator moodStreamGenerator =
                new MoodStreamGenerator(getApplicationContext());

    }

    //populate the list of elements in home screen
    private void initializeActionList() {
        final ListView listview = (ListView) findViewById(R.id.list_view_actions_list);

        // the action object is the model behind the list that is shown on the main screen.
        final ActionObject[] array = {
                new ActionObject("Take Glucose Reading Pictures", "A", R.drawable.camera),
                new ActionObject("Record Your Mood", "A", R.drawable.mood),
                new ActionObject("Huff", "A", R.drawable.reject)
        };
        // The adapter takes the action object array and converts it into a view that can be
        // rendered as a list, one item at a time.
        final StableArrayAdapter adapter = new StableArrayAdapter(
                this.getApplicationContext(), array);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // The position here corresponds to position of objects in the array passed above.
                switch (position) {
                    case 0:
                        Intent addPhotoIntent = new Intent(MainActivity.this, ImageDataRecordActivity.class);
                        startActivity(addPhotoIntent);
                        break;
                    case 1:
                        Intent addMoodIntent = new Intent(MainActivity.this, MoodDataRecordActivity.class);
                        startActivity(addMoodIntent);
                    default:
                        showToast("Clicked unknown");
                        break;
                }
            }
        });

    }

    protected void showToast(String aText) {
        Toast.makeText(this, aText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_settings) {
            showSettingsScreen();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showSettingsScreen() {
        //showToast("Clicked settings");
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
    }
}
