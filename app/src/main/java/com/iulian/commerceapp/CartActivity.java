package com.iulian.commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iulian.commerceapp.Model.Cart;
import com.iulian.commerceapp.Prevalent.Prevalent;
import com.iulian.commerceapp.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextStepBtn;
    private TextView totalAmount, messageConfirmation;

    private int Totalprice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_cart );

        recyclerView=(RecyclerView) findViewById ( R.id.cart_list );
        recyclerView.setHasFixedSize ( true );
        layoutManager=new LinearLayoutManager ( this );
        recyclerView.setLayoutManager ( layoutManager );
        NextStepBtn=( Button) findViewById ( R.id.next_process_btn );
        totalAmount=(TextView) findViewById ( R.id.total_price );
        messageConfirmation=(TextView)findViewById ( R.id.messagefinal );


        NextStepBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                totalAmount.setText ( "Total Price: " + String.valueOf ( Totalprice ) + "$");


                Intent intent = new Intent(CartActivity.this, ConfirmationActivity.class);
                intent.putExtra ( "Total Price", String.valueOf ( Totalprice ) );
                startActivity ( intent );
                finish ();
            }
        } );


    }

    @Override
    protected void onStart() {
        super.onStart ();
        CheckOrderStatus();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance ().getReference ().child ( "Cart List" );

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery ( cartListRef.child ( "User View" ).child ( Prevalent.currentonlineUser.getPhone () ).child ( "Products" ), Cart.class ).build ();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<Cart, CartViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
            holder.txtproductQuantity.setText ( "Quantity" + model.getQuantity () );
            holder.txtproductPrice.setText ("Price per unit" + model.getPrice () + "$");
            holder.textproductName.setText ( model.getPname () );

            int TotalPricePerProduct=((Integer.valueOf ( model.getPrice () )))* Integer.valueOf ( model.getQuantity () );
            Totalprice= Totalprice + TotalPricePerProduct;

            holder.itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                CharSequence optoons[] = new CharSequence[]
                        {
                                "Edit",
                                "Remove"

                        };
                    AlertDialog.Builder builder= new AlertDialog.Builder ( CartActivity.this );
                    builder.setTitle ( "Cart Options:" );

                    builder.setItems (optoons, new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (i==0)
                            {
                                Intent intent= new Intent (CartActivity.this, ProductDetailsActivity.class);
                                intent.putExtra ( "pid", model.getPid () );
                                startActivity ( intent );
                            }
                            if (i==1)
                            {
                                cartListRef.child ( "User View" ).child ( Prevalent.currentonlineUser.getPhone ()  )
                                        .child ( "Products" ).child ( model.getPid () ).removeValue ().addOnCompleteListener ( new OnCompleteListener<Void> () {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful ())
                                    {
                                        Toast.makeText ( CartActivity.this, "The item was removed from the cart", Toast.LENGTH_SHORT ).show ();
                                        Intent intent= new Intent (CartActivity.this, HomeActivity.class);
                                        startActivity ( intent );
                                    }
                                    }
                                } );
                            }
                        }
                    } );
                    builder.show ();
                }
            } );

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder ( view );
                        return holder ;

            }
        };
        recyclerView.setAdapter ( adapter );
        adapter.startListening ();


    }

    private void CheckOrderStatus()
    {
        DatabaseReference orderRefer;
        orderRefer= FirebaseDatabase.getInstance ().getReference ().child ( "Orders" )
                .child ( Prevalent.currentonlineUser.getPhone () );
        orderRefer.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    String shippmentStatus=snapshot.child ( "state" ).getValue ().toString ();
                    String username=snapshot.child ( "name" ).getValue ().toString ();

                    if (shippmentStatus.equals ( "shipped" ))
                    {
                        totalAmount.setText("Dear"+ username + " \n your order is on its way to you!");
                        recyclerView.setVisibility (View.GONE );
                        messageConfirmation.setVisibility ( View.VISIBLE );
                        NextStepBtn.setVisibility ( View.GONE );
                        Toast.makeText ( CartActivity.this, "You can make another order!", Toast.LENGTH_SHORT ).show ();
                        
                    }else if (shippmentStatus.equals ( "not shipped" ))
                    {
                        totalAmount.setText("Package not shipped yet!");
                        recyclerView.setVisibility (View.GONE );
                        messageConfirmation.setVisibility ( View.VISIBLE );
                        messageConfirmation.setText ( "Thank you for placing your order! It will be verified and shipped soon!" );
                        NextStepBtn.setVisibility ( View.GONE );
                        Toast.makeText ( CartActivity.this, "Please come back later to check the status of the order.", Toast.LENGTH_SHORT ).show ();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
}