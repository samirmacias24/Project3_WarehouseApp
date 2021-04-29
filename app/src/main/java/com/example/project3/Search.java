package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class Search extends AppCompatActivity {

    private int val=0;

    AutoCompleteTextView textsearch;
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Item> list;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.listData);
        database = FirebaseDatabase.getInstance().getReference("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        final ArrayList<String> list1 = new ArrayList<>();
        myAdapter = new MyAdapter(Search.this, list);
        recyclerView.setAdapter(myAdapter);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (final DataSnapshot dataSnapshot : snapshot.getChildren()){

                    final String user = dataSnapshot.getKey();
                    //list.add(user);

                    //Toast.makeText(Search.this, "Failed" + dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Items").child(user);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(final DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                final String user1 = dataSnapshot1.getKey();

                                //Toast.makeText(Search.this, "Failed" + dataSnapshot1.getKey(),Toast.LENGTH_SHORT).show();


                                DatabaseReference re = FirebaseDatabase.getInstance().getReference().child("Items").child(user).child(user1);
                                ValueEventListener eventListener1 = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                            String user2 = dataSnapshot2.getKey();

                                            //Toast.makeText(Search.this, "Failed" + dataSnapshot2.getKey(),Toast.LENGTH_SHORT).show();

                                            DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Items").child(user).child(user1)
                                                    .child(user2);

                                            ValueEventListener eventListener2 = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot dataSnapshot3 : snapshot.getChildren()){
                                                        String item = dataSnapshot3.getValue().toString();

                                                        list1.add(item);

                                                        val++;
                                                        if(val%8 == 0) {
                                                            String name = list1.get(4);
                                                            String brand = list1.get(0);
                                                            String quant = list1.get(6);


                                                            Item product = new Item(name, brand, quant);

                                                            //Toast.makeText(Search.this, ""+ product.getQuantity(), Toast.LENGTH_SHORT).show();

                                                            list.add(product);

                                                            //Toast.makeText(Search.this, ""+ list.toString(), Toast.LENGTH_SHORT).show();

                                                            list1.clear();
                                                        }



                                                        //Toast.makeText(Search.this, dataSnapshot3.getValue().toString(),Toast.LENGTH_SHORT).show();
                                                    }
                                                    myAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            };
                                            r.addListenerForSingleValueEvent(eventListener2);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                re.addListenerForSingleValueEvent(eventListener1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    ref.addListenerForSingleValueEvent(eventListener);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

/*
    public void createObject(ArrayList<String> array){
        ArrayList<String> newArray = new ArrayList<>();
        String value;

        val++;
        if(val%8 == 0){
                String name = array.get(4);
                String brand = array.get(0);
                String quant = array.get(6);

                Item item = new Item(name, brand, quant);

                list.add(item);

            array.clear();
        }
    }*/
}