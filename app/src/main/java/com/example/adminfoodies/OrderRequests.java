package com.example.adminfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminfoodies.Common.Common;
import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.Model.Food;
import com.example.adminfoodies.Model.Order;
import com.example.adminfoodies.Model.Request;
import com.example.adminfoodies.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import static com.example.adminfoodies.Common.Common.convertCodeToStatus;

public class OrderRequests extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    Food food;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_requests);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");


        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);

//        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, int position) {


                viewHolder.order_id.setText("Order Number : " + adapter.getRef(position).getKey());
                viewHolder.order_status.setText("Order Status : " + convertCodeToStatus(model.getStatus()));
//                viewHolder.txtOrderStatus.setTextColor();

                viewHolder.order_address.setText("Order Address : " + model.getAddress());
                viewHolder.order_phone.setText("Order Phone : " + model.getPhone());
                viewHolder.requestTotal.setText("Total : " + model.getTotal());
//                viewHolder.timeStart.setText( String.valueOf(model.getTimeStart()));

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent intent = new Intent(OrderRequests.this, OrderTracker.class);
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderRequests.this);
                        alertDialog.setTitle("Order Details");
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.food_request, null);

                        final TextView requestName;
                        final TextView requestQuantity;

                        requestName = (TextView) view1.findViewById(R.id.food_name);
                        requestQuantity = (TextView) view1.findViewById(R.id.food_quantity);

                        requests.child(adapter.getRef(position).getKey()).child("foods")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        food = dataSnapshot.getValue(Food.class);
                                        order = dataSnapshot.getValue(Order.class);
                                        requestName.setText("Name : " + order.getProductName());
                                        requestQuantity.setText("Quantity : " + order.getQuantity());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
//                        alertDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requests.child(adapter.getRef(position).getKey()).child("foods")
//                                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                                request = new Request("3");
////                                                requests.child(adapter.getRef(position).getKey()).child("foods").child("status").setValue(request);
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//                            }
//                        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requests.child(adapter.getRef(position).getKey()).removeValue();
//                            }
//                        });

                        alertDialog.setView(view1);

                        alertDialog.show();
//                        Common.currentRequest = model;
                        //because category id is key, so we get key of this item
//                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
//                        startActivity(intent);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
//            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
            updateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
//            updateFood(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
            // we are reuse AddNewMenuItem Class for update menu item
//
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
////            intent.putExtra("update", Common.UPDATE);
//            intent.putExtra("key", adapter.getRef(item.getOrder()).getKey());
//            intent.putExtra("OrderName", (Parcelable) adapter.getItem(item.getOrder()));
        } else {
            delete(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void updateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(OrderRequests.this);
        alertDialog1.setTitle("Add New User");
        alertDialog1.setMessage("fill the information");
        LayoutInflater layoutInflater = getLayoutInflater();
        View viewOrderUpdate = layoutInflater.inflate(R.layout.order_update, null);
        final MaterialSpinner spinnerForOrder = (MaterialSpinner) viewOrderUpdate.findViewById(R.id.spinnerForOrder);
        spinnerForOrder.setItems("Confirmed", "Deny", "Placed", "On The Way", "Cancel", "Delivered");
        alertDialog1.setView(viewOrderUpdate);
        final String currentKey = key;
        alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinnerForOrder.getSelectedIndex()));
                requests.child(currentKey).setValue(item);
            }
        });
        alertDialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog1.show();
    }

    private void delete(String key) {
        requests.child(key).removeValue();
    }
}
