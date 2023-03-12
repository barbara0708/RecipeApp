package com.example.restaurantpos;

import static com.android.volley.VolleyLog.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.restaurantpos.Login.Login;
import com.example.restaurantpos.Login.Registration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnRegistration;
    ImageView btnGoogle, btnFacebook;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogin=findViewById(R.id.btnLogin);
        btnRegistration=findViewById(R.id.btnRegistration);
        btnFacebook=findViewById(R.id.btnFacebook);
        btnGoogle=findViewById(R.id.btnGoogle);
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc=GoogleSignIn.getClient(this,gso);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegistration:
                startActivity(new Intent(getApplicationContext(), Registration.class));
                break;
            case R.id.btnLogin:
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
            case R.id.btnFacebook:
                startActivity(new Intent(MainActivity.this,FacebookAuthActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                break;
            case R.id.btnGoogle:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent intent=gsc.getSignInIntent();
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                finish();
                startActivity(new Intent(MainActivity.this, MainMenuPage.class));
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}