package umn.ac.id.lanpu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private Button signUpButtonInSignUp;
    private EditText nameSignUp, emailSignUp, passwordSignUp1, passwordSignUp2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nameSignUp = findViewById(R.id.nameSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        passwordSignUp1 = findViewById(R.id.passwordSignUp1);
        passwordSignUp2 = findViewById(R.id.passwordSignUp2);


        signUpButtonInSignUp = findViewById(R.id.signUpButtonInSignUp);

        signUpButtonInSignUp.setOnClickListener(view -> {
//                Intent intentToSignIn = new Intent(SignUpActivity.this, LoginActivity.class);
//                startActivity(intentToSignIn);
//                finish();
            signUpUser();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    private void signUpUser() {
        String name = nameSignUp.getText().toString().trim();
        String email = emailSignUp.getText().toString().trim();
        String password1 = passwordSignUp1.getText().toString().trim();
        String password2 = passwordSignUp2.getText().toString().trim();

        //    Validasi Input
        if (name.isEmpty()) {
            nameSignUp.setError("Please Enter Your Name.");
            nameSignUp.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailSignUp.setError("Please Enter Your Email.");
            emailSignUp.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailSignUp.setError("Please provide valid email. (example@gmail.com)");
            emailSignUp.requestFocus();
            return;
        }

        if (password1.isEmpty()) {
            passwordSignUp1.setError("Please Enter Password.");
            passwordSignUp1.requestFocus();
            return;
        }

        if (password1.length() < 6) {
            passwordSignUp1.setError("Password must be more than 6 characters.");
            passwordSignUp1.requestFocus();
            return;
        }

        if (password2.isEmpty()) {
            passwordSignUp2.setError("Please Confirm Password");
            passwordSignUp2.requestFocus();
            return;
        }

        if (password2.length() < 6) {
            passwordSignUp2.setError("Password must be more than 6 characters.");
            passwordSignUp2.requestFocus();
            return;
        }

        if (!password2.equals(password1)) {
            passwordSignUp2.setError("Must be the same as the password.");
            passwordSignUp2.requestFocus();
            return;
        }

//      Masukkan User ke database
        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(name, email, "0");
                        FirebaseDatabase.getInstance().getReference("https://if570-mobileappuas-lanpu-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User has been registered succesfully", Toast.LENGTH_LONG).show();
//                                                Redirect kembali ke Login
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to Registered! Try Again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to Registered! Try Again!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loginInSignUp(View view) {
        Intent intentLoginInSignUp = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intentLoginInSignUp);
        finish();
    }
}
