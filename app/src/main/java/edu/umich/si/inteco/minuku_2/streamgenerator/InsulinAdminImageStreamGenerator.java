package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;

import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;

/**
 * Created by shriti on 7/31/16.
 */
public class InsulinAdminImageStreamGenerator extends
        AnnotatedImageStreamGenerator<InsulinAdminImage> {

    public InsulinAdminImageStreamGenerator(Context applicationContext) {
        super(applicationContext, InsulinAdminImage.class);
    }
}
