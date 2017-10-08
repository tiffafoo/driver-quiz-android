package cs.dawson.dqtiffanytheodore;

import android.content.SharedPreferences;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
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
    static final String TAG = "MainActivity Class: "; // tag for Logging
    static final int QUIZ_COUNT = 4;
    TextView tvQuizNumber, tvDefinition, tvCorrectScore, tvIncorrectScore, tvScore;
    Button bHint, bAbout, bNext;
    ImageButton image1, image2, image3, image4;
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Question> currQuestions = new ArrayList<>();
    static ArrayList<Question> askedDefinitions = new ArrayList<>();
    static ArrayList<Question> questionsHolder = new ArrayList<>();
    Question currQuestion;
    int quizNumber = 1, position = 1, correctCtr = 0, incorrectCtr = 0, quizAttempts = 0, attempts = 1;
    int totalIncorrect, totalCorrect;
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
        tvScore = (TextView) findViewById(R.id.tvScore);

        bHint = (Button) findViewById(R.id.buttonHint);
        bAbout = (Button) findViewById(R.id.buttonAbout);
        bNext = (Button) findViewById(R.id.buttonCheckedAnswer);

        image1 = (ImageButton) findViewById(R.id.ib1);
        image2 = (ImageButton) findViewById(R.id.ib2);
        image3 = (ImageButton) findViewById(R.id.ib3);
        image4 = (ImageButton) findViewById(R.id.ib4);

        getSharedPreferences();

        setQuestions();

        if (savedInstanceState != null) {
            // Get the saved values from the Bundle
            quizNumber = savedInstanceState.getInt("quizNumber");

            incorrectCtr = savedInstanceState.getInt("incorrectCtr");
            correctCtr = savedInstanceState.getInt("correctCtr");
            position = savedInstanceState.getInt("position");
            attempts = savedInstanceState.getInt("attempts");
            bNextVisible = savedInstanceState.getBoolean("bNextVisible");
            currQuestion = questions.get(savedInstanceState.getInt("currQuestionIndex"));

            if (bNextVisible) {
                // Enable next button
                bNext.setVisibility(View.VISIBLE);
                bNext.setEnabled(true);
            }

            // Set the images
            // This is where the crash happens
            Log.d(TAG, "image1Index: " + savedInstanceState.getInt("image1Index"));

            image1.setImageResource(questions.get(savedInstanceState.getInt("image1Index")).getImageLink());
            image2.setImageResource(questions.get(savedInstanceState.getInt("image2Index")).getImageLink());
            image3.setImageResource(questions.get(savedInstanceState.getInt("image3Index")).getImageLink());
            image4.setImageResource(questions.get(savedInstanceState.getInt("image4Index")).getImageLink());

            // Set text views
            tvDefinition.setText(currQuestion.getDefinition());
            tvQuizNumber.setText(String.valueOf(quizNumber));
            tvCorrectScore.setText(String.valueOf(correctCtr));
            tvIncorrectScore.setText(String.valueOf(incorrectCtr));
            tvScore.setText(String.valueOf(correctCtr/ QUIZ_COUNT *100));
        } else {
            // Initialize layout
            currQuestion = getCurrQuestion();
            tvDefinition.setText(currQuestion.getDefinition());
            tvQuizNumber.setText(String.valueOf(quizNumber));

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

        questionsHolder.addAll(currQuestions);
        questionsHolder.add(currQuestion);

        Collections.shuffle(questionsHolder);

        position = questionsHolder.indexOf(currQuestion) + 1;

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

        for (int i = 0; i < 3; i++) {
            Question holder = getRandomQuestion();

            currQuestions.add(holder);
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
        totalCorrect = sharedPreferences.getInt("correctScore", R.integer.correct_score_default);
        totalIncorrect = sharedPreferences.getInt("incorrectScore", R.integer.incorrect_score_default);
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

        if (key.equals("correctScore")) {
            totalCorrect = sharedPreferences.getInt("correctScore", R.integer.correct_score_default);
        } else if (key.equals("incorrectScore")) {
            totalIncorrect = sharedPreferences.getInt("incorrectScore", R.integer.incorrect_score_default);
        } else if (key.equals("attempts")) {
            quizAttempts = sharedPreferences.getInt("quizAttempts", R.integer.attempts_default);
        } else if (key.equals("previousScores")) {
            previousScores = sharedPreferences.getStringSet("previousScores", new HashSet<String>());
        }

        //TODO: Most of these appear in the credits

    }

    /**
     * Save all values into sharedPreferences
     */
    public void saveToSharedPreferences() {
        // Store values between app instances
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the key/value pairs
        editor.putInt("correctScore", totalCorrect);
        editor.putInt("incorrectScore", totalIncorrect);
        editor.putInt("quizAttempts", quizAttempts);
        editor.putStringSet("previousScores", previousScores);

        // We are using apply here because it's fine if
        // this is in the background as it only affects credits
        editor.apply();

        // onSharedPreferenceChanged should get called
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
        editor.apply();
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
        editor.apply();
    }

    /**
     * Initiates the questions array with road sign
     * images from res/drawable
     */
    private void setQuestions() {
        if (questions.isEmpty()) {
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
        while (askedDefinitions.contains(randomQuestion) || currQuestions.contains(randomQuestion)) {
            randomIndex = random.nextInt(questions.size());
            randomQuestion = questions.get(randomIndex);
        }

        return randomQuestion;
    }

    /**
     * Gets the current question that the
     * user will have to answer
     * @return {Question} question
     */
    private Question getCurrQuestion() {
        Log.i(TAG, "getRandomQuestion(): int");

        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());

        Question randomQuestion = questions.get(randomIndex);

        // Make sure this question hasn't been asked before
        while (askedDefinitions.contains(randomQuestion)) {
            randomIndex = random.nextInt(questions.size());
            randomQuestion = questions.get(randomIndex);
        }

        // Add to asked definitions
        askedDefinitions.add(randomQuestion);

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

        Log.d(TAG, "Correct position: " + position);

        // If correct position
        if (chosenPosition == position) {
            Log.d(TAG, "Correct position was chosen");
            // Increment and update correct answer counter views
            Log.d(TAG, "correctCtr: " + correctCtr);
            Log.d(TAG, "totalCorrect: " + totalCorrect);
            correctCtr++;
            totalCorrect++;

            double score = (double)correctCtr/ QUIZ_COUNT * 100;

            tvScore.setText(String.valueOf(score) + "%");
            tvCorrectScore.setText(String.valueOf(correctCtr));
            tvQuizNumber.setText(String.valueOf(quizNumber));

            // Disable all images
            image1.setClickable(false);
            image2.setClickable(false);
            image3.setClickable(false);
            image4.setClickable(false);

            Log.d(TAG, "Set clickable false");

            // Alter image to show user answer is correct
            selectedImage.setImageResource(R.drawable.correct);

            Log.d(TAG, "Show correct");

            // Check if it's question 4
            if (checkQuizNumber()) {
                // Enable next button
                bNext.setVisibility(View.VISIBLE);
                bNext.setEnabled(true);
                bNextVisible = true;

                Log.d(TAG, "Enable next");

                attempts = 1;
            }
        } else {
            // Only first image
           if(attempts>1) {
               // Increment and update incorrect answer counter views
               incorrectCtr++;
               totalIncorrect++;
               tvIncorrectScore.setText(String.valueOf(incorrectCtr));
               tvQuizNumber.setText(String.valueOf(quizNumber));

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

               if (checkQuizNumber()) {
                   // Enable next button
                   bNext.setVisibility(View.VISIBLE);
                   bNext.setEnabled(true);

                   attempts = 1;
               }
           } else {
               // Disable selected image
               selectedImage.setClickable(false);

               // Alter image to show user answer is correct
               selectedImage.setImageResource(R.drawable.incorrect);

               if (checkQuizNumber()) {
                   // Increment attempts
                   attempts++;
               }
           }
        }

        Log.d(TAG, "Save to SharedPreferences");
        saveToSharedPreferences();
        Log.i(TAG, "imageClick() position: " + chosenPosition);
    }


    /**
     * Check if the four questions have been asked
     */
    private boolean checkQuizNumber() {
        if (quizNumber >= 4) {
            // TODO: Show replay button
            previousScores.add((double)correctCtr/ QUIZ_COUNT * 100 + "%");
            saveToSharedPreferences("previousScores", previousScores);
            return false;
        }

        return true;
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
    /**
     * Save values to state
     * @param {Bundle} savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState()");

        savedInstanceState.putInt("quizNumber", quizNumber);
        savedInstanceState.putInt("position", position);
        savedInstanceState.putInt("attempts", attempts);
        savedInstanceState.putBoolean("bNextVisible", bNextVisible);
        savedInstanceState.putInt("incorrectCtr", incorrectCtr);
        savedInstanceState.putInt("correctCtr", correctCtr);
        savedInstanceState.putInt("image1Index", questions.indexOf(questionsHolder.get(0)));
        savedInstanceState.putInt("image2Index", questions.indexOf(questionsHolder.get(1)));
        savedInstanceState.putInt("image3Index", questions.indexOf(questionsHolder.get(2)));
        savedInstanceState.putInt("image4Index", questions.indexOf(questionsHolder.get(3)));
        savedInstanceState.putInt("currQuestionIndex", questions.indexOf(currQuestion));

    }

}
