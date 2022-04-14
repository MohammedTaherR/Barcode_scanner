package com.example.zeroq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.joanzapata.pdfview.PDFView;

import java.io.File;

public class Invoice_view extends AppCompatActivity {
PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view);
        pdfView= findViewById(R.id.pdfview);

        String path= getExternalFilesDir(null).getAbsolutePath().toString()+"/Invoice.pdf" ;
        File file= new File(path);
        pdfView.fromFile(file)
                .enableSwipe(true)
                .defaultPage(0)
                .load();
    }
}