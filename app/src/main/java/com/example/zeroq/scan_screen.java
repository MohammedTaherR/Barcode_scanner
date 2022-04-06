package com.example.zeroq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scan_screen extends AppCompatActivity {

private Button button,button2;
DrawerLayout drawerLayout;
TextView nav_name,nav_Email;
FirebaseAuth auth;
FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_screen);
        button= findViewById(R.id.button);

      auth=FirebaseAuth.getInstance();
      user=auth.getCurrentUser();
        drawerLayout= findViewById(R.id.drawer_layout);
        button2=findViewById(R.id.button2);
nav_name=findViewById(R.id.Nav_Name);
nav_Email=findViewById(R.id.Nav_Email);
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

    userRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String name= snapshot.child("name").getValue().toString();
            String email= snapshot.child("email").getValue().toString();
            nav_Email.setText("Email:"+email);
            nav_name.setText("Name:"+name);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });


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
    public void ClickMenu(View view){
        openDrawer(drawerLayout);


    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void clicklogo(View view){
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    protected  void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("products");
        IntentResult intentResult= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(intentResult.getContents()!=null){

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AlertDialog.Builder dialog= new AlertDialog.Builder(scan_screen.this);
                   View mview=getLayoutInflater().inflate(R.layout.dialog_product,null);

         dbhandler db = new dbhandler(scan_screen.this);
                    for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                        if(intentResult.getContents().equals(snapshot.getKey())) {

                            final EditText dialogEdittext= (EditText)mview.findViewById(R.id.dialog_edittext);
                            final TextView dialogname= (TextView) mview.findViewById(R.id.dialog_name);
                            final TextView dialogprice= (TextView)mview.findViewById(R.id.dialog_price);
                            final TextView dialogcode= (TextView)mview.findViewById(R.id.dialog_codeno);

                            dialogcode.setText(snapshot.getKey());
                            dialogname.setText(snapshot.child("name").getValue().toString());
                            dialogprice.setText(snapshot.child("price").getValue().toString());


                            dialog.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String  no_of_quantity= dialogEdittext.getText().toString();
                                     Integer Num_p_price = Integer.parseInt(snapshot.child("price").getValue().toString());
                                     Integer Num_p_Quantity=Integer.parseInt(no_of_quantity);

                                     int total_amt=Num_p_price*Num_p_Quantity;
                                    // tw2.setText(Integer.toString(total_amt));






                                    Toast.makeText(scan_screen.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(scan_screen.this,MainActivity2.class) ;
                                    intent.putExtra("noofquantity",no_of_quantity);
                                    startActivity(intent);
                                    db.addlist(intentResult.getContents(),snapshot.child("name").getValue().toString(),Integer.toString(total_amt),no_of_quantity);

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
dialog.setView(mview);
                    AlertDialog dialog1=dialog.create();

                    dialog1.show();

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