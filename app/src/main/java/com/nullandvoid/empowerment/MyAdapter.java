package com.nullandvoid.empowerment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

   private List<MessageItem> messageItems;

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

        header = header.replace("ItemName", messageItem.getItemName());
        content = content.replace("ItemName", messageItem.getItemName());
        content = content.replace("Name", messageItem.getName());
        content = content.replace("Surname", messageItem.getSurname());
        content = content.replace("QUANTITY", String.valueOf(messageItem.getQuantity()));

        holder.Header.setText(header);
        holder.Content.setText(header);
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Header, Content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Header = itemView.findViewById(R.id.messageHeader);
            Content = itemView.findViewById(R.id.messageContent);

        }
    }
}
