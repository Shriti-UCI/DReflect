package edu.umich.si.inteco.minuku_2;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;


import edu.umich.si.inteco.minuku.logger.Log;
import edu.umich.si.inteco.minuku_2.dao.EODQuestionAnswerDAO;
import edu.umich.si.inteco.minuku_2.model.EODQuestionDataRecord;
import edu.umich.si.inteco.minuku_2.preferences.ApplicationConstants;
import edu.umich.si.inteco.minukucore.dao.DAOException;

/**
 * Created by shriti on 10/12/16.
 */

public class EODQuestionsActivity extends BaseActivity {

    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    EODQuestionDataRecord eodQuestionDataRecord = new EODQuestionDataRecord();
    String TAG = "EODQuestionsActivity";
    EODQuestionAnswerDAO eodQuestionAnswerDAO = new EODQuestionAnswerDAO();

    final int NEXT = 1;
    final int PREVIOUS = 2;
    final int SUBMIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eod_questions);
        viewPager = (ViewPager)findViewById(R.id.myviewpager);
        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        Log.d(TAG, " map: " + eodQuestionDataRecord);

    }

    private class MyPagerAdapter extends PagerAdapter{

        int NumberOfPages = 2;

        String[] titles = {ApplicationConstants.EOD_QUESTIONS_TITLE_2,
                ApplicationConstants.EOD_QUESTIONS_TITLE_3};

        /**HashMap<String, List<String>> questions = new HashMap<String, List<String>>() {{
            put(titles[0], Arrays.asList("Did anything good or bad happen today?"));
            put(titles[1], Arrays.asList("Did anything good or bad happen today?",
                    "Was there anything hard about managing diabetes today?"));

        }};**/

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            switch (position) {
                case 0:
                    //create a layout
                    LinearLayout layout_one = new LinearLayout(EODQuestionsActivity.this);
                    layout_one.setOrientation(LinearLayout.VERTICAL);
                    LayoutParams layoutOneParams = new LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    layout_one.setLayoutParams(layoutOneParams);
                    //write title two
                    TextView textView_one = new TextView(EODQuestionsActivity.this);
                    textView_one.setTextColor(Color.BLACK);
                    textView_one.setTextSize(18);
                    textView_one.setTypeface(Typeface.DEFAULT_BOLD);
                    textView_one.setText(ApplicationConstants.EOD_QUESTIONS_TITLE_2);
                    //create question two
                    TextView textView_two = new TextView(EODQuestionsActivity.this);
                    textView_two.setTextColor(Color.BLACK);
                    textView_two.setTextSize(14);
                    textView_two.setTypeface(Typeface.DEFAULT);
                    textView_two.setText(ApplicationConstants.EOD_QUESTION_TWO_LIFE_EVENTS);

                    final EditText editText_one = new EditText(EODQuestionsActivity.this);
                    LayoutParams editTextOneParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    editText_one.setLayoutParams(editTextOneParams);
                    editText_one.setHeight(200);

                    Button nextButton_one = new Button(EODQuestionsActivity.this);
                    nextButton_one.setText("Next");
                    nextButton_one.setId(NEXT);
                    RelativeLayout.LayoutParams nextButtonParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    nextButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    nextButton_one.setLayoutParams(nextButtonParams);

                    Button previousButton_one = new Button(EODQuestionsActivity.this);
                    previousButton_one.setText("Back");
                    previousButton_one.setId(PREVIOUS);
                    RelativeLayout.LayoutParams previousButtonParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    previousButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    previousButton_one.setLayoutParams(previousButtonParams);

                    RelativeLayout buttonLayout = new RelativeLayout(EODQuestionsActivity.this);
                    LayoutParams buttonLayoutParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    buttonLayout.setLayoutParams(buttonLayoutParams);

                    buttonLayout.addView(previousButton_one);
                    buttonLayout.addView(nextButton_one);

                    layout_one.addView(textView_one);
                    layout_one.addView(textView_two);
                    layout_one.addView(editText_one);
                    layout_one.addView(buttonLayout);

                    final int page = position;
                    nextButton_one.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String answer_two = editText_one.getText().toString();
                            if(answer_two==null || answer_two=="" || answer_two.trim()==""){
                                eodQuestionDataRecord.setEODAnswerTwo("");
                            }
                            else {
                                eodQuestionDataRecord.setEODAnswerTwo(answer_two);
                            }
                            viewPager.setCurrentItem(1);
                            Toast.makeText(EODQuestionsActivity.this,
                                    "Page " + page + " clicked",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    container.addView(layout_one);
                    return layout_one;
                case 1:
                    LinearLayout layout_two = new LinearLayout(EODQuestionsActivity.this);
                    layout_two.setOrientation(LinearLayout.VERTICAL);
                    LayoutParams layoutTwoParams = new LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    layout_two.setLayoutParams(layoutTwoParams);

                    TextView textView_three = new TextView(EODQuestionsActivity.this);
                    textView_three.setTextColor(Color.BLACK);
                    textView_three.setTextSize(18);
                    textView_three.setTypeface(Typeface.DEFAULT_BOLD);
                    textView_three.setText(ApplicationConstants.EOD_QUESTIONS_TITLE_3);

                    TextView textView_four = new TextView(EODQuestionsActivity.this);
                    textView_four.setTextColor(Color.BLACK);
                    textView_four.setTextSize(14);
                    textView_four.setTypeface(Typeface.DEFAULT);
                    textView_four.setText(ApplicationConstants.EOD_QUESTION_THREE_DIABETES_EVENTS);

                    final EditText editText_three = new EditText(EODQuestionsActivity.this);
                    LayoutParams editTextThreeParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    editText_three.setLayoutParams(editTextThreeParams);
                    editText_three.setHeight(200);

                    TextView textView_five = new TextView(EODQuestionsActivity.this);
                    textView_five.setTextColor(Color.BLACK);
                    textView_five.setTextSize(14);
                    textView_five.setTypeface(Typeface.DEFAULT);
                    textView_five.setText(ApplicationConstants.EOD_QUESTION_FOUR_DIABETES_EVENTS);

                    final EditText editText_four = new EditText(EODQuestionsActivity.this);
                    LayoutParams editTextFourParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    editText_four.setLayoutParams(editTextFourParams);
                    editText_four.setHeight(200);

                    Button nextButton_two = new Button(EODQuestionsActivity.this);
                    nextButton_two.setText("Submit");
                    nextButton_two.setId(SUBMIT);
                    RelativeLayout.LayoutParams nextButtonTwoParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    nextButtonTwoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    nextButton_two.setLayoutParams(nextButtonTwoParams);

                    Button previousButton_two = new Button(EODQuestionsActivity.this);
                    previousButton_two.setText("Back");
                    previousButton_two.setId(PREVIOUS);
                    RelativeLayout.LayoutParams previousButtonTwoParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    previousButtonTwoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    previousButton_two.setLayoutParams(previousButtonTwoParams);

                    RelativeLayout buttonLayoutTwo = new RelativeLayout(EODQuestionsActivity.this);
                    LayoutParams buttonLayoutTwoParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    buttonLayoutTwo.setLayoutParams(buttonLayoutTwoParams);

                    buttonLayoutTwo.addView(previousButton_two);
                    buttonLayoutTwo.addView(nextButton_two);

                    layout_two.addView(textView_three);
                    layout_two.addView(textView_four);
                    layout_two.addView(editText_three);
                    layout_two.addView(textView_five);
                    layout_two.addView(editText_four);
                    layout_two.addView(buttonLayoutTwo);

                    final int page_ = position;
                    previousButton_two.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(0);
                            Toast.makeText(EODQuestionsActivity.this,
                                    "Page " + page_ + " clicked",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    nextButton_two.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String answer_three = editText_three.getText().toString();
                            String answer_four = editText_four.getText().toString();

                            if(answer_three==null || answer_three=="" || answer_three.trim()==""){
                                eodQuestionDataRecord.setEODAnswerThree("");
                            }
                            else {
                                eodQuestionDataRecord.setEODAnswerThree(answer_three);
                            }

                            if(answer_four==null || answer_four=="" || answer_four.trim()==""){
                                eodQuestionDataRecord.setEODAnswerFour("");
                            }
                            else {
                                eodQuestionDataRecord.setEODAnswerFour(answer_four);
                            }

                            try {
                                eodQuestionAnswerDAO.add(eodQuestionDataRecord);
                            } catch (DAOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(EODQuestionsActivity.this,
                                    "Adding to database",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    container.addView(layout_two);
                    return layout_two;
                default:
                    return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }

    }


}