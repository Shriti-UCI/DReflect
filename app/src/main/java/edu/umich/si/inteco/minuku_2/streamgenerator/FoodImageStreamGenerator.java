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

    private String TAG = "FoodImageStreamGenerator";
    private FoodImageStream foodImageStream;
    private FoodImageDAO foodImageDAO;
    public FoodImageStreamGenerator(Context applicationContext) {
        super(applicationContext, FoodImage.class);
        this.foodImageStream = new FoodImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.foodImageDAO = MinukuDAOManager.getInstance().getDaoFor(FoodImage.class);
        this.register();
    }

    @Override
    public boolean updateStream() {
        Log.d(TAG,
                "Update Stream called: The update frequency is - \n" +
        Constants.FOOD_IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES);
        MinukuStreamManager.getInstance().handleNoDataChangeEvent(
                new NoDataChangeEvent(FoodImage.class));
        return true;
    }

    @Override
    public long getUpdateFrequency() {
        return Constants.FOOD_IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES;
    }

    @Override
    public void offer(FoodImage anImage) {
        try {
            //add to stream
            Log.d(TAG, "Adding to stream in the offer method");
            foodImageStream.add(anImage);
            //add to database
            foodImageDAO.add(anImage);
        } catch (DAOException e){
            e.printStackTrace();
        }
    }
}
