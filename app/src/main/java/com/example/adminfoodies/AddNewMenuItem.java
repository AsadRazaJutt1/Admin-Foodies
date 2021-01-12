package com.example.adminfoodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminfoodies.Model.Category;
import com.example.adminfoodies.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.UUID;

public class AddNewMenuItem extends AppCompatActivity {
    ImageView viewImage;
    Category addNewCategory;
    private final int SELECT_IMAGE = 0;
    Button btnSelectImage, btnUploadImage, cancel, add;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri newUri;
    TextView image_Select;
    MaterialEditText edtNameMenu;
    FirebaseDatabase database;
    DatabaseReference category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_menu_item);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        edtNameMenu = (MaterialEditText) findViewById(R.id.edtNameMenu);
//        edtMenuTiming = (MaterialEditText) findViewById(R.id.edtMenuTiming);
        image_Select = (TextView) findViewById(R.id.image_Select);
        btnSelectImage = (Button) findViewById(R.id.select_image);

        viewImage = (ImageView) findViewById(R.id.viewImage);

//        btnUploadImage = (Button) findViewById(R.id.upload_image);
        cancel = (Button) findViewById(R.id.cancel);
        add = (Button) findViewById(R.id.add);


        btnSelectImage.setOnClickListener(new View.OnClickListener() {
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ((edtNameMenu.getText().toString()).equals(null) && (edtMenuTiming.getText().toString()).equals(null)) {
//                    Toast.makeText(AddNewMenuItem.this, "fill the Empty fields", Toast.LENGTH_SHORT).show();
                if (!(edtNameMenu.getText().toString()).isEmpty()) {
                    if (newUri != null) {


                        final ProgressDialog mDialog = new ProgressDialog(AddNewMenuItem.this);
                        mDialog.setMessage("Wait a Moment.....");
                        mDialog.show();

                        String imageName = UUID.randomUUID().toString();
                        final StorageReference imageAddress = storageReference.child("images/*" + imageName);
                        imageAddress.putFile(newUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDialog.dismiss();
                                Toast.makeText(AddNewMenuItem.this, "Successfully Uploaded !", Toast.LENGTH_SHORT).show();
                                imageAddress.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
//                                    if( (edtNameMenu.getText().toString()).equals(null) && (edtMenuTiming.getText().toString()).equals(null))
//                                    {
//                                        Toast.makeText(AddNewMenuItem.this, "fill the Empty fields", Toast.LENGTH_SHORT).show();
//                                    }else {
                                        addNewCategory = new Category(
                                                edtNameMenu.getText().toString()
                                                , uri.toString());
//                                    }

                                        if (addNewCategory != null) {
                                            category.push().setValue(addNewCategory);
                                            Intent intent = new Intent(AddNewMenuItem.this, AdminPanel.class);
//                                        Toast.makeText(AddNewMenuItem.this, "Added", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mDialog.dismiss();
                                Toast.makeText(AddNewMenuItem.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mDialog.setMessage("Uploaded!! " + progress + "%");
                            }
                        });
                    } else {
                        Toast.makeText(AddNewMenuItem.this, "Select a image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddNewMenuItem.this, "fill the Empty fields", Toast.LENGTH_SHORT).show();

                }
//
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddNewMenuItem.this, AdminPanel.class);
                Toast.makeText(AddNewMenuItem.this, "Canceled", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (addNewCategory != null) {
////                    category.push().setValue(addNewCategory);
////                    Intent intent = new Intent(AddNewMenuItem.this, AdminPanel.class);
////                    Toast.makeText(AddNewMenuItem.this, "Added", Toast.LENGTH_SHORT).show();
////                    startActivity(intent);
////                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newUri = data.getData();
            viewImage.setImageURI(newUri);
            btnSelectImage.setText("Change Image");
            image_Select.setText("Selected");
// btnSelectImage.
        }
    }
}
