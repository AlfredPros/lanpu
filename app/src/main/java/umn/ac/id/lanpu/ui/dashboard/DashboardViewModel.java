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

    private final MutableLiveData<Long> duration = new MutableLiveData<>();
    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");
    private static final String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private static final DatabaseReference userReference = usersTableReference.child(userID);
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(userReference);

    public DashboardViewModel() {
        duration.setValue(0L);
    }

    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

    public LiveData<DataSnapshot> getStatusLiveData() {
        return new FirebaseQueryLiveData(userReference.child("checkedIn"));
    }

    public LiveData<Long> getDurationLiveDate() {
        return duration;
    }

    // TODO: Create FUNCTION that RETURNS DURATION as STRING time format
    //  Return 00 days 00 minutes 00 seconds
    //  public String getDurationTime(long duration)

    // TODO: Create BACKGROUND PROCESS THAT UPDATES THE DURATION IN MILLISECONDS
    //  Buatlah sebuah background process yang akan menhitung seconds ketika checkedIN berubah menjadi true
    //  Simple aja buatnya cuma counter
    //  public void startCounter()

}