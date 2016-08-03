package edu.umich.si.inteco.minuku_2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dkharrat.nexusdialog.FormActivity;
import com.github.dkharrat.nexusdialog.FormController;
import com.github.dkharrat.nexusdialog.FormElementController;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;

import java.util.*;

import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;
import edu.umich.si.inteco.minuku.manager.QuestionManager;
import edu.umich.si.inteco.minuku_2.question.QuestionConfig;
import edu.umich.si.inteco.minukucore.exception.StreamNotFoundException;
import edu.umich.si.inteco.minukucore.model.question.FreeResponse;
import edu.umich.si.inteco.minukucore.model.question.MultipleChoice;
import edu.umich.si.inteco.minukucore.model.question.Question;

/**
 * Created by shriti on 7/28/16.
 */
public class QuestionnaireActivity<T extends Question> extends BaseActivity {

    private FormController formController;

    private ImageView acceptButton;
    private ImageView rejectButton;
    
    private Map<T, FormElementController> questionControllerMap;
    private String TAG = "QuestionnaireActivity";

    //FreeResponse q1= new FreeResponse("First Name");
    //FreeResponse q2= new FreeResponse("Last Name");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "creating activity");

        setContentView(R.layout.custom_form);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setupForm(savedInstanceState);

        acceptButton = (ImageView) findViewById(R.id.acceptButton);
        rejectButton = (ImageView) findViewById(R.id.rejectButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    acceptResults();
                } catch (StreamNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectResults();
            }
        });

    }

    protected void setupForm(Bundle savedInstanceState) {
        formController = new FormController(this);

        Log.d(TAG, "creating form");
        //get questionnaire ID from bundle
        //QuestionManager.getInstance().getQuestionnaireForID(ID)
        //
        FormSectionController section = new FormSectionController(this, "Personal Info");
        QuestionConfig.getInstance().setUpQuestions(getApplicationContext());
        questionControllerMap = QuestionManager.getInstance().getQuestionFormControllerMap();
        for (Map.Entry<T, FormElementController> entry:questionControllerMap.entrySet()) {
            section.addElement(entry.getValue());
        }
        //section.addElement(new EditTextController(this, "firstName", "First name"));
        //section.addElement(new EditTextController(this, "lastName", "Last name"));
        formController.addSection(section);

        ViewGroup containerView = (ViewGroup)findViewById(R.id.form_elements_container);
        formController.recreateViews(containerView);
        Log.d(TAG, "creating activity");

    }

    public void acceptResults() throws StreamNotFoundException {

        //Object firstName = formController.getModel().getValue();
        //Object lastName = formController.getModel().getValue("lastName");

        for (Map.Entry<T, FormElementController> entry:questionControllerMap.entrySet()) {
            T question = entry.getKey();
            Object answer = formController.getModel().getValue(String.valueOf(question.getID()));
            if(question instanceof FreeResponse) {
                ((FreeResponse) question).setAnswer(answer.toString());
                MinukuStreamManager.getInstance()
                        .getStreamGeneratorFor(FreeResponse.class)
                        .offer((FreeResponse) question);
            }
            if(question instanceof MultipleChoice) {
                Log.d(TAG, answer.toString());
                Set<Integer> answerSet = (HashSet<Integer>) answer;
                List<Integer> answers = new ArrayList<Integer>();
                for(Object someAnswer: answerSet.toArray()) {
                    answers.add(Integer.valueOf((String)someAnswer));
                }
                ((MultipleChoice) question).setAnswerValue(answers.toArray(new Integer[0]));
                MinukuStreamManager.getInstance()
                        .getStreamGeneratorFor(MultipleChoice.class)
                        .offer((MultipleChoice) question);
            }
        }
        //q1.setAnswer(firstName.toString());
        //q2.setAnswer(lastName.toString());

        /*try {

            MinukuStreamManager.getInstance().getStreamGeneratorFor(FreeResponse.class).offer(q1);
            MinukuStreamManager.getInstance().getStreamGeneratorFor(FreeResponse.class).offer(q2);
        } catch (StreamNotFoundException e) {
            e.printStackTrace();
        }*/
        showToast("Your answer has been recorded");
        finish();
    }

    /**
     * This is called when the user presses the "X" button the screen.
     */
    public void rejectResults() {
        showToast("Going back to home screen");
        finish();
    }
}
