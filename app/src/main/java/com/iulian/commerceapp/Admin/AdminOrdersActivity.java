package com.iulian.commerceapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iulian.commerceapp.Model.AdminOrdersTC;
import com.iulian.commerceapp.R;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference orderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_admin_orders );


        orderReference= FirebaseDatabase.getInstance ().getReference ().child ( "Orders" );
        orderList=(RecyclerView) findViewById ( R.id.recycler_d );
        orderList.setLayoutManager ( new LinearLayoutManager ( this  ) );




    }

    @Override
    protected void onStart() {
        super.onStart ();

        FirebaseRecyclerOptions<AdminOrdersTC> options=
                new FirebaseRecyclerOptions.Builder<AdminOrdersTC> ()
                .setQuery ( orderReference, AdminOrdersTC.class ).build ();

        FirebaseRecyclerAdapter<AdminOrdersTC, AdminOrderViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrdersTC, AdminOrderViewHolder> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrdersTC model) {

                        holder.userName.setText ( "Name: "+model.getName () );
                        holder.userPhone.setText ( "Phone: " + model.getPhone () );
                        holder.TotalPrice.setText ( "Total Price: " + model.getTotalAmount () );
                        holder.DateAndTime.setText ( "Ordered at: " + model.getDate () + ", "+ model.getTime () );
                        holder.Address.setText ( "Shipping address:" + model.getAddress() + ", "+ model.getCity ());

                        holder.detailsBtn.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View view) {
                                String uID=getRef ( holder.getBindingAdapterPosition () ).getKey ();

                                Intent intent= new Intent (AdminOrdersActivity.this, AdminProductsOrder.class);
                                intent.putExtra ( "uid", uID );
                                startActivity ( intent );
                            }
                        } );
                        holder.itemView.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder ( AdminOrdersActivity.this );
                                builder.setTitle ( "Is this order shipped?" );

                                builder.setItems ( options, new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i==0)
                                    {
                                        String uID=getRef ( holder.getBindingAdapterPosition () ).getKey ();
                                        ConfirmOrder(uID);


                                    }
                                    else
                                    {
                                        finish ();
                                    }
                                    }
                                } );
                                builder.show ();
                            }
                        } );

                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from ( parent.getContext () ). inflate (R.layout.orders_toconf_layout, parent,false );
                        return new AdminOrderViewHolder ( view);
                    }
                };
        orderList.setAdapter ( adapter );
        adapter.startListening ();
    }

    private void ConfirmOrder(String uID) {
        orderReference.child ( uID ).removeValue ();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder
    {
            public TextView userName, userPhone, TotalPrice, DateAndTime, Address;
            public Button detailsBtn;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super ( itemView );

            userName= itemView.findViewById ( R.id.username_otf );
            userPhone= itemView.findViewById ( R.id.phone_otf );
            TotalPrice= itemView.findViewById ( R.id.price_otf );
            DateAndTime= itemView.findViewById ( R.id.dat_otf );
            Address= itemView.findViewById ( R.id.adress_otf );
            detailsBtn=itemView.findViewById ( R.id.details_btn );





        }
    }
}