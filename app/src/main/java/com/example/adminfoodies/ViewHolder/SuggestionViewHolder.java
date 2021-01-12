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

public class SuggestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView suggestion_id, suggestion_Title, suggestion_Msg, suggestion_phone;
    public ImageView dotsForSuggestion;
    private ItemClickListner itemClickListner;

    public SuggestionViewHolder(@NonNull View itemView) {
        super(itemView);
        suggestion_id = (TextView) itemView.findViewById(R.id.suggestion_id);
        suggestion_Title = (TextView) itemView.findViewById(R.id.suggestion_Title);
        suggestion_Msg = (TextView) itemView.findViewById(R.id.suggestion_Msg);
        suggestion_phone = (TextView) itemView.findViewById(R.id.suggestion_phone);
//        txtOrderPhone = (TextView)itemView.findViewById();
//        dotsForSuggestion = (ImageView) itemView.findViewById(R.id.dotsForSuggestion);
//        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        dotsForSuggestion.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
//                menu.add(0, 1, getAdapterPosition(), Common.DELETE);
//            }
//        });
//
//    }
}
