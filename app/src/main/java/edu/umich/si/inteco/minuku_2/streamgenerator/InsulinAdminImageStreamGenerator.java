package edu.umich.si.inteco.minuku_2.streamgenerator;

import android.content.Context;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.UserPreferences;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.streamgenerator.AnnotatedImageStreamGenerator;
import edu.umich.si.inteco.minuku_2.dao.InsulinAdminImageDAO;
import edu.umich.si.inteco.minuku_2.model.GlucoseReadingImage;
import edu.umich.si.inteco.minuku_2.model.InsulinAdminImage;
import edu.umich.si.inteco.minuku_2.stream.InsulinAdminImageStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.event.NoDataChangeEvent;

/**
 * Created by shriti on 7/31/16.
 */
public class InsulinAdminImageStreamGenerator extends
        AnnotatedImageStreamGenerator<InsulinAdminImage> {

    private String TAG = "InsulinAdminImageStreamGen";
    private InsulinAdminImageStream insulinAdminImageStream;
    private InsulinAdminImageDAO insulinAdminImageDAO;

    public InsulinAdminImageStreamGenerator(Context applicationContext) {
        super(applicationContext,InsulinAdminImage.class);
        this.insulinAdminImageStream = new InsulinAdminImageStream(Constants.DEFAULT_QUEUE_SIZE);
        this.insulinAdminImageDAO = MinukuDAOManager.getInstance().getDaoFor(InsulinAdminImage.class);
        this.register();
    }

    @Override
    public boolean updateStream() {
        Log.d(TAG,
                "Update Stream called: The preference values are - \n" +
                        UserPreferences.getInstance().getPreferenceSet("insulinTimes") + "\n" +
                        UserPreferences.getInstance().getPreference("endTime"));
        MinukuStreamManager.getInstance().handleNoDataChangeEvent(
                new NoDataChangeEvent(InsulinAdminImage.class));
        return true;
    }

    @Override
    public long getUpdateFrequency() {
        return Constants.IMAGE_STREAM_GENERATOR_UPDATE_FREQUENCY_MINUTES;
    }

    @Override
    public void offer(InsulinAdminImage anImage) {
        try {
            //add to stream
            Log.d(TAG, "Adding to stream in the offer method");
            insulinAdminImageStream.add(anImage);
            //add to database
            insulinAdminImageDAO.add(anImage);
        } catch (DAOException e){
            e.printStackTrace();
        }
    }
}
