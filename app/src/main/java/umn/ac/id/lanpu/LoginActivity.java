package umn.ac.id.lanpu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Button loginButtoninLogin;
    private EditText emailLogin;
    private EditText passwordLogin;
    private ProgressBar progressBarLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        loginButtoninLogin = findViewById(R.id.loginButtoninLogin);
        loginButtoninLogin.setOnClickListener(view -> login());

        // Mengest mAuth sebagai sebuah Firebase instance sesuai google-service.json;
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUpInLogin(View view) {
        Intent intentSignUpInLogin = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intentSignUpInLogin);
        finish();
    }

    public void login() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();
        if (email.isEmpty()) {
            emailLogin.setError("Please Enter your Email");
            emailLogin.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordLogin.setError("Please enter your password");
            passwordLogin.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLogin.setError("Please provide valid email. (example@gmail.com)");
            emailLogin.requestFocus();
            return;
        }
        progressBarLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) { // Jika user berhasil di authentikasi

                // Apakah user telah di verified?
                FirebaseUser user = mAuth.getCurrentUser();

                if (Objects.requireNonNull(user).isEmailVerified()) {
                    // Redirect ke Home Page
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Log.e("LOGIN", "SUCCESS");
                } else {
                    user.sendEmailVerification();
                    Toast.makeText(LoginActivity.this, "User has not been verified. Please Check email for verification.", Toast.LENGTH_LONG).show();
                }

            } else { // User gagal masuk
                Toast.makeText(LoginActivity.this, "Failed to Login! Please check your credentials.", Toast.LENGTH_LONG).show();
            }
            progressBarLogin.setVisibility(View.GONE);
        });
    }
}
