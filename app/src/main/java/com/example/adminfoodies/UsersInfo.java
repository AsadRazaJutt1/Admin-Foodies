package com.example.adminfoodies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminfoodies.Common.Common;
import com.example.adminfoodies.Interface.ItemClickListner;
import com.example.adminfoodies.Model.Request;
import com.example.adminfoodies.Model.User;
import com.example.adminfoodies.ViewHolder.OrderViewHolder;
import com.example.adminfoodies.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class UsersInfo extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference user_table;

    MaterialEditText edtName1, edtPhone1, edtPassword1;
      EditText code1;
    Button addNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);
        database = FirebaseDatabase.getInstance();
        user_table = database.getReference("User");


        recyclerView = (RecyclerView) findViewById(R.id.listUser);
        addNewUser = (Button) findViewById(R.id.addNewUser);
        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(UsersInfo.this);
                alertDialog1.setTitle("Add New User");
                alertDialog1.setMessage("fill the information");
                LayoutInflater layoutInflater = getLayoutInflater();
                View viewForUser = layoutInflater.inflate(R.layout.add_new_user, null);
                edtName1 = viewForUser.findViewById(R.id.edtName1);
                edtPhone1 = viewForUser.findViewById(R.id.edtPhone1);
                edtPassword1 = viewForUser.findViewById(R.id.edtPassword1);
                code1 = viewForUser.findViewById(R.id.code1);
                alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog mDialog1 = new ProgressDialog(UsersInfo.this);
                        mDialog1.setMessage("Please wait..");
                        mDialog1.show();

                        user_table.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // if already user has phone

                                if (dataSnapshot.child(edtPhone1.getText().toString()).exists()) {

                                    mDialog1.dismiss();

                                    Toast.makeText(UsersInfo.this, "Phone number already register as a Staff", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog1.dismiss();

                                    User user1 = new User(edtName1.getText().toString(), "false", edtPassword1.getText().toString());
                                    user_table.child(code1.getText().toString()+""+edtPhone1.getText().toString()).setValue(user1);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                alertDialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog1.setView(viewForUser);
                alertDialog1.show();

            }
        });
        recyclerView.setHasFixedSize(true);

//        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadUser();
    }

    private void loadUser() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.user_profile_layout,
                UserViewHolder.class,
                user_table) {
            @Override
            protected void populateViewHolder(UserViewHolder userViewHolder, User user, int i) {
                userViewHolder.userName.setText("Name: "+ user.getName());
                userViewHolder.userPhone.setText("Phone No: "+adapter.getRef(i).getKey());
                userViewHolder.userPassword.setText("Password: "+user.getPassword());

                userViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

//    public void addNewUser(View view) {
//
//        Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
//    }
}