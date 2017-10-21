package sjohnsoncf.whatzapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.signup_email)
    EditText mSignupEmail;
    @BindView(R.id.signup_password)
    EditText mSignupPassword;
    private Intent mHomeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mHomeIntent = new Intent(this, HomeActivity.class);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: user not null??" + user);
                    startActivity(mHomeIntent);
                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @OnClick(R.id.btn_signup)
    public void userSignup(){
        String pw = mSignupPassword.getText().toString();
        if(pw.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters in length.", Toast.LENGTH_LONG).show();
            return;
        }
        createAccount(mSignupEmail.getText().toString(), pw);
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //task is successful?
                if(task.isSuccessful()) {
//                    Toast.makeText(SignUpActivity.this, "auth success", Toast.LENGTH_LONG).show();
                    startActivity(mHomeIntent);
                } else {
                    //task failed (auth failed)
                    Toast.makeText(SignUpActivity.this, "auth failure", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_signup_to_login)
    public void gotoLogin(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
