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
    private RecyclerView rview;
    private TextView txt_total;
    private OrderAdapter adapter;
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

                startActivity(new Intent(admin.this, admin_order.class));
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
        rview.setHasFixedSize(true);


        firebaseDatabase = FirebaseDatabase.getInstance();

        String userid = FirebaseAuth.getInstance().getUid();

        db = firebaseDatabase.getReference("orders");
        db.keepSynced(true);

        list = new ArrayList<>();
        tlist = new ArrayList<add_products_model>();


        adapter = new OrderAdapter(this, list);

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

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                add_products_model sm = snapshot.getValue(add_products_model.class);

                list.add(sm);

                adapter.notifyDataSetChanged();

                if (!snapshot.exists())
                {
                    Toast.makeText(getApplicationContext(), "No DATA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//
//                int sum = 0;
//
//                for (DataSnapshot datasnap : snapshot.getChildren())
//                {
//
//
//
//                    add_products_model sm = datasnap.getValue(add_products_model.class);
//                    list.add(sm);
//
//                    adapter.notifyDataSetChanged();
//
//                    if (!snapshot.exists())
//                    {
//                        Toast.makeText(getApplicationContext(), "No DATA", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
}