package com.iulian.commerceapp.Functionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iulian.commerceapp.Model.Products;
import com.iulian.commerceapp.Connect.Connect;
import com.iulian.commerceapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
public Button addToCart;
private ImageView productImage;
private ElegantNumberButton numberButton;
private TextView productPrice, productDescription, productName;
private String productID="", status="shipped";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_product_details );

        productID= getIntent ().getStringExtra ( "pid" );
        addToCart =(Button) findViewById ( R.id.add_product_to_cart_btn );
        numberButton=(ElegantNumberButton) findViewById ( R.id.number_btn );
        productImage=(ImageView)findViewById ( R.id.product_image_details );
        productPrice=(TextView) findViewById ( R.id.product_price_details );
        productDescription=(TextView) findViewById ( R.id.product_description_details );
        productName=(TextView) findViewById ( R.id.product_name_details );

        getProductDetails(productID);

        addToCart.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                addToCartList();

                if (status.equals ( "Order Placed" ) || status.equals ( "Order Shipped" ))
                {
                    Toast.makeText ( ProductDetailsActivity.this, "You will be able to make another order after the current one will be shipped", Toast.LENGTH_LONG ).show ();

                }
                else
                {
                    addToCartList();

                }
            }
        } );
    }


    @Override
    protected void onStart() {
        super.onStart ();

        CheckOrderStatus ();

    }

    private void addToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calforDate = Calendar.getInstance ();
        SimpleDateFormat currentDate= new SimpleDateFormat ("MMM dd, yyyy");
        saveCurrentDate=currentDate.format ( calforDate.getTime () );

        SimpleDateFormat currentTime= new SimpleDateFormat ("HH:mm:ss a");
        saveCurrentTime= currentTime.format ( calforDate.getTime () );

       final DatabaseReference cartListRef= FirebaseDatabase.getInstance ().getReference ().child ("Cart List");
        final HashMap<String, Object> cartMap= new HashMap<> ();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText ().toString ());
        cartMap.put ("price", productPrice.getText().toString () );
        cartMap.put("description", productDescription.getText ().toString ());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber ());
        cartMap.put("discount", "");
        cartListRef.child ( "User View" ).child ( Connect.currentonlineUser.getPhone () ).child ( "Products" ).child ( productID ).updateChildren ( cartMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful ())
                {
                    cartListRef.child ( "Admin View" ).child ( Connect.currentonlineUser.getPhone () ).child ( "Products" ).child ( productID ).updateChildren ( cartMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful ())
                            {
                                Toast.makeText ( ProductDetailsActivity.this, "Product added to Cart ", Toast.LENGTH_SHORT ).show ();
                                Intent intent = new Intent (ProductDetailsActivity.this,HomeActivity.class);
                                startActivity ( intent );
                            }
                        }
                    } );

                    }
            }
        } );




    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance ().getReference ().child ( "Products" );
        productRef.child ( productID ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    Products products=snapshot.getValue (Products.class);

                    productName.setText(products.getPname ());
                    productPrice.setText ( products.getPrice ()  );
                    productDescription.setText ( products.getDescription () );
                    Picasso.get ().load ( products.getImage () ).into ( productImage );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
    private void CheckOrderStatus()
    {
        DatabaseReference orderRefer;
        orderRefer= FirebaseDatabase.getInstance ().getReference ().child ( "Orders" )
                .child ( Connect.currentonlineUser.getPhone () );
        orderRefer.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    String shippmentStatus=snapshot.child ( "state" ).getValue ().toString ();


                    if (shippmentStatus.equals ( "shipped" ))
                    {
                            status="Order Shipped";

                    }
                    else if (shippmentStatus.equals ( "not shipped" ))
                    {
                        status="Order Placed";



                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }



}