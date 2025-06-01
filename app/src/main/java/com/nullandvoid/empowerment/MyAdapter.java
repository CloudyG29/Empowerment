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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

   private List<MessageItem> messageItems;
    private SparseBooleanArray expandedPositions = new SparseBooleanArray();

    public MyAdapter(List<MessageItem> messageItems) {
        this.messageItems = messageItems;
    }



    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        MessageItem messageItem = messageItems.get(position);
        String header = "Approved request for ItemName";
        String content = "Name Surname has agreed to provide ItemName with quantity QUANTITY";
        String email = messageItem.getEmail();
        header = header.replace("ItemName", messageItem.getItemName());
        content = content.replace("ItemName", messageItem.getItemName());
        content = content.replace("Name", messageItem.getName());
        content = content.replace("Surname", messageItem.getSurname());
        content = content.replace("QUANTITY", String.valueOf(messageItem.getQuantity()));

        holder.Header.setText(header);
        holder.Content.setText(content);
        holder.Email.setText(email);

        boolean isExpanded = expandedPositions.get(position, false);
        holder.Content.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.Email.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            boolean currentlyExpanded = expandedPositions.get(position, false);
            expandedPositions.put(position, !currentlyExpanded);

            TransitionManager.beginDelayedTransition(holder.l);
            holder.Content.setVisibility(!currentlyExpanded ? View.VISIBLE : View.GONE);
            holder.Email.setVisibility(!currentlyExpanded ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Header, Content, Email;
        CardView cardView;
        LinearLayout l;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Header = itemView.findViewById(R.id.messageHeader);
            Content = itemView.findViewById(R.id.messageContent);
            Email = itemView.findViewById(R.id.messageEmail);
            cardView = itemView.findViewById(R.id.cardView);
            l = itemView.findViewById(R.id.rootLayout);
        }
    }
}
