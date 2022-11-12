package umn.ac.id.lanpu.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import umn.ac.id.lanpu.databinding.FragmentHomeBinding;

import umn.ac.id.lanpu.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final LinkedList<String[]> mHistoryList = new LinkedList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Recycler View
        String[] arr = new String[4];

        arr[0] = "24/10/2022 12:30:00"; arr[1] = "24/10/2022 17:30:00"; arr[2] = "ASDFGHJKL12567"; arr[3] = "Rp 2.000,-";
        mHistoryList.add(arr);

        arr[0] = "23/10/2022 12:30:00"; arr[1] = "24/10/2022 17:30:00"; arr[2] = "ASDFGHJKL12567"; arr[3] = "Rp 2.000,-";
        mHistoryList.add(arr);

        arr[0] = "24/10/2022 12:30:00"; arr[1] = "24/10/2022 17:30:00"; arr[2] = "ASDFGHJKL12567"; arr[3] = "Rp 2.000,-";
        mHistoryList.add(arr);

        mHistoryList.add(arr);
        mHistoryList.add(arr);

        RecyclerView mRecyclerView = binding.recyclerHistory;
        HistoryAdapter mAdapter = new HistoryAdapter(inflater.getContext(), mHistoryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}