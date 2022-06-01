package com.iulian.commerceapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iulian.commerceapp.Interfaces.ItemClickListener;
import com.iulian.commerceapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductName, txtproductDescription, txtproductPrice;

    public ImageView imageView;
    public ItemClickListener listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=(ImageView) itemView.findViewById(R.id.product_image);
        txtproductName=(TextView) itemView.findViewById(R.id.product_name);
        txtproductDescription=(TextView) itemView.findViewById(R.id.product_description);
        txtproductPrice=(TextView) itemView.findViewById(R.id.product_price);

    }

    public void setItemClickListener(ItemClickListener listner)
    {
        this.listner=listner;

    }

    @Override
    public void onClick(View view) {
        listner.OnClick(view, getBindingAdapterPosition(),false);

    }
}
