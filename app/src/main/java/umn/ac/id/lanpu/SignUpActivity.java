package umn.ac.id.lanpu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameSignUp, emailSignUp, passwordSignUp1, passwordSignUp2;
    private ProgressBar progressBar;
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
        progressBar = findViewById(R.id.signUpProgressBar);


        Button signUpButtonInSignUp = findViewById(R.id.signUpButtonInSignUp);

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
    }

    private void signUpUser() {
        String name = nameSignUp.getText().toString().trim();
        String email = emailSignUp.getText().toString().trim();
        String password1 = passwordSignUp1.getText().toString().trim();
        String password2 = passwordSignUp2.getText().toString().trim();

        progressBar.setVisibility(View.GONE);

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
                        User user = new User(name, email, 0);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                                        Toast.makeText(SignUpActivity.this, "Please Check email for verification.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
//                                                Redirect kembali ke Login
                                        mAuth.signOut();
                                        Intent intentLoginInSignUp = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intentLoginInSignUp);
                                        finish();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "Failed to Registered! Try Again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Failed to Registered! Try Again!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loginInSignUp(View view) {
        Intent intentLoginInSignUp = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intentLoginInSignUp);
        finish();
    }


    @Override
    public void onBackPressed() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Exiting Lanpu");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}
