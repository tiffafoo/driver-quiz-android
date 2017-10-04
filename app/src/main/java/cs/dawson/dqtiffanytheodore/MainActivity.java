package cs.dawson.dqtiffanytheodore;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import cs.dawson.dqtiffanytheodore.entities.Question;

/**
 * Driver Quiz App which asks four questions to a user,
 * giving them the choice to select one of four images
 * that correspond to the description.
 *
 * @author Tiffany Le-Nguyen <sirMerr>
 * @author Theodore Accos-Thomas <theoathomas>
 */
public class MainActivity extends AppCompatActivity {
    // Global vars
    String TAG = "MainActivity Class: "; // tag for Logging
    TextView tvQuizNumber, tvDefinition, tvCorrectScore, tvIncorrectScore;
    Button bHint, bAbout, bNext;
    ImageButton image1, image2, image3, image4;
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Question> usedQuestions = new ArrayList<>();
    Question currQuestion;
    int quizNumber = 1;
    int position;
    int rightPointsCtr = 0;
    int wrongPointsCtr = 0;
    int attempts = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Get handles to fields
        tvQuizNumber = (TextView) findViewById(R.id.tvQuizNumber);
        tvDefinition = (TextView) findViewById(R.id.tvDefinition);
        tvCorrectScore = (TextView) findViewById(R.id.tvCorrectScore);
        tvIncorrectScore = (TextView) findViewById(R.id.tvIncorrectScore);

        bHint = (Button) findViewById(R.id.buttonHint);
        bAbout = (Button) findViewById(R.id.buttonAbout);
        bNext = (Button) findViewById(R.id.buttonCheckedAnswer);

        image1 = (ImageButton) findViewById(R.id.imageView1);
        image2 = (ImageButton) findViewById(R.id.imageView2);
        image3 = (ImageButton) findViewById(R.id.imageView3);
        image4 = (ImageButton) findViewById(R.id.imageView4);

