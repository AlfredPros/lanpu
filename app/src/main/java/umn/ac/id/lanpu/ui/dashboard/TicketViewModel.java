package umn.ac.id.lanpu.ui.dashboard;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import umn.ac.id.lanpu.FirebaseQueryLiveData;

public class TicketViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final DatabaseReference activeTicketTableReference = FirebaseDatabase.getInstance().getReference("ActiveTickets");
    private static final DatabaseReference ticketsTableReference = FirebaseDatabase.getInstance().getReference("Tickets");
    private static final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


    // Get ticket information given a ticket
    public DatabaseReference getTicket(String ticketID) {
        return ticketsTableReference.child(ticketID);
    }

    // Get Ticket active Ticket of a user
    public DatabaseReference getActiveTicketofUser(String userID) {
        return activeTicketTableReference.child(userID);
    }
}