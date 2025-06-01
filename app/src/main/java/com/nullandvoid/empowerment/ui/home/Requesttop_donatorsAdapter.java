package com.nullandvoid.empowerment.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nullandvoid.empowerment.R;

import java.util.List;

public class Requesttop_donatorsAdapter extends RecyclerView.Adapter<Requesttop_donatorsAdapter.MyViewHolder> {
    private Context context;
    private List<top_donators> userList;

    public Requesttop_donatorsAdapter(List<top_donators> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        top_donators user = userList.get(position);
        holder.name.setText(user.name);
        holder.surname.setText(user.surname);
        holder.numDonations.setText(String.valueOf(user.num_donations));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<top_donators> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname, numDonations;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            surname = itemView.findViewById(R.id.surname);
            numDonations = itemView.findViewById(R.id.num_donations);
        }
    }
}