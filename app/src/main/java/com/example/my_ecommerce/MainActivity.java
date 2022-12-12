package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.my_ecommerce.Model.Users;
import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button join_btn,login_btn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        loadingBar=new ProgressDialog(this);
        join_btn=(Button)findViewById(R.id.main_join_now_btn);
        login_btn=(Button)findViewById(R.id.main_login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        String UserPhone=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPassword=Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPhone !="" && UserPassword !="")
        {
            if(!TextUtils.isEmpty(UserPhone) && !TextUtils.isEmpty(UserPassword))
            {
                AllowAccess(UserPhone,UserPassword);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.............");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowAccess(String phone, String password) {
        final DatabaseReference Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users userdata=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(),"Please wait, you are already logged in",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(MainActivity.this, HomePageActivity.class);
                            Prevalent.onlineUsers=userdata;
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(),"password is wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"this phone number is not found please try again with the correct number",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}