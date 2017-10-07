package cs.dawson.dqtiffanytheodore;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cs.dawson.dqtiffanytheodore.entities.Question;

/**
 * Driver Quiz App which asks four questions to a user,
 * giving them the choice to select one of four images
 * that correspond to the definition. Each image is also associated
 * to a definition, found through google search using said definition.
 *
 * The user has two attempts for each questions
 * before having to move to the next question.
 *
 * correct, incorrect and attemps should persist (SharedPreferences),
 * and the layout should be properly responsive (check specs in wiki)
 *
 * @author Tiffany Le-Nguyen <sirMerr>
 * @author Theodore Accos-Thomas <theoathomas>
 */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
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
    private Set<String> previousScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        // Get handles to fields
        tvQuizNumber = (TextView) findViewById(R.id.tvQuizNumber);
        tvDefinition = (TextView) findViewById(R.id.tvDefinition);
        tvCorrectScore = (TextView) findViewById(R.id.tvCorrectScore);
        tvIncorrectScore = (TextView) findViewById(R.id.tvIncorrectScore);

        bHint = (Button) findViewById(R.id.buttonHint);
        bAbout = (Button) findViewById(R.id.buttonAbout);
        bNext = (Button) findViewById(R.id.buttonCheckedAnswer);

        image1 = (ImageButton) findViewById(R.id.ib1);
        image2 = (ImageButton) findViewById(R.id.ib2);
        image3 = (ImageButton) findViewById(R.id.ib3);
        image4 = (ImageButton) findViewById(R.id.ib4);


        if (savedInstanceState != null) {
            // Get the saved values from the Bundle
            quizNumber = savedInstanceState.getInt("quizNumber");
            position = savedInstanceState.getInt("position");
            rightPointsCtr = savedInstanceState.getInt("rightPointsCtr");
            wrongPointsCtr = savedInstanceState.getInt("wrongPointsCtr");
            attempts = savedInstanceState.getInt("attempts");

            tvQuizNumber.setText(Integer.toString(quizNumber));
            tvCorrectScore.setText(Integer.toString(rightPointsCtr));

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
     * Set shared preferences. Gets the quiz number
     * and counters from the shared preferences if available
     * or the default.
     */
    private void setSharedPreferences() {
        // Get reference to default shared preferences
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        // Assign shared preferences to global var
        quizNumber = sharedPreferences.getInt("quizNumber", R.integer.quiz_number_default);
        rightPointsCtr = sharedPreferences.getInt("correctScore", R.integer.correct_score_default);
        wrongPointsCtr = sharedPreferences.getInt("incorrectScore", R.integer.incorrect_score_default);
        attempts = sharedPreferences.getInt("attempts", R.integer.attempts_default);
        previousScores = sharedPreferences.getStringSet("previousScores", new HashSet<String>());

    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     * <p>
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("quizNumber")) {
            quizNumber = sharedPreferences.getInt(key, R.integer.quiz_number_default);
        } else if (key.equals("correctScore")) {
            rightPointsCtr = sharedPreferences.getInt("correctScore", R.integer.correct_score_default);
        } else if (key.equals("incorrectScore")) {
            wrongPointsCtr = sharedPreferences.getInt("incorrectScore", R.integer.incorrect_score_default);
        } else if (key.equals("attempts")) {
            attempts = sharedPreferences.getInt("attempts", R.integer.attempts_default);
        } else if (key.equals("previousScores")) {
            previousScores = sharedPreferences.getStringSet("previousScores", new HashSet<String>());
        }
    }

    /**
     * Save all values into sharedPreferences
     */
    public void saveToSharedPreferences() {
        // Store values between app instances
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the key/value pairs
        editor.putInt("quizNumber", quizNumber);
        editor.putInt("correctScore", rightPointsCtr);
        editor.putInt("incorrectScore", wrongPointsCtr);
        editor.putInt("attempts", attempts);
        editor.putStringSet("previousScores", previousScores);

        // Commit the changes
        editor.commit();
    }

    /**
     * Save a key-value pair, where the value
     * is an int
     * @param key String
     * @param value int
     */
    public void saveToSharedPreferences(String key, int value) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the key/value pairs
        editor.putInt(key, value);

        // Commit changes
        editor.commit();
    }

    /**
     * Save a key-value pair, where the value
     * is an Set of Strings
     * @param key String
     * @param value Set<String>
     */
    public void saveToSharedPreferences(String key, Set<String> value) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the key/value pairs
        editor.putStringSet(key, value);

        // Commit changes immediately.
        // Note: We could use editor.apply() so it's
        // handled in the background instead.
        editor.commit();
    }

    /**
     * Initiates the questions array with road sign
     * images from res/drawable
     */
    private void setQuestions() {
        // Temporary
        questions.clear();
        questions.add(new Question(R.drawable.sign1, getResources().getString(R.string.definition1)));
        questions.add(new Question(R.drawable.sign2, getResources().getString(R.string.definition2)));
        questions.add(new Question(R.drawable.sign3, getResources().getString(R.string.definition3)));
        questions.add(new Question(R.drawable.sign4, getResources().getString(R.string.definition4)));
        questions.add(new Question(R.drawable.sign5, getResources().getString(R.string.definition5)));
        questions.add(new Question(R.drawable.sign6, getResources().getString(R.string.definition6)));
        questions.add(new Question(R.drawable.sign7, getResources().getString(R.string.definition7)));
        questions.add(new Question(R.drawable.sign8, getResources().getString(R.string.definition8)));
        questions.add(new Question(R.drawable.sign9, getResources().getString(R.string.definition9)));
        questions.add(new Question(R.drawable.sign10, getResources().getString(R.string.definition10)));
        questions.add(new Question(R.drawable.sign11, getResources().getString(R.string.definition11)));
        questions.add(new Question(R.drawable.sign12, getResources().getString(R.string.definition12)));
        questions.add(new Question(R.drawable.sign13, getResources().getString(R.string.definition13)));
        questions.add(new Question(R.drawable.sign14, getResources().getString(R.string.definition14)));
        questions.add(new Question(R.drawable.sign15, getResources().getString(R.string.definition15)));
    }

    /**
     * Gets a random Question
     * @return Question
     */
    private Question getRandomQuestion() {
        Log.i(TAG, "getRandomQuestion()");
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

        int chosenPosition = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(2));

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

    public void aboutClick(View view) {

        Log.i(TAG, "aboutClick()");

        //open about page
        Intent myIntent = new Intent(MainActivity.this, AboutPage.class);
        startActivity(myIntent);

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
