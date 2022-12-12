package com.example.my_ecommerce.Aapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Category;
import com.example.my_ecommerce.Model.Product;
import com.example.my_ecommerce.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<Category> categoryList;
    private CategoryAdapter.OnItemClickListener qListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CategoryAdapter.OnItemClickListener listener){
        qListener=listener;
    }

    public CategoryAdapter(Context context,List<Category> categoryList) {
        this.categoryList=categoryList;
        this.context=context;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view,qListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category=categoryList.get(position);
        holder.txtCategoryName.setText(category.getCategory_Name());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setdata(List<Category> List)
    {
        categoryList.clear();
        categoryList.addAll(List);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;
        public ViewHolder(@NonNull View itemView, CategoryAdapter.OnItemClickListener listener) {
            super(itemView);
            txtCategoryName=(TextView) itemView.findViewById(R.id.category_name_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
