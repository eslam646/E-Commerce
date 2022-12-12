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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName,p_description,p_price,p_name,current_date,current_time,ProductRandomKey,downloadImageUrl;
    private Button AddProductButton;
    private ImageView ProductImage;
    private EditText ProductName,ProductDescription,ProductPrice;
    private static final int GallreyPick=1;
    private Uri image_uri;
    private StorageReference ProductImageReference;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName=getIntent().getExtras().get("category").toString();
        ProductImageReference= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");

        AddProductButton=(Button)findViewById(R.id.add_new_product);
        ProductImage=(ImageView)findViewById(R.id.select_product_image);
        ProductName=(EditText)findViewById(R.id.product_name);
        ProductDescription=(EditText)findViewById(R.id.product_description);
        ProductPrice=(EditText)findViewById(R.id.product_price);
        loadingBar=new ProgressDialog(this);
        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });


    }

    private void ValidateProductData() {
        p_description=ProductDescription.getText().toString();
        p_name=ProductName.getText().toString();
        p_price=ProductPrice.getText().toString();

        if(image_uri == null)
        {
            Toast.makeText(getApplicationContext(), "Product image is not selcted", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(p_description))
        {
            Toast.makeText(getApplicationContext(), "Please write product description..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(p_name))
        {
            Toast.makeText(getApplicationContext(), "Please write product name..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(p_price))
        {
            Toast.makeText(getApplicationContext(), "Please write product price..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait, while we are adding the new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calender=Calendar.getInstance();
        SimpleDateFormat date=new SimpleDateFormat("MM:dd:yyyy");
        current_date=date.format(calender.getTime());
        SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss a");
        current_time=time.format(calender.getTime());

        ProductRandomKey=current_date+current_time;

        StorageReference filepath=ProductImageReference.child(image_uri.getLastPathSegment()+ProductRandomKey +".jpg");
        final UploadTask uploadTask=filepath.putFile(image_uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(),"Error: "+e.toString(),Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getApplicationContext(),"Image uploaded Successfully",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(getApplicationContext(),"got the product image Url Successfully..",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String,Object> product_info=new HashMap<>();
        product_info.put("P_Id",ProductRandomKey);
        product_info.put("P_Name",p_name);
        product_info.put("P_Description",p_description);
        product_info.put("P_Price",p_price);
        product_info.put("P_Image",downloadImageUrl);
        product_info.put("P_Date",current_date);
        product_info.put("P_Time",current_time);
        product_info.put("Category_Name",CategoryName);
        ProductRef.child(ProductRandomKey).updateChildren(product_info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Intent intent=new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Product is added successfully..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Error: "+task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void OpenGallery() {
        Intent gallery_Intent=new Intent();
        gallery_Intent.setAction(Intent.ACTION_GET_CONTENT);
        gallery_Intent.setType("image/*");
        startActivityForResult(gallery_Intent,GallreyPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==GallreyPick && resultCode==RESULT_OK && data!=null)
        {
            image_uri=data.getData();
            ProductImage.setImageURI(image_uri);
        }
    }
}