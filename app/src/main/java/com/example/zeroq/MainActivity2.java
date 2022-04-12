package com.example.zeroq;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements  PaymentResultListener{

    ListView listView;
Button scan , pay;
TextView textView,total_amt;
    private FirebaseAuth auth;
    FirebaseDatabase rootnode;
    DatabaseReference databaseReference,databaseReference1;
@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    dbhandler db = new dbhandler(MainActivity2.this);
    textView = findViewById(R.id.textView);
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<String> code = new ArrayList<>();
    ArrayList<String> quantity = new ArrayList<>();
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




                    name.clear();
                    price.clear();
                    code.clear();
                    quantity.clear();
                    ad.notifyDataSetChanged();
                    db.delete();










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
    int total_Amount = 0;
    for (int i = 0; i < price.size(); i++) {

        total_Amount = total_Amount + Integer.parseInt(price.get(i));
String str_Amount= String.valueOf(total_Amount);
        total_amt.setText(str_Amount);



    }

}

    @Override
    public void onPaymentSuccess(String s) {
        try {
            total_amt.setText("0");

            Toast.makeText(this, "Payment success!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onPaymentError(int i, String s) {


    }
}
