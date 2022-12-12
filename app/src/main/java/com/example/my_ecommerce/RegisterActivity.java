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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button createAccount_btn;
    EditText name, phone, password,BirthDate,gender,job;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createAccount_btn = (Button) findViewById(R.id.register_btn);
        name = (EditText) findViewById(R.id.register_name_input);
        phone = (EditText) findViewById(R.id.register_phone_number_input);
        password = (EditText) findViewById(R.id.register_password_input);
        BirthDate = (EditText) findViewById(R.id.register_birthdate_input);
        gender = (EditText) findViewById(R.id.register_gender_input);
        job = (EditText) findViewById(R.id.register_job_input);
        loadingBar = new ProgressDialog(this);
        BirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,CalendarActivity.class));
                BirthDate.setText(getIntent().getExtras().get("date").toString());
            }
        });
        createAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_name = name.getText().toString();
                String input_phone = phone.getText().toString();
                String input_password = password.getText().toString();
                String input_birthdate = BirthDate.getText().toString();
                String input_gender = gender.getText().toString();
                String input_job = job.getText().toString();

                if (TextUtils.isEmpty(input_name)) {
                    Toast.makeText(getApplicationContext(), "Please enter your name....", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(input_phone)) {
                    Toast.makeText(getApplicationContext(), "Please enter your phone number....", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(input_password)) {
                    Toast.makeText(getApplicationContext(), "Please enter your password....", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(input_birthdate)) {
                    Toast.makeText(getApplicationContext(), "Please enter your birthdate....", Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(input_gender)) {
                    Toast.makeText(getApplicationContext(), "Please enter your gender....", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(input_job)) {
                    Toast.makeText(getApplicationContext(), "Please enter your job....", Toast.LENGTH_LONG).show();
                } else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking the correctness of data");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    Validatephonenumber(input_name, input_phone, input_password,input_birthdate,input_gender,input_job);
                }

            }
        });

    }

    private void Validatephonenumber(String input_name, String input_phone, String input_password,String input_birthdate,String input_gender,String input_job) {
        final DatabaseReference Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(input_phone).exists())) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("phone", input_phone);
                    data.put("password", input_password);
                    data.put("name", input_name);
                    data.put("birthDate", input_birthdate);
                    data.put("gender", input_gender);
                    data.put("job", input_job);
                    Rootref.child("Users").child(input_phone).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Congratulations, your account has been created", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Network Error: Please try again later", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "This" + input_phone + "already exists. ", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again using another phone number", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
