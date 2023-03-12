package com.example.restaurantpos.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.restaurantpos.Models.User;
import com.example.restaurantpos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;


public class Registration extends AppCompatActivity {
    TextView toLogin;
    EditText etEmail, etName, etPassword;
    Button btnConfirmSignup;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        databaseReference= db.getReference(User.class.getSimpleName());

        toLogin=findViewById(R.id.toLogin);
        etEmail=findViewById(R.id.etEmail);
        etName=findViewById(R.id.etName);
        etPassword=findViewById(R.id.etPassword);
        btnConfirmSignup=findViewById(R.id.btnConfirmSignup);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        btnConfirmSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
    }

    private void register() {
        String password=etPassword.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String name=etName.getText().toString().trim();
        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if(name.isEmpty()){
            etPassword.setError("Name is required");
            etPassword.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etPassword.setError("Email is required");
            etPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etPassword.setError("Please enter a valid email");
            etPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(name,email, new ArrayList<String>());
                            databaseReference
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Registration.this,Login.class));
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Registration is failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getApplicationContext(), "Registration is failed! Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}