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
    private final int LOAD_ENTRY = 0;
    private final int LOAD_PAYMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        Bundle extras = getIntent().getExtras();
        String title = null;
        if (extras != null) {
            loadMode = extras.getInt("loadMode");
            title = extras.getString("processingTitle");
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
                    // Jika user baru di checked in maka tampilkan ticket masuk
                    Intent showTicket = new Intent();
                    if (loadMode == LOAD_ENTRY) {
                         showTicket = new Intent(ProcessingActivity.this, EntryActivity.class);
                    } else if (loadMode == LOAD_PAYMENT) {
                        showTicket = new Intent(ProcessingActivity.this, VerifyPayment.class);
                    }
                    showTicket.putExtra("ticketID", task.getResult().getValue(String.class));
                    startActivity(showTicket);
                    finish();
                }
            }
        });


    }
}