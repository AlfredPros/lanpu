package umn.ac.id.lanpu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ProcessingActivity extends AppCompatActivity {

    private  int loadMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loadMode = extras.getInt("load_mode");
        }
        TextView processingTextview = findViewById(R.id.processing_textview);
        switch (loadMode) {
            case 1:
                processingTextview.setText("FINDING\nPARKING TICKET DATA");
                break;
            case 2:
                processingTextview.setText("PROCESSING\nPAYMENT METHOD");
            default: break;
        }
        (new Handler()).postDelayed(this::proceed, 3000);
    }

    private void proceed() {
        Intent intent = new Intent(ProcessingActivity.this, ProcessedActivity.class);
        intent.putExtra("load_mode", loadMode);
        startActivity(intent);
        Intent returnIntent = new Intent();
        if (loadMode == 0) returnIntent.putExtra("mode", 1);
        else returnIntent.putExtra("mode", 0);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}