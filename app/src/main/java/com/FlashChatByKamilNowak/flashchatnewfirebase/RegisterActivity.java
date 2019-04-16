package com.FlashChatByKamilNowak.flashchatnewfirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";


    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);

        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        mAuth=FirebaseAuth.getInstance();

    }


    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {


        mEmailView.setError(null);
        mPasswordView.setError(null);
        mUsernameView.setError(null);

        String username=mUsernameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if(TextUtils.isEmpty(username))
        {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        else if(!isUserNameValid(username))
        {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView=mUsernameView;
            cancel=true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            createFirebaseUser();
        }
    }
    private boolean isUserNameValid(String username)
    {
        return username.length()>5;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.length()>4;
    }

    private boolean isPasswordValid(String password) {
        String confirmPassword=mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length()>6 ;
    }

        private void createFirebaseUser()
        {
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("FlashChat","user create account "+task.isSuccessful());

                    if(!(task.isSuccessful()))
                    {
                        Log.d("FlashChat", "fail");
                        showErrorDialog("Registration attempt failed");
                    }
                    else
                    {
                        saveDisplayName();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }

        private void saveDisplayName()
        {
            FirebaseUser user = mAuth.getCurrentUser();
            String displayName = mUsernameView.getText().toString();

            if (user !=null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("FlashChat", "User name updated.");
                                }
                            }
                        });

            }

        }

        private void showErrorDialog(String ms)
        {
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("Oops");
            builder.setMessage(ms);
            builder.setPositiveButton(android.R.string.ok,null);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }



}
