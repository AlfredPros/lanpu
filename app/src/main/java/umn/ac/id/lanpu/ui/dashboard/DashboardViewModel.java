package umn.ac.id.lanpu.ui.dashboard;

<<<<<<< Updated upstream
=======
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

>>>>>>> Stashed changes
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
<<<<<<< Updated upstream
=======

    public LiveData<Long> getDurationLiveDate() {
        return duration;
    }

    // TODO: Create FUNCTION that RETURNS DURATION as STRING time format
    //  Return 00 days 00 minutes 00 seconds
    //  public String getDurationTime(long duration)

    public String getDurationTime () {
        dateTime = thisDuration.addTo(dateTime);
        dateTime = dateTime.plus(thisDuration);
    }

    // TODO: Create BACKGROUND PROCESS THAT UPDATES THE DURATION IN MILLISECONDS
    //  Buatlah sebuah background process yang akan menhitung seconds ketika checkedIN berubah menjadi true
    //  Simple aja buatnya cuma counter
    //  public void startCounter()

    public void startCounter() {}

>>>>>>> Stashed changes
}