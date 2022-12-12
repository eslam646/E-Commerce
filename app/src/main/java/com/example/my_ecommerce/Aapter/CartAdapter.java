package com.example.my_ecommerce.Aapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_ecommerce.Model.Cart;

import com.example.my_ecommerce.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    public List<Cart> cartList;
    private CartAdapter.OnItemClickListener hListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CartAdapter.OnItemClickListener listener){
        hListener=listener;
    }
    public CartAdapter(Context context,List<Cart> productList) {
        this.cartList=productList;
        this.context=context;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_items,parent,false);
        return new ViewHolder(view,hListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart cart=cartList.get(position);
        holder.txtProductName.setText(cart.getP_Name());
        holder.txtProductPrice.setText("Price: "+cart.getP_Price()+"$");
        holder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
    public void setdata(List<Cart> List)
    {
        cartList.clear();
        cartList.addAll(List);
        notifyDataSetChanged();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName,txtProductQuantity,txtProductPrice;
        public ViewHolder(@NonNull View itemView, CartAdapter.OnItemClickListener listener) {
            super(itemView);

            txtProductName=(TextView) itemView.findViewById(R.id.cart_product_name);
            txtProductQuantity=(TextView) itemView.findViewById(R.id.cart_product_quantity);
            txtProductPrice=(TextView) itemView.findViewById(R.id.cart_product_price);
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
