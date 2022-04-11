package com.example.zeroq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Expense_graph extends AppCompatActivity {
    BarChart barChart;
    FirebaseDatabase rootnode;
    DatabaseReference databaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_graph);
        barChart=findViewById(R.id.bar_chart);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootnode = FirebaseDatabase.getInstance();
        databaseReference1 = rootnode.getReference("Users").child(uid).child("purchase");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double i=1;
               ArrayList<BarEntry> Graph_list= new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren() ){
                    String prices= snap.getValue().toString();
                    Graph_list.add( new BarEntry((float) i, Float.parseFloat(prices)));
                    i++;

                }
                BarDataSet barDataSet= new BarDataSet(Graph_list,"Times");
                BarData barData= new BarData();
                barData.addDataSet(barDataSet);
                barChart.animateY(5000);
                 barChart.getDescription().setText("Times");
                barChart.getDescription().setTextColor(Color.CYAN);


                barChart.setData(barData);
                barChart.invalidate();
                barChart.setFitBars(true);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }
}