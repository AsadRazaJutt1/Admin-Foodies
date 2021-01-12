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


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView txtMenuName, timing;
    public ImageView imageView;
    public ImageView dotsForMenu;
    private ItemClickListner itemClickListner;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName = (TextView) itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
//        timing = (TextView) itemView.findViewById(R.id.restaurant_timing);
        dotsForMenu = (ImageView) itemView.findViewById(R.id.dotsForMenu);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public ItemClickListner getItemClickListner() {
        return itemClickListner;
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(), false);
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
