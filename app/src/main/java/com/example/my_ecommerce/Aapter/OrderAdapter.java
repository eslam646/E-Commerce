package com.example.my_ecommerce.Aapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.my_ecommerce.AdminNewOrdersActivity;
import com.example.my_ecommerce.AdminUserProductsActivity;
import com.example.my_ecommerce.Model.Cart;
import com.example.my_ecommerce.Model.Order;
import com.example.my_ecommerce.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    public List<Order> orderList;
    private OrderAdapter.OnItemClickListener hListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onButtonClick(int position);
    }
    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener){
        hListener=listener;
    }

    public OrderAdapter(Context context,List<Order> orderList) {
        this.orderList=orderList;
        this.context=context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.orders_layout,parent,false);
        return new OrderAdapter.ViewHolder(view,hListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order=orderList.get(position);
        holder.userName.setText("Name: "+order.getName());
        holder.userPhone.setText("Phone: "+order.getPhone());
        holder.userTotalPrice.setText("Total Price= "+order.getPrice()+"$");
        holder.userAddress.setText("Address: "+order.getAddress()+","+order.getCity());
        holder.userDateTime.setText("Order at: "+order.getDate()+" , "+order.getTime());


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public void setdata(List<Order> List)
    {
        orderList.clear();
        orderList.addAll(List);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userPhone,userTotalPrice,userAddress,userDateTime;
        Button showOrderBtn;
        public ViewHolder(@NonNull View itemView,OrderAdapter.OnItemClickListener listener) {
            super(itemView);

            userName=(TextView)itemView.findViewById(R.id.order_user_name);
            userPhone=(TextView)itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=(TextView)itemView.findViewById(R.id.order_total_price);
            userAddress=(TextView)itemView.findViewById(R.id.order_address_city);
            userDateTime=(TextView)itemView.findViewById(R.id.order_date_time);
            showOrderBtn=(Button)itemView.findViewById(R.id.show_the_order_btn);

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
            showOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onButtonClick(position);
                        }
                    }
                }
            });
        }
    }
}
