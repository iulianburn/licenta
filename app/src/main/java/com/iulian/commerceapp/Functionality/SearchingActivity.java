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
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iulian.commerceapp.Model.Products;
import com.iulian.commerceapp.R;
import com.iulian.commerceapp.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SearchingActivity extends AppCompatActivity {
    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_searching );

        inputText=(EditText) findViewById ( R.id.search_product );
        searchBtn=(Button) findViewById ( R.id.search_btn );
        searchList=(RecyclerView) findViewById ( R.id.searching_list);
        searchList.setLayoutManager ( new LinearLayoutManager ( SearchingActivity.this ) );


        searchBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view)
            {
                searchInput= inputText.getText ().toString ();

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
                .setQuery ( reference.orderByChild ( "pname" ).startAt (searchInput), Products.class).build ();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        holder.txtproductName.setText(model.getPname());
                        holder.txtproductPrice.setText("Price:" + model.getPrice()+"$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent (SearchingActivity.this,ProductDetailsActivity.class);
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
                } ;
        searchList.setAdapter ( adapter );
        adapter.startListening ();


    }
}