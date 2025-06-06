package com.nullandvoid.empowerment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder>{

    private List<DonationItem> DonationItems;

    public DonationAdapter(List<DonationItem> donationItems) {
        DonationItems = donationItems;
    }

    @NonNull
    @Override
    public DonationAdapter.DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new DonationAdapter.DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationAdapter.DonationViewHolder holder, int position) {
        DonationItem donationItem = DonationItems.get(position);
        String header = donationItem.getItemName() + ": " + donationItem.getQuantity();

        holder.Header.setText(header);

    }

    @Override
    public int getItemCount() {
        return DonationItems.size();
    }

    static class DonationViewHolder extends RecyclerView.ViewHolder {

        TextView Header;
        CardView cardView;
        LinearLayout l;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            Header = itemView.findViewById(R.id.Header);
            cardView = itemView.findViewById(R.id.cardView);
            l = itemView.findViewById(R.id.rootLayout);
        }
    }
}
