package edu.umich.si.inteco.minukucore.stream;

import edu.umich.si.inteco.minukucore.exception.QuestionNotFoundException;
import edu.umich.si.inteco.minukucore.model.question.Question;

/**
 * Created by shriti on 7/19/16.
 */
public abstract class AbstractStreamFromQuestion<T extends Question> extends AbstractStream<T> {

    public AbstractStreamFromQuestion(int maxSize) {
        super(maxSize);
    }

    @Override
    public StreamType getType() {
        return StreamType.FROM_QUESTION;
    }

    public T getLastDataFor(int questionID) throws QuestionNotFoundException {

        for (T question:this) {

            if(questionID == question.getID()) {
                return question;
            }
        }
        throw new QuestionNotFoundException();
    }
}
