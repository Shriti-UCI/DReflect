package edu.umich.si.inteco.minuku.stream;

import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromUser;

/**
 * Created by shriti on 7/21/16.
 */
public class MoodStream extends AbstractStreamFromUser {

    public MoodStream(int maxSize) {

        super(maxSize);
    }
    @Override
    public List<Class> dependsOnDataRecord() {
        return new ArrayList<>();
    }
}
