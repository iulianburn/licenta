package com.iulian.commerceapp.Functionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.iulian.commerceapp.R;

public class CardDetailsActivity extends AppCompatActivity {
    CardForm cardForm;
    Button buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_card_details );
        buy=(Button) findViewById ( R.id.buybtn );
        CardForm cardForm = (CardForm) findViewById ( R.id.card_form );
        cardForm.cardRequired ( true )
                .expirationRequired ( true )
                .cvvRequired ( true )
                .cardholderName ( CardForm.FIELD_REQUIRED )
                .mobileNumberRequired ( true )
                .mobileNumberExplanation ( "SMS is required on this number" )
                .actionLabel ( "Purchase" )
                .setup ( CardDetailsActivity.this );
        cardForm.getCvvEditText ().setInputType ( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD );

        buy.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText ( CardDetailsActivity.this, "Your order is confirmed! The package will be with you soon!", Toast.LENGTH_SHORT ).show ();
                Intent intent=new Intent (CardDetailsActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity ( intent );
                finish ();
            }
        } );

    }
}