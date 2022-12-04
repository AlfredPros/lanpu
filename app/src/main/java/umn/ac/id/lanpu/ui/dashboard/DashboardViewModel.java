package umn.ac.id.lanpu.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Date;

import umn.ac.id.lanpu.FirebaseQueryLiveData;

public class DashboardViewModel extends ViewModel {

//    private final MutableLiveData<String> name, nim;
//    private final MutableLiveData<Integer> balance;
//    private final MutableLiveData<String> duration;
    private final MutableLiveData<Long> duration = new MutableLiveData<>();

    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");
    private static final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final DatabaseReference userReference  = usersTableReference.child(userID);
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(userReference);
//    private final MutableLiveData<String> currDate;

    public DashboardViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
//        name = new MutableLiveData<>();
//        nim = new MutableLiveData<>();
//        duration = new MutableLiveData<>();
//        nim.setValue("00000048211");
//        duration.setValue("05 HOURS 00 MINUTE 00 SECONDS");
//        balance = new MutableLiveData<>();
//        balance.setValue(100000);
        duration.setValue(0L);
    }

    public LiveData<DataSnapshot> getDataSnapshotLiveData(){
        return liveData;
    }

//    public LiveData<DataSnapshot> getBasicInformationLiveData() {
//        FirebaseQueryLiveData
//    }

    public LiveData<DataSnapshot> getStatusLiveData() {
        FirebaseQueryLiveData statusData = new FirebaseQueryLiveData( userReference.child("checkedIn"));
        return statusData;
    }

    public LiveData<Long> getDurationLiveDate() {return duration; }
//    public LiveData<String> getName() {
//        return name;
//    }
//
//    public LiveData<String> getNim() {
//        return nim;
//    }
//
//    public LiveData<Integer> getBalance() {
//        return balance;
//    }
//
//    public LiveData<String> getDuration(int mode) {
//        if (mode == 1) {
//            duration.setValue("05 HOURS 00 MINUTE 00 SECONDS");
//        } else {
//            duration.setValue("");
//        }
//        return duration;
//    }
//    public LiveData<String> getCurrDate() {
//        return currDate;
//    }
}