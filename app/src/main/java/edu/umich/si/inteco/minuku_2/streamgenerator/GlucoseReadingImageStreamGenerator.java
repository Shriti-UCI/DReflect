package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;
import android.content.pm.ComponentInfo;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.model.MoodDataRecord;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minukucore.event.IsDataExpectedEvent;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;

/**
 * Created by shriti on 7/31/16.
 */
public class GlucoseReadingImageStreamGenerator extends
        AnnotatedImageStreamGenerator<GlucoseReadingImage> {

    String TAG = "GlucoseReadingImageStreamGenerator";
    public GlucoseReadingImageStreamGenerator(Context applicationContext) {
        super(applicationContext, GlucoseReadingImage.class);
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
}
