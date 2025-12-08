package edu.bpi.humanbodyquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Questions extends AppCompatActivity {

    private TextView tvQuestion, tvTitle;
    private Button option1, option2, option3, option4, hintButton;

    private String difficulty;
    private int currentAnswer;
    private double score = 0;
    private int questionNumber = 0;
    private final int totalQuestions = 5;
    private Random random = new Random();
    private boolean answerSelected = false;
    private boolean hintUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Get difficulty from intent
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        if (difficulty == null) {
            difficulty = "EASY"; // Default
        }

        initViews();
        generateQuestion();
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvTitle = findViewById(R.id.tvTitle);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        hintButton = findViewById(R.id.hintbutton);

        // Update title to show difficulty
        if (tvTitle != null) {
            tvTitle.setText("MathPath! - " + difficulty);
        }

        // Set click listeners for all options
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option3);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option4);
            }
        });

        // Hint button click listener
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useHint();
            }
        });
    }

    private void generateQuestion() {
        if (questionNumber >= totalQuestions) {
            showFinalScore();
            return;
        }

        questionNumber++;
        answerSelected = false;
        hintUsed = false;

        // Reset button colors and visibility
        resetButtonColors();
        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
        hintButton.setEnabled(true);

        switch (difficulty) {
            case "EASY":
                generateEasyQuestion();
                break;
            case "MEDIUM":
                generateMediumQuestion();
                break;
            case "HARD":
                generateHardQuestion();
                break;
            default:
                generateEasyQuestion();
        }
    }

    private void generateEasyQuestion() {
        int num1 = random.nextInt(50) + 1;
        int num2 = random.nextInt(50) + 1;

        if (random.nextBoolean()) {
            // Addition
            currentAnswer = num1 + num2;
            tvQuestion.setText("What's " + num1 + " + " + num2 + "?");
        } else {
            // Subtraction (positive result)
            int larger = Math.max(num1, num2);
            int smaller = Math.min(num1, num2);
            currentAnswer = larger - smaller;
            tvQuestion.setText("What's " + larger + " - " + smaller + "?");
        }

        setOptions(currentAnswer);
    }

    private void generateMediumQuestion() {
        int type = random.nextInt(4);

        switch (type) {
            case 0: // Multiplication
                int num1 = random.nextInt(13) + 2;
                int num2 = random.nextInt(13) + 2;
                currentAnswer = num1 * num2;
                tvQuestion.setText("What's " + num1 + " × " + num2 + "?");
                break;

            case 1: // Division
                int divisor = random.nextInt(10) + 2;
                int quotient = random.nextInt(13) + 2;
                int dividend = divisor * quotient;
                currentAnswer = quotient;
                tvQuestion.setText("What's " + dividend + " ÷ " + divisor + "?");
                break;

            case 2: // Larger addition
                int add1 = random.nextInt(80) + 20;
                int add2 = random.nextInt(80) + 20;
                currentAnswer = add1 + add2;
                tvQuestion.setText("What's " + add1 + " + " + add2 + "?");
                break;

            case 3: // Larger subtraction
                int sub1 = random.nextInt(100) + 50;
                int sub2 = random.nextInt(sub1 - 10) + 10;
                currentAnswer = sub1 - sub2;
                tvQuestion.setText("What's " + sub1 + " - " + sub2 + "?");
                break;
        }

        setOptions(currentAnswer);
    }

    private void generateHardQuestion() {
        int type = random.nextInt(4);

        switch (type) {
            case 0: // Squares
                int num = random.nextInt(9) + 11;
                currentAnswer = num * num;
                tvQuestion.setText("What's " + num + "²?");
                break;

            case 1: // Powers
                int base = random.nextInt(6) + 2;
                int exp = random.nextInt(2) + 3;
                currentAnswer = (int) Math.pow(base, exp);
                tvQuestion.setText("What's " + base + "^" + exp + "?");
                break;

            case 2: // Square roots
                int[] perfects = {64, 81, 100, 121, 144, 169, 196, 225};
                int perfect = perfects[random.nextInt(perfects.length)];
                currentAnswer = (int) Math.sqrt(perfect);
                tvQuestion.setText("What's √" + perfect + "?");
                break;

            case 3: // Complex expression
                int n1 = random.nextInt(10) + 5;
                int n2 = random.nextInt(6) + 2;
                int n3 = random.nextInt(8) + 2;
                currentAnswer = n1 * n2 + n3;
                tvQuestion.setText("What's " + n1 + " × " + n2 + " + " + n3 + "?");
                break;
        }

        setOptions(currentAnswer);
    }

    private void setOptions(int correctAnswer) {
        List<Integer> options = new ArrayList<>();
        options.add(correctAnswer);

        while (options.size() < 4) {
            int wrongAnswer = correctAnswer + random.nextInt(21) - 10;
            if (wrongAnswer > 0 && wrongAnswer != correctAnswer && !options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        Collections.shuffle(options);

        option1.setText(String.valueOf(options.get(0)));
        option2.setText(String.valueOf(options.get(1)));
        option3.setText(String.valueOf(options.get(2)));
        option4.setText(String.valueOf(options.get(3)));
    }

    private void useHint() {
        if (hintUsed || answerSelected) {
            return;
        }

        hintUsed = true;
        hintButton.setEnabled(false);

        // Find buttons with wrong answers
        List<Button> wrongButtons = new ArrayList<>();

        if (!option1.getText().toString().equals(String.valueOf(currentAnswer))) {
            wrongButtons.add(option1);
        }
        if (!option2.getText().toString().equals(String.valueOf(currentAnswer))) {
            wrongButtons.add(option2);
        }
        if (!option3.getText().toString().equals(String.valueOf(currentAnswer))) {
            wrongButtons.add(option3);
        }
        if (!option4.getText().toString().equals(String.valueOf(currentAnswer))) {
            wrongButtons.add(option4);
        }

        // Randomly remove 2 wrong answers
        Collections.shuffle(wrongButtons);
        for (int i = 0; i < 2 && i < wrongButtons.size(); i++) {
            wrongButtons.get(i).setVisibility(View.INVISIBLE);
        }

        Toast.makeText(this, "Hint used! 2 wrong answers removed. (Worth 0.5 points)", Toast.LENGTH_SHORT).show();
    }

    private void checkAnswer(Button selectedButton) {
        if (answerSelected) {
            return;
        }

        answerSelected = true;
        int selectedAnswer = Integer.parseInt(selectedButton.getText().toString());

        if (selectedAnswer == currentAnswer) {
            if (hintUsed) {
                score += 0.5;
                Toast.makeText(this, "Correct! +0.5 points ✓", Toast.LENGTH_SHORT).show();
            } else {
                score += 1;
                Toast.makeText(this, "Correct! +1 point ✓", Toast.LENGTH_SHORT).show();
            }
            selectedButton.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
        } else {
            selectedButton.setBackgroundColor(Color.parseColor("#F44336")); // Red
            highlightCorrectAnswer();
            Toast.makeText(this, "Wrong! The answer was " + currentAnswer, Toast.LENGTH_SHORT).show();
        }

        // Wait 1.5 seconds then move to next question
        selectedButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateQuestion();
            }
        }, 1500);
    }

    private void highlightCorrectAnswer() {
        if (option1.getText().toString().equals(String.valueOf(currentAnswer))) {
            option1.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else if (option2.getText().toString().equals(String.valueOf(currentAnswer))) {
            option2.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else if (option3.getText().toString().equals(String.valueOf(currentAnswer))) {
            option3.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else if (option4.getText().toString().equals(String.valueOf(currentAnswer))) {
            option4.setBackgroundColor(Color.parseColor("#4CAF50"));
        }
    }

    private void resetButtonColors() {
        option1.setBackgroundColor(Color.WHITE);
        option2.setBackgroundColor(Color.WHITE);
        option3.setBackgroundColor(Color.WHITE);
        option4.setBackgroundColor(Color.WHITE);
    }

    private void showFinalScore() {
        new AlertDialog.Builder(this)
                .setTitle("Quiz Complete!")
                .setMessage("Your Score: " + score + "/" + totalQuestions + "\n\nDifficulty: " + difficulty)
                .setPositiveButton("Email Score", (dialog, which) -> {
                    // Go to email activity
                    Intent intent = new Intent(Questions.this, Email.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("TOTAL_QUESTIONS", totalQuestions);
                    intent.putExtra("DIFFICULTY", difficulty);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Try Again", (dialog, which) -> {
                    score = 0;
                    questionNumber = 0;
                    generateQuestion();
                })
                .setNeutralButton("Go Home", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}