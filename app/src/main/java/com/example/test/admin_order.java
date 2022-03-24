package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.client.OrderAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;

public class admin_order extends AppCompatActivity {

    private Button btn_return, btn_confirmed, btn_rejected;
    private TextView txt_title, txt_price, txt_category;
    private ImageView imgview;
    private RecyclerView rrview;
    private fOrderAdapter adapterr;
    private DatabaseReference ref;
    private StorageReference storageRef;
    private ArrayList<add_products_model> list;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        rrview = findViewById(R.id.rview_admin_order);
        btn_confirmed = findViewById(R.id.btn_confirmed_orders);
        btn_return = findViewById(R.id.btn_m_orders_return);
        btn_rejected = findViewById(R.id.btn_rejected_orders);

        rrview.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<add_products_model> options =
                new FirebaseRecyclerOptions.Builder<add_products_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("orders"), add_products_model.class)
                        .build();

        adapterr = new fOrderAdapter(options);
        rrview.setAdapter(adapterr);

        ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.keepSynced(true);


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_order.this, admin.class));
            }
        });

        btn_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_order.this, confirmed_order.class));
            }
        });

        btn_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_order.this, rejected_orders.class));
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterr.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterr.stopListening();
    }
}