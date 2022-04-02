package com.example.zeroq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    ListView listView;
Button scan , pay;
TextView textView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dbhandler db = new dbhandler(MainActivity2.this);
        textView = findViewById(R.id.textView);
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> code = new ArrayList<>();
        ArrayList<String> quantity= new ArrayList<>();
        listView = findViewById(R.id.listview);



        customadapter ad = new customadapter(this, name, price, code, quantity);
        listView.setAdapter(ad);
        scan = findViewById(R.id.button5);
        pay = findViewById(R.id.button3);
        Intent intent = getIntent();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity2.this, scan_screen.class);
                startActivity(i);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.clear();
                price.clear();
                code.clear();
                quantity.clear();
                ad.notifyDataSetChanged();
                db.delete();
                Toast.makeText(MainActivity2.this, "Payment Successful", Toast.LENGTH_SHORT).show();


            }
        });
        Cursor res = db.getdata();
        while (res.moveToNext()) {
            code.add(res.getString(0));
            name.add(res.getString(1));
            price.add(res.getString(2));
            ad.notifyDataSetChanged();


        }


    }

    }
