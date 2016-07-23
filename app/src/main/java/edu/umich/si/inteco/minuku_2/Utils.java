package edu.umich.si.inteco.minuku_2;

/**
 * Created by neera_000 on 3/26/2016.
 *
 * modified by shriti 07/22/2016
 */
public class Utils {

    public static String TAG = "UtilityClass";
    /**
     * Given an email Id as string, encodes it so that it can go into the firebase object without
     * any issues.
     * @param unencodedEmail
     * @return
     */
    public static final String encodeEmail(String unencodedEmail) {
        if (unencodedEmail == null) return null;
        return unencodedEmail.replace(".", ",");
    }

}