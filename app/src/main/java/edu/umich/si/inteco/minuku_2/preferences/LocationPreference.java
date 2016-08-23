package edu.umich.si.inteco.minuku_2.preferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.umich.si.inteco.minuku.config.UserPreferences;

/**
 * Created by neerajkumar on 8/23.
 */
public class LocationPreference {

    private List<SelectedLocation> selectedLocationList =
            new ArrayList<>();
    private static LocationPreference instance;

    private LocationPreference() {
        this.selectedLocationList = getSelectedLocationsFromPersistedStorage();
    }

    public static LocationPreference getInstance() {
        if(instance == null) {
            instance = new LocationPreference();
        }
        return instance;
    }

    public List<SelectedLocation> getLocations() {
        return new ArrayList<>(this.selectedLocationList);
    }

    public void deleteLocation(int itemPosition) {
        selectedLocationList.remove(itemPosition);
        persistSelectedLocations();
    }

    public void addLocation(SelectedLocation location) {
        selectedLocationList.add(location);
        persistSelectedLocations();
    }

    public boolean isEmpty() {
        if(selectedLocationList.size()==0)
            return true;
        return false;
    }

    public void updateMapMarkers(GoogleMap googleMap) {

        LatLng sydney = new LatLng(-33.867, 151.206);

        //TODO: iterate through the location list and add markers
        if(!this.isEmpty()) {
            for (SelectedLocation location : selectedLocationList) {
                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(coordinates)
                        .title(location.getPlace()));
            }
        }
        else {
            googleMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Sydney"));
        }
    }

    private List<SelectedLocation> getSelectedLocationsFromPersistedStorage() {
        Type typeOfListObject = new TypeToken<List<SelectedLocation>>(){}.getType();
        Gson gson = new Gson();
        String serializedLocationList = UserPreferences.getInstance()
                .getPreference(ApplicationConstants.SELECTED_LOCATIONS);
        if(serializedLocationList == null) {
            return new ArrayList<>();
        }
        return gson.fromJson(serializedLocationList, typeOfListObject);
    }

    private void persistSelectedLocations() {
        Type typeOfListObject = new TypeToken<List<SelectedLocation>>(){}.getType();
        Gson gson = new Gson();
        UserPreferences.getInstance().writePreference(ApplicationConstants.SELECTED_LOCATIONS,
                gson.toJson(selectedLocationList, typeOfListObject));
    }
}
