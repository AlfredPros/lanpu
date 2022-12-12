package umn.ac.id.lanpu.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

import umn.ac.id.lanpu.databinding.FragmentHomeBinding;
import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final LinkedList<String[]> mHistoryList = new LinkedList<>();
    private DatePickerDialog datePickerDialogFrom;
    private DatePickerDialog datePickerDialogTo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TicketViewModel ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Date Picker
        initDatePickerFrom(inflater);
        binding.historyFromDate.setText(getTodaysDate());
        binding.historyFromDate.setOnClickListener(v -> {
            openDatePickerFrom(v);
        });
        initDatePickerTo(inflater);
        binding.historyToDate.setText(getTodaysDate());
        binding.historyToDate.setOnClickListener(v -> {
            openDatePickerTo(v);
        });

        RecyclerView mRecyclerView = binding.recyclerHistory;
        HistoryAdapter mAdapter = new HistoryAdapter(inflater.getContext(), mHistoryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));


        // Fetch Database
        LiveData<DataSnapshot> ticketsLiveData = ticketViewModel.getAllTicketofUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        ticketsLiveData.observe(getViewLifecycleOwner(), dataSnapshot -> {
            mAdapter.removeAll(mAdapter.getItemCount());
            //String ticketID = dataSnapshot.getValue(String.class);
            for (DataSnapshot ticketQuery : dataSnapshot.getChildren()) {
                Ticket ticket = ticketQuery.getValue(Ticket.class);
                assert ticket != null;
                ticket.ticketID = ticketQuery.getKey();

                String[] arr = new String[4];
                arr[0] = String.valueOf(ticket.entryTime);
                arr[1] = String.valueOf(ticket.exitTime);
                arr[2] = String.valueOf(ticket.ticketID);

                NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                arr[3] = cf.format(ticket.price).replace("p", "p ");

                if (ticket.exitTime != null) {
                    mAdapter.addItem(arr);
                }
            }
        });


        binding.historyFilterButton.setOnClickListener(v -> {
            // Filter
            String fromDate = binding.historyFromDate.getText().toString();
            String toDate = binding.historyToDate.getText().toString();

            if (!fromDate.equals("") && !toDate.equals("")) {
                mAdapter.filterItem(fromDate, toDate);
            }
            else {
                Toast.makeText(v.getContext(), "Date must be entered!", Toast.LENGTH_LONG).show();
            }
        });

        binding.historyResetButton.setOnClickListener(v -> {
            // Remove all
            mAdapter.removeAll(mHistoryList.size());
            ticketViewModel.eraseUserTickets(FirebaseAuth.getInstance().getCurrentUser().getUid());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return day + "/" + month + "/" + year;
    }

    private void initDatePickerFrom(@NonNull LayoutInflater inflater)
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                binding.historyFromDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogFrom = new DatePickerDialog(inflater.getContext(), style, dateSetListener, year, month, day);
        datePickerDialogFrom.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    public void openDatePickerFrom(View view)
    {
        datePickerDialogFrom.show();
    }

    private void initDatePickerTo(@NonNull LayoutInflater inflater)
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                binding.historyToDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogTo = new DatePickerDialog(inflater.getContext(), style, dateSetListener, year, month, day);
        datePickerDialogTo.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    public void openDatePickerTo(View view)
    {
        datePickerDialogTo.show();
    }
}