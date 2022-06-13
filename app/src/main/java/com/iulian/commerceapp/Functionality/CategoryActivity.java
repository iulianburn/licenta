package com.iulian.commerceapp.Functionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iulian.commerceapp.Model.Products;
import com.iulian.commerceapp.R;
import com.iulian.commerceapp.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class CategoryActivity extends AppCompatActivity {
    private TextView title;
    private ImageView laptops, phones, headphones, books, watches;
    private String type="";
    private RecyclerView searchListC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_category );


        laptops=(ImageView) findViewById ( R.id.laptops_ca );
        phones=(ImageView) findViewById ( R.id.mobiles_ca );
        headphones=(ImageView) findViewById ( R.id.headphones_ca );
        books= (ImageView) findViewById ( R.id.books_ca );
        watches=(ImageView) findViewById ( R.id.watches_ca );
        title=(TextView) findViewById ( R.id.choose_category );
        searchListC=(RecyclerView) findViewById ( R.id.searching_listC);
        searchListC.setLayoutManager ( new LinearLayoutManager ( CategoryActivity.this ) );


        laptops.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                type="laptops";

                onStart ();
            }
        } );

        headphones.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                type="headphones";
                onStart ();
            }
        } );
        books.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                type="books";
            onStart ();
            }
        } );

        watches.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                type="watches";
                onStart ();
            }
        } );
    phones.setOnClickListener ( new View.OnClickListener () {
        @Override
        public void onClick(View view) {
            type="phones";
            onStart ();

        }
    } );

    }

    @Override
    protected void onStart() {
        super.onStart ();
        DatabaseReference reference= FirebaseDatabase.getInstance ().getReference ().child ( "Products" );


            FirebaseRecyclerOptions<Products> options=
                    new FirebaseRecyclerOptions.Builder<Products> ()
                            .setQuery ( reference.orderByChild ( "category" ).equalTo (type), Products.class).build ();

        FirebaseRecyclerAdapter <Products, ProductViewHolder > adapter= new FirebaseRecyclerAdapter<Products, ProductViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtproductName.setText(model.getPname());
                holder.txtproductPrice.setText("Price:" + model.getPrice()+ "$");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent (CategoryActivity.this,ProductDetailsActivity.class);
                        intent.putExtra ( "pid", model.getPid ());
                        startActivity ( intent );

                    }
                } );
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        searchListC.setAdapter ( adapter );
        adapter.startListening ();

    }
}