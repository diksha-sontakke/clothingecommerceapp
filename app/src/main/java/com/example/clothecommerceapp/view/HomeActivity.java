package com.example.clothecommerceapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.adapter.ProductAdapter;
import com.example.clothecommerceapp.constant.Constant;
import com.example.clothecommerceapp.model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {



    CardView shoes1,shoes2,shoes3;
    TextView oddShoeName, oddShoePrice,evenShoeName,evenShoePrice;
    TextView viewAllProducts;
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView home_cart,popMenuImage;

    //for image to change for the cart
    Intent intentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        shoes1=findViewById(R.id.shirt1);
        shoes2=findViewById(R.id.shirt2);
        shoes3=findViewById(R.id.shirt3);

        oddShoeName=findViewById(R.id.oddShirtName);
        oddShoePrice=findViewById(R.id.oddShirtName);
        evenShoeName=findViewById(R.id.evenShirtName);
        evenShoePrice=findViewById(R.id.evenShirtName);
        viewAllProducts=findViewById(R.id.viewAllProducts);



        storage=FirebaseStorage.getInstance();

        home_cart=findViewById(R.id.home_cart);
        popMenuImage=findViewById(R.id.popMenuImage);

        menuMethod();

        shirtClicked();

        viewAll();
        imageCart();

        listViewProducts();
        addingToProduct();
        changeCartImg();
        popMenuImageButton();


    }

    public void popMenuImageButton(){
        popMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(HomeActivity.this,popMenuImage);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.addProduct:
                                startActivity(new Intent(getApplicationContext(),AddProductActivity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            default:
                                return  false;


                        }

                    }
                });

                popupMenu.show();
            }
        });


    }

    public void menuMethod() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home:

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

    private  void  shirtClicked() {
        shoes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                i.putExtra("name", oddShoeName.getText().toString());
                i.putExtra("category", "shirt");
                i.putExtra("price", oddShoePrice.getText().toString());
                i.putExtra("uniqueId", oddShoeName.getText().toString());
                i.putExtra("id", 1);
                startActivity(i);
            }
        });
        shoes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                i.putExtra("name", evenShoeName.getText().toString());
                i.putExtra("category", "tie");
                i.putExtra("price", evenShoePrice.getText().toString());
                i.putExtra("uniqueId", evenShoeName.getText().toString());
                i.putExtra("id", 2);
                startActivity(i);
            }
        });

        shoes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                i.putExtra("name", oddShoeName.getText().toString());
                i.putExtra("category", "shirt");
                i.putExtra("price", oddShoePrice.getText().toString());
                i.putExtra("uniqueId", oddShoeName.getText().toString());
                i.putExtra("id", 1);
                startActivity(i);
            }
        });
    }

    private void viewAll(){

        viewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void imageCart(){
        home_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyCartBagActivity.class);
                startActivity(intent);
            }
        });



    }

    private  void listViewProducts(){

        ListView lvProducts = (ListView) findViewById(R.id.productList);

        ProductAdapter productAdapter = new ProductAdapter(this);
        productAdapter.updateProducts(Constant.PRODUCT_LIST);

        lvProducts.setAdapter(productAdapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //basically we are going to the product class and getting that perticular
                //item index
                ProductModel product = Constant.PRODUCT_LIST.get(position);
                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                //id 3 because we took the id evenshoe i.e shoe2 listener as 2 and oddshoe i.e shoe1 as 1
                intent.putExtra("id", 3);
                intent.putExtra("uniqueId", product.getpName());
                intent.putExtra("name", product.getpName());
                intent.putExtra("description", product.getpDescription());
                intent.putExtra("category", "cloths");
                intent.putExtra("pprice", Constant.CURRENCY + String.valueOf(product
                        .getpPrice().setScale(0, BigDecimal.ROUND_HALF_UP)));
                // intent.putExtra("quantity", product.getpQuantity());
                intent.putExtra("imageName", product.getpImageName());

                //
                Log.d("TAG", "View product: " + product.getpName());
                startActivity(intent);
            }
        });



    }

    private void addingToProduct(){
        String saveCurrentDate,saveCurrentTime;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All");



        String name="White and Black Shirt Combo";
        final HashMap<String, Object> prodMap = new HashMap<>();
        prodMap.put("pid",name );
        prodMap.put("name", name);
        prodMap.put("price", "₹3099");
        prodMap.put("category", "cloths");
        prodMap.put("description","made of the most durable materials\nand are they are unmatched in\nsophistication and style.\n. It’s slim fit and has accentuated\nshoulders that give a classic style.");


        prodMap.put("date", saveCurrentDate);
        prodMap.put("time", saveCurrentTime);
        prodListRef.child("User View").child("Products")
                .child(name).updateChildren(prodMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("Task","successful");

                        }
                    }
                });



    }

    private void changeCartImg(){


        intentCart = getIntent();
        if (intentCart.getStringExtra("cartAdd") != null && intentCart.getStringExtra("cartAdd").equals("yes")) {
            home_cart.setImageResource(R.drawable.cartbagcolorchange);
        } else if (intentCart.getStringExtra("cartAdd") != null && intentCart.getStringExtra("cartAdd").equals("no")) {
            home_cart.setImageResource(R.drawable.cart_bag);
        } else {
            home_cart.setImageResource(R.drawable.cart_bag);
        }



    }


}