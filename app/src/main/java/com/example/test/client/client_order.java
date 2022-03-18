package com.example.test.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.add_products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class client_order extends AppCompatActivity {

    private Button btn_order;
    private TextView txt_title, txt_price, txt_category;
    private ImageView imgview;
    private DatabaseReference db;
    private StorageReference storageRef;
    private FirebaseDatabase database;
    private Uri image_uri;
    private StorageTask mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_order);

        txt_title = findViewById(R.id.txt_order_title);
        txt_price = findViewById(R.id.txt_order_price);
        txt_category = findViewById(R.id.txt_order_category);
        btn_order = findViewById(R.id.btn_order_submit);
        imgview = findViewById(R.id.img_order_cake);


        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUpload != null && mUpload.isInProgress())
                {
                    Toast.makeText(client_order.this, "Confirming Purchase...", Toast.LENGTH_SHORT).show();
                }else{

                    upload_file();
//                    img_view.setImageResource(R.drawable.ic_cake_24);
//
//                    txt_category.setText("");
//                    txt_price.setText("");
//                    txt_title.setText("");
                }


            }
        });


    }

    private String getFileExtension (Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void upload_file()
    {
        if (image_uri != null)
        {
            StorageReference fileRef =  storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(image_uri));

            mUpload = fileRef.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {



                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    }, 500);

                                    Toast.makeText(getApplicationContext(), "Order Sucessfull! Confirmation Call Will Take Place Soon", Toast.LENGTH_SHORT).show();


                                    String title = txt_title.getText().toString();
                                    String price = txt_price.getText().toString();
                                    String category = txt_category.getText().toString();
                                    String url = uri.toString();
                                    String userid = FirebaseAuth.getInstance().getUid();


                                    Map<String, Object> add = new HashMap<>();
                                    add.put("title", title);
                                    add.put("price", price);
                                    add.put("category", category);
                                    add.put("uri", url);

                                    FirebaseDatabase.getInstance().getReference("orders").push()
                                            .setValue(add)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

                                    startActivity(new Intent(client_order.this, client_dashboard.class));


                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());


                        }
                    });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No File Found", Toast.LENGTH_SHORT).show();
        }
    }

}