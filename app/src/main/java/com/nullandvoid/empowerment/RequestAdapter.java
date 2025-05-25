package com.nullandvoid.empowerment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private List<RequestItem> RequestItems;

    public RequestAdapter(List<RequestItem> requestItems) {
        RequestItems = requestItems;
    }
    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages, parent, false);
        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, int position) {
        RequestItem requestItem = RequestItems.get(position);
        String header = requestItem.getItemName() + ": " + requestItem.getQuantity();
        String content = requestItem.getBio();

        holder.Header.setText(header);
        holder.Content.setText(content);
    }

    @Override
    public int getItemCount() {
        return RequestItems.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView Header, Content;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            Header = itemView.findViewById(R.id.messageHeader);
            Content = itemView.findViewById(R.id.messageContent);
        }
    }
}
