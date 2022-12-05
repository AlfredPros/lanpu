package umn.ac.id.lanpu.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import umn.ac.id.lanpu.ProcessingActivity;
import umn.ac.id.lanpu.R;
import umn.ac.id.lanpu.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    DashboardViewModel dashboardViewModel;
    private boolean checker = false;

    private final Handler mHandler = new Handler();
    public Calendar c;
    public String strDate;
    public String strTime;

    private String entryTime;

    // Digunakan untuk menentukan Activity mana yang akan diload.
    private final int LOAD_ENTRY = 0;
    private final int LOAD_PAYMENT = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardViewModel.getChecker().observe(getViewLifecycleOwner(), aBoolean -> checker = aBoolean);


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView nimTextView = binding.nimTextview;
        final TextView nameTextView = binding.nameTextview;
        final ImageView qrImageView = binding.imageView;
        final TextView durationTextView = binding.durationTextview;
        final TextView balanceTextView = binding.balanceTextview;

        final MaterialCardView statusCard = binding.statusCard;
        final MaterialCardView warningdebt = binding.warningdebt;

        // Set Date Time
        final TextView dateText = binding.dateTextview;
        final TextView timeText = binding.timeTextview;

        // This function refreshes every second
        final Runnable mRunnable = new Runnable() {
            public void run() {

                if (entryTime == null) {
                    c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                    strDate = sdf.format(c.getTime());
                    dateText.setText(strDate);

                    sdf = new SimpleDateFormat("HH:mm:ss ZZZZ");
                    strTime = sdf.format(c.getTime());
                    timeText.setText(strTime);

                    durationTextView.setText("");
                } else {
//                    statusCard.setCardBackgroundColor(getResources().getColor(R.color.green));

                    c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                    strDate = sdf.format(c.getTime());
                    dateText.setText(strDate);

                    sdf = new SimpleDateFormat("HH:mm:ss ZZZZ");
                    strTime = sdf.format(c.getTime());
                    timeText.setText(strTime);

                    sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String time = sdf.format(c.getTime());
                    durationTextView.setText(findDifference(entryTime, time));
                }

                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(mRunnable);

        // Live Data
        LiveData<DataSnapshot> liveData = dashboardViewModel.getDataSnapshotLiveData();
        liveData.observe(getViewLifecycleOwner(), dataSnapshot -> {

            if (dataSnapshot != null) {
//                    Updata UI ketika terjadi perubahan dalam User

//                    Get data
                String name = dataSnapshot.child("name").getValue(String.class);
//                    String uid = dataSnapshot.getValue(String.class);
                int balance = dataSnapshot.child("balance").getValue(int.class);
                entryTime = dataSnapshot.child("entryTime").getValue(String.class);
//                    if (entryTime != null) mHandler.post(durationRunnable);
//                    else mHandler.post(mRunnable);

//                    Set data to View
                nameTextView.setText(name);
                nimTextView.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                balanceTextView.setText(cf.format(balance).replace("p", "p "));

                // QR Code
                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(nimTextView.getText().toString(), BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    qrImageView.setPadding(0, 0, 0, 0);
                    qrImageView.setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                if (balance <= -50000) {
                    warningdebt.setVisibility(View.VISIBLE);
                } else {
                    warningdebt.setVisibility(View.GONE);
                }
            }
        });

        LiveData<DataSnapshot> paymentLiveData = dashboardViewModel.getPaymentRequestLiveData(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        paymentLiveData.observe(getViewLifecycleOwner(), dataSnapshot -> {
            if (dataSnapshot.exists()) {
                viewTicketDetail(LOAD_PAYMENT);
            }
        });

        LiveData<DataSnapshot> statusLiveData = dashboardViewModel.getStatusLiveData();
        statusLiveData.observe(getViewLifecycleOwner(), dataSnapshot -> {
            boolean checkedIn = dataSnapshot.getValue(boolean.class);
            changeStatus(checkedIn);
            if (checkedIn)
                binding.statusCard.setCardBackgroundColor(getResources().getColor(R.color.green));
            else
                binding.statusCard.setCardBackgroundColor(getResources().getColor(R.color.red));
        });
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void changeStatus(boolean checkedIn) {
        if (checkedIn) {
            if (checkedIn != checker) { // Fire ketika hanya berubah
                viewTicketDetail(LOAD_ENTRY);
                dashboardViewModel.checker.setValue(checkedIn);
                c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                strDate = sdf.format(c.getTime());
            }
        }
    }


    public void viewTicketDetail(int loadMode) {
        Intent getTicket = new Intent(getActivity(), ProcessingActivity.class);
        getTicket.putExtra("processingTitle", "Finding Ticket");
        getTicket.putExtra("loadMode", loadMode);
        startActivity(getTicket);
    }


    static String findDifference(String start_date, String end_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = d2.getTime() - d1.getTime();

            long difference_In_Seconds = (difference_In_Time / 1000) % 60;

            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            return (difference_In_Hours + " hours "
                    + difference_In_Minutes + " mins "
                    + difference_In_Seconds + " secs");

        } catch (ParseException e) {
            Log.d("PARSE ERROR", Objects.requireNonNull(e.getMessage()));
        }

        return "";
    }

}