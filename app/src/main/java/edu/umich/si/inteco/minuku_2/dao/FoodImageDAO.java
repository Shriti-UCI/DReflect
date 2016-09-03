package edu.umich.si.inteco.minuku_2.dao;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;

/**
 * Created by shriti on 7/31/16.
 */
public class FoodImageDAO extends AnnotatedImageDataRecordDAO<FoodImage> {

    protected static String TAG = "FoodImageDAO";

    public FoodImageDAO() {
        super(FoodImage.class, ApplicationConstants.IMAGE_TYPE_FOOD);
    }
}
