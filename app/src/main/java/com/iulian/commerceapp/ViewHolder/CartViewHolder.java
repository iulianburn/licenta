package com.iulian.commerceapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iulian.commerceapp.Interfaces.ItemClickListener;
import com.iulian.commerceapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textproductName, txtproductPrice, txtproductQuantity;
    private ItemClickListener itemClickListener;
    public CartViewHolder(@NonNull View itemView) {
        super ( itemView );
        textproductName= itemView.findViewById ( R.id.product_name_cart );
        txtproductPrice=itemView.findViewById ( R.id.product_price_cart );
        txtproductQuantity=itemView.findViewById ( R.id.product_quantity_cart );

    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick ( view, getBindingAdapterPosition (), false);


    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.setItemClickListener ( itemClickListener );

    }

}
