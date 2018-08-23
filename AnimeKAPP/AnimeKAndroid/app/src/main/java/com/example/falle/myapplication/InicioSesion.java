package com.example.falle.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InicioSesion extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "google";
    private FirebaseAuth mAuth;
    private static int RC_SIGN_IN = 9001;
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    EditText pass;
    EditText email;
    Button register;
    Button LogIn;
    int i=0;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference = database.child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio_sesion2);
        email= findViewById(R.id.email);
        pass = findViewById(R.id.password);
        LogIn = findViewById(R.id.Log_in);
        register= findViewById(R.id.register);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mAuth=FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

        mAuth = FirebaseAuth.getInstance();
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InicioSesion.this, Register.class);
                startActivity(i);
            }
        });

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            firebaseAuthWithGoogle(googleSignInResult);

        }

    }
    @Override
    public void onStart() {
        super.onStart();


    }



    private void firebaseAuthWithGoogle(GoogleSignInResult acct) {
        if(acct.isSuccess()){
            GoogleSignInAccount googleSignInAccount= acct.getSignInAccount();
            Log.i("key", googleSignInAccount.getPhotoUrl()+"");
            final Intent intent = new Intent(InicioSesion.this, MainActivity.class);
            intent.putExtra("nombre", googleSignInAccount.getDisplayName());
            intent.putExtra("photo", googleSignInAccount.getPhotoUrl().toString());
            intent.putExtra("correo", googleSignInAccount.getEmail());
            final Users u = new Users(googleSignInAccount.getDisplayName(), googleSignInAccount.getEmail(), googleSignInAccount.getPhotoUrl().toString(),"pass");
            Query query = databaseReference.orderByChild("email").equalTo(googleSignInAccount.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        databaseReference.push().setValue(u);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            startActivity(intent);
        }


    }
    public void logIn(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                Toast.makeText(InicioSesion.this, "Log In Succesful", Toast.LENGTH_SHORT).show();
                                final Query query = databaseReference.orderByChild("email").equalTo(email.getText().toString());
                                query.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        Intent i = new Intent(InicioSesion.this, MainActivity.class);
                                        i.putExtra("email", email.getText().toString());
                                        i.putExtra("userName", dataSnapshot.child("name").getValue().toString());
                                        i.putExtra("profile_picture", String.valueOf(dataSnapshot.child("profile_picture").getValue()));
                                        i.putExtra("uniqueID", "fromUserConfiguration");
                                        startActivity(i);


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


                        }catch (IllegalArgumentException i){
                            Toast.makeText(InicioSesion.this,"Fill up the email and password field", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }







    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
