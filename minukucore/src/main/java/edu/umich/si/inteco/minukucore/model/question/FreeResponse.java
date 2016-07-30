package edu.umich.si.inteco.minukucore.model.question;

/**
 * Free response question has a question string and a free response answer that the user provides.
 *
 * Created by neerajkumar on 7/13/16.
 */
public class FreeResponse extends Question {

    private String answer;

    public FreeResponse () {

    }

    /**
     *
     * @param aQuestion Question to be shown to the user.
     */
    public FreeResponse(String aQuestion) {
        super(aQuestion);
    }

    /**
     *
     * @return The answer provided by the user.
     */
    public String getAnswer() {

        return answer;
    }

    public void setAnswer(String answer) {

        this.answer = answer;
    }
}
