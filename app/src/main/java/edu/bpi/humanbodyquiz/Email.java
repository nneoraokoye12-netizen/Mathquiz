package edu.bpi.humanbodyquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Email extends AppCompatActivity {

    private EditText emailInput;
    private Button sendButton, goHomeButton;
    private double score;
    private int totalQuestions;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email); // Replace with your actual layout name

        // Get score data from intent
        Intent intent = getIntent();
        score = intent.getDoubleExtra("SCORE", 0);
        totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 5);
        difficulty = intent.getStringExtra("DIFFICULTY");
        if (difficulty == null) {
            difficulty = "EASY";
        }

        initViews();
    }

    private void initViews() {
        emailInput = findViewById(R.id.typeName1);
        sendButton = findViewById(R.id.sendbutton);
        goHomeButton = findViewById(R.id.gohomebutton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendScoreToEmail();
            }
        });

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
    }

    private void sendScoreToEmail() {
        String email = emailInput.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create email intent
        String subject = "MathPath Quiz Score - " + difficulty;
        String body = "Congratulations on completing the quiz!\n\n" +
                "Difficulty: " + difficulty + "\n" +
                "Score: " + score + "/" + totalQuestions + "\n" +
                "Percentage: " + String.format("%.1f", (score / totalQuestions) * 100) + "%\n\n" +
                "Keep practicing to improve your math skills!";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            Toast.makeText(this, "Sent!", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToHome() {
        Intent intent = new Intent(Email.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}