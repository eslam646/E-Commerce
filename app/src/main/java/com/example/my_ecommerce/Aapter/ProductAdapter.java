package com.example.my_ecommerce.Aapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_ecommerce.HomePageActivity;
import com.example.my_ecommerce.Model.Product;
import com.example.my_ecommerce.ProductDetailsActivity;
import com.example.my_ecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    Context context;
    List<Product> productList;
    List<Product> productListFull;
    private OnItemClickListener hListener;

    public ProductAdapter(Context context,List<Product> productList) {
        this.productList=productList;
        this.context=context;
       // productListFull=new ArrayList<>(productList);
        //System.out.println("ngy"+productList.size()+" "+productListFull.size());
    }
    public void setdata(List<Product> productList)
    {
        this.productList=productList;
        this.productListFull=new ArrayList<>(productList);
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return ProductFilter;
    }
    private Filter ProductFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filterList =new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filterList.addAll(productListFull);
            }
            else
            {
                String filterPattern=constraint.toString().toLowerCase().trim();
                System.out.println("amir "+productListFull.size()+""+filterPattern);
                for(Product item:productListFull)
                {
                    if(item.getP_Name().toLowerCase().contains(filterPattern))
                    {
                        filterList.add(item);
                    }
                    System.out.println("tark");
                }
                System.out.println("sooka "+filterList.size());
            }
            FilterResults results=new FilterResults();
            results.values=filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        hListener=listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view,hListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product=productList.get(position);
        holder.txtProductName.setText(product.getP_Name());
        holder.txtProductPrice.setText("Price: "+product.getP_Price()+"$");
        holder.txtProductDescription.setText(product.getP_Description());
        Picasso.get().load(product.getP_Image()).into(holder.imageView);
        System.out.println("wwwww "+product.getP_Name());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName,txtProductDescription,txtProductPrice;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.product_image_view);
            txtProductName=(TextView) itemView.findViewById(R.id.product_name_view);
            txtProductDescription=(TextView) itemView.findViewById(R.id.product_description_view);
            txtProductPrice=(TextView) itemView.findViewById(R.id.product_price_view);
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
