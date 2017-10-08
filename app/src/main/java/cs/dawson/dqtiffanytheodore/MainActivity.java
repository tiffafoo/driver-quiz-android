package cs.dawson.dqtiffanytheodore;

import android.content.SharedPreferences;
import android.app.SearchManager;
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
import java.util.Collection;
import java.util.Collections;
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
    ArrayList<Question> currQuestions = new ArrayList<>();
    Question currQuestion;
    int quizNumber, position, rightPointsCtr, wrongPointsCtr, quizAttempts, attempts = 1;
    Set<String> previousScores;
    boolean bNextVisible = false;

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


        getSharedPreferences();

        // These are available through shared preferences
        tvCorrectScore.setText(rightPointsCtr);
        tvQuizNumber.setText(quizNumber);
        tvIncorrectScore.setText(wrongPointsCtr);

        if (savedInstanceState != null) {
            // Get the saved values from the Bundle
            position = savedInstanceState.getInt("position");
            attempts = savedInstanceState.getInt("attempts");
            bNextVisible = savedInstanceState.getBoolean("bNextVisible");

            if (bNextVisible) {
                // Enable next button
                bNext.setVisibility(View.VISIBLE);
                bNext.setEnabled(true);
            }

            // Set the images
            image1.setImageResource(questions.get(savedInstanceState.getInt("image1Index")).getImageLink());
            image2.setImageResource(questions.get(savedInstanceState.getInt("image2Index")).getImageLink());
            image3.setImageResource(questions.get(savedInstanceState.getInt("image3Index")).getImageLink());
            image4.setImageResource(questions.get(savedInstanceState.getInt("image4Index")).getImageLink());
        } else {
            // Initiate questions
            setQuestions();

            // Initialize layout
            currQuestion = getCurrQuestion();
            tvDefinition.setText(currQuestion.getDefinition());
            tvQuizNumber.setText(quizNumber);

            // Prepare and display images
            setCurrQuestions();
            displayImages();
        }
    }

    /**
     * Randomizes and displays the images
     */
    private void displayImages() {
        Log.i(TAG, "Display Images");

        ArrayList<Question> questionsHolder = new ArrayList<>();

        questionsHolder.addAll(currQuestions);
        questionsHolder.add(currQuestion);

        Collections.shuffle(questionsHolder);

        image1.setImageResource(questionsHolder.get(0).getImageLink());
        image2.setImageResource(questionsHolder.get(1).getImageLink());
        image3.setImageResource(questionsHolder.get(2).getImageLink());
        image4.setImageResource(questionsHolder.get(3).getImageLink());
    }
    /**
     * Sets the 3 other current questions
     * (not the one the user has to guess
     */
    private void setCurrQuestions() {
        Log.i(TAG, "setCurrQuestions()");
        for (int i = 0; i < 4; i++) {
            currQuestions.add(getRandomQuestion());
        }
    }
    /**
     * Set shared preferences. Gets the quiz number
     * and counters from the shared preferences if available
     * or the default.
     */
    private void getSharedPreferences() {
        // Get reference to default shared preferences
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        // Assign shared preferences to global var
        quizNumber = sharedPreferences.getInt("quizNumber", R.integer.quiz_number_default);
        rightPointsCtr = sharedPreferences.getInt("correctScore", R.integer.correct_score_default);
        wrongPointsCtr = sharedPreferences.getInt("incorrectScore", R.integer.incorrect_score_default);
        quizAttempts = sharedPreferences.getInt("quizAttempts", R.integer.attempts_default);
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
            quizAttempts = sharedPreferences.getInt("quizAttempts", R.integer.attempts_default);
        } else if (key.equals("previousScores")) {
            previousScores = sharedPreferences.getStringSet("previousScores", new HashSet<String>());
        }

        tvCorrectScore.setText(rightPointsCtr);
        tvQuizNumber.setText(quizNumber);
        tvIncorrectScore.setText(wrongPointsCtr);
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
        editor.putInt("quizAttempts", quizAttempts);
        editor.putStringSet("previousScores", previousScores);

        // Commit the changes
        editor.commit();

        // onSharedPreferenceShanged should get called
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
     * Gets a random Question and add the index
     * of the question to the array of ints that we do not want
     * to be selected
     * @return Question
     */
    private Question getRandomQuestion() {
        Log.i(TAG, "getRandomQuestion()");

        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());

        Question randomQuestion = questions.get(randomIndex);

        // Make sure the question is not one of the currQuestions
        while (currQuestions.contains(randomQuestion)) {
            randomIndex = random.nextInt(questions.size());
            randomQuestion = questions.get(randomIndex);
        }
        currQuestions.add(randomQuestion);

        return randomQuestion;
    }

    private Question getCurrQuestion() {
        Log.i(TAG, "getRandomQuestion(): int");

        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());

        Question randomQuestion = questions.get(randomIndex);

        questions.remove(randomIndex);

        return randomQuestion;
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
            tvCorrectScore.setText(rightPointsCtr);
            tvQuizNumber.setText(quizNumber);

            // Disable all images
            image1.setClickable(false);
            image2.setClickable(false);
            image3.setClickable(false);
            image4.setClickable(false);


            // Alter image to show user answer is correct
            selectedImage.setImageResource(R.drawable.correct);

            // Enable next button
            bNext.setVisibility(View.VISIBLE);
            bNext.setEnabled(true);
        } else {
           if(attempts>1) {
               // Increment and update incorrect answer counter views
               wrongPointsCtr++;
               quizNumber++;
               tvIncorrectScore.setText(wrongPointsCtr);
               tvQuizNumber.setText(quizNumber);

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

        saveToSharedPreferences();
        Log.i(TAG, "imageClick(): " + chosenPosition);

    }


    /**
     * Handles an about click, fires an intent to the about page.
     * @param view
     */

    public void aboutClick(View view) {
        Log.i(TAG, "aboutClick()");

        //open about page
        Intent myIntent = new Intent(MainActivity.this, AboutPage.class);
        startActivity(myIntent);

    }

    /**
     * Handles an hint click, launches a web search for the current question.
     * @param view
     */
    public void hintClick(View view){

        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, "road sign " + currQuestion.getDefinition());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState()");

        savedInstanceState.putInt("quizNumber", quizNumber);
        savedInstanceState.putInt("position", position);
        savedInstanceState.putInt("attempts", attempts);
    }

}
