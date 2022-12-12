package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.my_ecommerce.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn,deleteProductBtn;
    private EditText nameMaintain,priceMaintain,descriptionMaintain;
    private ImageView maintainImageView;
    private DatabaseReference productReference;
    private String productID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);
        productID=getIntent().getStringExtra("pid");
        productReference= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);


        applyChangesBtn=(Button)findViewById(R.id.product_maintain_btn);
        deleteProductBtn=(Button)findViewById(R.id.product_delete_btn);
        nameMaintain=(EditText)findViewById(R.id.product_name_maintain);
        priceMaintain=(EditText)findViewById(R.id.product_price_maintain);
        descriptionMaintain=(EditText)findViewById(R.id.product_description_maintain);
        maintainImageView=(ImageView)findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();
        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProduct();
            }
        });
    }

    private void DeleteProduct() {
        productReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductsActivity.this, "The product deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applyChanges() {
        String pName=nameMaintain.getText().toString();
        String pPrice=priceMaintain.getText().toString();
        String pDescription=descriptionMaintain.getText().toString();
        if(pName.equals(""))
        {
            Toast.makeText(this, "Write product name", Toast.LENGTH_SHORT).show();
        }
        else if(pPrice.equals(""))
        {
            Toast.makeText(this, "Write product price", Toast.LENGTH_SHORT).show();
        }
        else if(pDescription.equals(""))
        {
            Toast.makeText(this, "Write product description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> product_info=new HashMap<>();
            product_info.put("P_Id",productID);
            product_info.put("P_Name",pName);
            product_info.put("P_Description",pDescription);
            product_info.put("P_Price",pPrice);
            productReference.updateChildren(product_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }

    private void displaySpecificProductInfo() {
        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                    Product product=snapshot.getValue(Product.class);
                    nameMaintain.setText(product.getP_Name());
                    descriptionMaintain.setText(product.getP_Description());
                    priceMaintain.setText(product.getP_Price());
                    Picasso.get().load(product.getP_Image()).into(maintainImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}