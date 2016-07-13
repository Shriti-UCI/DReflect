package edu.umich.si.inteco.minukucore.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User is a well defined entity which will have a first name,
 * a last name, email and a set of devices that belong to the user
 * and have the Minuku app installed.
 *
 * Created by Neeraj Kumar on 7/12/2016.
 */
public abstract class User {
    private String firstName;
    private String lastName;
    private List<UUID> deviceIDs;
    private String email;

    public User(String aFirstName, String aLastName, String aEmail) {
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.email = aEmail;
        this.deviceIDs = new ArrayList<UUID>(2);
    }

    /**
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName The first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName The last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return List of device IDs associated with the User.
     */
    public List<UUID> getDeviceIDs() {
        return deviceIDs;
    }

    /**
     *
     * @return User's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email User's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Add a device to the list of devices associated with this user.
     *
     * @param deviceID
     */
    public void addDevice(UUID deviceID) {
        this.deviceIDs.add(deviceID);
    }
}
