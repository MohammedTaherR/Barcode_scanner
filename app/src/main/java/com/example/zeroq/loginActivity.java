package com.example.zeroq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    ImageView back;
    EditText emailEdit,passEdit;
    TextView signup;
    Button login;
    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back= findViewById(R.id.back);
        emailEdit=  findViewById(R.id.emailEdit);
        passEdit=  findViewById(R.id.passEdit);
        login= findViewById(R.id.logintoscreen);
        signup= findViewById(R.id.signupagain);
        auth= FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= emailEdit.getText().toString();
                String pass = passEdit.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(loginActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)) {
                    emailEdit.setError("Invalid error");
                    Toast.makeText(loginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Intent intent = new Intent(loginActivity.this, scan_screen.class);

                                startActivity(intent);
                            } else {
                                Toast.makeText(loginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,Signup.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(loginActivity.this,siginorlogin.class);
                startActivity(intent);
            }
        });

    }
}