package umn.ac.id.lanpu.ui.dashboard;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private static final String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


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

    public void pay(int payment) {
//        TODO: Change how the payment works (cannot be done by user device, must be on admin's device)
        DatabaseReference revRef = FirebaseDatabase.getInstance().getReference("Revenue");
        revRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int amount = snapshot.getValue(int.class);
                revRef.setValue(amount + payment);
                activeTicketTableReference.child(userID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PAYMENT", "Payment onCancelled:  " + error.getMessage().toString());
            }
        });
    }

    ;

    public FirebaseQueryLiveData getEntryTime(String userID) {
        String ticketID = getActiveTicketofUser(userID).get().getResult().getValue(String.class);
        return new FirebaseQueryLiveData(getTicket(ticketID).child("entryTime"));
    }

    public void eraseUserTickets(String userID) {
        getActiveTicketofUser(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String filter_key = task.getResult().getValue(String.class);
                    getAllTicketofUserQuery(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                                if (!ticketSnapshot.getRef().getKey().equals(filter_key)) {
                                    ticketSnapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}