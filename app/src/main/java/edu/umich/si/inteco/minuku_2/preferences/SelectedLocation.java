package edu.umich.si.inteco.minuku_2.preferences;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.SemanticLocationDataRecord;
import edu.umich.si.inteco.minuku_2.view.helper.ActionObject;

/**
 * Created by shriti on 8/16/16.
 */
public class SelectedLocation extends ActionObject{

    private String place;
    private String address;
    private double latitude;
    private double longitude;
    private String label;

    public SelectedLocation() {
        super();
    }

    public SelectedLocation(String place, String address, double latitude, double longitude,
                            String label, int imageResourceID) {
        super(place, imageResourceID);
        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.imageResourceId = imageResourceID;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
}
