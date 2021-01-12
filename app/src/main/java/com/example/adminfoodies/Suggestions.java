package com.example.adminfoodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.Model.Getter_Setter_Compains;
import com.example.adminfoodies.Model.Getter_Setter_Suggestions;
import com.example.adminfoodies.ViewHolder.ComplainViewHolder;
import com.example.adminfoodies.ViewHolder.SuggestionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Suggestions extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Getter_Setter_Suggestions, SuggestionViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference suggestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        database = FirebaseDatabase.getInstance();
        suggestion = database.getReference("Suggestions");


        recyclerView = (RecyclerView) findViewById(R.id.suggestionList);
        recyclerView.setHasFixedSize(true);

//        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Getter_Setter_Suggestions, SuggestionViewHolder>(
                Getter_Setter_Suggestions.class,
                R.layout.suggestions_layout,
                SuggestionViewHolder.class,
                suggestion
        ) {
            @Override
            protected void populateViewHolder(SuggestionViewHolder viewHolder, final Getter_Setter_Suggestions model, int position1) {

                viewHolder.suggestion_id.setText(adapter.getRef(position1).getKey());
                viewHolder.suggestion_Title.setText(model.getTitle());
//                viewHolder.txtOrderStatus.setTextColor();

                viewHolder.suggestion_Msg.setText(model.getSuggestionMsg());
                viewHolder.suggestion_phone.setText(model.getPhone());
//                String.valueOf(model.getTimeStart());
//                viewHolder.timeStart.setText( String.valueOf(model.getTimeStart()));

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent intent = new Intent(OrderRequests.this, OrderTracker.class);

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

}
