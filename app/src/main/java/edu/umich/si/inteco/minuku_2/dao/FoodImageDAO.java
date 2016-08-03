package edu.umich.si.inteco.minuku_2.dao;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku_2.model.FoodImage;

/**
 * Created by shriti on 7/31/16.
 */
public class FoodImageDAO extends AnnotatedImageDataRecordDAO<FoodImage> {

    public FoodImageDAO() {
        super(FoodImage.class);
    }
}
