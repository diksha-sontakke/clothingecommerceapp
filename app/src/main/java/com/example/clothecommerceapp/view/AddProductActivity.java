package com.example.clothecommerceapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    ImageView addProdImg,addProductBack;

    EditText addProdName, addProdPrice, addProdDesc, addProdCategory;
    TextView confirmAdd;
    Uri setImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    String finalImageUri;
    public static String passName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        storage=FirebaseStorage.getInstance();

        addProdImg=findViewById(R.id.addProductImg);
        addProdName=findViewById(R.id.addProductName);
        addProdPrice=findViewById(R.id.addProductPrice);
        addProdDesc=findViewById(R.id.addProductDesc);
        addProdCategory=findViewById(R.id.addProductCategory);
        confirmAdd=findViewById(R.id.confirmAddProd);
        addProductBack=findViewById(R.id.addProductBack);




        addProductImage();

        confirmAddButton();
        addProductBackImage();






        menuMethod();
    }

    private void menuMethod() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set home selected


        //to set no item pre selected in bottom navigation view
        bottomNavigationView.setSelectedItemId(R.id.invisible);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;




                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.my_cart:
                        startActivity(new Intent(getApplicationContext(), MyCartBagActivity.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;




                }


                return true;
            }
        });
    }

    private void addProductBackImage(){
        addProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddProductActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });



    }

    private  void addProductImage(){



        addProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //we have override activity result below
                startActivityForResult(intent, 1);
            }
        });

    }

    private  void confirmAddButton(){
        confirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= addProdName.getEditableText().toString();
                String price=addProdPrice.getEditableText().toString();
                String desc=addProdDesc.getEditableText().toString();
                String category=addProdCategory.getEditableText().toString();


                if(TextUtils.isEmpty(name)){
                    addProdName.setError("Please enter name of the product");
                    Toast.makeText(AddProductActivity.this, "Please fill all the details correctly before confirming", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(price)){
                    addProdPrice.setError("Please enter price of the product");
                    Toast.makeText(AddProductActivity.this, "Please fill all the details correctly before confirming", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(desc)){
                    addProdDesc.setError("Please enter description of the product");
                    Toast.makeText(AddProductActivity.this, "Please fill all the details correctly before confirming", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(category)){
                    addProdCategory.setError("Please enter category of the product");
                    Toast.makeText(AddProductActivity.this, "Please fill all the details correctly before confirming", Toast.LENGTH_SHORT).show();
                }else if(addProdImg==null || addProdImg.getDrawable().equals(R.drawable.shopping_bag)){
                    Toast.makeText(AddProductActivity.this, "Please choose a product image", Toast.LENGTH_SHORT).show();
                }else{
                    storageReference = storage.getReference().child("products").child(name);
                    addingToProdList();
                }
            }
        });




    }

    private void addingToProdList() {
        String saveCurrentDate, saveCurrentTime;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        DatabaseReference prodListRef= FirebaseDatabase.getInstance().getReference().child("View All");

        final HashMap<String, Object> prodMap= new HashMap<>();
        prodMap.put("pid",addProdName.getText().toString());
        prodMap.put("name",addProdName.getText().toString());
        prodMap.put("price","₹"+addProdPrice.getText().toString());
        prodMap.put("category",addProdCategory.getText().toString());
        prodMap.put("description",addProdDesc.getText().toString());
        prodMap.put("date",saveCurrentDate);
        prodMap.put("time",saveCurrentTime);

        if (setImageUri != null) {
            storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            finalImageUri= uri.toString();
                            Log.i("Image", "added successfully to storage");
                            prodMap.put("img",finalImageUri);

                            prodListRef.child("User View").child("Products")
                                    //basically putting all the values of hashmap in product list refernce
                                    .child(addProdName.getText().toString()).updateChildren(prodMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                passName=addProdName.getText().toString();
                                                Toast.makeText(AddProductActivity.this, "Product added successfully after verification", Toast.LENGTH_LONG).show();
                                                Intent intent= new Intent(AddProductActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

    //to add image form gallery

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setImageUri = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), setImageUri);
                addProdImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}