        if (savedInstanceState != null) {
            // Get the saved values from the Bundle
            quizNumber = savedInstanceState.getInt("quizNumber");
            position = savedInstanceState.getInt("position");
            rightPointsCtr = savedInstanceState.getInt("rightPointsCtr");
            wrongPointsCtr = savedInstanceState.getInt("wrongPointsCtr");
            attempts = savedInstanceState.getInt("attempts");

            tvQuizNumber.setText(quizNumber);
            tvCorrectScore.setText(rightPointsCtr);

            // Temporary
            setQuestions();
            image1.setImageResource(getRandomQuestion().getImageLink());
            image2.setImageResource(getRandomQuestion().getImageLink());
            image3.setImageResource(getRandomQuestion().getImageLink());
            image4.setImageResource(getRandomQuestion().getImageLink());
        } else {
            // Initiate questions
            setQuestions();

            // Initialize layout
            currQuestion = getRandomQuestion();
            tvDefinition.setText(currQuestion.getDefinition());
            tvQuizNumber.setText(Integer.toString(quizNumber));

            // Ensure that correct answer is never in the same place at startup
            Random random = new Random();
            position = random.nextInt(4) + 1;

            Log.i(TAG, "Selected position :" + position);

            switch(position) {
                case 1:
                    image1.setImageResource(currQuestion.getImageLink());
                    image2.setImageResource(getRandomQuestion().getImageLink());
                    image3.setImageResource(getRandomQuestion().getImageLink());
                    image4.setImageResource(getRandomQuestion().getImageLink());
                    break;
                case 2:
                    image2.setImageResource(currQuestion.getImageLink());
                    image1.setImageResource(getRandomQuestion().getImageLink());
                    image3.setImageResource(getRandomQuestion().getImageLink());
                    image4.setImageResource(getRandomQuestion().getImageLink());
                    break;
                case 3:
                    image3.setImageResource(currQuestion.getImageLink());
                    image2.setImageResource(getRandomQuestion().getImageLink());
                    image1.setImageResource(getRandomQuestion().getImageLink());
                    image4.setImageResource(getRandomQuestion().getImageLink());
                    break;
                case 4:
                    image4.setImageResource(currQuestion.getImageLink());
                    image2.setImageResource(getRandomQuestion().getImageLink());
                    image3.setImageResource(getRandomQuestion().getImageLink());
                    image1.setImageResource(getRandomQuestion().getImageLink());
                    break;
                default: break;
            }
        }
    }

    /**
     * Initiates the questions array with road sign
     * images from res/drawable
     */
    private void setQuestions() {
        // Temporary
        questions.clear();
        questions.add(new Question(R.drawable.sign1, getResources().getString(R.string.definition1), getResources().getString(R.string.hint1)));
        questions.add(new Question(R.drawable.sign2, getResources().getString(R.string.definition2), getResources().getString(R.string.hint2)));
        questions.add(new Question(R.drawable.sign3, getResources().getString(R.string.definition3), getResources().getString(R.string.hint3)));
        questions.add(new Question(R.drawable.sign4, getResources().getString(R.string.definition4), getResources().getString(R.string.hint4)));
        questions.add(new Question(R.drawable.sign5, getResources().getString(R.string.definition5), getResources().getString(R.string.hint5)));
        questions.add(new Question(R.drawable.sign6, getResources().getString(R.string.definition6), getResources().getString(R.string.hint6)));
        questions.add(new Question(R.drawable.sign7, getResources().getString(R.string.definition7), getResources().getString(R.string.hint7)));
        questions.add(new Question(R.drawable.sign8, getResources().getString(R.string.definition8), getResources().getString(R.string.hint8)));
        questions.add(new Question(R.drawable.sign9, getResources().getString(R.string.definition9), getResources().getString(R.string.hint9)));
        questions.add(new Question(R.drawable.sign10, getResources().getString(R.string.definition10), getResources().getString(R.string.hint10)));
        questions.add(new Question(R.drawable.sign11, getResources().getString(R.string.definition11), getResources().getString(R.string.hint11)));
        questions.add(new Question(R.drawable.sign12, getResources().getString(R.string.definition12), getResources().getString(R.string.hint12)));
        questions.add(new Question(R.drawable.sign13, getResources().getString(R.string.definition13), getResources().getString(R.string.hint13)));
        questions.add(new Question(R.drawable.sign14, getResources().getString(R.string.definition14), getResources().getString(R.string.hint14)));
        questions.add(new Question(R.drawable.sign15, getResources().getString(R.string.definition15), getResources().getString(R.string.hint15)));
    }

    /**
     * Gets a random Question
     * @return Question
     */
    private Question getRandomQuestion() {
        Random random = new Random();
        Question question = questions.remove(random.nextInt(questions.size()));
        usedQuestions.add(question);
        return question;
    }

    /**
     * Handles an image click, determines the position and
     * checks if it corresponds to the correct definition
     * @param view
     */
    public void imageClick(View view) {
        ImageButton selectedImage = (ImageButton) findViewById(view.getId());
        int chosenPosition = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(9));

        if(chosenPosition == position) {
            // Increment and update correct answer counter views
            rightPointsCtr++;
            quizNumber++;
            tvCorrectScore.setText(Integer.toString(rightPointsCtr));
            tvQuizNumber.setText(Integer.toString(quizNumber));

            // Disable all images
            image1.setClickable(false);
            image2.setClickable(false);
            image3.setClickable(false);
            image4.setClickable(false);


            // Alter image to show user answer is oorrect
            selectedImage.setImageResource(R.drawable.correct);

            // Enable next button
            bNext.setVisibility(View.VISIBLE);
            bNext.setEnabled(true);
        } else {
           if(attempts>1) {
               // Increment and update incorrect answer counter views
               wrongPointsCtr++;
               quizNumber++;
               tvIncorrectScore.setText(Integer.toString(wrongPointsCtr));
               tvQuizNumber.setText(Integer.toString(quizNumber));

               // Disable all images
               image1.setClickable(false);
               image2.setClickable(false);
               image3.setClickable(false);
               image4.setClickable(false);

               // Alter image to show user answer is oorrect
               selectedImage.setImageResource(R.drawable.incorrect);

               // Indicate to user which answer is the correct one
               switch (position) {
                   case 1: image1.setBackgroundColor(Color.GREEN);
                       break;
                   case 2: image2.setBackgroundColor(Color.GREEN);
                       break;
                   case 3: image3.setBackgroundColor(Color.GREEN);
                       break;
                   case 4: image4.setBackgroundColor(Color.GREEN);
                       break;
                   default: break;
               }
               // Enable next button
               bNext.setVisibility(View.VISIBLE);
               bNext.setEnabled(true);
           } else {
               // Disable selected image
               selectedImage.setClickable(false);

               // Alter image to show user answer is oorrect
               selectedImage.setImageResource(R.drawable.incorrect);

               // Increment attempts
               attempts++;
           }
        }
        Log.i(TAG, "imageClick(): " + chosenPosition);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState()");

        savedInstanceState.putInt("quizNumber", quizNumber);
        savedInstanceState.putInt("position", position);
        savedInstanceState.putInt("rightPointsCtr", rightPointsCtr);
        savedInstanceState.putInt("wrongPointsCtr", wrongPointsCtr);
        savedInstanceState.putInt("attempts", attempts);
    }

}
