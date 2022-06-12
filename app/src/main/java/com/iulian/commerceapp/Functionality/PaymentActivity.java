package com.iulian.commerceapp.Functionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iulian.commerceapp.R;

public class PaymentActivity extends AppCompatActivity {
    private Button cashBtn, cardBtn;
    private TextView txt1, txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_payment );
        txt1=(TextView) findViewById ( R.id.message_payment );
        txt2=(TextView) findViewById ( R.id.message_payment2 );
        cashBtn=(Button) findViewById ( R.id.cash_button );
        cardBtn=(Button) findViewById ( R.id.card_button );

        cashBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( PaymentActivity.this, "Your order is confirmed! The package will be with you soon!", Toast.LENGTH_SHORT ).show ();
                Intent intent=new Intent (PaymentActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity ( intent );
                finish ();
            }
        } );
        cardBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent (PaymentActivity.this,CardDetailsActivity.class);
                startActivity ( intent );
            }
        } );


    }
}