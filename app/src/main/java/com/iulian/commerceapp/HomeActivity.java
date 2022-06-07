package com.iulian.commerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iulian.commerceapp.Admin.AdminManageProds;
import com.iulian.commerceapp.Model.Products;
import com.iulian.commerceapp.Connect.Connect;

import com.iulian.commerceapp.ViewHolder.ProductViewHolder;
import com.iulian.commerceapp.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent ();
        Bundle bundle=intent.getExtras ();
        if (bundle!=null)
        {
            type=getIntent ().getExtras ().get ( "Admin" ).toString ();
        }


        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(Objects.requireNonNull(binding.appBarHome.toolbar));
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!type.equals ( "Admin" ))
                {
                    Intent intent = new Intent (HomeActivity.this, CartActivity.class );
                    startActivity ( intent );

                }



            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_categories,R.id.nav_logout, R.id.nav_search, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener ( new NavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId ();

                if (id== R.id.nav_settings)
                {
                    if (!type.equals ( "Admin" )) {

                        Intent intent= new Intent (HomeActivity.this, SettingsActivity.class);
                    startActivity ( intent );}
                }
                else if (id==R.id.nav_search)
                {
                    if (!type.equals ( "Admin" )) {
                    Intent intent = new Intent ( HomeActivity.this, SearchingActivity.class );
                    startActivity ( intent );
                }
                }
                else if (id==R.id.nav_cart)
                {
                    if (!type.equals ( "Admin" ))
                {
                    Intent intent= new Intent (HomeActivity.this, CartActivity.class);
                    startActivity ( intent );
                }
                }
                else if (id==R.id.nav_logout)
                {
                    if (!type.equals ( "Admin" )) {
                        Paper.book ().destroy ();
                        Intent intent= new Intent (HomeActivity.this, MainActivity.class);
                        intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        finish ();
                    }
                    }
                else if (id==R.id.nav_categories)
                {
                    Intent intent= new Intent (HomeActivity.this,CategoryActivity.class);
                    startActivity ( intent );
                }
                return true;
            }
        } );

        View headerView =navigationView.getHeaderView(0);
        TextView userNameTextView=headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView=headerView.findViewById(R.id.user_profile_image);

        if (!type.equals ( "Admin" ))
        {
            userNameTextView.setText( Connect.currentonlineUser.getName());
            Picasso.get ().load ( Connect.currentonlineUser.getImage () ).placeholder ( R.drawable.profile ).into ( profileImageView );

        }


        recyclerView= (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductRef,Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter= new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtproductName.setText(model.getPname());
                holder.txtproductDescription.setText(model.getDescription ());
                holder.txtproductPrice.setText("Price:" + model.getPrice()+"$");
                Picasso.get().load(model.getImage()).into(holder.imageView);



                holder.itemView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        if (type.equals ( "Admin" ))
                        {
                            Intent intent =new Intent (HomeActivity.this, AdminManageProds.class);
                            intent.putExtra ( "pid", model.getPid () );

                            startActivity ( intent );

                        }
                        else {
                            Intent intent = new Intent ( HomeActivity.this, ProductDetailsActivity.class );
                            intent.putExtra ( "pid", model.getPid () );
                            startActivity ( intent );
                        }
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected ( item );

    }

    public void initNavigationdrawer(){
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener ( new NavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId ();

                if (id== R.id.nav_settings)
                {
                    Intent intent= new Intent (HomeActivity.this, SettingsActivity.class);
                    startActivity ( intent );
                }
                else if (id== R.id.nav_cart)
                {
                    Intent intent = new Intent (HomeActivity.this, CartActivity.class );
                    startActivity ( intent );
                }
                else if (id== R.id.nav_logout)
                {
                    Intent intent = new Intent (HomeActivity.this, MainActivity.class );
                    startActivity ( intent );
                }
                else if (id==R.id.nav_categories)
                {
                    Intent intent= new Intent (HomeActivity.this,CategoryActivity.class);
                    startActivity ( intent );
                }
                return true;
            }
        } );

    }

}