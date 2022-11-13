package umn.ac.id.lanpu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerifyPaymentActivity extends AppCompatActivity {

    private AppCompatButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payment);
        homeButton = findViewById(R.id.home_page_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifyPaymentActivity.this, ProcessingActivity.class);
                intent.putExtra("load_mode", 2);
                startActivity(intent);
                finish();
            }
        });
    }
}