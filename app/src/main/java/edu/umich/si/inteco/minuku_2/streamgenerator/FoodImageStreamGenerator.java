package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.model.FoodImage;

/**
 * Created by shriti on 7/31/16.
 */
public class FoodImageStreamGenerator extends AnnotatedImageStreamGenerator<FoodImage> {

    public FoodImageStreamGenerator(Context applicationContext) {
        super(applicationContext, FoodImage.class);
        TAG = "FoodImageStreamGenerator";
    }

    @Override
    public long getUpdateFrequency() {
        return Constants.FOOD_IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES;
    }
}
