package cs.dawson.dqtiffanytheodore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    String TAG = "DQ"; // tag for Logging
    TextView quizNumberTV, definitionTV;
    Button hintButton, aboutButton, nextButton;
    ImageButton image1, image2, image3, image4;
    ArrayList<Question> questions = new ArrayList<>();
    Question currQuestion;
    int quizNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get handle to fields
        quizNumberTV = (TextView) findViewById(R.id.textViewQuizNumber);
        definitionTV = (TextView) findViewById(R.id.textViewDefinition);

        hintButton = (Button) findViewById(R.id.buttonHint);
        aboutButton = (Button) findViewById(R.id.buttonAbout);
        nextButton = (Button) findViewById(R.id.buttonCheckedAnswer);

        image1 = (ImageButton) findViewById(R.id.imageView1);
        image2 = (ImageButton) findViewById(R.id.imageView2);
        image3 = (ImageButton) findViewById(R.id.imageView3);
        image4 = (ImageButton) findViewById(R.id.imageView4);

        // Initiate questions
        setQuestions();

        // Initialize layout
        currQuestion = getRandomQuestion();
        definitionTV.setText(currQuestion.getDefinition());
        image1.setImageResource(currQuestion.getImageLink());
        image2.setImageResource(getRandomQuestion().getImageLink());
        image3.setImageResource(getRandomQuestion().getImageLink());
        image4.setImageResource(getRandomQuestion().getImageLink());
    }

    /**
     * Initiates the questions array with road sign
     * images from res/drawable
     */
    private void setQuestions() {
        questions.add(new Question(R.drawable.sign1, getResources().getString(R.string.definition_sign1)));
        questions.add(new Question(R.drawable.sign2, getResources().getString(R.string.definition_sign2)));
        questions.add(new Question(R.drawable.sign3, getResources().getString(R.string.definition_sign3)));
        questions.add(new Question(R.drawable.sign4, getResources().getString(R.string.definition_sign4)));
        questions.add(new Question(R.drawable.sign5, getResources().getString(R.string.definition_sign5)));
        questions.add(new Question(R.drawable.sign6, getResources().getString(R.string.definition_sign6)));
        questions.add(new Question(R.drawable.sign7, getResources().getString(R.string.definition_sign7)));
        questions.add(new Question(R.drawable.sign8, getResources().getString(R.string.definition_sign8)));
        questions.add(new Question(R.drawable.sign9, getResources().getString(R.string.definition_sign9)));
        questions.add(new Question(R.drawable.sign10, getResources().getString(R.string.definition_sign10)));
        questions.add(new Question(R.drawable.sign11, getResources().getString(R.string.definition_sign11)));
        questions.add(new Question(R.drawable.sign12, getResources().getString(R.string.definition_sign12)));
        questions.add(new Question(R.drawable.sign13, getResources().getString(R.string.definition_sign13)));
        questions.add(new Question(R.drawable.sign14, getResources().getString(R.string.definition_sign14)));
        questions.add(new Question(R.drawable.sign15, getResources().getString(R.string.definition_sign15)));
    }

    /**
     * Gets a random Question
     * @return Question
     */
    private Question getRandomQuestion() {
        Random random = new Random();

        return questions.get(random.nextInt(questions.size()));
    }
}
