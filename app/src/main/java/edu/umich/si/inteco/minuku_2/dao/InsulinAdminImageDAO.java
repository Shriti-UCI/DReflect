package edu.umich.si.inteco.minuku_2.dao;

import edu.umich.si.inteco.minuku.dao.AnnotatedImageDataRecordDAO;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;

/**
 * Created by shriti on 7/31/16.
 */
public class InsulinAdminImageDAO extends AnnotatedImageDataRecordDAO<InsulinAdminImage> {

    public InsulinAdminImageDAO() {
        super(InsulinAdminImage.class);
    }
}
