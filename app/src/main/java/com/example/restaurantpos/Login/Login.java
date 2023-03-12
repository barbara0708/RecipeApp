package com.example.restaurantpos.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantpos.MainMenuPage;
import com.example.restaurantpos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class Login extends AppCompatActivity implements View.OnClickListener {
    TextView toSignup;
    Button btnConfirmLogin;
    FirebaseAuth mAuth;
    EditText etEmailLogin, etPasswordLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        toSignup=findViewById(R.id.toSignup);
        btnConfirmLogin=findViewById(R.id.btnConfirmLogin);
        etEmailLogin=findViewById(R.id.etEmailLogin);
        etPasswordLogin=findViewById(R.id.etPasswordLogin);
        toSignup.setOnClickListener(this);
        btnConfirmLogin.setOnClickListener(this);
    }


    private void login() {
        String password=etPasswordLogin.getText().toString().trim();
        String email=etEmailLogin.getText().toString().trim();
        if(email.isEmpty()){
            etEmailLogin.setError("Email is required");
            etEmailLogin.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailLogin.setError("Please provide a valid email");
            etEmailLogin.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPasswordLogin.setError("Password is required");
            etPasswordLogin.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                startActivity(new Intent(getApplicationContext(), MainMenuPage.class));
                            }else {

                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Failed to login!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.toSignup:
                startActivity(new Intent(getApplicationContext(),Registration.class));
                break;
            case R.id.btnConfirmLogin:
                login();
                break;
        }
    }
}