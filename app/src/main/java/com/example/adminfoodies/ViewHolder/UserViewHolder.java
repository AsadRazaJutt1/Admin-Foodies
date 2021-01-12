package com.example.adminfoodies.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminfoodies.Common.Common;
import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView userName, userPhone, userPassword, txtOrderAddress;
    public ImageView dotsForMenu;
    private ItemClickListner itemClickListner;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = (TextView)itemView.findViewById(R.id.userName);
        userPhone = (TextView)itemView.findViewById(R.id.userPhone);
        userPassword = (TextView)itemView.findViewById(R.id.userPassword);
//        txtOrderPhone = (TextView)itemView.findViewById();
        dotsForMenu = (ImageView) itemView.findViewById(R.id.dotsForMenu);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }



    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        dotsForMenu.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
                menu.add(0, 1, getAdapterPosition(), Common.DELETE);
            }
        });

    }
}
