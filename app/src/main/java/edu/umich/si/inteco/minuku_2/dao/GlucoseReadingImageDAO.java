package edu.umich.si.inteco.minuku_2.dao;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;

/**
 * Created by shriti on 7/31/16.
 */
public class GlucoseReadingImageDAO extends AnnotatedImageDataRecordDAO<GlucoseReadingImage> {

    public GlucoseReadingImageDAO() {
        super(GlucoseReadingImage.class);
    }
}
