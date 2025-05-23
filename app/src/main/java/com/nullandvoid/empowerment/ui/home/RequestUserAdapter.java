package com.nullandvoid.empowerment.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nullandvoid.empowerment.R;

import java.util.List;

public class RequestUserAdapter extends RecyclerView.Adapter<RequestUserAdapter.ViewHolder> {
    private List<RequestUser> userList;

    public RequestUserAdapter(List<RequestUser> userList) {
        this.userList = userList;
    }
    public void updateData(List<RequestUser> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, biography;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            quantity = itemView.findViewById(R.id.userQuantity);
            biography = itemView.findViewById(R.id.userBio);
        }
    }

    @NonNull
    @Override
    public RequestUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestUserAdapter.ViewHolder holder, int position) {
        RequestUser user = userList.get(position);
        holder.name.setText(user.name + " " + user.surname);
        holder.quantity.setText("Quantity: " + user.quantity);
        holder.biography.setText("Bio: " + user.biography);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

