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

    /**
     * Register itself to the StreamManager - calls the register method of StreamManager
     * Get the source/input streams
     * @see edu.umich.si.inteco.minukucore.manager.StreamManager
     */
    public void register();

    /**
     * Get transformed stream from a raw stream
     * @return the semantic/transformed stream
     */
    public Stream<T> generateNewStream();

    /**
     * Whenever a new DataRecord comes in, add the new entry to update the stream.
     * This will be called every 1 sec.
     * @return true if the stream is updated, false otherwise
     * @throws edu.umich.si.inteco.minukucore.exception.StreamNotFoundException
     */
    public boolean updateStream();

    /**
     * Fetch the sampling rate of the stream - could be same as the
     * sampling rate of the underlying DataRecord - unit is seconds
     * @return the sampling rate of the data underlying the stream
     */
    public long getUpdateFrequency();

    /**
     * Generate a state change event
     * The state change event will be added to the event bus
     */
    public void sendStateChangeEvent();

    /**
     * Once the new stream is registered, this method tells the stream generator
     * that the new stream has been registered successfully
     * This is most likely to be used by
     * {@link edu.umich.si.inteco.minukucore.manager.StreamManager},
     * which would be registering all the streams, as and when they are created.
     */
    public void onStreamRegistration();

    //add a method for activity to pass data to stream generator
    public void offer(T dataRecord);

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
