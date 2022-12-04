package umn.ac.id.lanpu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;

import umn.ac.id.lanpu.databinding.FragmentHomeBinding;

import umn.ac.id.lanpu.R;
import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final LinkedList<String[]> mHistoryList = new LinkedList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        TicketViewModel ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);


        LiveData<DataSnapshot> ticketsLiveData = ticketViewModel.getAllTicketofUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ticketsLiveData.observe(getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot ticketQuery : dataSnapshot.getChildren()) {
                    Ticket ticket = ticketQuery.getValue(Ticket.class);
                    ticket.ticketID = ticketQuery.getKey();
                    Log.d("Ticket", ticket.ticketID);
                }
            }
        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Recycler View
        String[] arr1 = new String[4];
        arr1[0] = "24/10/2022 12:30:00"; arr1[1] = "24/10/2022 17:30:00"; arr1[2] = "ASDFGHJKL12567"; arr1[3] = "Rp 2.000,-";
        mHistoryList.add(arr1);

        String[] arr2 = new String[4];
        arr2[0] = "21/11/2022 12:00:00"; arr2[1] = "21/11/2022 12:30:00"; arr2[2] = "MNOPQRSTU34890"; arr2[3] = "Rp 5.000,-";
        mHistoryList.add(arr2);

        String[] arr3 = new String[4];
        arr3[0] = "20/13/2022 12:40:00"; arr3[1] = "20/13/2022 13:30:00"; arr3[2] = "VWXYZABCD16358"; arr3[3] = "Rp 2.000,-";
        mHistoryList.add(arr3);

        mHistoryList.add(arr3);
        mHistoryList.add(arr3);

        RecyclerView mRecyclerView = binding.recyclerHistory;
        HistoryAdapter mAdapter = new HistoryAdapter(inflater.getContext(), mHistoryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        binding.historyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Filter
                String fromDate = binding.historyFromDate.getText().toString();
                String toDate = binding.historyToDate.getText().toString();

                if (!fromDate.equals("") && !toDate.equals("")) {
                    mAdapter.filterItem(mHistoryList.size());
                }
                else {
                    Toast.makeText(v.getContext(), "Date must be entered!", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.historyResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove all
                mAdapter.removeAll(mHistoryList.size());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}