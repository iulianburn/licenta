package com.iulian.commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iulian.commerceapp.Admin.AdminCategoryActivity;
import com.iulian.commerceapp.Model.Users;
import com.iulian.commerceapp.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink, ForgetPass;


    private String parentDbName ="Users";
    private CheckBox chkBoxRememberMe;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=(Button) findViewById(R.id.login_btn);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        InputNumber=(EditText) findViewById(R.id.login_phone_number_input);

       AdminLink=(TextView) findViewById(R.id.admin_panel_link);
       NotAdminLink=(TextView) findViewById(R.id.not_admin_panel_link);
        ForgetPass=(TextView) findViewById (R.id.forget_password_link );

        loadingBar =new ProgressDialog(this);
        chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_checkbox);
        Paper.init(this);
        
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }



            private void loginUser() {
                String phone = InputNumber.getText().toString();
                String password =InputPassword.getText().toString();

                if (TextUtils.isEmpty(phone))
                {
                    Toast.makeText(LoginActivity.this, "Please introduce your phone number", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Please introduce your password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(phone, password);
                }

            }

            private void AllowAccessToAccount(final String phone,final String password) {
                if (chkBoxRememberMe.isChecked())
                {
                    Paper.book().write(Prevalent.UserPhoneKey, phone);
                    Paper.book().write(Prevalent.UserPasswordKey,password);


                }


                final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(parentDbName).child(phone).exists())
                    {
                        Users userData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                        if (userData.getPhone().equals(phone))
                        {
                            if (userData.getPassword().equals(password))
                            {
                               if (parentDbName.equals("Admins")) {
                                   Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();

                                   Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                   startActivity(intent);
                               }
                               else if (parentDbName.equals("Users"))
                               {
                                   Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();
                                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                   Prevalent.currentonlineUser=userData;

                                   startActivity(intent);
                               }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Incorret password", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }

                    } else
                    {
                        Toast.makeText(LoginActivity.this, "We couldn`t find an account associated with this phone number", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }
        });

        ForgetPass.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent (LoginActivity.this,ForgotPasswordActivity.class);
                intent.putExtra ( "check", "login" );
                startActivity ( intent );
            }
        } );

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";

            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });


    }
}