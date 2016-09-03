package edu.umich.si.inteco.minuku.config;

/**
 * Created by shriti on 8/16/16.
 */
public class SelectedLocation {

    private String place;
    private String address;
    private double latitude;
    private double longitude;
    private String label;
    private int imageResourceId;

    public SelectedLocation() {
        super();
    }

    public SelectedLocation(String place, String address, double latitude, double longitude,
                            String label, int imageResourceID) {
        //super(place, imageResourceID);
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPreferenceKey() {
        return this.place;
    }

    public int getImageResourceId() { return this.imageResourceId; }
}
