package com.veldro.remember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class StartActivity extends Activity {

    private  FirebaseAuth mAuth;

    Button emailButton;
    Button twitterButton;
    Button facebookButton;
    Button googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseApp.initializeApp(this);

        emailButton = findViewById(R.id.ButtonEmail);
        twitterButton = findViewById(R.id.ButtonTwitter);
        facebookButton = findViewById(R.id.ButtonFacebook);
        googleButton = findViewById(R.id.ButtonGoogle);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailButtonClicked();
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterButtonClicked();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookButtonClicked();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleButtonClicked();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            startMainActivity();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void emailButtonClicked()
    {
        startLoginActivity();
    }

    public void twitterButtonClicked()
    {
        Toast.makeText(this, "No Twitter Login", Toast.LENGTH_SHORT).show();
    }
    public void facebookButtonClicked()
    {
        Toast.makeText(this, "No Facebook Login", Toast.LENGTH_SHORT).show();
    }
    public void googleButtonClicked()
    {
        Toast.makeText(this, "No Google Login", Toast.LENGTH_SHORT).show();
    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
