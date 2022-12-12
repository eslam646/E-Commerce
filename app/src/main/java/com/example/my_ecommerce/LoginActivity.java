     package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_ecommerce.Model.Users;
import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText inputnumber,inputpassword;
    private Button LoginButton;
    private com.rey.material.widget.CheckBox Remmberme;
    private ProgressDialog loadingBar;
    private TextView Admin,NotAdmin,ForgetPassword;
    private String parentDbname="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputnumber=(EditText)findViewById(R.id.login_phone_number_input);
        inputpassword=(EditText)findViewById(R.id.login_password_input);
        LoginButton=(Button)findViewById(R.id.login_btn);
        Remmberme=(com.rey.material.widget.CheckBox)findViewById(R.id.remmber_me_checkbox);
        Admin=(TextView)findViewById(R.id.admin);
        NotAdmin=(TextView)findViewById(R.id.not_admin);
        ForgetPassword=(TextView)findViewById(R.id.forget_password);
        loadingBar=new ProgressDialog(this);
        Paper.init(this);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=inputnumber.getText().toString();
                String password=inputpassword.getText().toString();
                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(getApplicationContext(),"Please enter your phone number....",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(),"Please enter your password....",Toast.LENGTH_LONG).show();
                }
                else
                {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait, while we are checking the correctness of data");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    AllowAccessToAccount(phone,password);
                }

            }
        });
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,VerificationActivity.class));
                finish();
            }
        });
        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                Admin.setVisibility(View.INVISIBLE);
                NotAdmin.setVisibility(View.VISIBLE);
                parentDbname="Admins";

            }
        });
        NotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                Admin.setVisibility(View.VISIBLE);
                NotAdmin.setVisibility(View.INVISIBLE);
                parentDbname="Users";
            }
        });

    }

    private void AllowAccessToAccount(String phone, String password) {
        if(Remmberme.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        final DatabaseReference Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbname).child(phone).exists())
                {
                    Users userdata=dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            if(parentDbname.equals("Admins"))
                            {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(),"Welcome Admin, you are logged in successfully...",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbname.equals("Users"))
                            {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(LoginActivity.this, HomePageActivity.class);
                                Prevalent.onlineUsers =userdata;
                                startActivity(intent);
                            }
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