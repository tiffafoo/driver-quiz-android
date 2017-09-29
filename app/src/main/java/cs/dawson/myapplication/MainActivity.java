package cs.dawson.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
    }
    
}
