package umn.ac.id.lanpu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class EntryActivity extends AppCompatActivity {

    private TextView ticketNumberTextView, nameTextView, idTextView, entryTimeTextView, categoryTextView;
    private TicketViewModel ticketViewModel;
    private AppCompatButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ticketNumberTextView = findViewById(R.id.ticket_number_data);
        nameTextView = findViewById(R.id.ticket_name_data);
        idTextView = findViewById(R.id.ticket_nim_data);
        entryTimeTextView = findViewById(R.id.ticket_time_data);
        categoryTextView = findViewById(R.id.ticket_category_data);
        homeButton = findViewById(R.id.home_page_button);

        String ticketID = null;
        // Get intent extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ticketID = extras.getString("ticketID");
        }


        ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        String finalTicketID = ticketID;
        ticketViewModel.getTicket(ticketID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Ticket ticket = task.getResult().getValue(Ticket.class);
                    Log.d("FINALTICKETID", finalTicketID);
                    ticketNumberTextView.setText(finalTicketID);
                    nameTextView.setText(ticket.name);
                    idTextView.setText(ticket.userID);
//                    TODO: FIX THIS ENTRY TIME
//                    entryTimeTextView.setText((int) ticket.entryTime);
                    categoryTextView.setText(ticket.category);
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}