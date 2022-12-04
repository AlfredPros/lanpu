package umn.ac.id.lanpu.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import umn.ac.id.lanpu.R;
import umn.ac.id.lanpu.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private int mode = 0;
    DashboardViewModel dashboardViewModel;

    private final Handler mHandler = new Handler();
    public Calendar c;
    public String strDate;
    public String strTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView nimTextView = binding.nimTextview;
        final TextView nameTextView = binding.nameTextview;
        final ImageView qrImageView = binding.imageView;
        final TextView durationTextView = binding.durationTextview;
        final TextView balanceTextView = binding.balanceTextview;

        // Set Date Time
        final TextView dateText = binding.dateTextview;
        final TextView timeText = binding.timeTextview;

        // This function refreshes every second
        final Runnable mRunnable = new Runnable() {
            public void run() {
                c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                strDate = sdf.format(c.getTime());
                dateText.setText(strDate);

                sdf = new SimpleDateFormat("HH:mm:ss ZZZZ");
                strTime = sdf.format(c.getTime());
                timeText.setText(strTime);

                mHandler.postDelayed(this, 1000);
            }
        };

        mHandler.post(mRunnable);

        // Live Data
        LiveData<DataSnapshot> liveData = dashboardViewModel.getDataSnapshotLiveData();
        liveData.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
//                    Updata UI ketika terjadi perubahan dalam User

//                    Get data
                    String name = dataSnapshot.child("name").getValue(String.class);
//                    String uid = dataSnapshot.getValue(String.class);
                    int balance = dataSnapshot.child("balance").getValue(int.class);

//                    Set data to View
                    nameTextView.setText(name);
                    nimTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    balanceTextView.setText(("Rp " + balance));

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
                }
            }
        });

        LiveData<DataSnapshot> statusLiveData = dashboardViewModel.getStatusLiveData();
        statusLiveData.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@NonNull DataSnapshot dataSnapshot) {
                boolean checkedIn = dataSnapshot.getValue(boolean.class);
                changeStatus(checkedIn);
                if (checkedIn) {
                    startViewTicket();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
//                mode = data.getIntExtra("mode", 1);
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                mode = 0;
//            }
//        }
//        TextView durationTextView = binding.durationTextview;
//        MaterialCardView statusCard = binding.statusCard;
////        dashboardViewModel.getDuration(this.mode).observe(getViewLifecycleOwner(), durationTextView::setText);
//        if (mode == 1) {
//            statusCard.setCardBackgroundColor(getResources().getColor(R.color.green));
//        } else {
//            statusCard.setCardBackgroundColor(getResources().getColor(R.color.red));
//        }
//    }

    public void changeStatus(boolean checkedIn) {
        MaterialCardView statusCard = binding.statusCard;
        TextView durationTextView = binding.durationTextview;
        Observer<Long> changeDuration = new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                // Handle duration TextView ketika data Duration berubah
                durationTextView.setText(aLong.toString());
            }
        };
        if (checkedIn) {
            statusCard.setCardBackgroundColor(getResources().getColor(R.color.green));
            dashboardViewModel.getDurationLiveDate().observe(getViewLifecycleOwner(), changeDuration);
        } else {
            statusCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            dashboardViewModel.getDurationLiveDate().removeObserver(changeDuration);

//            Masukkan Durasi jika tidak bisa
            durationTextView.setText("");
        }
    }


    public void startViewTicket(){
        Intent getTicket = new Intent(getActivity(), ProcessingActivity.class);
        getTicket.putExtra("processing_title", "Finding Ticket");
        startActivity(getTicket);
    }
}