package umn.ac.id.lanpu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

public class VerifyPayment extends AppCompatActivity {

    private TextView ticketNumberTextView, nameTextView, idTextView, entryTimeTextView, categoryTextView, durationTextView, priceTextView;
    private TicketViewModel ticketViewModel;
    private AppCompatButton payButton;

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payment);

        ticketNumberTextView = findViewById(R.id.ticket_number_data);
        nameTextView = findViewById(R.id.ticket_name_data);
        idTextView = findViewById(R.id.ticket_nim_data);
        entryTimeTextView = findViewById(R.id.ticket_time_data);
        categoryTextView = findViewById(R.id.ticket_category_data);
        durationTextView = findViewById(R.id.duration_textview);
        priceTextView = findViewById(R.id.price_textview);
        payButton = findViewById(R.id.pay_button);

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
                } else {
                    Ticket ticket = task.getResult().getValue(Ticket.class);
                    Log.d("FINALTICKETID", finalTicketID);
                    ticketNumberTextView.setText(finalTicketID);
                    nameTextView.setText(ticket.name);
                    idTextView.setText(ticket.userID);
                    entryTimeTextView.setText(convertTime(ticket.entryTime));
                    durationTextView.setText(convertTime(ticket.exitTime - ticket.entryTime));
                    priceTextView.setText(String.valueOf((int) ticket.price));
                    categoryTextView.setText(ticket.category);
                    payButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ticketViewModel.pay((int) ticket.price);
                            Intent paymentReport = new Intent(VerifyPayment.this, PaymentReport.class);
                            paymentReport.putExtra("ticketID", finalTicketID);
                            startActivity(paymentReport);
                            finish();
                        }
                    });
                }
            }
        });

    }
}