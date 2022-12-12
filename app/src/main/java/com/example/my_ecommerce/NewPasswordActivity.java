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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewPasswordActivity extends AppCompatActivity {
    private EditText newPasswordInput,confirmPasswordInput;
    private Button confirmBtn;
    private DatabaseReference Reference= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPasswordInput=(EditText)findViewById(R.id.new_passsword_input);
        confirmPasswordInput=(EditText)findViewById(R.id.confirm_password_input);
        confirmBtn=(Button)findViewById(R.id.confirm_password_btn);
        String phone=getIntent().getExtras().get("phone").toString();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = newPasswordInput.getText().toString();
                String cpassword = confirmPasswordInput.getText().toString();

                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(NewPasswordActivity.this, "please write the new password", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(cpassword))
                {
                    Toast.makeText(NewPasswordActivity.this, "please confirm password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(password.equals(cpassword))
                    {
                      /*  databaseReference.child("Users").child(phone).child("password").setValue(confirmPasswordInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(NewPasswordActivity.this, "password updated successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(NewPasswordActivity.this,LoginActivity.class));
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(NewPasswordActivity.this,RegisterActivity.class));
                                    finish();
                                }
                            }
                        });*/
                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("Users").child(phone).exists())
                                {
                                    Reference.child("Users").child(phone).child("password").setValue(confirmPasswordInput.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(NewPasswordActivity.this, "password updated successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(NewPasswordActivity.this,LoginActivity.class));
                                            finish();
                                        }
                                    });

                                }
                                else
                                {
                                    startActivity(new Intent(NewPasswordActivity.this,RegisterActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(NewPasswordActivity.this, "please make the password equal confirm password to Confirm that", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}