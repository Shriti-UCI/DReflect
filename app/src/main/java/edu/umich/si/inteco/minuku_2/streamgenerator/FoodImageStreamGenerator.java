package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;

import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.model.FoodImage;

/**
 * Created by shriti on 7/31/16.
 */
public class FoodImageStreamGenerator extends AnnotatedImageStreamGenerator<FoodImage> {

    public FoodImageStreamGenerator(Context applicationContext) {
        super(applicationContext, FoodImage.class);
    }
}
