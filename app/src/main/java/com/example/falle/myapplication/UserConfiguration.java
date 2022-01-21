package com.example.falle.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserConfiguration extends AppCompatActivity {

    EditText userName;
    EditText password;
    Button ok;
    Button Cancel;
    TextView email;
    ImageView profile_picture;
    Uri uri;
    private static final int RESULT_LOAD_IMAGE = 100;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference = database.child("Users");
    String nombre;
    String emailChange;
    String profile_pictureChange;
    String unique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_configuration);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        ok = findViewById(R.id.ok);
        email = findViewById(R.id.email);
        Cancel = findViewById(R.id.cancelar);
        profile_picture = findViewById(R.id.profile_picture);


        Intent i = getIntent();
        final String emailIntent = i.getStringExtra("email");
        final String pass = i.getStringExtra("pass");

        unique = i.getStringExtra("Unique");




        if(unique!=null){
            emailChange = i.getStringExtra("email");
            nombre =i.getStringExtra("userName");
            profile_pictureChange = i.getStringExtra("profile_picture");
            email.setText("Email "+ emailChange);
            userName.setText(nombre);
            Picasso.with(UserConfiguration.this).load(profile_pictureChange).into(profile_picture);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            dataSnapshot.getRef().child("name").setValue(userName.getText().toString());
                            dataSnapshot.getRef().child("pass").setValue(password.getText().toString());
                            dataSnapshot.getRef().child("picture").setValue(uri.toString());


                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }



        email.setText("Email" + emailIntent);
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Users u;

                try {
                    Intent i = new Intent(UserConfiguration.this, MainActivity.class);
                    i.putExtra("userName", userName.getText().toString());
                    i.putExtra("email", emailIntent);
                    i.putExtra("pass", pass);
                    i.putExtra("profile_picture", uri.toString());
                    i.putExtra("uniqueID", "fromUserConfiguration");
                    startActivity(i);
                    if (password.getText().toString().length() == 0) {
                        u = new Users(userName.getText().toString(), emailIntent, uri.toString(), pass);
                    }else{
                        u = new Users(userName.getText().toString(), emailIntent, uri.toString(), password.getText().toString());
                    }
                    final Query query = databaseReference.orderByChild("name").equalTo(userName.getText().toString());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    databaseReference.push().setValue(u);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }catch(NullPointerException n){
                        Toast.makeText(UserConfiguration.this, "Please select a photo", Toast.LENGTH_SHORT).show();
                    }




            }
        });


    }

    private void getImageFromAlbum() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE) {
            uri = data.getData();
            Picasso.with(UserConfiguration.this).load(uri).into(profile_picture);
          }
        }
    }
