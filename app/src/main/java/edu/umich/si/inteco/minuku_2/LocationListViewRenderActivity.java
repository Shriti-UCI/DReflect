package edu.umich.si.inteco.minuku_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.umich.si.inteco.minuku_2.preferences.LocationArrayListAdapter;
import edu.umich.si.inteco.minuku_2.preferences.LocationPreference;

/**
 * Created by shriti on 8/15/16.
 * An activity to render the list of preferred locations
 * Works on top of a list view
 * TODO: persist the location data in a list
 */
public class LocationListViewRenderActivity extends BaseActivity {

    private String TAG = "LocationListViewRenderActivity";
    private ListView locationListView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_listview);
        locationListView = (ListView) findViewById(R.id.place_list);

        //creating the location array list adapter for list view UI element
        final LocationArrayListAdapter adapter = new LocationArrayListAdapter(
                this.getApplicationContext(),
                LocationPreference.getInstance().getLocations());

        locationListView.setAdapter(adapter);
    }
}
