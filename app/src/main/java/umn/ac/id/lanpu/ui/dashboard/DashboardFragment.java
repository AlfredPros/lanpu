package umn.ac.id.lanpu.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import umn.ac.id.lanpu.ProcessingActivity;
import umn.ac.id.lanpu.R;
import umn.ac.id.lanpu.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private int mode = 0;
    DashboardViewModel dashboardViewModel;

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
        qrImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProcessingActivity.class);
                intent.putExtra("load_mode", 0);
                startActivityForResult(intent, 1);
            }
        });

        nimTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProcessingActivity.class);
                intent.putExtra("load_mode", 1);
                startActivityForResult(intent, 1);
            }
        });

        dashboardViewModel.getNim().observe(getViewLifecycleOwner(), nimTextView::setText);
        dashboardViewModel.getName().observe(getViewLifecycleOwner(), nameTextView::setText);
        dashboardViewModel.getDuration(this.mode).observe(getViewLifecycleOwner(), durationTextView::setText);
//        final ImageView qrImageView = binding.imageView;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mode =data.getIntExtra("mode", 1);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                mode = 0;
            }
        }
        TextView durationTextView = binding.durationTextview;
        MaterialCardView statusCard = binding.statusCard;
        dashboardViewModel.getDuration(this.mode).observe(getViewLifecycleOwner(), durationTextView::setText);
        if (mode == 1) {
            statusCard.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else {
            statusCard.setCardBackgroundColor(getResources().getColor(R.color.red));
        }
    }
}