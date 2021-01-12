package com.example.adminfoodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminfoodies.Model.Restaurant_Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Add_Restaurant extends AppCompatActivity {
    TextView nameAdd, AddressAdd, idAdd;
    MaterialEditText timingAdd, edtPhoneAdd, edtPasswordAdd;
    EditText codeAdd1;
    Button btnAdd;

    String name = "", address = "", id = "";

    FirebaseDatabase database;
    DatabaseReference reference_1, reference_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        nameAdd = (TextView) findViewById(R.id.nameAdd);
        AddressAdd = (TextView) findViewById(R.id.AddressAdd);
        idAdd = (TextView) findViewById(R.id.idAdd);

        timingAdd = (MaterialEditText) findViewById(R.id.timingAdd);
        codeAdd1 = (EditText) findViewById(R.id.codeAdd1);
        edtPhoneAdd = (MaterialEditText) findViewById(R.id.edtPhoneAdd);
        edtPasswordAdd = (MaterialEditText) findViewById(R.id.edtPasswordAdd);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        id = getIntent().getStringExtra("id");

        nameAdd.setText("Name: " + name);
        AddressAdd.setText("Address: " + address);
        idAdd.setText("ID: " + id);

        database = FirebaseDatabase.getInstance();
        reference_1 = database.getReference("Restaurants");
        reference_2 = database.getReference("Restaurants_id");

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(Add_Restaurant.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();
                reference_1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhoneAdd.getText().toString()).exists()) {
//
                            mDialog.dismiss();

                            Toast.makeText(Add_Restaurant.this, "Phone number already register as a Restaurant", Toast.LENGTH_SHORT).show();
                        } else {

                            reference_2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(id).exists()) {
//
                                        mDialog.dismiss();

                                        Toast.makeText(Add_Restaurant.this, "Phone number already register as a Restaurant", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mDialog.dismiss();
////
                                        Restaurant_Profile restaurant_1 = new Restaurant_Profile(name, codeAdd1.getText().toString() + "" + edtPhoneAdd.getText().toString(), edtPasswordAdd.getText().toString(), address, id, timingAdd.getText().toString());
                                        reference_1.child(codeAdd1.getText().toString() + "" + edtPhoneAdd.getText().toString()).setValue(restaurant_1);

                                        Restaurant_Profile restaurant_2 = new Restaurant_Profile(name, codeAdd1.getText().toString() + "" + edtPhoneAdd.getText().toString(), edtPasswordAdd.getText().toString(), address, id, timingAdd.getText().toString());
                                        reference_2.child(id).setValue(restaurant_2);
//
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

//
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
