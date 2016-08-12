package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;
import android.content.pm.ComponentInfo;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.stream.AnnotatedImageStream;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.dao.GlucoseReadingImageDAO;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.stream.GlucoseReadingImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.IsDataExpectedEvent;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;

/**
 * Created by shriti on 7/31/16.
 */
public class GlucoseReadingImageStreamGenerator extends
        AnnotatedImageStreamGenerator<GlucoseReadingImage> {

    String TAG = "GlucoseReadingImageStreamGenerator";
    private GlucoseReadingImageStream glucoseReadingImageStream;
    private GlucoseReadingImageDAO glucoseReadingImageDAO;

    public GlucoseReadingImageStreamGenerator(Context applicationContext) {
        super(applicationContext, GlucoseReadingImage.class);
        this.glucoseReadingImageStream = new GlucoseReadingImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.glucoseReadingImageDAO = MinukuDAOManager.getInstance().getDaoFor(GlucoseReadingImage.class);
        this.register();
    }

    @Override
    public boolean updateStream() {
        Log.d(TAG,
                "Update Stream called: The preference values are - \n" +
                        UserPreferences.getInstance().getPreferenceSet("gmTimes") + "\n" +
                        UserPreferences.getInstance().getPreference("endTime"));
        MinukuStreamManager.getInstance().handleNoDataChangeEvent(
                new NoDataChangeEvent(GlucoseReadingImage.class));
        return true;
    }

    @Override
    public long getUpdateFrequency() {
        return Constants.IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES;
    }

    @Override
    public void offer(GlucoseReadingImage anImage) {
        try {
            //add to stream
            Log.d(TAG, "Adding to stream in the offer method");
            glucoseReadingImageStream.add(anImage);
            //add to database
            glucoseReadingImageDAO.add(anImage);
        } catch (DAOException e){
            e.printStackTrace();
        }
    }
}
