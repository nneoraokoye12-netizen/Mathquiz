package edu.bpi.humanbodyquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button easyButton, mediumButton, hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        easyButton = findViewById(R.id.easybutton);
        mediumButton = findViewById(R.id.mediumbutton);
        hardButton = findViewById(R.id.hardbutton);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("EASY");
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("MEDIUM");
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("HARD");
            }
        });
    }

    private void startQuiz(String difficulty) {
        Intent intent = new Intent(MainActivity.this, Questions.class);
        intent.putExtra("DIFFICULTY", difficulty);
        startActivity(intent);
    }
}