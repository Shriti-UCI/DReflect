package edu.umich.si.inteco.minuku.streamgenerator;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.config.LocationPreference;
import edu.umich.si.inteco.minuku.config.SelectedLocation;
import edu.umich.si.inteco.minuku.dao.SemanticLocationDataRecordDAO;
import edu.umich.si.inteco.minuku.manager.MinukuDAOManager;
import edu.umich.si.inteco.minuku.model.LocationDataRecord;
import edu.umich.si.inteco.minuku.model.SemanticLocationDataRecord;
import edu.umich.si.inteco.minuku.stream.LocationStream;
import edu.umich.si.inteco.minuku.stream.SemanticLocationStream;
import edu.umich.si.inteco.minukucore.dao.DAOException;
import edu.umich.si.inteco.minukucore.manager.DAOManager;
import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromDevice;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by neerajkumar on 7/21/16.
 */
public class SemanticLocationStreamGenerator
        extends AndroidStreamGenerator<SemanticLocationDataRecord> {

    private SemanticLocationStream mStream;
    private String TAG = "SemanticLocationStreamGenerator";
    private SemanticLocationDataRecordDAO mDAO;




    public SemanticLocationStreamGenerator(Context applicationContext) {
        super(applicationContext);
        mStream = new SemanticLocationStream(Constants.LOCATION_QUEUE_SIZE);
        mDAO = MinukuDAOManager.getInstance().getDaoFor(SemanticLocationDataRecord.class);

        // A potential subscriber must register to the event bus before calling subscribe
        // on it.
        EventBus.getDefault().register(this);
    }

    @Override
    public void register() {

    }

    @Override
    public Stream<SemanticLocationDataRecord> generateNewStream() {
        return mStream;
    }

    @Override
    public boolean updateStream() {
        return false;
    }

    @Override
    public long getUpdateFrequency() {
        return 0;
    }

    @Override
    public void sendStateChangeEvent() {

    }

    @Override
    public void onStreamRegistration() {
        Log.d(TAG, "Stream " + TAG + " registered successfully");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Log.d(TAG, "Stream " + TAG + "initialized from previous state");
                    Future<List<SemanticLocationDataRecord>> listFuture =
                            mDAO.getLast(Constants.DEFAULT_QUEUE_SIZE);
                    while(!listFuture.isDone()) {
                        Thread.sleep(1000);
                    }
                    Log.d(TAG, "Received data from Future for " + TAG);
                    mStream.addAll(listFuture.get());
                } catch (DAOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void offer(SemanticLocationDataRecord dataRecord) {
        // do nothing. The only way data records can be added to semantic location is via
        // the onLocationDataChangeEvent
    }

    // This is how one streamgenerator listens to another stream generators events
    // The event bus is class-agnostic, i.e. any POJO can be passed as an event object.
    @Subscribe
    public void onLocationDataChangeEvent(LocationDataRecord d) {
        try {
            mDAO.add(convertToSemanticLocation(d));
        } catch (DAOException e) {
            e.printStackTrace();
            Log.e(TAG, "There was an error adding the semantic location data record." +
                    Log.getStackTraceString(e));

        }
    }

    private SemanticLocationDataRecord convertToSemanticLocation(
            LocationDataRecord aLocationDataRecord) {
        for(SelectedLocation selectedLocation: LocationPreference.getInstance().getLocations()) {
            Location loc1 = new Location("");
            loc1.setLatitude(aLocationDataRecord.getLatitude());
            loc1.setLongitude(aLocationDataRecord.getLongitude());

            Location loc2 = new Location("");
            loc2.setLatitude(selectedLocation.getLatitude());
            loc2.setLongitude(selectedLocation.getLongitude());

            if(loc1.distanceTo(loc2) < 50) {
                return new SemanticLocationDataRecord(selectedLocation.getLabel());
            }
        }
        return new SemanticLocationDataRecord("unknown");
    }
}
