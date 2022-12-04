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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class PaymentReport extends AppCompatActivity {

    private TextView ticketNumberTextView, nameTextView, idTextView, entryTimeTextView, categoryTextView, exitTimeTextView, durationTextView, priceTextView;
    private TicketViewModel ticketViewModel;
    private AppCompatButton homeButton;

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_report);

//        Connect ke View Model
        ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);

        ticketNumberTextView = findViewById(R.id.ticket_number_data);
        nameTextView = findViewById(R.id.ticket_name_data);
        idTextView = findViewById(R.id.ticket_nim_data);
        entryTimeTextView = findViewById(R.id.ticket_time_data);
        categoryTextView = findViewById(R.id.ticket_category_data);
        homeButton = findViewById(R.id.home_page_button);
        exitTimeTextView = findViewById(R.id.ticket_outtime_data);
        durationTextView = findViewById(R.id.ticket_duration_data);
        priceTextView = findViewById(R.id.ticket_price_data);

        String ticketID = null;
        // Get intent extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ticketID = extras.getString("ticketID");
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        Nunjukkan data
        String finalTicketID = ticketID;
        ticketViewModel.getTicket(ticketID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Ticket ticket = task.getResult().getValue(Ticket.class);
                    Log.d("FINALTICKETID", finalTicketID);
                    ticketNumberTextView.setText(finalTicketID);
                    nameTextView.setText(ticket.name);
                    idTextView.setText(ticket.userID);
//                    TODO: FIX THIS ENTRY TIME
                    entryTimeTextView.setText(convertTime(ticket.entryTime));
                    exitTimeTextView.setText(convertTime(ticket.exitTime));
                    durationTextView.setText(convertTime(ticket.exitTime - ticket.entryTime));
                    priceTextView.setText((int) ticket.price);
                    categoryTextView.setText(ticket.category);
                }
            }
        });
    }
}