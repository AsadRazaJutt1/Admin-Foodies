package com.example.adminfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminfoodies.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;


public class SignUp extends AppCompatActivity {

    MaterialEditText edtName, edtEmail, edtPhone, edtPassword, secretBox;
    Button btnSignUp;
    TextView term;
    CheckBox checkBox;
    Bundle bundle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText) findViewById(R.id.edtName);
//        edtEmail=(MaterialEditText)findViewById(R.id.edtEmail);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        //    secretBox=(MaterialEditText)findViewById(R.id.secret);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        term = (TextView) findViewById(R.id.term);
        checkBox = (CheckBox) findViewById(R.id.checked);

        //       bundle =new Bundle();


        term.setText("Terms \u0026 Conditions");
        // in it firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please wait..");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // if already user has phone

                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                                mDialog.dismiss();

                                Toast.makeText(SignUp.this, "Phone number already register as a Staff", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
//                                            Intent intent = new Intent(getApplicationContext(),VerifyUserNo.class);

//                                            intent.putExtra("edtName",edtName.getText().toString());
//                                            intent.putExtra("edtEmail",edtEmail.getText().toString());
//                                            intent.putExtra("edtPhone",edtPhone.getText().toString());
//                                            intent.putExtra("edtPassword",edtPassword.getText().toString());

//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                        Toast.makeText(SignUp.this, "Phone Code And Data has been Sent.", Toast.LENGTH_SHORT).show();
//                                        //Toast.makeText(SignUpActivity.this, "Account on FireBase", Toast.LENGTH_LONG).show();
//                                        startActivity(intent);
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);
//
                                Toast.makeText(SignUp.this, "Sign Up successfully!!", Toast.LENGTH_SHORT).show();
//                                            finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("You must agreed on the Terms and Conditions.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }
}
