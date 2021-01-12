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


public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        , View.OnCreateContextMenuListener {

    public TextView food_name;
    public ImageView food_image;
    public TextView restaurant_timing;
    public ImageView dotsForF;
    private ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        food_name = (TextView)itemView.findViewById(R.id.food_name);
        food_image = (ImageView)itemView.findViewById(R.id.food_image);
        restaurant_timing = (TextView)itemView.findViewById(R.id.restaurant_timing);
        dotsForF = (ImageView) itemView.findViewById(R.id.dotsForFood);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        dotsForF.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
                menu.add(0, 1, getAdapterPosition(), Common.DELETE);
            }
        });
    }
}
