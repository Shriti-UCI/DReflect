package edu.umich.si.inteco.minuku_2.dao;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;

/**
 * Created by shriti on 7/31/16.
 */
public class InsulinAdminImageDAO extends AnnotatedImageDataRecordDAO<InsulinAdminImage> {

    protected static String TAG = "FoodImageDAO";

    public InsulinAdminImageDAO() {
        super(InsulinAdminImage.class, ApplicationConstants.IMAGE_TYPE_INSULIN_SHOT);
    }
}
