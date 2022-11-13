package umn.ac.id.lanpu.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Date;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> name, nim;
    private final MutableLiveData<Integer> balance;
//    private final MutableLiveData<String> currDate;

    public DashboardViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
        name = new MutableLiveData<>();
        nim = new MutableLiveData<>();
        name.setValue("Adhitya Bagus Wicaksono");
        nim.setValue("00000048211");
        balance = new MutableLiveData<>();
        balance.setValue(100000);
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getNim() {
        return nim;
    }

    public LiveData<Integer> getBalance() {
        return balance;
    }

//    public LiveData<String> getCurrDate() {
//        return currDate;
//    }
}