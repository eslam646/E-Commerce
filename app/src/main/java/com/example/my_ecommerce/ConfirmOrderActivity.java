package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {
    private EditText confirmName,confirmPhone,confirmAddress,confirmCity;
    private Button confirmOrderBtn,address;
    private String current_date,current_time;
    private String price;
    private DatabaseReference orderReference= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.onlineUsers.getPhone());
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        price=getIntent().getStringExtra("price");
        confirmName=(EditText)findViewById(R.id.confirm_name);
        confirmPhone=(EditText)findViewById(R.id.confirm_phone_number);
        confirmAddress=(EditText)findViewById(R.id.confirm_address);
        confirmCity=(EditText)findViewById(R.id.confirm_city);
        confirmOrderBtn=(Button) findViewById(R.id.confirm_order_btn);

       confirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfirmOrderActivity.this,AddressActivity.class);
                startActivity(intent);
                finish();

            }
        });
       String x=getIntent().getStringExtra("ad");
       confirmAddress.setText(x);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(confirmName.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(confirmPhone.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(confirmAddress.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(confirmCity.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Please enter your city", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    confirmOrder();
                }
            }
        });
    }

    private void confirmOrder() {
        Calendar calender=Calendar.getInstance();
        SimpleDateFormat date=new SimpleDateFormat("MM:dd:yyyy");
        current_date=date.format(calender.getTime());
        SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss a");
        current_time=time.format(calender.getTime());

        HashMap<String,Object> orderMap=new HashMap<>();

        orderMap.put("Name",confirmName.getText().toString());
        orderMap.put("Phone",confirmPhone.getText().toString());
        orderMap.put("Address",confirmAddress.getText().toString());
        orderMap.put("Date",current_date);
        orderMap.put("Time",current_time);
        orderMap.put("State","not shipped");
        orderMap.put("Price",price);

        orderReference.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                   reference.child("Cart List").child("User View")
                            .child(Prevalent.onlineUsers.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Success Operation", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConfirmOrderActivity.this,HomePageActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }
}