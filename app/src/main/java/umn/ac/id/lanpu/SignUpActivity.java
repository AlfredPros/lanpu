package umn.ac.id.lanpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private Button signUpButtonInSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        signUpButtonInSignUp = findViewById(R.id.signUpButtonInSignUp);

        signUpButtonInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSignIn = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intentToSignIn);
                finish();
            }
        });
    }

    public void loginInSignUp(View view) {
        Intent intentLoginInSignUp = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intentLoginInSignUp);
        finish();
    }
}
