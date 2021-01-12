package com.example.adminfoodies;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminfoodies.Common.Common;
import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.Model.Category;
import com.example.adminfoodies.Model.Food;
import com.example.adminfoodies.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FoodList extends AppCompatActivity {

    private TextView restaurant_timing;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Category currentCategory;
    FirebaseDatabase database;
    DatabaseReference foodList, table_catagory;
    String categoryIdForTable = "";
    String categoryId = "";
    Uri newUri;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //search Functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    private final int SELECT_IMAGE_FOR_FOOD = 0;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView viewImage4Food;
    Button select_image4Food;
    //Favorites
    Button cancel4Food, update4Food;
//    DatabaseReference food;
    TextView image_Select4Food;
    TextView edtNameFood, edtDescription,  edtPrice;
    MaterialEditText edtRestName,edtRestTiming,edtRestCity,edtOpenTiming,edtCloseTiming;

    //Database localDB;
    //Facebook share
    // CallbackManager callbackManager;
    //ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        database = FirebaseDatabase.getInstance();
        restaurant_timing = (TextView) findViewById(R.id.restaurant_timing);

        // For Update
        viewImage4Food = (ImageView) findViewById(R.id.viewImage4Food);
        image_Select4Food = (TextView) findViewById(R.id.image_Select4Food);

        table_catagory = database.getReference("Category");
               if (getIntent() != null)
        categoryIdForTable = getIntent().getStringExtra("CategoryId");

//        if (!categoryIdForTable.isEmpty() && categoryIdForTable != null) {
//
//            loadTiming(categoryIdForTable);
//        } else {
//            Toast.makeText(this, "Error on restaurant time", Toast.LENGTH_SHORT).show();
//        }
        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FloatingActionButton fabFood = findViewById(R.id.fabFood);
        fabFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodList.this, AddNewFood_Item.class);
                intent.putExtra("CategoryId", categoryIdForTable);
                startActivity(intent);
//                showDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //get intent here
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");

        if (!categoryId.isEmpty() && categoryId != null) {

            loadListFood(categoryId);
        }

        //search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your Food");

        //materialSearchBar.setSpeechMode(false);       no need bcz we already define in XML
        loadSuggest();    //write functoin to load suggest from database
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text,  we will change suggest list

                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }

                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                //when search bar is close
                //restore original suggest adapter
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //when search finished
                //show result of search bar
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("name").equalTo(text.toString())     //compare name
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_name.setText(model.getName());
                viewHolder.restaurant_timing.setText("Restaurant Timing: "+model.getOpening()+" - "+model.getClosing());
                Picasso.get().load(model.getImage()).into(viewHolder.food_image);

                final Food local = model;


                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //start new activity
//                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
//                        foodDetail.putExtra("FoodId", searchAdapter.getRef(position).getKey());  //send food id to new activity
//                        startActivity(foodDetail);

                    }
                });
            }
        };

        recyclerView.setAdapter(searchAdapter);      //set adapter for recycler view is search result
    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName());    // add name of food to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

//    private void loadTiming(final String categoryIdForTable) {
//        table_catagory.child(categoryIdForTable).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                currentCategory = dataSnapshot.getValue(Category.class);
//
//                //set Timing
//
//                timing.setText(currentCategory.getTiming());
//                // Toast.makeText(FoodList.this, "timing", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId) //like: select * from foods where menu id =
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_name.setText(model.getName());
                viewHolder.restaurant_timing.setText("Restaurant Timing: "+model.getOpening()+" - "+model.getClosing());
                Picasso.get().load(model.getImage()).into(viewHolder.food_image);
//viewHolder.timing.setText((model.get));
                final Food local = model;

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //start new activity
//                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
//                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());  //send food id to new activity
//                        startActivity(foodDetail);

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        //set adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
//            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
            updateFood(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
//            updateFood(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
            // we are reuse AddNewMenuItem Class for update menu item
//
////            intent.putExtra("update", Common.UPDATE);
//            intent.putExtra("key", adapter.getRef(item.getOrder()).getKey());
//            intent.putExtra("OrderName", (Parcelable) adapter.getItem(item.getOrder()));
        } else {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {

        foodList.child(key).removeValue();
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
    }

//    private void deleteMenu(String key) {
//        foodList.child(key).removeValue();
//        Toast.makeText(this, "Menu Deleted", Toast.LENGTH_SHORT).show();
//    }

    private void updateFood(final String key, final Food item) {
//        Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Update Food");
        alertDialog.setMessage("fill the information");
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_food_new_item, null);
        edtNameFood = view.findViewById(R.id.edtNameFood);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtPrice = view.findViewById(R.id.edtPrice);

        edtRestName = (MaterialEditText) view.findViewById(R.id.edtRestName);
        edtRestCity = (MaterialEditText) view.findViewById(R.id.edtRestCity);

        edtNameFood.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());

        edtRestCity.setText(item.getCity());


//        viewImageUpdate = view.findViewById(R.id.viewImageUpdate);

        select_image4Food = view.findViewById(R.id.select_image4Food);
        update4Food = view.findViewById(R.id.update4Food);
        cancel4Food = view.findViewById(R.id.cancel4Food);
//        cancel_popup=view.findViewById(R.id.cancel_popup)

        select_image4Food.setOnClickListener(new View.OnClickListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
//                chooseImage();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select the Image"), SELECT_IMAGE_FOR_FOOD);
            }
        });
        update4Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newUri != null) {

                    final ProgressDialog mDialog = new ProgressDialog(FoodList.this);
                    mDialog.setMessage("Wait a Moment.....");
                    mDialog.show();
//
                    String imageName = UUID.randomUUID().toString();
                    final StorageReference imageAddress = storageReference.child("images/*" + imageName);
                    imageAddress.putFile(newUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Successfully Uploaded !", Toast.LENGTH_SHORT).show();
                            imageAddress.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                    Toast.makeText(AdminPanel.this, "Clicked", Toast.LENGTH_SHORT).show();

                                    item.setImage(uri.toString());
                                    item.setName(edtNameFood.getText().toString());
                                    item.setDescription(edtDescription.getText().toString());
                                    item.setPrice(edtPrice.getText().toString());
                                    item.setRestaurant(edtRestName.getText().toString());
                                    item.setOpening(edtOpenTiming.getText().toString());
                                    item.setOpening(edtOpenTiming.getText().toString());
                                    item.setPrice(edtRestCity.getText().toString());
                                    foodList.child(key).setValue(item);
//                                    Toast.makeText(FoodList.this, "Updated", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
//
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded!! " + progress + "%");
                        }
                    });
                }
            }
        });


        alertDialog.setView(view);    // add edit text to alert dialog

        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_FOR_FOOD && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newUri = data.getData();
//            viewImage4Food.setImageURI(newUri);
            select_image4Food.setText("Change Image");
//            image_Select4Food.setText("Selected");
// btnSelectImage.
        }
    }
}
