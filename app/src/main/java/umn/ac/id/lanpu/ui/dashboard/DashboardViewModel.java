package umn.ac.id.lanpu.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import umn.ac.id.lanpu.FirebaseQueryLiveData;

public class DashboardViewModel extends ViewModel {
//    private final MutableLiveData<Long> entryTime = new MutableLiveData<>(); //  TODO: Update data ini
//    private final MutableLiveData<String> duration = new MutableLiveData<>();


    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");
    private static final String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private static final DatabaseReference userReference = usersTableReference.child(userID);
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(userReference);

    public DashboardViewModel() {

    }

    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

    public LiveData<DataSnapshot> getStatusLiveData() {
        return new FirebaseQueryLiveData(userReference.child("checkedIn"));
    }

    public void setEntryTime(String time) {
        userReference.child("entryTime").setValue(time);
    }
//    public String getDurationTime (long Duration) {
//        java.time.Duration duration = Duration.between(LocalTime.NOON, LocalTime.MAX);
//
//        LocalDateTime date = LocalDateTime.now();
//        System.out.println(date);
//
//        date = (LocalDateTime)duration.addTo(date);
//        System.out.println(date);
//    }

    public LiveData<DataSnapshot> getEntryTime(){
        return new FirebaseQueryLiveData(userReference.child("entryTime"));
    }


}