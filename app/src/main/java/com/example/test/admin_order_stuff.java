package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class admin_order_stuff extends AppCompatActivity {

    RecyclerView rview;
    fOrderAdapter adapter;
    Button btn_return, btn_confirmed, btn_rejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_stuff);

        rview = findViewById(R.id.rview_admin_order_stuff);
        btn_return = findViewById(R.id.btn_m_orders_return_stuff);
        btn_confirmed = findViewById(R.id.btn_confirmed_orders_stuff);
        btn_rejected = findViewById(R.id.btn_rejected_orders_stuff);

        rview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<add_products_model> options =
                new FirebaseRecyclerOptions.Builder<add_products_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("orders"), add_products_model.class)
                        .build();

        adapter = new fOrderAdapter(options);

        rview.setAdapter(adapter);

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_order_stuff.this, admin.class));
            }
        });

        btn_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_order_stuff.this, confirmed_order.class));
            }
        });

        btn_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_order_stuff.this, rejected_orders.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}