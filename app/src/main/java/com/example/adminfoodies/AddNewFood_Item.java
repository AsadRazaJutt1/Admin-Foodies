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

public class AddNewFood_Item extends AppCompatActivity {
    ImageView viewImage4Food;
    Food addNewFood;
    private final int SELECT_IMAGE_FOR_FOOD = 1;
    Button select_image4Food, cancel4Food, add4Food;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri newUri;
    MaterialEditText edtNameFood, edtDescription, edtPrice, edtRestName, edtRestTiming, edtRestCity, edtRestId,edtOpenTiming,edtCloseTiming;
    TextView image_Select4Food;
    FirebaseDatabase database;
    DatabaseReference food;
    String categoryIdForTable = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_item);
//        table_catagory = database.getReference("Category");
        //       if (getIntent() != null)
        categoryIdForTable = getIntent().getStringExtra("CategoryId");
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Foods");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        edtNameFood = (MaterialEditText) findViewById(R.id.edtNameFood);
        edtDescription = (MaterialEditText) findViewById(R.id.edtDescription);
        image_Select4Food = (TextView) findViewById(R.id.image_Select4Food);
        edtPrice = (MaterialEditText) findViewById(R.id.edtPrice);
        edtRestName = (MaterialEditText) findViewById(R.id.edtRestName);
        edtOpenTiming = (MaterialEditText) findViewById(R.id.edtOpenTiming);
        edtCloseTiming = (MaterialEditText) findViewById(R.id.edtCloseTiming);
        edtRestCity = (MaterialEditText) findViewById(R.id.edtRestCity);
        edtRestId = (MaterialEditText) findViewById(R.id.edtRestId);


        select_image4Food = (Button) findViewById(R.id.select_image4Food);

//        edtNameFood
//                edtDescription
//        edtPrice

        viewImage4Food = (ImageView) findViewById(R.id.viewImage4Food);

//        btnUploadImage = (Button) findViewById(R.id.upload_image);
        cancel4Food = (Button) findViewById(R.id.cancel4Food);
        add4Food = (Button) findViewById(R.id.add4Food);


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
        add4Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(edtNameFood.getText().toString()).isEmpty() && !(edtDescription.getText().toString()).isEmpty() && !edtPrice.getText().toString().isEmpty()) {
                    if (newUri != null) {

//                        Toast.makeText(AddNewFood_Item.this, "Not Empty fields", Toast.LENGTH_SHORT).show();
//
                        final ProgressDialog mDialog = new ProgressDialog(AddNewFood_Item.this);
                        mDialog.setMessage("Wait a Moment.....");
                        mDialog.show();

                        String imageName = UUID.randomUUID().toString();
                        final String id_food = UUID.randomUUID().toString();
                        final StorageReference imageAddress = storageReference.child("images/*" + imageName);
                        imageAddress.putFile(newUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                          @Override
                                                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                              mDialog.dismiss();
                                                              Toast.makeText(AddNewFood_Item.this, "Successfully Uploaded !", Toast.LENGTH_SHORT).show();
                                                              imageAddress.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                  @Override
                                                                  public void onSuccess(Uri uri) {

                                                                      addNewFood = new Food(
                                                                              edtNameFood.getText().toString()
                                                                              , uri.toString()
                                                                              , edtDescription.getText().toString()
                                                                              , edtPrice.getText().toString()
                                                                              , categoryIdForTable
                                                                              , null
                                                                              , edtOpenTiming.getText().toString()
                                                                              , edtCloseTiming.getText().toString()
                                                                              , id_food
                                                                              , categoryIdForTable + "_" + edtRestId.getText().toString()
                                                                              , edtRestId.getText().toString()
                                                                              , edtRestName.getText().toString()
                                                                              , edtRestCity.getText().toString());
                                                                      if (addNewFood != null) {
//                                                                          food.push().setValue(addNewFood);
                                                                          food.child(id_food).setValue(addNewFood);
                                                                          Intent intent = new Intent(AddNewFood_Item.this, AdminPanel.class);
                                                                          startActivity(intent);
//                                                                          Intent intent = new Intent(AddNewFood_Item.this, AdminPanel.class);
//                                                                          startActivity(intent);
                                                                      }
                                                                  }
                                                              });
                                                          }
                                                      }
                                ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mDialog.dismiss();
                                Toast.makeText(AddNewFood_Item.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mDialog.setMessage("Uploaded!! " + progress + "%");
                            }
                        });
                    } else {

                        Toast.makeText(AddNewFood_Item.this, "Select a image", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(AddNewFood_Item.this, "fill the Empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel4Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddNewFood_Item.this, AdminPanel.class);
                Toast.makeText(AddNewFood_Item.this, "Canceled", Toast.LENGTH_SHORT).show();
                startActivity(intent);
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
        if (requestCode == SELECT_IMAGE_FOR_FOOD && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newUri = data.getData();
            viewImage4Food.setImageURI(newUri);
            select_image4Food.setText("Change Image");
            image_Select4Food.setText("Selected");
// btnSelectImage.
        }
    }
}
