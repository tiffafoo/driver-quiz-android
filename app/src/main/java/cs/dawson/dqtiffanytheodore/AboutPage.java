package cs.dawson.dqtiffanytheodore;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class AboutPage extends AppCompatActivity {

    String previousScore, newScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        TextView tvScores = (TextView) findViewById(R.id.pastscores);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        previousScore = sharedPreferences.getString("previousScore", "--%");
        newScore = sharedPreferences.getString("newScore", "--%");

        String scores = getResources().getString(R.string.previous_score) + previousScore + getResources().getString(R.string.new_score) + newScore;
        tvScores.setText(scores);

    }
}
