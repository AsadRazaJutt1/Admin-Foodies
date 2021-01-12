package com.example.adminfoodies.Helper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adminfoodies.Add_Restaurant;
import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.R;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<PlacesPOJO.CustomA> stLstStores;
    private List<StoreModel> models;

    public RecyclerViewAdapter(List<PlacesPOJO.CustomA> stores, List<StoreModel> storeModels) {
        stLstStores = stores;
        models = storeModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setData(holder, models.get(holder.getAdapterPosition()));
        holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Context context = view.getContext();
                Intent intent = new Intent(context, Add_Restaurant.class);
                intent.putExtra("name",holder.model.name);
                intent.putExtra("address",holder.model.address);
                intent.putExtra("id",holder.model.txtStoreID);
                ((Activity) context).startActivityForResult(intent,1);
//                Toast.makeText(context, "" + holder.model.txtStoreID, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return Math.min(models.size(), stLstStores.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtStoreName;
        TextView txtStoreAddr;
        TextView txtStoreDist, txtStoreID;
        StoreModel model;
        private ItemClickListner itemClickListner;

        public void setItemClickListner(ItemClickListner itemClickListner) {
            this.itemClickListner = itemClickListner;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtStoreDist = (TextView) itemView.findViewById(R.id.txtStoreDist);
            this.txtStoreName = (TextView) itemView.findViewById(R.id.txtStoreName);
            this.txtStoreAddr = (TextView) itemView.findViewById(R.id.txtStoreAddr);
            this.txtStoreID = (TextView) itemView.findViewById(R.id.txtStoreID);
            itemView.setOnClickListener(this);
        }

        public void setData(MyViewHolder holder, StoreModel storeModel) {
            this.model = storeModel;
            holder.txtStoreDist.setText("Rating: " + model.rating);
            holder.txtStoreName.setText(model.name);
            holder.txtStoreAddr.setText(model.address);
            holder.txtStoreID.setText(model.txtStoreID);
//            Context context=view.getContext();
//            Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListner.onClick(v, getAdapterPosition(), false);
        }
    }
}

