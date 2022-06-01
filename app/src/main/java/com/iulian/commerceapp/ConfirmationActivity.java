package com.iulian.commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iulian.commerceapp.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmationActivity extends AppCompatActivity
{
    private EditText nameConf, phoneConf, addressConf, cityConf;
    private Button orderConfirmBtn;
    private String totalAmount= "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_confirmation );
        totalAmount= getIntent ().getStringExtra ( "Total Price" );


        Toast.makeText ( this, "Total Price ="+ totalAmount, Toast.LENGTH_SHORT ).show ();

        nameConf=(EditText) findViewById ( R.id.confirmation_name );
        phoneConf=(EditText) findViewById ( R.id.confirmation_phone_nr );
        addressConf=(EditText) findViewById ( R.id.confirmation_address );
        cityConf=(EditText) findViewById ( R.id.confirmation_city );
        orderConfirmBtn=(Button) findViewById ( R.id.confirmation_btn );

        orderConfirmBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Check();
            }
        } );
    }

    private void Check()
    {
        if (TextUtils.isEmpty ( nameConf.getText ().toString ()))
        {
            Toast.makeText ( this, "Please fill the name field", Toast.LENGTH_SHORT ).show ();
        }
        else if(TextUtils.isEmpty ( phoneConf.getText ().toString () ))
        {
            Toast.makeText ( this, "Please fill the phone number field", Toast.LENGTH_SHORT ).show ();
        }
        else if (TextUtils.isEmpty ( addressConf.getText ().toString () ))
        {
            Toast.makeText ( this, "Please fill the address field", Toast.LENGTH_SHORT ).show ();
        }
        else if(TextUtils.isEmpty ( cityConf.getText ().toString () ))
        {
            Toast.makeText ( this, "Please fill the city field", Toast.LENGTH_SHORT ).show ();
        }
        else{
            ConfirmOrder();
        }


    }

    private void ConfirmOrder()
    {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calforDate = Calendar.getInstance ();
        SimpleDateFormat currentDate= new SimpleDateFormat ("MMM dd, yyyy");
        saveCurrentDate=currentDate.format ( calforDate.getTime () );

        SimpleDateFormat currentTime= new SimpleDateFormat ("HH:mm:ss a");
        saveCurrentTime= currentTime.format ( calforDate.getTime () );

            final DatabaseReference orderReference= FirebaseDatabase.getInstance ().getReference ().child ( "Orders" ).child ( Prevalent.currentonlineUser.getPhone () );
            HashMap<String, Object> orderMap= new HashMap<> ();
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameConf.getText ().toString ());
        orderMap.put("phone", phoneConf.getText ().toString ());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("address", addressConf.getText ().toString ());
        orderMap.put("city", cityConf.getText ().toString ());
        orderMap.put("state", "not shipped");

        orderReference.updateChildren ( orderMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful ())
                {
                    FirebaseDatabase.getInstance ().getReference ().child ( "Cart List" )
                            .child ( "User View" )
                            .child ( Prevalent.currentonlineUser.getPhone () )
                            .removeValue ().addOnCompleteListener ( new OnCompleteListener<Void> () {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful ())
                         {
                             Toast.makeText ( ConfirmationActivity.this, "Your order is confirmed! The package will be with you soon!", Toast.LENGTH_SHORT ).show ();
                             Intent intent=new Intent (ConfirmationActivity.this,HomeActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity ( intent );
                             finish ();


                         }
                        }
                    } );
                    

                }
            }
        } );
    }
}