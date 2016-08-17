package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.dao.FoodImageDAO;
import edu.umich.si.inteco.minuku_2.model.FoodImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.stream.FoodImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;

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
