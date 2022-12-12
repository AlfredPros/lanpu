package umn.ac.id.lanpu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import umn.ac.id.lanpu.ui.dashboard.DashboardViewModel;
import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class VerifyPayment extends AppCompatActivity {

    private TextView ticketNumberTextView, nameTextView, idTextView, entryTimeTextView, categoryTextView, durationTextView, priceTextView;
    private AppCompatButton payButton;
    private DashboardViewModel dashboardViewModel;
    private long mLastClickTime = 0;

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

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        String ticketID = null;
        // Get intent extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ticketID = extras.getString("ticketID");
        }

        TicketViewModel ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        String finalTicketID = ticketID;
        if (ticketID != null) {
            ticketViewModel.getTicket(ticketID).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Ticket ticket = task.getResult().getValue(Ticket.class);
                    Log.d("FINALTICKETID", finalTicketID);
                    ticketNumberTextView.setText(finalTicketID);
                    assert ticket != null;
                    nameTextView.setText(ticket.name);
                    idTextView.setText(ticket.userID);
                    entryTimeTextView.setText(ticket.entryTime);
                    durationTextView.setText(findDifference(ticket.entryTime, ticket.exitTime));
                    NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                    priceTextView.setText(cf.format(ticket.price).replace("p", "p "));
                    categoryTextView.setText(ticket.category);
//                    TODO: Kalau mau bisa tambahkan cancel payment button tinggal pakai dashboardViewmodel.pay(userID, false)
                    payButton.setOnClickListener(view -> {
//                                Menghindari Double Clicked
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        //                            Confirm Payment
                        dashboardViewModel.pay(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), true);
                        Intent paymentReport = new Intent(VerifyPayment.this, PaymentReport.class);
                        paymentReport.putExtra("ticketID", finalTicketID);
                        startActivity(paymentReport);
                        finish();
                    });
                }
            });
        } else {
            finish();
        }
    }

    static String findDifference(String start_date, String end_date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            assert d2 != null;
            assert d1 != null;
            long difference_In_Time = d2.getTime() - d1.getTime();

            long difference_In_Seconds = (difference_In_Time / 1000) % 60;

            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

            return (difference_In_Hours + " hours "
                    + difference_In_Minutes + " mins "
                    + difference_In_Seconds + " secs");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

}