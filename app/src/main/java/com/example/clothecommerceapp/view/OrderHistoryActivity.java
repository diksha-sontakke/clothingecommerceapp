package com.example.clothecommerceapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.model.OrdersModel;
import com.example.clothecommerceapp.viewholders.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView ordersList;
    ImageView backProfile;
    FirebaseAuth auth;
    DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        backProfile=findViewById(R.id.backProfile);
        ordersList=findViewById(R.id.orders_list);
        auth=FirebaseAuth.getInstance();
        ordersList.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));



        onStart();
        backProfileButton();

    }

    private void  backProfileButton(){
        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderHistoryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ordersRef= FirebaseDatabase.getInstance().getReference();

        //to load the data on recycler view
        FirebaseRecyclerOptions<OrdersModel> options= new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(ordersRef.child("Orders").child(auth.getCurrentUser().getUid()).child("History"),OrdersModel.class).build();

        FirebaseRecyclerAdapter<OrdersModel, OrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<OrdersModel, OrdersViewHolder>(options){


                    @NonNull
                    @Override
                    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_recycler_data,parent,false);
                        OrdersViewHolder holder=new OrdersViewHolder(view);
                        return holder;
                    }


                    @Override
                    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull OrdersModel model) {

                        holder.orderName.setText(model.getName());
                        holder.orderPhone.setText(model.getPhone());
                        holder.orderAddr.setText(model.getAddress());
                        holder.orderCity.setText(model.getCity());
                        holder.orderDate.setText(model.getDate());
                        holder.orderPrice.setText(model.getTotalAmount());
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();




    }
}