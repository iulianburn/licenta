package com.iulian.commerceapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iulian.commerceapp.Model.Cart;
import com.iulian.commerceapp.R;
import com.iulian.commerceapp.ViewHolder.CartViewHolder;

public class AdminProductsOrder extends AppCompatActivity {

    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productsReference;

    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_admin_products_order );

        userID=getIntent ().getStringExtra ("uid");

        productList=(RecyclerView)findViewById ( R.id.productlist_apo );
        productList.setHasFixedSize ( true );
        layoutManager=new LinearLayoutManager ( this );
        productList.setLayoutManager ( layoutManager );

        productsReference= FirebaseDatabase.getInstance ().getReference ().child ( "Cart List" )
                .child ( "Admin View" )
         .child ( userID ).child ( "Products" );



    }

    @Override
    protected void onStart() {
        super.onStart ();

        FirebaseRecyclerOptions <Cart> options = new FirebaseRecyclerOptions.Builder<Cart> ()
                .setQuery ( productsReference, Cart.class ).build ();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtproductQuantity.setText ( "Quantity: " + model.getQuantity () );
                holder.txtproductPrice.setText ("Price per unit: " + model.getPrice () + "$");
                holder.textproductName.setText ( model.getPname () );

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder ( view );
                return holder ;
            }
        };
        productList.setAdapter ( adapter );
        adapter.startListening ();

    }
}