package umn.ac.id.lanpu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.LinkedList;

import umn.ac.id.lanpu.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.KataViewHolder> {

    private final LinkedList<String[]> mDaftarKata;
    private final LayoutInflater mInflater;

    HistoryAdapter(Context context, LinkedList<String[]> daftarKata) {
        mInflater = LayoutInflater.from(context);
        mDaftarKata = daftarKata;
    }

    @NonNull
    @Override
    public HistoryAdapter.KataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.history_item, parent, false);
        return new KataViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.KataViewHolder holder, int position) {
        String[] mCurrent = mDaftarKata.get(position);

        holder.entryTimeView.setText(mCurrent[0]);
        holder.outTimeView.setText(mCurrent[1]);
        holder.ticketNumberView.setText(mCurrent[2]);
        holder.priceView.setText(mCurrent[3]);

        /*
        switch(mCurrent) {
            case "Video 1": {
                holder.kataItemView.setText("idk");
                break;
            }
            case "Video 2": {
                holder.kataItemView.setText("moe");
                break;
            }
            case "Video 3": {
                holder.kataItemView.setText("le what");
                break;
            }
        }
        */

        //holder.kataItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mDaftarKata.size();
    }

    class KataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView entryTimeView;
        public final TextView outTimeView;
        public final TextView ticketNumberView;
        public final TextView priceView;
        final HistoryAdapter mAdapter;

        public KataViewHolder(@NonNull View itemView, HistoryAdapter adapter) {
            super(itemView);
            entryTimeView = (TextView) itemView.findViewById(R.id.entry_time);
            outTimeView = (TextView) itemView.findViewById(R.id.out_time);
            ticketNumberView = (TextView) itemView.findViewById(R.id.ticket_number);
            priceView = (TextView) itemView.findViewById(R.id.price);
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
        }
    }
}