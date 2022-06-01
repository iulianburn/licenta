package com.iulian.commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iulian.commerceapp.Prevalent.Prevalent;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    private String check="";
    private TextView resetPass, titleQ;
    private EditText phonenr, q1,q2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forgot_password );

        check=getIntent ().getStringExtra ( "check" );
        resetPass=(TextView) findViewById ( R.id.resetpass );
        titleQ=(TextView) findViewById ( R.id.titleq );
        phonenr=(EditText) findViewById ( R.id.fp_phone );
        q1=(EditText) findViewById ( R.id.question_1 );
        q2=(EditText) findViewById ( R.id.question_2 );
        verifyBtn=(Button) findViewById ( R.id.set_verify_btn );


    }

    @Override
    protected void onStart() {

        super.onStart ();
        if (check.equals ( "settings" ))
        {
            resetPass.setText ( "Set Security Questions" );
            phonenr.setVisibility ( View.GONE );
            titleQ.setText ( "Please set the answers for the questions" );
            verifyBtn.setText ( "Set Answers" );
            displayPrevAnswers ();


            verifyBtn.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    setSecAnswers ();

                }
            } );

        }
        else if (check.equals ( "login" ))
        {
            phonenr.setVisibility ( View.VISIBLE );

            verifyBtn.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    verifyUserphone();
                }
            } );


        }
    }

    private void verifyUserphone() {
        String phone=phonenr.getText ().toString ();
        String an1=q1.getText ().toString ();
        String an2=q2.getText ().toString ();

        if (!phone.equals ( "" )&& !an1.equals ( "" ) && !an2.equals ( "" )) {

            final DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ().child ( "Users" )
                    .child ( phone );
            ref.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists ()) {
                        String Uphone = snapshot.child ( "phone" ).getValue ().toString ();


                        if (snapshot.hasChild ( "Security Questions" )) {
                            String a1 = snapshot.child ( "Security Questions" ).child ( "answer1" ).getValue ().toString ();

                            String a2 = snapshot.child ( "Security Questions" ).child ( "answer2" ).getValue ().toString ();

                            if (!a1.equals ( an1 )) {
                                Toast.makeText ( ForgotPasswordActivity.this, "Your answer to the first question is not right!", Toast.LENGTH_SHORT ).show ();
                            } else if (!a2.equals ( an2 )) {
                                Toast.makeText ( ForgotPasswordActivity.this, "Your answer to the second question is not right!", Toast.LENGTH_SHORT ).show ();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder ( ForgotPasswordActivity.this );
                                builder.setTitle ( "Set the new password!" );
                                final EditText newPassInput = new EditText ( ForgotPasswordActivity.this );
                                newPassInput.setHint ( "Write the new password" );
                                builder.setView ( newPassInput );
                                builder.setPositiveButton ( "Change", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newPassInput.getText ().toString ().equals ( "" )) ;
                                        {
                                            ref.child ( "password" )
                                                    .setValue ( newPassInput.getText ().toString () ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful ()) {
                                                        Toast.makeText ( ForgotPasswordActivity.this, "Your password was changed! You can log in now!", Toast.LENGTH_SHORT ).show ();

                                                        Intent intent= new Intent (ForgotPasswordActivity.this, LoginActivity.class );
                                                        startActivity ( intent );
                                                    }
                                                }
                                            } );

                                        }
                                    }
                                } );

                                builder.setNegativeButton ( "Cancel", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel ();
                                    }
                                } );
                                builder.show ();
                            }

                        } else
                        {
                            Toast.makeText ( ForgotPasswordActivity.this, "You did not set any security questions!", Toast.LENGTH_SHORT ).show ();
                        }

                    } else
                    {
                        Toast.makeText ( ForgotPasswordActivity.this, "We couldn`t find an account associated with this number ", Toast.LENGTH_SHORT ).show ();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            } );
        }else if (phone.equals ( "" ))
        {
            Toast.makeText ( this, "Please type your phone number ", Toast.LENGTH_SHORT ).show ();
        }
        else if (an1.equals ( "" ))
        {
            Toast.makeText ( this, "Please answer the first question!", Toast.LENGTH_SHORT ).show ();
        }
        else if (an2.equals ( "" ))
        {
            Toast.makeText ( this, "Please answer the second question!", Toast.LENGTH_SHORT ).show ();
        }
    }

    private void setSecAnswers(){
        String a1=q1.getText ().toString ();
        String a2=q2.getText ().toString ();

        if (q1.equals ( "" ) && q2.equals ( "" ))
        {
            Toast.makeText ( ForgotPasswordActivity.this, "Please answer both questions", Toast.LENGTH_SHORT ).show ();
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance ().getReference ().child ( "Users" )
                    .child ( Prevalent.currentonlineUser.getPhone () );

            HashMap<String, Object>userdataMap= new HashMap<> ();
            userdataMap.put("answer1", a1);
            userdataMap.put ( "answer2", a2 );

            ref.child("Security Questions").updateChildren ( userdataMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful ())
                    {
                        Toast.makeText ( ForgotPasswordActivity.this, "You set the answers successfully", Toast.LENGTH_SHORT ).show ();
                        Intent intent= new Intent (ForgotPasswordActivity.this, HomeActivity.class);
                        startActivity ( intent );
                    }

                }
            } );

        }

    }

    private void    displayPrevAnswers()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance ().getReference ().child ( "Users" )
                .child ( Prevalent.currentonlineUser.getPhone () );

        ref.child ( "Security Questions" ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ())
                {
                    String a1=snapshot.child ( "answer1" ).getValue ().toString ();

                    String a2=snapshot.child ( "answer2" ).getValue ().toString ();

                    q1.setText ( a1 );
                    q2.setText ( a2 );


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

    }
}