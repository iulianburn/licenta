package com.iulian.commerceapp.Admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iulian.commerceapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, Description, Price, Pname,saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private ImageView InputProductImage;
    private String productRandomKey, downloadImageURI;
    private StorageReference ProductImagesRef;
    private Uri imageUri;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_admin_add_new_product);

        Toast.makeText(this, "welcome to admin activity", Toast.LENGTH_SHORT).show();

        categoryName=getIntent().getExtras().get("category").toString();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");

        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        AddNewProductButton=(Button) findViewById(R.id.add_new_product);
        InputProductName=(EditText) findViewById(R.id.product_name);
        InputProductDescription=(EditText) findViewById(R.id.product_description);
        InputProductPrice=(EditText) findViewById(R.id.product_price);
        InputProductImage=(ImageView) findViewById(R.id.select_product_image);
        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar= new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            }
        });




      AddNewProductButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              ValidateProductData();
          }
      });
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
        if(result.getResultCode()==Activity.RESULT_OK)
        {
         Intent data= result.getData();
         imageUri= data.getData();
         InputProductImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(AdminAddNewProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
        }
    });

    private void ValidateProductData() {
        Description=InputProductDescription.getText().toString();
        Price=InputProductPrice.getText().toString();
        Pname=InputProductName.getText().toString();

        if(imageUri==null)
        {
            Toast.makeText(this, "Produt image is manadatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Product description is mandatory", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "The price of the product is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "The name of the product  is mandatory", Toast.LENGTH_SHORT).show();
        } else if(InputProductImage == null)
        {
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
        } else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Adding new product...");
        loadingBar.setMessage("Please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM ,dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate + saveCurrentTime;


        StorageReference filePath= ProductImagesRef.child(imageUri.getLastPathSegment()+productRandomKey+ ".jpg");

        final UploadTask uploadTask= filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
        String message= e.toString();
            Toast.makeText(AdminAddNewProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Toast.makeText(AdminAddNewProductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();

                    }
                    downloadImageURI= filePath.getDownloadUrl().toString();

                    return filePath.getDownloadUrl();

                    
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful())
                    {
                       downloadImageURI=task.getResult().toString();


                        Toast.makeText(AdminAddNewProductActivity.this, "Getting Product image URL successfully", Toast.LENGTH_SHORT).show();
                        SaveProductInfoToDatabase();
                    }
                }
            });

        }
    });


    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> ProductMap =new HashMap<>();
        ProductMap.put("pid", productRandomKey);
        ProductMap.put("date",saveCurrentDate);
        ProductMap.put("time",saveCurrentTime);
        ProductMap.put("description", Description);
        ProductMap.put("image", downloadImageURI);
        ProductMap.put("category", categoryName);
        ProductMap.put("price", Price);
        ProductMap.put("pname", Pname);

        ProductsRef.child(productRandomKey).updateChildren(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "The product was added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                } else
                {
                    loadingBar.dismiss();

                    String message=task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Something went wrong"+message
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}