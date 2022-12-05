package umn.ac.id.lanpu.ui.dashboard;

import android.util.Log;

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
    public MutableLiveData<Boolean> checker = new MutableLiveData<Boolean>();

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

    public LiveData<Boolean> getChecker() {
        return checker;
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