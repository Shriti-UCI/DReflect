package edu.umich.si.inteco.minuku_2.dao;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;

/**
 * Created by shriti on 7/31/16.
 */
public class GlucoseReadingImageDAO extends AnnotatedImageDataRecordDAO<GlucoseReadingImage> {

    protected static String TAG = "FoodImageDAO";

    public GlucoseReadingImageDAO() {
        super(GlucoseReadingImage.class, ApplicationConstants.IMAGE_TYPE_GLUCOSE_READIMG);
    }

}
