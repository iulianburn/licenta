package com.iulian.commerceapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.iulian.commerceapp.HomeActivity;
import com.iulian.commerceapp.MainActivity;
import com.iulian.commerceapp.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView laptops, headphones, mobiles, books, watches;
    private Button logout, checkOrders, pmaintananceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_admin_category);
        laptops=(ImageView) findViewById(R.id.laptops);
        watches=(ImageView) findViewById(R.id.watches);
        books=(ImageView) findViewById(R.id.books);
        headphones=(ImageView) findViewById(R.id.headphones);
        mobiles=(ImageView) findViewById(R.id.mobiles);

        checkOrders=(Button) findViewById ( R.id.validateorders_btn );
        pmaintananceBtn=(Button)findViewById ( R.id.maintain_btn);
        logout=(Button)findViewById ( R.id.adlogout_btn );

        logout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
                finish ();
            }
        } );

        pmaintananceBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AdminCategoryActivity.this, HomeActivity.class);
                //intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.putExtra ( "Admin", "Admin" );
                startActivity ( intent );
            }
        } );


        checkOrders.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent (AdminCategoryActivity.this, AdminOrdersActivity.class);
                startActivity ( intent );
            }
        } );






        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "laptops");
                startActivity(intent);

            }
        });


        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "phones");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "watches");
                startActivity(intent);
            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "headphones");
                startActivity(intent);
            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "books");
                startActivity(intent);
            }
        });


    }
}