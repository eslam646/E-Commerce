package com.example.my_ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_ecommerce.Aapter.ProductAdapter;
import com.example.my_ecommerce.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    private String type="";
    private String CatName="";
    private int i;
    private RecyclerView.Recycler recycler ;
    private ProductAdapter productAdapter;
    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Products");
    private ArrayList<Product> list;
    @SuppressLint("ValidFragment")
    public HomeFragment(String type) {
        // Required empty public constructor
        this.type=type;
    }
    public HomeFragment(String CatName,int i) {
        // Required empty public constructor
        this.CatName=CatName;
        this.i=i;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recycler=v.findViewById(R.id.recyclerview_home);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        productAdapter=new ProductAdapter(getContext(),list);

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (type.equals("Admin"))
                {
                    Product product= list.get(position);
                    Intent intent=new Intent(getActivity(),AdminMaintainProductsActivity.class);
                    intent.putExtra("pid",product.getP_Id());
                    startActivity(intent);
                }
                else
                {
                    Product product= list.get(position);
                    Intent intent=new Intent(getActivity(),ProductDetailsActivity.class);
                    intent.putExtra("pid",product.getP_Id());
                    startActivity(intent);
                }

            }
        });
      /*  productAdapter= new ProductAdapter.ViewHolder();
        productAdapter*/
        recycler.setAdapter(productAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Product product=dataSnapshot.getValue(Product.class);

                    if(CatName=="")
                    {
                    list.add(product);}
                    else
                    {
                        if(product.getCategory_Name().equals(CatName))
                        {
                            list.add(product);
                        }
                    }
                    System.out.println("wwwww "+product.getP_Name());
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return v;
    }
}