package com.example.clothecommerceapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.model.AddProductModel;
import com.example.clothecommerceapp.viewholders.ViewProductsHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    EditText inputText;
    AppCompatButton searchBtn;
    RecyclerView searchList;
    String searchInput;
    ImageView backHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        inputText = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchBtn);


        searchList = findViewById(R.id.searchList);
        backHome = findViewById(R.id.backHome);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        menuMethod();
        searchButton();
        onBackPressedImage();
    }

    public void menuMethod() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.search);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;




                    case R.id.search:
                        return true;

                    case R.id.my_cart:
                        startActivity(new Intent(getApplicationContext(), MyCartBagActivity.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                }


                return true;
            }
        });
    }


    private void searchButton() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = inputText.getEditableText().toString();
                onStart();
            }
        });


    }

    private void onBackPressedImage() {
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All")
                .child("User View").child("Products");


        FirebaseRecyclerOptions<AddProductModel> options = new FirebaseRecyclerOptions.Builder<AddProductModel>()

                .setQuery(prodListRef.orderByChild("name").startAt(searchInput), AddProductModel.class).build();

        FirebaseRecyclerAdapter<AddProductModel, ViewProductsHolder> adapter =
                new FirebaseRecyclerAdapter<AddProductModel, ViewProductsHolder>(options) {


                    @NonNull
                    @Override
                    public ViewProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_data, parent, false);
                        ViewProductsHolder holder = new ViewProductsHolder(view);
                        return holder;
                    }


                    @Override
                    protected void onBindViewHolder(@NonNull ViewProductsHolder holder, int position, @NonNull AddProductModel model) {

                        String name = model.getName().replaceAll("\n", " ");
                        String price = model.getPrice();
                        String imgUri = model.getImg();

                        holder.addProductName.setText(name);
                        holder.addProductPrice.setText(price);
                        Picasso.get().load(imgUri).into(holder.addProductImg);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("id", 4);
                                intent.putExtra("uniqueId", name);
                                intent.putExtra("addProdName", name);
                                intent.putExtra("addProdPrice", price);
                                intent.putExtra("addProdDesc", model.getDescription());
                                intent.putExtra("addProdCategory", model.getCategory());
                                intent.putExtra("img", imgUri);
                                startActivity(intent);
                            }
                        });
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}