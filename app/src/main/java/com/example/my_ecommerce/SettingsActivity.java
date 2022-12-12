package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameSettings,phoneNumberSettings,addressSettings;
    private TextView closeTextBtn,updateTextBtn;

    private Uri imageUri;
    private String myUrl;



    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        System.out.println("wwwww");
        nameSettings=(EditText)findViewById(R.id.settings_name);
        phoneNumberSettings=(EditText)findViewById(R.id.settings_phone_number);
        addressSettings=(EditText)findViewById(R.id.settings_Address);
        closeTextBtn=(TextView) findViewById(R.id.settings_close);
        updateTextBtn=(TextView) findViewById(R.id.settings_update);
        progressDialog=new ProgressDialog(getApplicationContext());
       // storageReference= FirebaseStorage.getInstance().getReference().child("Users Images");
        userInfoDisplay(nameSettings,phoneNumberSettings,addressSettings);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(checker.equals("clicked"))
                {
                  //  userInfoSaved();
                }
                else
                {
                    upateOnlyUserInfo();
                }*/
                upateOnlyUserInfo();
            }
        });
      /*  profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checker="clicked";
                CropImage.activity(imageUri).setAspectRatio(1,1).start(SettingsActivity.this);
            }
        });*/
    }

    private void upateOnlyUserInfo() {

        DatabaseReference  databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> map=new HashMap<>();
        map.put("name",nameSettings.getText().toString());
        map.put("address",addressSettings.getText().toString());
        map.put("phone",phoneNumberSettings.getText().toString());
        databaseReference.child(Prevalent.onlineUsers.getPhone()).updateChildren(map);
        startActivity(new Intent(SettingsActivity.this,HomePageActivity.class));
        Toast.makeText(getApplicationContext(),"Profile Info updated sussfully",Toast.LENGTH_SHORT).show();
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            circleImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error, Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
        }
    }*/

  /*  private void userInfoSaved()
    {
        if(TextUtils.isEmpty(nameSettings.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Name is empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNumberSettings.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Phone Number is empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressSettings.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Address is empty",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait,while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            StorageReference fileRef=storageReference.child(Prevalent.onlineUsers.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference  databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("name",nameSettings.getText().toString());
                        map.put("address",addressSettings.getText().toString());
                        map.put("phone",phoneNumberSettings.getText().toString());
                        map.put("image",myUrl);
                        databaseReference.child(Prevalent.onlineUsers.getPhone()).updateChildren(map);
                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this,HomePageActivity.class));
                        Toast.makeText(getApplicationContext(),"Profile Info updated sussfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"image is not selected",Toast.LENGTH_SHORT).show();
        }

    }*/

    private void userInfoDisplay( EditText nameSettings, EditText phoneNumberSettings, EditText addressSettings) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineUsers.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                   /* if(snapshot.child("image").exists())
                    {
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(circleImageView);
                        nameSettings.setText(name);
                        phoneNumberSettings.setText(phone);
                        addressSettings.setText(address);
                    }*/
                    /*else
                    {
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
//                        String address=snapshot.child("address").getValue().toString();
                        nameSettings.setText(name);
                        phoneNumberSettings.setText(phone);
                //        addressSettings.setText(address);
                    }*/
                    String name=snapshot.child("name").getValue().toString();
                    String phone=snapshot.child("phone").getValue().toString();
                    String address=snapshot.child("address").getValue().toString();
                    nameSettings.setText(name);
                    phoneNumberSettings.setText(phone);
                    addressSettings.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}