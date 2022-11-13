package umn.ac.id.lanpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button loginButtoninLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        loginButtoninLogin = findViewById(R.id.loginButtoninLogin);
        loginButtoninLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToHomeApps = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentToHomeApps);
                finish();
            }
        });
    }

    public void signUpInLogin(View view) {
        Intent intentSignUpInLogin = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intentSignUpInLogin);
        finish();
    }
}