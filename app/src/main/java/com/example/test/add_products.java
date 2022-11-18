package com.example.test;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class add_products extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView img_view;
    private Button btn_upload, btn_save, btn_return;
    private Uri image_uri;
    private ProgressBar progressBar;
    private StorageReference storageRef;
    private DatabaseReference db;
    private EditText txt_title, txt_price, txt_category;
    FirebaseDatabase database;
    StorageTask mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        progressBar = findViewById(R.id.progress_bar);
        img_view = findViewById(R.id.upload_img);
        btn_upload = findViewById(R.id.btn_upload_image);
        btn_save = findViewById(R.id.btn_save_products);
        btn_return = findViewById(R.id.btn_admin_return);

        txt_title = findViewById(R.id.txt_product_title);
        txt_price = findViewById(R.id.txt_product_price);
        txt_category = findViewById(R.id.txt_product_category);

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(add_products.this, admin.class));
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference("cakes");
        db = FirebaseDatabase.getInstance().getReference("cupcake");
        database = FirebaseDatabase.getInstance();

        progressBar.setVisibility(View.INVISIBLE);



        DatabaseReference myRef = database.getReference().child("cupcakes").child("Cup Cakes").push();



        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUpload != null && mUpload.isInProgress())
                {
                    Toast.makeText(add_products.this, "Still Uploading. Wait a sec.", Toast.LENGTH_SHORT).show();
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

    private void cleartext(){
        txt_title.setText("");
        txt_category.setText("");
        txt_price.setText("");
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
                            progressBar.setVisibility(View.INVISIBLE);
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {



                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(0);
                                        }
                                    }, 500);

                                    Toast.makeText(add_products.this, "Upload Sucessfull", Toast.LENGTH_SHORT).show();




                                    String title = txt_title.getText().toString();
                                    String price = txt_price.getText().toString();
                                    String category = txt_category.getText().toString();
                                    String url = uri.toString();


                                    Map<String, Object> add = new HashMap<>();
                                    add.put("title", title);
                                    add.put("price", price);
                                    add.put("category", category);
                                    add.put("uri", url);

                                    FirebaseDatabase.getInstance().getReference("cupcakes").child(category).push()
                                            .setValue(add)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

                                    startActivity(new Intent(add_products.this, add_products.class));

                                    cleartext();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            progressBar.setVisibility(View.VISIBLE);
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);

                        }
                    });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No File Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            image_uri = data.getData();
            img_view.setImageURI(image_uri);
            Picasso.get().load(image_uri).into(img_view);



        }
    }
}