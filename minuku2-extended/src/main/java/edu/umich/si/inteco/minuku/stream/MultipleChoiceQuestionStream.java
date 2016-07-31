package edu.umich.si.inteco.minuku.stream;

import java.util.ArrayList;
import java.util.List;

import edu.umich.si.inteco.minukucore.model.DataRecord;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.stream.AbstractStreamFromQuestion;

/**
 * Created by shriti on 7/31/16.
 */
public class MultipleChoiceQuestionStream extends AbstractStreamFromQuestion<FreeResponse> {

    public MultipleChoiceQuestionStream(int maxSize) {
        super(maxSize);
    }

    @Override
    public List<Class<? extends DataRecord>> dependsOnDataRecordType() {
        return new ArrayList<>();
    }
}
