package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.my_ecommerce.Aapter.CartAdapter;
import com.example.my_ecommerce.Aapter.OrderAdapter;
import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Order;
import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private ArrayList<Order> list;
    private DatabaseReference orderReference;
    private OrderAdapter orderAdapter;
    private DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderReference= FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList=(RecyclerView)findViewById(R.id.orders_list);
        list=new ArrayList<>();
        orderAdapter=new OrderAdapter(getApplicationContext(),list);
        orderList.setHasFixedSize(true);
        orderList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orderList.setAdapter(orderAdapter);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(int position) {
               Order  order= list.get(position);
               CharSequence options[]=new CharSequence[]{
                       "Yes",
                       "No"
               };
               builder.setTitle("Have you shipped this order products");
               builder.setItems(options, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if(which==0)
                       {
                           orderAdapter.setdata(list);
                           orderReference.child(order.getPhone()).removeValue();
                           reference.child(order.getPhone()).removeValue();

                       }
                       else
                       {
                           finish();
                       }
                   }

               });
               builder.show();
           }

           @Override
           public void onButtonClick(int position) {
               Order order1=list.get(position);
               Intent intent=new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
               intent.putExtra("uid",order1.getPhone());
               System.out.println("abcabc");
               startActivity(intent);


           }
       });

        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Order order=dataSnapshot.getValue(Order.class);
                    list.add(order);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RemoverOrder(String phone) {
        orderReference.child(phone).removeValue();
    }
}