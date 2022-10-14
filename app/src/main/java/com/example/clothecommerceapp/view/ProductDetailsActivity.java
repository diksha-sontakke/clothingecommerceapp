package com.example.clothecommerceapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.model.AddProductModel;
import com.example.clothecommerceapp.model.CartModel;
import com.example.clothecommerceapp.viewholders.RelatedProductHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {



    Intent intent;
    ImageView productImg;
    TextView productName, productCategory, productDesc, productPrice;
    AppCompatButton order;


    FirebaseAuth auth;
    String uniqueId;
    ImageView back;
    RecyclerView related_prod_list;
    String relCategory;
    String name;

    //for spinner
    String[] spinnerData = {"1","2","3","4","5"};
    public Spinner spinner_quantity;
    private static final String TAG="ProductDetailActivity";
    public int forCartQuantity;
    String actualPositionOfMySpinner;
    String selectedItemOfMySpinner;

    TextView textQuantity;

    CartModel cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        auth=FirebaseAuth.getInstance();


        productImg=findViewById(R.id.product_image);
        productName=findViewById(R.id.product_name);
        productCategory=findViewById(R.id.product_category);
        productDesc=findViewById(R.id.product_description);
        productPrice=findViewById(R.id.product_price);


        back=findViewById(R.id.product_back);

        order=findViewById(R.id.order);



        //might create error
        related_prod_list = findViewById(R.id.related_prod_list);
        //this line might create error
        related_prod_list.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this,
                LinearLayoutManager.HORIZONTAL,true));


        //for spinner
        spinner_quantity=findViewById(R.id.spinner_quantity);
        textQuantity=findViewById(R.id.textQuantity);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,spinnerData);


        arrayAdapter.setDropDownViewResource(  android.R.layout
                .simple_spinner_dropdown_item);
        spinner_quantity.setAdapter(arrayAdapter);







        detailProduct();

        imgBack();
        orderButton();
        onStart();

        spinnerQuantityData();
    }

    private void spinnerQuantityData(){

        spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                ((TextView)view).setText(null);// if you want you can change background here
                selectedItemOfMySpinner= spinnerData[i];
                textQuantity.setText(selectedItemOfMySpinner);

                //cart.setQuantity(textQuantity);

                Log.d(TAG, "onItemClick Assign " + spinner_quantity.getSelectedItem());




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






    }


    private void detailProduct(){
        intent=getIntent();

        productCategory.setText(intent.getStringExtra("category"));


        int id=intent.getIntExtra("id",1);
        uniqueId=intent.getStringExtra("uniqueId");


        relCategory= productCategory.getText().toString();



        if(id==1){
            uniqueId=uniqueId.replaceAll("\n"," ");
            productName.setText(intent.getStringExtra("name").replaceAll("\n"," "));
            productPrice.setText("₹732");
            productDesc.setText("Pink pastel shirt\nDurable Material");
            productImg.setImageResource(R.drawable.four);
        }else if(id==2){
            uniqueId=uniqueId.replaceAll("\n"," ");
            productName.setText(intent.getStringExtra("name").replaceAll("\n"," "));
            productPrice.setText("₹532");
            productDesc.setText("Long Lasting\nFine Material\nDurable Quality");
            productImg.setImageResource(R.drawable.five);
        }else if(id==3){

            //this details are same as lvProduct for HomeActivity list
            productName.setText(intent.getStringExtra("name").replaceAll("\n"," "));
            productPrice.setText(intent.getStringExtra("pprice"));
            String img = intent.getStringExtra("imageName");
            productDesc.setText(intent.getStringExtra("description"));
            productImg.setImageResource(this.getResources().getIdentifier(img, "drawable", this.getPackageName()));
        }else{
            //Toast.makeText(this, "In correct block", Toast.LENGTH_SHORT).show();
            //details are form search activity holder.itemView
            productName.setText(intent.getStringExtra("addProdName"));
            productPrice.setText(intent.getStringExtra("addProdPrice"));
            productCategory.setText(intent.getStringExtra("addProdCategory"));
            productDesc.setText(intent.getStringExtra("addProdDesc"));
            String img = intent.getStringExtra("img");
            Picasso.get().load(img).into(productImg);
        }
    }

    private void imgBack(){
        back.bringToFront();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailsActivity.this,HomeActivity.class));
            }
        });


    }

    private void orderButton(){

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });


    }

    private void addingToCartList() {
        String saveCurrentDate, saveCurrentTime;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        //basically creating new db in name cart list
        DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        //putting the value in cartListRef with hashmap
        final HashMap<String, Object> cartMap= new HashMap<>();
        cartMap.put("pid",uniqueId);
        //getting the data from different instances
        cartMap.put("name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);

        //adding the spinner textView data
        //putting quantity into firebase
        cartMap.put("quantity", textQuantity.getText().toString());



        cartListRef.child("User View").child(auth.getCurrentUser().getUid()).child("Products")
                .child(uniqueId).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            //to change cart image after adding the product to cart
                            //1
                            Intent intentCart= new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            intentCart.putExtra("cartAdd","yes");
                            startActivity(intentCart);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //this method is for related products recylerview
        //when we tap on certain product related product will shown below

        final DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All")
                .child("User View").child("Products");

        FirebaseRecyclerOptions<AddProductModel> options = new FirebaseRecyclerOptions.Builder<AddProductModel>()
                .setQuery(prodListRef.orderByChild("category").startAt(relCategory), AddProductModel.class).build();


        FirebaseRecyclerAdapter<AddProductModel, RelatedProductHolder> adapter =
                new FirebaseRecyclerAdapter<AddProductModel, RelatedProductHolder>(options) {


                    @NonNull
                    @Override
                    public RelatedProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_product_recycler_data, parent, false);
                        RelatedProductHolder holder = new RelatedProductHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull RelatedProductHolder holder, int position, @NonNull AddProductModel model) {

                        name = model.getName();
                        String price = model.getPrice();
                        String imgUri = model.getImg();

                        holder.relatedProdName.setText(name);
                        holder.relatedProdPrice.setText(price);
                        Picasso.get().load(imgUri).into(holder.relatedProdImg);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
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

        related_prod_list.setAdapter(adapter);
        adapter.startListening();


    }


}