package umn.ac.id.lanpu.ui.dashboard;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import umn.ac.id.lanpu.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView nimTextView = binding.nimTextview;
        final TextView nameTextView = binding.nameTextview;
        dashboardViewModel.getNim().observe(getViewLifecycleOwner(), nimTextView::setText);
        dashboardViewModel.getName().observe(getViewLifecycleOwner(), nameTextView::setText);
//        dashboardViewModel.getCurrDate().observe(getViewLifecycleOwner(), dateTextView::setText);
//        dashboardViewModel.getCurrDate().observe(getViewLifecycleOwner(), dateTextView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}