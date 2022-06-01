package com.iulian.commerceapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iulian.commerceapp.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminManageProds extends AppCompatActivity {
    private Button applyChanges, deletePBtn;
    private EditText name, price, description;
    private ImageView imageView;

    private String productID="";
    private DatabaseReference productRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_admin_manage_prods);

        productID=getIntent ().getStringExtra ( "pid" );
        productRef= FirebaseDatabase.getInstance ().getReference ().child ( "Products" ).child ( productID );


        applyChanges=(Button) findViewById ( R.id.apply_changes );
        name=(EditText) findViewById ( R.id.product_name_admp );
        description=(EditText) findViewById ( R.id.product_description_admp );
        price=(EditText) findViewById ( R.id.product_price_admp );
        imageView=(ImageView) findViewById ( R.id.product_image_admp );
        deletePBtn=(Button)findViewById ( R.id.delete_product_btn );


        ProductSpecifications();
        applyChanges.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                applyChangesMeth();
            }
        } );


        deletePBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        } );
    }

    private void deleteProduct()
    {
        productRef.removeValue ().addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent=new Intent (AdminManageProds.this,AdminManageProds.class);
                startActivity ( intent );
                finish ();

                Toast.makeText ( AdminManageProds.this, "Product Deleted", Toast.LENGTH_SHORT ).show ();

            }
        } );
    }

    private void applyChangesMeth()
    {
        String Name=name.getText ().toString ();
        String Description=description.getText ().toString ();
        String Price=price.getText ().toString ();
        if (Name.equals ( "" ))
        {
            Toast.makeText ( this, "Please set a name for the product.", Toast.LENGTH_SHORT ).show ();
        }
        else if (Description.equals ( "" ))
        {
            Toast.makeText ( this, "Please write down a description of the product.", Toast.LENGTH_SHORT ).show ();
        }
        else if (Price.equals ( "" ))
        {
            Toast.makeText ( this, "Please set the price of the product.", Toast.LENGTH_SHORT ).show ();
        }
        else
        {
            HashMap<String, Object> ProductMap =new HashMap<>();
            ProductMap.put("pid", productID);
            ProductMap.put("description", Description);
            ProductMap.put("price", Price);
            ProductMap.put("pname", Name);

            productRef.updateChildren ( ProductMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful ())
                    {
                        Toast.makeText ( AdminManageProds.this, "Changes applied", Toast.LENGTH_SHORT ).show ();
                        Intent intent= new Intent (AdminManageProds.this, AdminCategoryActivity.class);
                        startActivity ( intent );
                        finish ();
                    }
                }
            } );

        }
    }

    private void ProductSpecifications()
    {
        productRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    String pName=snapshot.child ( "pname" ).getValue ().toString ();
                    String pPrice=snapshot.child ( "price" ).getValue ().toString ();
                    String pDescription=snapshot.child ( "description" ).getValue ().toString ();
                    String pImage=snapshot.child ( "image" ).getValue ().toString ();

                    name.setText ( pName );
                    price.setText ( pPrice );
                    description.setText ( pDescription );
                    Picasso.get ().load ( pImage ).into ( imageView );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
}