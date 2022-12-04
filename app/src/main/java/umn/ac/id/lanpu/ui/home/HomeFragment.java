package umn.ac.id.lanpu.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedList;
import java.util.Objects;

import umn.ac.id.lanpu.databinding.FragmentHomeBinding;
import umn.ac.id.lanpu.ui.dashboard.Ticket;
import umn.ac.id.lanpu.ui.dashboard.TicketViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final LinkedList<String[]> mHistoryList = new LinkedList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TicketViewModel ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


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
                arr[3] = String.valueOf(ticket.price);

                mAdapter.addItem(arr);
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
            ticketViewModel.getActiveTicketofUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}