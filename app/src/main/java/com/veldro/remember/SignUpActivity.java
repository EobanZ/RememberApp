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
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends Activity {

    EditText emailEditText;
    EditText passwordEditText;
    Button signUpButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordSignUpEditText);
        signUpButton = findViewById(R.id.signUpButton);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

    }

    private void attemptSignIn()
    {
        emailEditText.setError(null);
        passwordEditText.setError(null);
        boolean cancel = false;

        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();


        if(TextUtils.isEmpty(email) && !cancel)
        {
            emailEditText.setError("Field Required");
            cancel = true;
        }
        if(!isValidEmail(email) && !cancel)
        {
            emailEditText.setError("Invalid Email Adress");
            cancel = true;
        }

        if(TextUtils.isEmpty(password) && !cancel)
        {
            passwordEditText.setError("Field Required");
            cancel = true;
        }
        if(!isValidPassword(password) && !cancel)
        {
            passwordEditText.setError("Invalid Password");
            cancel = true;
        }

        if(cancel)
            return;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Account Created. You can now Sign In and verify your email adress", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Sign Up Failed :/", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private boolean isValidEmail(String email)
    {
        return email.contains("@");
    }

    private boolean isValidPassword(String password)
    {
        boolean isValid = true;
        if(password.length() < 4 || password.length() > 32)
        {
            isValid = false;
        }
        return  isValid;
    }
}
