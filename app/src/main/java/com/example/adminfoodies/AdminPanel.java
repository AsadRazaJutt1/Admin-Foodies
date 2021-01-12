package com.example.adminfoodies;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.adminfoodies.Common.Common;
import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.Model.Category;
import com.example.adminfoodies.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Parcelable;
import android.text.Html;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;


public class AdminPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    private AppBarConfiguration mAppBarConfiguration;
//    final DatabaseReference table_admin = database.getReference("");
    //ClipData.Item nav_log_out;
    MenuItem nav_log_out, nav_User;
    DrawerLayout drawer_layout;
    ActionBarDrawerToggle mToggle;

    RecyclerView.LayoutManager layoutManager;
    private Menu menu;

    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri newUri;
    TextView txtFullName, edtNameMenuUpdate, edtMenuTimingUpdate, btnUpdateImage, btnUploadImage;
    RecyclerView recycler_menu;
    Category addNewCategory;
    private final int SELECT_IMAGE = 0;
    Button btnSelectImage;
    Button btnUpdateMenu, cancel_popup;
    //    RecyclerView.LayoutManager layoutManager;
    LinearLayout dotLayout;
    ImageView dotsForMenu, viewImageUpdate;


    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Menu");

        setSupportActionBar(toolbar);

        //init Firebase

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanel.this, AddNewMenuItem.class);
                startActivity(intent);
//                showDialog();
//                finish();
            }
        });

        drawer_layout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(mToggle);
        mToggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());
//}
        //load menu
        dotsForMenu = (ImageView) findViewById(R.id.dotsForMenu);
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);

        recycler_menu.setLayoutManager(new GridLayoutManager(this, 1));

        loadMenu();

    }




    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.exit, null);
            Button btnExitYes = view.findViewById(R.id.btnExitYes);
//            Button btnExitNo = view.findViewById(R.id.btnExitNo);
            btnExitYes.setOnClickListener(new View.OnClickListener() {
                //                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    finishAffinity();
                }
            });

            AlertDialog.Builder alertDialogBox = new AlertDialog.Builder(this);
            alertDialogBox.setMessage("Are you sure you want to exit from application?");
            alertDialogBox.setView(view);

            alertDialogBox.show();
        }
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {

                viewHolder.txtMenuName.setText(model.getName());
//                viewHolder.timing.setText(model.getTiming());
                //Picasso.get().load(model.getTimeing());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);
//                final Category clickItem = model;
                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(AdminPanel.this, FoodList.class);


                        //because category id is key, so we get key of this item
                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);

                    }
                });

            }
        };
        //refresh if data change
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //@override
    public boolean onOptionsItemSelected(MenuItem item) {

        // if(item.getItemId()==R.id.menu_search)
        //startActivity(new Intent(Home.this, SearchActivity.class));

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("Statement with Empty body")

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference table_user = database.getReference("User");
        int id = menuItem.getItemId();

        if (id == R.id.nav_menu) {
            Intent intent = new Intent(AdminPanel.this, Restaurant_Map.class);
            startActivity(intent);
        } else if (id == R.id.complains) {

            Intent cartIntent = new Intent(AdminPanel.this, Complains.class);
            startActivity(cartIntent);

        } else if (id == R.id.suggestion) {

            Intent cartIntent = new Intent(AdminPanel.this, Suggestions.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_order) {

            Intent orderIntent = new Intent(AdminPanel.this, OrderRequests.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_log_out) {


            Intent signOut = new Intent(AdminPanel.this, MainActivity.class);
            signOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(signOut);



        } else if (id == R.id.nav_User) {
            Intent userInfo = new Intent(getApplicationContext(), UsersInfo.class);
            startActivity(userInfo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {

            updateMenu(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else {
            deleteMenu(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteMenu(String key) {
        category.child(key).removeValue();
    }


    private void updateMenu(final String key, final Category item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPanel.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("fill the information");
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_new_menu_layout, null);
        edtNameMenuUpdate = view.findViewById(R.id.edtNameMenuUpdate);
//        edtMenuTimingUpdate = view.findViewById(R.id.edtMenuTimingUpdate);
        viewImageUpdate = view.findViewById(R.id.viewImageUpdate);

        edtNameMenuUpdate.setText(item.getName());
//        edtMenuTimingUpdate.setText(item.getTiming());
        Picasso.get().load(item.getImage()).into(viewImageUpdate);


        btnUpdateImage = view.findViewById(R.id.btnUpdateImage);
        btnUpdateMenu = view.findViewById(R.id.btnUpdateMenu);
//        btnUploadImage=view.findViewById(R.id.btnUploadImage);

        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
//                chooseImage();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select the Image"), SELECT_IMAGE);
            }
        });
        btnUpdateMenu
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newUri != null) {

                            final ProgressDialog mDialog = new ProgressDialog(AdminPanel.this);
                            mDialog.setMessage("Wait a Moment.....");
                            mDialog.show();
//
                            String imageName = UUID.randomUUID().toString();
                            final StorageReference imageAddress = storageReference.child("images/*" + imageName);
                            imageAddress.putFile(newUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mDialog.dismiss();
                                    Toast.makeText(AdminPanel.this, "Successfully Uploaded !", Toast.LENGTH_SHORT).show();
                                    imageAddress.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
//                                    Toast.makeText(AdminPanel.this, "Clicked", Toast.LENGTH_SHORT).show();
                                            item.setName(edtNameMenuUpdate.getText().toString());
//                                            item.setTiming(edtMenuTimingUpdate.getText().toString());
                                            item.setImage(uri.toString());
                                            category.child(key).setValue(item);
                                            Toast.makeText(AdminPanel.this, "Updated", Toast.LENGTH_SHORT).show();

//                                    Toast.makeText(AdminPanel.this, "Updated", Toast.LENGTH_SHORT).show();
                                            mDialog.dismiss();
//
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mDialog.dismiss();
                                    Toast.makeText(AdminPanel.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    mDialog.setMessage("Uploaded! " + progress + "%");
                                }
                            });

                        } else {
                            if (item.getName().equals( edtNameMenuUpdate.getText().toString())) {
                                Toast.makeText(AdminPanel.this, "Please edit Menu", Toast.LENGTH_SHORT).show();
                            } else {
                                item.setImage(item.getImage());
                                item.setName(edtNameMenuUpdate.getText().toString());
//                                item.setTiming(edtMenuTimingUpdate.getText().toString());
                                category.child(key).setValue(item);
                                Toast.makeText(AdminPanel.this, "Updated", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AdminPanel.this, "Select a image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        alertDialog.setView(view);

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newUri = data.getData();
            viewImageUpdate.setImageURI(newUri);
//            btnSelectImage.setText("Change Image");
            btnUpdateImage.setText("Change Image");
// btnSelectImage.
        }
    }
}
