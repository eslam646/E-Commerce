package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.my_ecommerce.Aapter.OrderDetailsAdapter;
import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUserProductsActivity extends AppCompatActivity {
    private RecyclerView orderProductList;
    private ArrayList<Cart> list;
    private DatabaseReference orderProductReference;
    private String userID;
    private OrderDetailsAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        System.out.println("cbacba");

        userID=getIntent().getStringExtra("uid");
        list=new ArrayList<>();

        orderProductList=(RecyclerView)findViewById(R.id.order_details_list);
       orderAdapter=new OrderDetailsAdapter(getApplicationContext(),list);
        orderProductReference= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");
        orderProductList.setHasFixedSize(true);
        orderProductList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orderProductList.setAdapter(orderAdapter);

        orderProductReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Cart cart=dataSnapshot.getValue(Cart.class);
                    list.add(cart);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}