package umn.ac.id.lanpu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    }

    @Override
    public int getItemCount() {
        return mDaftarKata.size();
    }

    class KataViewHolder extends RecyclerView.ViewHolder {
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
    }

    public void removeAll(int totalSize) {
        if (totalSize > 0) {
            for (int i=0; i<totalSize; i++) {
                int position = 0;
                mDaftarKata.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDaftarKata.size());
            }
        }
    }

    public int parseDate(String date) {  // "DD/MM/YYYY"
        String[] fDate = date.split("/");
        if (fDate.length != 3) return -1;
        String fFromDate = fDate[2] + fDate[1] + fDate[0];
        return Integer.parseInt(fFromDate);
    }

    public int parseDateTime(String dateTime) {  // "DD/MM/YYYY HH:MM:SS"
        String[] date = dateTime.split(" ");
        String[] fDate = date[0].split("/");
        if (fDate.length != 3) return -1;
        String fFromDate = fDate[2] + fDate[1] + fDate[0];
        return Integer.parseInt(fFromDate);
    }

    public void filterItem(String fromDate, String toDate) {

        int from = parseDate(fromDate);
        if (from == -1) return;

        int to = parseDate(toDate);
        if (to == -1) return;

        int totalSize = mDaftarKata.size();

        if (totalSize > 0 && from < to) {
            int position = 0;
            for (int i=0; i<totalSize; i++) {

                int entry = parseDateTime(mDaftarKata.get(position)[0]);
                int exit = parseDateTime(mDaftarKata.get(position)[1]);

                // Check if in range
                if ((entry == -1) || (exit == -1) || (entry < from && exit < from) || (entry > to && exit > to)) {
                    mDaftarKata.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDaftarKata.size());
                }
                else {
                    position++;
                }
            }
        }


    }

    public void addItem(String[] arr) {
        mDaftarKata.add(arr);
        notifyItemRangeChanged(mDaftarKata.size(), mDaftarKata.size());
    }
}
