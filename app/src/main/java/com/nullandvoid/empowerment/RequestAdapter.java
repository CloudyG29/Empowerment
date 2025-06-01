package com.nullandvoid.empowerment;

import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private List<RequestItem> RequestItems;

    private SparseBooleanArray expandedPositions = new SparseBooleanArray();

    public RequestAdapter(List<RequestItem> requestItems) {
        RequestItems = requestItems;
    }
    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, int position) {
        RequestItem requestItem = RequestItems.get(position);
        String header = requestItem.getItemName() + ": " + requestItem.getQuantity();
        String content = requestItem.getBio();

        holder.Header.setText(header);
        holder.Content.setText(content);

        boolean isExpanded = expandedPositions.get(position, false);
        holder.Content.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            boolean currentlyExpanded = expandedPositions.get(position, false);
            expandedPositions.put(position, !currentlyExpanded);

            TransitionManager.beginDelayedTransition(holder.l);
            holder.Content.setVisibility(!currentlyExpanded ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return RequestItems.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView Header, Content;
        CardView cardView;
        LinearLayout l;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            Header = itemView.findViewById(R.id.Header);
            Content = itemView.findViewById(R.id.Content);
            cardView = itemView.findViewById(R.id.cardView);
            l = itemView.findViewById(R.id.rootLayout);
        }
    }
}
