package com.example.falle.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    EditText email;
    EditText pass;
    Button Log_in;
    Button register;
    FirebaseAuth mAuth;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference = database.child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);

        register=findViewById(R.id.register);
        Log_in = findViewById(R.id.Log_in);

       mAuth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();

            }
        });
        Log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Register.this, InicioSesion.class);
                startActivity(i);

            }
        });



    }
    private void signupUser(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                    Intent i = new Intent(Register.this, UserConfiguration.class);
                    i.putExtra("email", email.getText().toString());
                    i.putExtra("pass", pass.getText().toString());
                    startActivity(i);
                    Toast.makeText(Register.this, "Register succesful",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(Register.this, "User already Registered",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
