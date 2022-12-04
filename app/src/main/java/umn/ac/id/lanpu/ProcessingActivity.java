package umn.ac.id.lanpu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import umn.ac.id.lanpu.ui.dashboard.DashboardViewModel;
import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class ProcessingActivity extends AppCompatActivity {

    private  int loadMode = 0;
    private TicketViewModel ticketViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        Bundle extras = getIntent().getExtras();
        String title = null;
        if (extras != null) {
//            loadMode = extras.getInt("load_mode");
            title = extras.getString("processing_title");
        }
        TextView processingTextview = findViewById(R.id.processing_textview);
        Intent intent = getIntent();

        processingTextview.setText(title);

        ticketViewModel =
                new ViewModelProvider(this).get(TicketViewModel.class);
        ticketViewModel.getActiveTicketofUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Intent enterIntent = new Intent(ProcessingActivity.this, EntryActivity.class);
                    enterIntent.putExtra("ticketID", task.getResult().getValue(String.class));
                    startActivity(enterIntent);
                    finish();
                }
            }
        });
    }
}