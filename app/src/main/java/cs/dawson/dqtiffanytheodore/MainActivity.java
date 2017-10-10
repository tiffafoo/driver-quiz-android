package cs.dawson.dqtiffanytheodore;

import android.content.SharedPreferences;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
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
public class MainActivity extends AppCompatActivity {
    /* Global vars */
    // Tag for Logging
    static final String TAG = "MainActivity Class: ";
    // Total number of questions
    static final int QUIZ_COUNT = 4;
    // List of definitions the user answered for
    static ArrayList<Question> askedDefinitions = new ArrayList<>();

    // Current quiz number's 4 questions being displayed
    static ArrayList<Question> questionsHolder = new ArrayList<>();

    TextView tvQuizNumber, tvDefinition, tvCorrectScores, tvIncorrectScore, tvScore;
    Button bHint, bAbout, bNext, bReplay;
    ImageButton image1, image2, image3, image4;

    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Question> currQuestions = new ArrayList<>();
    ArrayList<Integer> incorrectChoices = new ArrayList<>();
    ArrayList<Integer> askedDefinitionsIndex = new ArrayList<>();

    Question currQuestion;

    int quizNumber = 1, position = 1, correctCtr = 0, incorrectCtr = 0, attempts = 0;

    String previousScore;
    boolean answeredCorrectly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        // Get handles to fields
        tvQuizNumber = (TextView) findViewById(R.id.tvQuizNumber);
        tvDefinition = (TextView) findViewById(R.id.tvDefinition);
        tvCorrectScores = (TextView) findViewById(R.id.tvCorrectScore);
        tvIncorrectScore = (TextView) findViewById(R.id.tvIncorrectScore);
        tvScore = (TextView) findViewById(R.id.tvScore);

        bHint = (Button) findViewById(R.id.buttonHint);
        bAbout = (Button) findViewById(R.id.buttonAbout);
        bNext = (Button) findViewById(R.id.buttonCheckedAnswer);
        bReplay = (Button) findViewById(R.id.buttonReplay);

        image1 = (ImageButton) findViewById(R.id.ib1);
        image2 = (ImageButton) findViewById(R.id.ib2);
        image3 = (ImageButton) findViewById(R.id.ib3);
        image4 = (ImageButton) findViewById(R.id.ib4);

        setQuestions();

