package edu.umich.si.inteco.minukucore.model.question;

/**
 * A multiple choice question gives users a specific question with a list of possible answers the
 * user needs to choose from.
 * @see <a href="https://en.wikipedia.org/wiki/Multiple_choice"> Wiki on Multiple Choice</a>.
 *
 * Created by neerajkumar on 7/13/16.
 */
public class MultipleChoice extends Question {

    private int numChoices;
    private String[] labels;
    private Integer[] selectedAnswerValues;

    public MultipleChoice () {

    }

    /**
     *
     * @param aQuestion The question to show.
     * @param aNumChoices The number of choices this MCQ will have.
     * @param aChoiceValues The label(s) for each choice.
     */
    public MultipleChoice(String aQuestion, int aNumChoices, String[] aChoiceValues) {
        super(aQuestion);
        numChoices = aNumChoices;
        labels = aChoiceValues;
    }

    /**
     *
     * @return The number of choices.
     */
    public int getNumChoices() {
        return numChoices;
    }

    /**
     *
     * @return The labels for choices.
     */
    public String[] getLabels() {
        return labels;
    }

    /**
     *
     * @return The answer selected by the user.
     */
    public Integer[] getSelectedAnswerValues() {
        return selectedAnswerValues;
    }

    /**
     * Sets the answer value.
     * @param aAnswerValue The answer selected by the user.
     */
    public void setSelectedAnswerValues(Integer[] aAnswerValue) {
        this.selectedAnswerValues = aAnswerValue;
    }
}
