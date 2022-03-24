package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.client.OrderAdapter;
import com.example.test.client.client_cart;
import com.example.test.client.client_dashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin extends AppCompatActivity {

    private Button btn_add, btn_logout, btn_orders;
    private Button btn_all;
    private RecyclerView rview, rview2;
    private TextView txt_total;
    private fOrderAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference db, ref;
    private ArrayList<add_products_model> list;
    private ArrayList<add_products_model> tlist;
    private ProgressBar pbar;
    private Button btn_return;
    private ImageButton btn_exit, btn_delete, btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btn_add = findViewById(R.id.dash_btn_add_products);
        btn_all = findViewById(R.id.btn_admin_view_all);
        btn_logout = findViewById(R.id.admin_btn_logout);
        rview = findViewById(R.id.admin_dash_rview);
        btn_orders = findViewById(R.id.btn_admin_orders);
        txt_total = findViewById(R.id.txt_admin_order_total);


        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin.this, admin_order_stuff.class));
            }
        });



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin.this, login.class));

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin.this, add_products.class));
            }
        });

        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin.this, products.class));
            }
        });

        rview.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<add_products_model> options =
                new FirebaseRecyclerOptions.Builder<add_products_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("orders"), add_products_model.class)
                        .build();

        adapter = new fOrderAdapter(options);
        rview.setAdapter(adapter);



        ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.keepSynced(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int count = 0;

                if(snapshot.exists())
                {
                    count = (int) snapshot.getChildrenCount();
                    txt_total.setText(Integer.toString(count) + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                txt_total.setText("ERROR 404");
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