package cs.dawson.dqtiffanytheodore;

import android.content.Context;
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
    String TAG = "MainActivity Class: "; // tag for Logging
    TextView quizNumberTV, definitionTV, correctScoresTV, incorrectScoresTV;
    Button hintButton, aboutButton, nextButton;
    ImageButton image1, image2, image3, image4;
    ArrayList<Question> questions = new ArrayList<>();
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

        // Get handle to fields
        quizNumberTV = (TextView) findViewById(R.id.textViewQuizNumber);
        definitionTV = (TextView) findViewById(R.id.textViewDefinition);
        correctScoresTV = (TextView) findViewById(R.id.textViewScore);
        incorrectScoresTV = (TextView) findViewById(R.id.tvIncorrectScore);

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
        quizNumberTV.setText(Integer.toString(quizNumber));

        //Ensure that correct answer is never in the same place at startup
        Random random = new Random();
        position = random.nextInt(4) + 1;

        Log.i(TAG, "Selected position :" + position);

        switch(position){
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

        }
    }

    /**
     * Initiates the questions array with road sign
     * images from res/drawable
     */
    private void setQuestions() {
        //temporary
        questions.clear();
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
        return questions.remove(random.nextInt(questions.size()));
    }

    public void imageClick(View view){

        ImageButton selectedImage = (ImageButton) findViewById(view.getId());

        int chosenPosition = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(9));

        Context context = getApplicationContext();
        CharSequence text = "Correct";
        CharSequence text2 = "wrong";
        int duration = Toast.LENGTH_SHORT;




        if(chosenPosition==position){

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //increment and update correct answer counter views
            rightPointsCtr++;
            quizNumber++;
            correctScoresTV.setText(Integer.toString(rightPointsCtr));
            quizNumberTV.setText(Integer.toString(quizNumber));

            //disable all images
            image1.setClickable(false);
            image2.setClickable(false);
            image3.setClickable(false);
            image4.setClickable(false);


            //alter image to show user answer is oorrect
            selectedImage.setImageResource(R.drawable.correct);

            //enable next button
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);


        }else{

           if(attempts>1){
               //increment and update incorrect answer counter views
               wrongPointsCtr++;
               quizNumber++;
               incorrectScoresTV.setText(Integer.toString(wrongPointsCtr));
               quizNumberTV.setText(Integer.toString(quizNumber));

               //disable all images
               image1.setClickable(false);
               image2.setClickable(false);
               image3.setClickable(false);
               image4.setClickable(false);

               //alter image to show user answer is oorrect
               selectedImage.setImageResource(R.drawable.incorrect);

               //indicate to user which answer is the correct one
               switch (position){
                   case 1: image1.setBackgroundColor(Color.GREEN);
                       break;
                   case 2: image2.setBackgroundColor(Color.GREEN);
                       break;
                   case 3: image3.setBackgroundColor(Color.GREEN);
                       break;
                   case 4: image4.setBackgroundColor(Color.GREEN);
                       break;

               }

               //enable next button
               nextButton.setVisibility(View.VISIBLE);
               nextButton.setEnabled(true);


           }else {

               //disable selected image
               selectedImage.setClickable(false);

               //alter image to show user answer is oorrect
               selectedImage.setImageResource(R.drawable.incorrect);

               //increment attempts
               attempts++;
           }



            Toast toast = Toast.makeText(context, text2, duration);
            toast.show();
        }



        Log.i(TAG, "imageClick(): " + chosenPosition);

    }
}
