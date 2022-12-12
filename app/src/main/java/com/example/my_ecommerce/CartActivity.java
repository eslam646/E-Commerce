package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_ecommerce.Aapter.CartAdapter;
import com.example.my_ecommerce.Aapter.ProductAdapter;
import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Order;
import com.example.my_ecommerce.Model.Product;
import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private Button nextProcessCart;
    private long TotalPrice;
    private RecyclerView cartList;
    private TextView totalPriceCart,cartTextMsg;
    private ArrayList<Cart> list;
    private CartAdapter cartAdapter;
    private DatabaseReference cartReference= FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
            .child(Prevalent.onlineUsers.getPhone()).child("Products");
    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Cart List");
    DatabaseReference orderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        nextProcessCart=(Button)findViewById(R.id.cart_next_process_btn);
        cartList=(RecyclerView)findViewById(R.id.cart_list);
        totalPriceCart=(TextView)findViewById(R.id.cart_total_price);
        cartTextMsg=(TextView)findViewById(R.id.cart_msg);
        checkOrderState();
        list=new ArrayList<>();
        cartAdapter=new CartAdapter(getApplicationContext(),list);
        cartList.setHasFixedSize(true);
        cartList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cartList.setAdapter(cartAdapter);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Cart cart= list.get(position);

                CharSequence options[]=new CharSequence[]{
                        "Edit",
                        "Remove"
                };

                builder.setTitle("Cart Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);

                            intent.putExtra("pid",cart.getP_Id());
                            startActivity(intent);
                        }
                        if(which==1)
                        {

                            cartAdapter.setdata(list);
                            databaseReference.child("User View")
                                    .child(Prevalent.onlineUsers.getPhone()).child("Products").child(cart.getP_Id())
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {

                                        databaseReference.child("Admin View")
                                                .child(Prevalent.onlineUsers.getPhone()).child("Products").child(cart.getP_Id())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {

                                                    Toast.makeText(getApplicationContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(CartActivity.this,HomePageActivity.class));

                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }

                });
                builder.show();

            }
        });
        cartReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            Cart cart=dataSnapshot.getValue(Cart.class);
                            list.add(cart);
                            long totalpriceforOneProduct=(Long.parseLong(cart.getP_Price()))*(Long.parseLong(cart.getQuantity()));
                            TotalPrice+=totalpriceforOneProduct;
                            totalPriceCart.setText("Total Price = "+String.valueOf(TotalPrice)+"$");
                        }
                        cartAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
       /* cartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
        nextProcessCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("price",TotalPrice);
                startActivity(intent);
            }
        });
    }

    private void checkOrderState()
    {
        orderReference=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.onlineUsers.getPhone());

        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Order order=snapshot.getValue(Order.class);
                    if(order.getState().equals("shipped"))
                    {
                        totalPriceCart.setText("Dear "+order.getName()+"/n order is delivered successfully");
                        cartList.setVisibility(View.GONE);
                        cartTextMsg.setVisibility(View.VISIBLE);
                        nextProcessCart.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "you can purchase more products, once you received your final order.", Toast.LENGTH_SHORT).show();
                    }
                    else if ((order.getState().equals("not shipped")))
                    {
                        totalPriceCart.setText("Shipping State="+order.getState());
                        cartList.setVisibility(View.GONE);
                        cartTextMsg.setVisibility(View.VISIBLE);
                        cartTextMsg.setText("Congratulations, your final order has been placed successfully. soon it will be verified.");
                        nextProcessCart.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "you can purchase more products, once you received your final order.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}