package umn.ac.id.lanpu.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import umn.ac.id.lanpu.FirebaseQueryLiveData;

public class TicketViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final DatabaseReference activeTicketTableReference = FirebaseDatabase.getInstance().getReference("ActiveTickets");
    private static final DatabaseReference ticketsTableReference = FirebaseDatabase.getInstance().getReference("Tickets");


    // Get ticket information given a ticket
    public DatabaseReference getTicket(String ticketID) {
        return ticketsTableReference.child(ticketID);
    }

    // Get Ticket active Ticket of a user
    public DatabaseReference getActiveTicketofUser(String userID) {
        return activeTicketTableReference.child(userID);
    }

    public FirebaseQueryLiveData getAllTicketofUser(String userID) {
        return new FirebaseQueryLiveData(ticketsTableReference.orderByChild("userID").equalTo(userID));
    }

    public Query getAllTicketofUserQuery(String userID) {
        return ticketsTableReference.orderByChild("userID").equalTo(userID);
    }

    public void eraseUserTickets(String userID) {
        getActiveTicketofUser(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String filter_key = task.getResult().getValue(String.class);
                getAllTicketofUserQuery(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                            if (!Objects.equals(ticketSnapshot.getRef().getKey(), filter_key)) {
                                ticketSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}