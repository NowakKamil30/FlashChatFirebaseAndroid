package com.FlashChatByKamilNowak.flashchatnewfirebase;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


     mFirebaseAuth= FirebaseAuth.getInstance();

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


    }


    public void signInExistingUser(View v)   {
        attemptLogin();

    }

    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.FlashChatByKamilNowak.flashchatnewfirebase.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin() {

        String email= mEmailView.getText().toString();
        String password=mPasswordView.getText().toString();

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"email or password field is empty",Toast.LENGTH_SHORT).show();
            return;
        }

           Toast.makeText(getApplicationContext(),"Login in progress",Toast.LENGTH_SHORT).show();


        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FlashChat", "singInWithEmail() onComplete: "+task.isSuccessful());

                if(!task.isSuccessful())
                {
                    Log.d("FlashChat", "Problem signing in: "+task.getException());
                    showErrorDialog("There was a problem signing in");
                }
                else
                {
                    Intent intent=new Intent(LoginActivity.this,MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

    }

        private void showErrorDialog(String message)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Oops")
                    .setPositiveButton(android.R.string.ok, null)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


}