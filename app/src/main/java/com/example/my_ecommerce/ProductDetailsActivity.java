package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.my_ecommerce.Model.Order;
import com.example.my_ecommerce.Model.Product;
import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button productAddToCartBtn;
    private ImageView productImage;
    private TextView productName,productPrice,productDescription;
    private ElegantNumberButton elegantNumberButton;
    private String ProductID="",price;
    private String current_date,current_time;
    private String state="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        View ovrlay=findViewById(R.id.product_details_activity);
        ovrlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN);

        productAddToCartBtn=(Button)findViewById(R.id.product_add_to_cart_btn_details);
        productImage=(ImageView)findViewById(R.id.product_image_details);
        productName=(TextView) findViewById(R.id.product_name_details);
        productPrice=(TextView) findViewById(R.id.product_price_details);
        productDescription=(TextView) findViewById(R.id.product_description_details);
        elegantNumberButton=(ElegantNumberButton)findViewById(R.id.product_counter_details);

        ProductID=getIntent().getStringExtra("pid");
        getProductDetails(ProductID);
        checkOrderState();
        productAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("order shipped")||state.equals("order placed"))
                {
                    Toast.makeText(getApplicationContext(), "you can add purchase more products, once your order is shipped or verified", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });

    }

    private void addingToCartList() {
        Calendar calender=Calendar.getInstance();
        SimpleDateFormat date=new SimpleDateFormat("MM:dd:yyyy");
        current_date=date.format(calender.getTime());
        SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss a");
        current_time=time.format(calender.getTime());
        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        HashMap<String,Object> cartlistMap=new HashMap<>();
        cartlistMap.put("P_Id",ProductID);
        cartlistMap.put("P_Name",productName.getText().toString());
        cartlistMap.put("P_Price",price);
        cartlistMap.put("P_Description",productDescription.getText().toString());
        cartlistMap.put("Date",current_date);
        cartlistMap.put("Time",current_time);
        cartlistMap.put("Quantity",elegantNumberButton.getNumber());
        cartlistMap.put("Discount","");

        cartListRef.child("User View").child(Prevalent.onlineUsers.getPhone()).child("Products").child(ProductID)
                .updateChildren(cartlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    cartListRef.child("Admin View").child(Prevalent.onlineUsers.getPhone()).child("Products").child(ProductID)
                            .updateChildren(cartlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Added to cart successfully",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(ProductDetailsActivity.this,HomePageActivity.class));
                            }
                        }
                    });
                }
            }
        });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(ProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Product product=snapshot.getValue(Product.class);

                    price=product.getP_Price();
                    productName.setText(product.getP_Name());
                    productPrice.setText(product.getP_Price()+"$");
                    productDescription.setText(product.getP_Description());
                    Picasso.get().load(product.getP_Image()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkOrderState()
    {
       DatabaseReference orderReference=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.onlineUsers.getPhone());

        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Order order=snapshot.getValue(Order.class);
                    if(order.getState().equals("shipped"))
                    {
                        state="order shipped";
                        Toast.makeText(getApplicationContext(), "you can purchase more products, once you received your final order.", Toast.LENGTH_SHORT).show();
                    }
                    else if ((order.getState().equals("not shipped")))
                    {
                        state="order placed";
                         }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}