package edu.umich.si.inteco.minuku_2.preferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shriti on 8/16/16.
 */
public class LocationPreference {

    public static ArrayList<SelectedLocation> selectedLocationList = new ArrayList<>();

    public static void deleteLocation(int itemPosition) {
       selectedLocationList.remove(itemPosition);
    }

    public static boolean isEmpty() {
        if(selectedLocationList.size()==0)
            return true;
        return false;
    }

    public static void addLocation(SelectedLocation location) {
        selectedLocationList.add(location);
    }

    public static void updateMapMarkers(GoogleMap googleMap) {

        LatLng sydney = new LatLng(-33.867, 151.206);

        //TODO: iterate through the location list and add markers
        if(!LocationPreference.isEmpty()) {
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
}