        if (savedInstanceState != null) {
            // Get the saved values from the Bundle
            quizNumber = savedInstanceState.getInt("quizNumber");
            incorrectCtr = savedInstanceState.getInt("incorrectCtr");
            correctCtr = savedInstanceState.getInt("correctCtr");
            position = savedInstanceState.getInt("position");
            attempts = savedInstanceState.getInt("attempts");
            answeredCorrectly = savedInstanceState.getBoolean("answeredCorrectly");
            currQuestion = questions.get(savedInstanceState.getInt("currQuestionIndex"));
            incorrectChoices = savedInstanceState.getIntegerArrayList("incorrectChoices");
            askedDefinitionsIndex = savedInstanceState.getIntegerArrayList("askedDefinitionsIndex");

            // Make sure no previously asked questions are used again
            for (int index : askedDefinitionsIndex) {
                askedDefinitions.add(questions.get(index));
            }
            restoreDisplay(savedInstanceState);

        } else {

            getSharedPreferences();

            // Initialize layout
            currQuestion = getCurrQuestion();
            tvDefinition.setText(currQuestion.getDefinition());
            tvQuizNumber.setText(String.valueOf(quizNumber));

            // Prepare and display images
            setCurrQuestions();
            displayImages();

            // If user quits app after completing the quiz without replaying, make sure to reset app
            if (quizNumber == QUIZ_COUNT) {
                bReplay.performClick();
            }

        }

    }

    /**
     * If any incorrect choices were made previously,
     * display them
     */
    private void displayAnyIncorrect() {
        // If any incorrect choices were made previously, display them no matter what
        if (!incorrectChoices.isEmpty()) {
            for (int choice : incorrectChoices) {
                switch (choice) {
                    case 1:
                        image1.setImageResource(R.drawable.incorrect);
                        break;
                    case 2:
                        image2.setImageResource(R.drawable.incorrect);
                        break;
                    case 3:
                        image3.setImageResource(R.drawable.incorrect);
                        break;
                    case 4:
                        image4.setImageResource(R.drawable.incorrect);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * If the user has reached the end of the quiz,
     * display the replay button and hide the next
     * button.
     */
    private void checkReplay() {
        // If the user has reached the end of the quiz, offer to replay
        if (quizNumber == QUIZ_COUNT) {
            bReplay.setVisibility(View.VISIBLE);
            bNext.setVisibility(View.GONE);
        } else {
            // Enable next button
            bNext.setVisibility(View.VISIBLE);
            bNext.setEnabled(true);
        }
    }
    /**
     * Rebuild the display.
     *
     * @param savedInstanceState
     */
    private void restoreDisplay(Bundle savedInstanceState) {
        // Set text views
        tvDefinition.setText(currQuestion.getDefinition());
        tvQuizNumber.setText(String.valueOf(quizNumber));
        tvCorrectScores.setText(String.valueOf(correctCtr));
        tvIncorrectScore.setText(String.valueOf(incorrectCtr));
        tvScore.setText(String.valueOf(calculateScore()) + "%");

        // Restore images
        image1.setImageResource(questions.get(savedInstanceState.getInt("image1Index")).getImageLink());
        image2.setImageResource(questions.get(savedInstanceState.getInt("image2Index")).getImageLink());
        image3.setImageResource(questions.get(savedInstanceState.getInt("image3Index")).getImageLink());
        image4.setImageResource(questions.get(savedInstanceState.getInt("image4Index")).getImageLink());

        displayAnyIncorrect();

        // If the user previously answered the question correctly,
        // prevent the user from clicking the images and restore their choice
        if (answeredCorrectly) {
            // Display a checkmark image on the image button
            // the user was able to properly guess
            switch (position) {
                case 1:
                    image1.setImageResource(R.drawable.correct);
                    break;
                case 2:
                    image2.setImageResource(R.drawable.correct);
                    break;
                case 3:
                    image3.setImageResource(R.drawable.correct);
                    break;
                case 4:
                    image4.setImageResource(R.drawable.correct);
                    break;
                default:
                    break;
            }

            setImagesClickable(false);
            checkReplay();
        } else {
            // If the user has not answered the question correctly and ran out of attempts,
            // prevent the user from clicking the images and restore their choices
            if (attempts > 1) {

                // Show which image is the correct one
                switch (position) {
                    case 1:
                        image1.setBackgroundColor(Color.GREEN);
                        break;
                    case 2:
                        image2.setBackgroundColor(Color.GREEN);
                        break;
                    case 3:
                        image3.setBackgroundColor(Color.GREEN);
                        break;
                    case 4:
                        image4.setBackgroundColor(Color.GREEN);
                        break;
                    default:
                        break;
                }

                // Disable all images
                setImagesClickable(false);

                checkReplay();

            } else {
                // If the user has not answered the question correctly and still has
                // more attempts, disable the next button and keep images clickable
                bNext.setVisibility(View.INVISIBLE);
                bNext.setEnabled(false);
            }
        }
    }

    /**
     * Initiates the questions array with road sign
     * images from res/drawable if the questions array
     * is empty
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
     * Gets the current question that the
     * user will have to answer
     *
     * @return {Question} question
     */
    private Question getCurrQuestion() {
        Log.i(TAG, "getCurrQuestion()");

        // Get a random question from available questions
        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());

        Question randomQuestion = questions.get(randomIndex);

        // Make sure this question hasn't been asked before
        while (askedDefinitions.contains(randomQuestion)) {
            randomIndex = random.nextInt(questions.size());
            randomQuestion = questions.get(randomIndex);
        }

        // Add to asked definitions (used for non-repetition on next button click)
        askedDefinitions.add(randomQuestion);
        // Add to asked definitions index (used keep track of used questions between runtimes (see onSaveInstanceState)
        askedDefinitionsIndex.add(randomIndex);

        return randomQuestion;
    }

    /**
     * Sets the 3 other current questions
     * (not the one the user has to guess
     */
    private void setCurrQuestions() {
        Log.i(TAG, "setCurrQuestions()");

        for (int i = 0; i < 3; i++) {
            currQuestions.add(getRandomQuestion());
        }
    }

    /**
     * Gets a random Question and add the index
     * of the question to the array of ints that we do not want
     * to be selected
     *
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
     * Handles an image click, determines the position and
     * checks if it corresponds to the correct definition
     *
     * @param view
     */
    public void imageClick(View view) {
        setImagesClickable(false);
        ImageButton selectedImage = (ImageButton) findViewById(view.getId());

        int chosenPosition = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(2));

        Log.d(TAG, "Correct position: " + position);

        // Player chose the right definition
        if (chosenPosition == position) {
            Log.d(TAG, "Correct position was chosen");
            Log.d(TAG, "correctCtr: " + correctCtr);

            // Increment and update correct answer counter views
            correctCtr++;
            answeredCorrectly = true;

            tvScore.setText(String.valueOf(calculateScore()) + "%");
            tvCorrectScores.setText(String.valueOf(correctCtr));
            tvQuizNumber.setText(String.valueOf(quizNumber));

            Log.d(TAG, "Set clickable false");

            // Alter image to show user answer is correct
            selectedImage.setImageResource(R.drawable.correct);

            Log.d(TAG, "Show correct");

            // Enable next button
            bNext.setVisibility(View.VISIBLE);
            bNext.setEnabled(true);

            if (quizNumber == QUIZ_COUNT) { saveScores(); }

            Log.d(TAG, "Enable next");
        } else {
            attempts++;

            // Save incorrect positions
            incorrectChoices.add(chosenPosition);

            // First attempt
            if (attempts <= 1) {

                // Alter image to show user answer is incorrect
                selectedImage.setImageResource(R.drawable.incorrect);

                setImagesClickable(true);
            } else { // Second attempt
                // Increment and update incorrect answer counter views
                incorrectCtr++;
                tvIncorrectScore.setText(String.valueOf(incorrectCtr));
                tvQuizNumber.setText(String.valueOf(quizNumber));
                tvScore.setText(String.valueOf(calculateScore()) + "%");

                // Alter image to show user answer is correct
                selectedImage.setImageResource(R.drawable.incorrect);

                // Indicate to user which answer is the correct one
                switch (position) {
                    case 1:
                        image1.setBackgroundColor(Color.GREEN);
                        break;
                    case 2:
                        image2.setBackgroundColor(Color.GREEN);
                        break;
                    case 3:
                        image3.setBackgroundColor(Color.GREEN);
                        break;
                    case 4:
                        image4.setBackgroundColor(Color.GREEN);
                        break;
                    default:
                        break;
                }

                // Enable next button
                bNext.setVisibility(View.VISIBLE);
                bNext.setEnabled(true);

                // Save score at last question
                if (quizNumber == QUIZ_COUNT) { saveScores(); }
            }
        }

        // Check if the four questions have been asked
        if (quizNumber == QUIZ_COUNT) {
            bReplay.setVisibility(View.VISIBLE);
            bNext.setVisibility(View.GONE);
            saveToSharedPreferences();
        }
    }

    /**
     * Saves and updates scores on disk.
     */
    private void saveScores() {
        Log.i(TAG, "saveScores()");
        Log.d(TAG, "save correctCtr: " + correctCtr);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        previousScore = sharedPreferences.getString("newScore", "0%");
        Log.d(TAG, "save previousScore: " + previousScore);

        editor.putString("previousScore", previousScore);

        editor.putString("newScore", calculateScore() + "%");
        // Commit changes
        editor.commit();

    }

    /**
     * Handles an about click, fires an intent to the about page.
     *
     * @param view
     */
    public void aboutClick(View view) {
        Log.i(TAG, "aboutClick()");

        // Open about page
        Intent myIntent = new Intent(MainActivity.this, AboutPage.class);
        startActivity(myIntent);
    }

    /**
     * Handles a next click, displays four new questions
     * and gets a new current question
     *
     * @param view
     */
    public void nextClick(View view) {
        setImagesClickable(false);
        switch (position) {
            case 1:
                image1.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case 2:
                image2.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case 3:
                image3.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case 4:
                image4.setBackgroundResource(android.R.drawable.btn_default);
                break;
            default:
                break;
        }
        // Reset counters and arrays
        attempts = 0;
        answeredCorrectly = false;
        incorrectChoices.clear();

        currQuestions.clear();
        questionsHolder.clear();

        // Get new questions
        currQuestion = getCurrQuestion();
        setCurrQuestions();

        // Set display and clickables
        displayImages();
        setImagesClickable(true);
        bNext.setVisibility(View.INVISIBLE);

        quizNumber++;

        // Set text for quiz and definition
        tvQuizNumber.setText(String.valueOf(quizNumber));
        tvDefinition.setText(currQuestion.getDefinition());

        Log.d(TAG, "Save to SharedPreferences");
        saveToSharedPreferences();
    }

    /**
     * Handles an hint click, launches a web search for the current question.
     *
     * @param view
     */
    public void hintClick(View view) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, getResources().getString(R.string.search_helper) + currQuestion.getDefinition());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * Handles an replay click, restarts the quiz.
     *
     * @param view
     */
    public void replayClick(View view) {
        switch (position) {
            case 1:
                image1.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case 2:
                image2.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case 3:
                image3.setBackgroundResource(android.R.drawable.btn_default);
                break;
            case 4:
                image4.setBackgroundResource(android.R.drawable.btn_default);
                break;
            default:
                break;
        }

        bReplay.setVisibility(View.GONE);
        bNext.setVisibility(View.INVISIBLE);

        // Rest counters
        quizNumber = 1;
        position = 1;
        correctCtr = 0;
        incorrectCtr = 0;
        attempts = 0;
        answeredCorrectly = false;

        // Reset questions
        askedDefinitionsIndex.clear();
        askedDefinitions.clear();
        incorrectChoices.clear();
        currQuestions.clear();
        questionsHolder.clear();

        // Initialize layout
        currQuestion = getCurrQuestion();
        tvDefinition.setText(currQuestion.getDefinition());
        tvQuizNumber.setText(String.valueOf(quizNumber));
        tvScore.setText(R.string.score_default);
        tvCorrectScores.setText(String.valueOf(correctCtr));
        tvIncorrectScore.setText(String.valueOf(incorrectCtr));

        // Prepare and display images
        setCurrQuestions();
        displayImages();
        setImagesClickable(true);

        Log.d(TAG, "Save to SharedPreferences");
        saveToSharedPreferences();
    }

    /**
     * Set shared preferences. Gets the quiz number
     * and counters from the shared preferences if available
     * or the default.
     */
    private void getSharedPreferences() {
        // Get reference to default shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Assign shared preferences to global var
        quizNumber = sharedPreferences.getInt("quizNumber", 1);
        incorrectCtr = sharedPreferences.getInt("incorrectCtr", 0);
        correctCtr = sharedPreferences.getInt("correctCtr", 0);

        Set<String> askedDefintionsIndexSet = sharedPreferences.getStringSet("askedDefinitionsIndex", new HashSet<String>());

        for (String usedQuestionNumber : askedDefintionsIndexSet) {
            askedDefinitions.add(questions.get(Integer.parseInt(usedQuestionNumber)));
            askedDefinitionsIndex.add(Integer.parseInt(usedQuestionNumber));
        }

        tvCorrectScores.setText(String.valueOf(correctCtr));
        tvIncorrectScore.setText(String.valueOf(incorrectCtr));

    }

    /**
     * Save values to state
     *
     * @param {Bundle} savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState()");

        savedInstanceState.putInt("quizNumber", quizNumber);
        savedInstanceState.putInt("position", position);
        savedInstanceState.putInt("attempts", attempts);
        savedInstanceState.putBoolean("answeredCorrectly", answeredCorrectly);
        savedInstanceState.putInt("incorrectCtr", incorrectCtr);
        savedInstanceState.putInt("correctCtr", correctCtr);
        savedInstanceState.putInt("image1Index", questions.indexOf(questionsHolder.get(0)));
        savedInstanceState.putInt("image2Index", questions.indexOf(questionsHolder.get(1)));
        savedInstanceState.putInt("image3Index", questions.indexOf(questionsHolder.get(2)));
        savedInstanceState.putInt("image4Index", questions.indexOf(questionsHolder.get(3)));
        savedInstanceState.putInt("currQuestionIndex", questions.indexOf(currQuestion));
        savedInstanceState.putIntegerArrayList("incorrectChoices", incorrectChoices);
        savedInstanceState.putIntegerArrayList("askedDefinitionsIndex", askedDefinitionsIndex);
    }

    /**
     * Save quiz number, incorrect and correct
     * counter in saved preferences
     */
    public void saveToSharedPreferences() {
        // Store values between app instances
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.d(TAG, "saveToSharedPreferences()");

        // Set the key/value pairs
        editor.putInt("quizNumber", quizNumber);
        editor.putInt("incorrectCtr", incorrectCtr);
        editor.putInt("correctCtr", correctCtr);

        Set<String> askedDefintionsIndexSet = new HashSet<>();

        for (int i = 0; i <= askedDefinitionsIndex.size() - 1; i++) {
            askedDefintionsIndexSet.add(String.valueOf(askedDefinitionsIndex.get(i)));
        }

        editor.putStringSet("askedDefinitionsIndex", askedDefintionsIndexSet);
        editor.commit();
    }

    /**
     * Set all four images to either clickable
     * or not clickable
     *
     * @param clickable true if clickable
     */
    public void setImagesClickable(boolean clickable) {
        image1.setClickable(clickable);
        image2.setClickable(clickable);
        image3.setClickable(clickable);
        image4.setClickable(clickable);
    }

    /**
     * Calculate the score based on the correct
     * counter
     *
     * @return score
     */
    public int calculateScore() {
        return (int) (1.0 * correctCtr / QUIZ_COUNT * 100);
    }

}
