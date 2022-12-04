package umn.ac.id.lanpu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class ProcessedActivity extends AppCompatActivity {

    private int loadMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loadMode = extras.getInt("load_mode");
        }
        Log.d("d", "LOYSING: Load activity " + loadMode);
        TextView processedTextview = findViewById(R.id.processed_textview);
        switch (loadMode) {
            case 1:
                processedTextview.setText("FINDING\nPARKING TICKET DATA\nCOMPLETED");
                break;
            case 2:
                processedTextview.setText("PROCESSING\nPAYMENT METHOD\nCOMPLETED");
                break;
            default: break;
        }
        (new Handler()).postDelayed(this::proceed, 2000);
    }

    private void proceed() {
        Intent intent = new Intent();
        switch (loadMode) {
            case 1:
//                intent = new Intent(ProcessedActivity.this, VerifyPaymentActivity.class);
//                startActivity(intent);
                break;
            case 2:
//                intent = new Intent(ProcessedActivity.this, PaymentReport.class);
//                startActivity(intent);
                break;
            default:
//                intent = new Intent(ProcessedActivity.this, EntryActivity.class);
//                startActivity(intent);
                break;
        }
        finish();
    }
}