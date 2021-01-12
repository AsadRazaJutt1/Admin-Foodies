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

public class ComplainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView order_id, order_Complain_Title, order_Complain_Msg, order_phone;
    public ImageView dotsForComp;
    private ItemClickListner itemClickListner;
    public ComplainViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id = (TextView)itemView.findViewById(R.id.order_id);
        order_Complain_Title = (TextView)itemView.findViewById(R.id.order_Complain_Title);
        order_Complain_Msg = (TextView)itemView.findViewById(R.id.order_Complain_Msg);
        order_phone = (TextView)itemView.findViewById(R.id.order_phone);
//        txtOrderPhone = (TextView)itemView.findViewById();
        dotsForComp = (ImageView) itemView.findViewById(R.id.dotsForComp);
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
        dotsForComp.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
                menu.add(0, 1, getAdapterPosition(), Common.DELETE);
            }
        });

    }
}
