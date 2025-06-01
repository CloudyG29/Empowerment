package com.nullandvoid.empowerment.ui.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.nullandvoid.empowerment.Menu;
import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.ui.dashboard.ConfirmEmp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestUserAdapter extends RecyclerView.Adapter<RequestUserAdapter.ViewHolder> {
    private List<RequestUser> userList;
    private Context context;
    private String selecteditem;
    public static int q;

    public RequestUserAdapter(List<RequestUser> userList, Context context, String selecteditem) {
        this.userList = userList;
        this.context = context;
        this.selecteditem = selecteditem;
    }

    public void updateData(List<RequestUser> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, biography;
        CircleImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            quantity = itemView.findViewById(R.id.userQuantity);
            biography = itemView.findViewById(R.id.userBio);
            profilePic = itemView.findViewById(R.id.ProfilePic);
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
        Glide.with(context)
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.profilePic);

        holder.itemView.setOnClickListener(v->{
            Intent intent=new Intent(context, ConfirmEmp.class);
            Menu.showProgressBar();
            intent.putExtra("name",user.name);

            intent.putExtra("surname",user.surname);
            intent.putExtra("quantity", Integer.parseInt(String.valueOf(user.quantity)));
            q = user.quantity;
            intent.putExtra("biography",user.biography);
            intent.putExtra("selectedItem", selecteditem);
            intent.putExtra("photoUrl", user.getPhotoUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void setSelectedItem(String selecteditem) {
        this.selecteditem = selecteditem;
    }

}

