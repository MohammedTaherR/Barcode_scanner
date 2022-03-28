package com.example.zeroq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class scan_screen extends AppCompatActivity {
   public static final String products="these are my product name";
    public static final   String price="these are my product price";
    public static final  String codeno="these are my product codeno";

private Button button,button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_screen);
        button= findViewById(R.id.button);

        button2=findViewById(R.id.button2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator= new IntentIntegrator(scan_screen.this);
                intentIntegrator.setPrompt("for flash use volume up");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(capture.class);
                intentIntegrator.initiateScan();


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent= new Intent(scan_screen.this,MainActivity2.class);
startActivity(intent);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> name = new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("products");
        IntentResult intentResult= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(intentResult.getContents()!=null){

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AlertDialog.Builder dialog= new AlertDialog.Builder(scan_screen.this);
                    dbhandler db = new dbhandler(scan_screen.this);
                    for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                        if(intentResult.getContents().equals(snapshot.getKey())) {
                            dialog.setTitle("your product is");
                            dialog.setMessage(snapshot.child("name").getValue().toString());
                            dialog.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(scan_screen.this, "Successfully Added", Toast.LENGTH_SHORT).show();

                                    db.addlist(intentResult.getContents(),snapshot.child("name").getValue().toString(),snapshot.child("price").getValue().toString());

                                }
                            });
                            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            break;
                        }else{
                            continue;



                        }


                    }

                    dialog.show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






        }
        else{
            Toast.makeText(getApplicationContext(), "OOPS,Unable to Add", Toast.LENGTH_SHORT).show();
        }

    }
}