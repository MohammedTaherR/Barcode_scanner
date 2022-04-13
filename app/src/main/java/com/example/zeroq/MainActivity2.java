package com.example.zeroq;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements  PaymentResultListener{

    ListView listView;
Button scan , pay;
TextView textView,total_amt;
    int total_Amount = 0;
    private FirebaseAuth auth;
    FirebaseDatabase rootnode;
    DatabaseReference databaseReference,databaseReference1;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<String> code = new ArrayList<>();
    dbhandler db = new dbhandler(MainActivity2.this);
    ArrayList<String> quantity = new ArrayList<>();
@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);

    textView = findViewById(R.id.textView);

    listView = findViewById(R.id.listview);
    total_amt = findViewById(R.id.textView10);
    customadapter ad = new customadapter(this, name, price, code, quantity);
    listView.setAdapter(ad);
    scan = findViewById(R.id.button5);
    pay = findViewById(R.id.button3);
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
            if (!name.isEmpty() && !price.isEmpty() && !code.isEmpty() && !quantity.isEmpty()) {




                Checkout checkout = new Checkout();
                checkout.setImage(R.drawable.ic_person);
                try {
                    String Total=total_amt.getText().toString();
                    Integer Int_i= Integer.parseInt(Total);
                    int float_i=Math.round(Float.parseFloat(String.valueOf(Int_i))*100);
                    String Total_graph = total_amt.getText().toString();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    rootnode = FirebaseDatabase.getInstance();
                    databaseReference1 = rootnode.getReference("Users").child(uid).child("purchase");
                    databaseReference1.push().setValue(Total_graph).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


                            } else {

                            }
                        }
                    });




                    JSONObject options = new JSONObject();
                    options.put("name", R.string.app_name);
                    options.put("description", "Payment for Anything");
                    options.put("send_sms_hash", true);
                    options.put("allow_rotation", false);

                    options.put("currency", "INR");
                    options.put("amount", float_i   );

                    JSONObject preFill = new JSONObject();
                    preFill.put("email", "email");
                    preFill.put("contact", "phone");

                    options.put("prefill", preFill);

                    checkout.open(MainActivity2.this, options);











                } catch (Exception e) {
                    e.printStackTrace();


                }





            } else {
                Toast.makeText(MainActivity2.this, "Your Cart is Empty!", Toast.LENGTH_SHORT).show();
            }

        }
            });



    Cursor res = db.getdata();
    while (res.moveToNext()) {
        code.add(res.getString(0));
        name.add(res.getString(1));
        price.add(res.getString(2));
        quantity.add(res.getString(3));
        ad.notifyDataSetChanged();


    }

    //get total Amount of All products in cart

    for (int i = 0; i < price.size(); i++) {

        total_Amount = total_Amount + Integer.parseInt(price.get(i));
String str_Amount= String.valueOf(total_Amount);
        total_amt.setText(str_Amount);



    }

}

    @Override
    public void onPaymentSuccess(String s) {
        try {
            customadapter ad = new customadapter(this, name, price, code, quantity);
            name.clear();
            price.clear();
            code.clear();
            quantity.clear();
ad.notifyDataSetChanged();
            db.delete();

            total_amt.setText("0");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invoice Text");
            builder.setMessage("This is the Demo Invoice");
            builder.setPositiveButton("download", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity2.this, "Downloading Invoice...", Toast.LENGTH_SHORT).show();

//                    ActivityCompat.requestPermissions(MainActivity2.this,new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//                    PdfDocument pdfDocument= new PdfDocument() ;
//                    Paint paint= new Paint();
//                    PdfDocument.PageInfo pageInfo= new PdfDocument.PageInfo.Builder(250,400,1).create();
//                    PdfDocument.Page page= pdfDocument.startPage(pageInfo);
//                    Canvas canvas= page.getCanvas();
//                    canvas.drawText("Welcome to Zeroq",40,50,paint);
//                    pdfDocument.finishPage(page);
//                    File file = new File(Environment.getExternalStorageDirectory(),"/trial.pdf");
//                    try {
//                        pdfDocument.writeTo(new FileOutputStream(file));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    pdfDocument.close();
//createPDF();

                    printInvoice();
                    Toast.makeText(MainActivity2.this, "Downloading!!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

            Toast.makeText(this, "Payment success!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void printInvoice() {

    PdfDocument mypdfDocument= new PdfDocument();
    Paint myPaint = new Paint();
    String [] columns = {"Name","Price","Quantity"};
    PdfDocument.PageInfo mypageinfo=new  PdfDocument.PageInfo.Builder(1000,900,1).create();
    PdfDocument.Page myPage = mypdfDocument.startPage(mypageinfo);
    Canvas canvas =myPage.getCanvas();
    myPaint.setTextSize(80);
    canvas.drawText("Invoice",30,80,myPaint);


    myPaint.setTextAlign(Paint.Align.LEFT);
myPaint.setColor(Color.rgb(150,150,150));
canvas.drawRect(30,150,canvas.getWidth()-40,160,myPaint);
        canvas.drawText("Name",50,200,myPaint);
        canvas.drawText("Quantity",80,200,myPaint);
        canvas.drawText("Price",120,200,myPaint);

        int j =150;
        int l = 50;
for(int i=0;i<=name.size();i++){

canvas.drawRect(30,j+10,canvas.getWidth()-40,160,myPaint);
j=j+10;
canvas.drawText(name.get(i),l,j,myPaint);
l=l+20;
    canvas.drawText(quantity.get(i),l+10,j,myPaint);
    l=l+20;
    canvas.drawText(price.get(i),l+10,j,myPaint);


}
myPaint.setColor(Color.BLACK);

        for (int i = 0; i < price.size(); i++) {

            total_Amount = total_Amount + Integer.parseInt(price.get(i));
            String str_Amount= String.valueOf(total_Amount);
//            total_amt.setText(str_Amount);



        }
canvas.drawText("Total="+total_amt,l,j+100,myPaint);
mypdfDocument.finishPage(myPage);

File file= new File(this.getExternalFilesDir("/"),"Invoice.pdf");
        try {
            mypdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
mypdfDocument.close();
    }


    @Override
    public void onPaymentError(int i, String s) {


    }
}
