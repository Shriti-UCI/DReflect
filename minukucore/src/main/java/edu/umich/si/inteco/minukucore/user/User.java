package edu.umich.si.inteco.minukucore.user;

/**
 * User is a well defined entity which will have a first name,
 * a last name, email and a set of devices that belong to the user
 * and have the Minuku app installed.
 *
 * Created by Neeraj Kumar on 7/12/2016.
 */
public class User {
    String firstName;
    String lastName;
    String email;

    public User() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
    }

    public User(String aFirstName, String aLastName, String aEmail) {
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.email = aEmail;
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
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }


    /**
     *
     * @return User's email.
     */
    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return this.firstName + ";" + this.lastName + ";" + this.email;
    }
}
