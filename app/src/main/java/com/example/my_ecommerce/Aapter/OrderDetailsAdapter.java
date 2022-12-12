package com.example.my_ecommerce.Aapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Order;
import com.example.my_ecommerce.R;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    Context context;
    public List<Cart> orderDetailsList;
    private OrderDetailsAdapter.OnItemClickListener hListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OrderDetailsAdapter.OnItemClickListener listener){
        hListener=listener;
    }

    public OrderDetailsAdapter(Context context,List<Cart> orderDetailsList) {
        this.orderDetailsList=orderDetailsList;
        this.context=context;
    }
    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_items,parent,false);
        return new OrderDetailsAdapter.ViewHolder(view,hListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        Cart cart=orderDetailsList.get(position);
        holder.txtProductName.setText(cart.getP_Name());
        holder.txtProductPrice.setText("Price: "+cart.getP_Price()+"$");
        holder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName,txtProductQuantity,txtProductPrice;

        public ViewHolder(@NonNull View itemView,OrderDetailsAdapter.OnItemClickListener listener) {
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

