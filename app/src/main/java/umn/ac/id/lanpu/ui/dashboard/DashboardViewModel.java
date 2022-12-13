package umn.ac.id.lanpu.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import umn.ac.id.lanpu.FirebaseQueryLiveData;


public class DashboardViewModel extends ViewModel {
    public MutableLiveData<Boolean> checker = new MutableLiveData<>();
    private static final DatabaseReference paymentRequestTableReference = FirebaseDatabase.getInstance().getReference("PaymentRequest");
    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");
    private static String userID;
    private static DatabaseReference userReference;
    private static FirebaseQueryLiveData liveData;

    public DashboardViewModel() {
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        userReference = usersTableReference.child(userID);
        liveData = new FirebaseQueryLiveData(userReference);
        checker.setValue(false);
    }

    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

    public LiveData<DataSnapshot> getStatusLiveData() {
        return new FirebaseQueryLiveData(userReference.child("checkedIn"));
    }

    public void pay(String userID, boolean ack) {
//        TODO: Change how the payment works (cannot be done by user device, must be on admin's device)
        paymentRequestTableReference.child(userID).child("ack").setValue(ack);
    }

    public LiveData<Boolean> getChecker() {
        return checker;
    }

    public FirebaseQueryLiveData getPaymentRequestLiveData(String userID) {
        return new FirebaseQueryLiveData(paymentRequestTableReference.child(userID));
    }

    public LiveData<DataSnapshot> getEntryTime() {
        return new FirebaseQueryLiveData(userReference.child("entryTime"));
    }
}