package edu.umich.si.inteco.minukucore.streamgenerator;

import android.content.Context;

import java.util.List;

import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.stream.Stream;

/**
 * Created by shriti on 7/9/16.
 * Example stream generator code:
 * public class SemanticLocationStreamGenerator<SemanticLocationData> extends AbstractStreamGenerator {
 *
 *     private Stream<LocationData> locationDataStream;
 *     private Stream<ActivityData> activityDataStream;
 *
 *     public SemanticLocationStreamGenerator() {
 *      try {
 *         locationDataStream = StreamManager.getStream(LocationData.class);
 *         activityDataStream = StreamManager.getStream(ActivityData.class);
 *         SteamManager.register(this, SemanticLocationData.class);
 *       } catch(StreamDoesNotExistError e) {
 *          // do nothing
 *       }
 *     }
 *
 * }
 *
 * public abstract class AbstractStreamGenerator<T extends DataRecord> {
 *     public AbstractStreamGenerator() {
 *         register();
 *     }
 *
 *     protected List<Stream> dependsOnStream();
 *
 *     protected final void register() {
 *         try {
 *
 *            for(Stream s: dependsOnStreams()) {
 *              StreamManager.getInstance.getStreamFor(s);
 *            }
 *
 *            ApplicationContext c = StreamManager.getInstance().register(T.class, this);
 *            onContextReceived(c);
 *
 *         } catch(StreamNotFoundException e) {
 *
 *         } catch(StreamAlreadyExistsException e) {
 *
 *         }
 *     }
 *
 *     public Stream<T> generateNewStream() {
 *
 *     }
 *
 *     public void onContextReceived(ApplicationContext c) {
 *       return;
 *     }
 *}
 */
public interface StreamGenerator<T extends DataRecord> {

    public void register();

    public Stream<T> generateNewStream();

    public boolean updateStream();

    public long getUpdateFrequency();

    public void sendStateChangeEvent();

    public void onStreamRegistration();

    public List<Stream> dependsOnStreams();
}

/**
 * class LocationDataRecord extend Data {
 *
 * }
 *
 * class LocationStreamGenerator<LocationDataRecord> extends AbstractStreamGenerator implement LocationListener {
 *
 *     private Stream<LocationDataRecord> mLocationDataRecordStream;
 *
 *     // If this returns  0, the StreamManager does not call updateStream()
 *     // else calls updateStream after every X seconds
 *     public uint getUpdateFrequency() {
 *         return 10; // 10 seconds
 *     }
 *
 *     // This method will be called every 10 seconds
 *     public boolean updateStream() {
 *         LocationDataRecord lDR = new lDR(latestLocation);
 *         mLocationDataRecordSteam.add(lDR);
 *         EventBus.getInstance().trigger(LocationDataRecord);
 *     }
 *
 *     public List<Stream> dependsOnStreams() {
 *      returns new ArrayList<>();
 *     }
 *
 *     public void onContextReceived(ApplicationContext c) {
 *          LocationManager manager = c.getSystemService(Context.LOCATION_SERVICE);
 *          manager.requestLocationUpdate(...., this);
 *     }
 *
 *     @Override
 *     public void onLocationChanged(Location l) {
 *      latestLocation = l;
 *     }
 *
 *
 * }
 *
 * class SemanticLocationStreamGenerator<SemanticLocationDataRecord> extends AbstractStreamGenerator {
 *     private Stream<SemanticLocationDataRecord> mSemanticLocationDataRecord;
 *     private Stream<LocationDataRecord> mLocationDataRecord;
 *     private Stream<MoodDataRecord> mMoodDataRecord;
 *
 *     public long getUpdateFrequency() {
 *      return -1;
 *     }
 *
 *     @Subscribe(LocationDataEvent.class)
 *     public void locationDataRecordChangeHandler(LocationDataRecord r) {
 *      StreamManager.getStreamFor(LocationDataRecord);
 *      // do some magic
 *      EventBus.getInstance().trigger(SemanticLocationDataRecord);
 *     }
 *
 *     protected void beforeRegister() throws StreamNotFoundException, StreamAlreadyExistsException {
 *         mLocationDataRecord = StreamManager.getInstance().getStreamFor(LocationDataRecord.class);
 *         mMoodDataRecord = StreamManager.getInstance().getStreamFor(MoodDataRecord.class);
 *
 *         if(StreamManager.getInstance().getStreamFor(SemanticLocationDataRecord.class)) != null) {
 *          throw new StreamAlreadyExistsException();
 *         }
 *     }
 * }
 */
