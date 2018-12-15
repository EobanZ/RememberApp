package com.veldro.remember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    EditText emailEditText;
    EditText passwordEditText;
    Button signInButton;
    Button signUpButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.SignInButton);
        signUpButton = findViewById(R.id.signUp1Button);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    public void attemptLogin(){

        boolean cancel = false;

        emailEditText.setError(null);
        passwordEditText.setError(null);
        emailEditText.setText("fabi.zwick@live.de");
        passwordEditText.setText("testtest");

        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();



        //Check for valid email adress
        if(TextUtils.isEmpty(email))
        {
            emailEditText.setError("Field Required");
            cancel = true;
        }


        if(!isEmailValid(email) && !cancel)
        {
            emailEditText.setError("Invalid Email");
            cancel = true;
        }

        //Check for a valid password, if the user entered one.
        if(TextUtils.isEmpty(password)&&!cancel)
        {
            passwordEditText.setError("Enter Password");
            cancel = true;
        }
        if(!isPasswordValid(password) && !cancel)
        {
            passwordEditText.setError("Invalid Password");
            cancel = true;
        }





        //Stops here if cancled
        if(cancel)
            return;

        mAuth.signOut();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null)
                    {
                        if(user.isEmailVerified())
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                            user.sendEmailVerification();
                        }
                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isPasswordValid(String pw)
    {
        if(!TextUtils.isEmpty(pw))
            return  pw.length()>4;

        return false;

    }


    public boolean isEmailValid(String email)
    {
        if(!TextUtils.isEmpty(email))
            return email.contains("@");
        return  false;
    }
}
