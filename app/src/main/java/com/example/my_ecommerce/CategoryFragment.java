package com.example.my_ecommerce;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_ecommerce.Aapter.CategoryAdapter;
import com.example.my_ecommerce.Aapter.ProductAdapter;
import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Category;
import com.example.my_ecommerce.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoryFragment extends Fragment {
    private RecyclerView.Recycler recycler ;
    private CategoryAdapter categoryAdapter;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Category");
    private ArrayList<Category> list;
    private Fragment fragment;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView recycler=v.findViewById(R.id.recyclerview_category);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        categoryAdapter=new CategoryAdapter(getContext(),list);
        recycler.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Category category=list.get(position);

                fragment= new HomeFragment(category.getCategory_Name(),1);
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout,fragment);
                fragmentTransaction.commit();

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryAdapter.setdata(list);
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Category category=dataSnapshot.getValue(Category.class);
                    list.add(category);
                }
                categoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        return v;
    }
}