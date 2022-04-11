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

public class Expense_graph extends AppCompatActivity {
    BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_graph);

        barChart=findViewById(R.id.bar_chart);
        ArrayList<BarEntry> barEntries= new ArrayList<>();
        for(int i=3;i<=10;i++){
            float values=(float) (i*10.0);
            BarEntry barEntry= new BarEntry(i,values);
            barEntries.add(barEntry);
        }
        BarDataSet barDataSet= new BarDataSet(barEntries,"Price");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(false);
        barChart.setData(new BarData(barDataSet));

        barChart.animateY(5000);
        barChart.getDescription().setText("PRICE");
        barChart.getDescription().setTextColor(Color.BLUE);



    }
}