package com.example.my_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private EditText inputPhoneNumber,inputVerificationCode;
    private Button SendVerificationCodeButton,VerifyButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String UserNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        SendVerificationCodeButton=(Button)findViewById(R.id.send_ver_code_button);
        VerifyButton=(Button)findViewById(R.id.verify_button);
        inputPhoneNumber=(EditText)findViewById(R.id.phone_number_input);
        inputVerificationCode=(EditText)findViewById(R.id.verification_code_input);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(VerificationActivity.this);



        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phonNumber=inputPhoneNumber.getText().toString();
                UserNumber=phonNumber.substring(3);

                if(TextUtils.isEmpty(phonNumber))
                {
                    Toast.makeText(VerificationActivity.this, "Phone Number is missing", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setTitle("Phone Verification");
                    progressDialog.setMessage("Please wait, while we are authenticating your phone");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phonNumber,60,TimeUnit.SECONDS,VerificationActivity.this,callbacks
                    );
                  /*  PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phonNumber)
                            .setTimeout(60L,TimeUnit.SECONDS).setActivity(VerificationActivity.this)
                            .setCallbacks(callbacks).build();
                    PhoneAuthProvider.verifyPhoneNumber(options);*/
                }
            }
        });

        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                String verificationCode=inputVerificationCode.getText().toString();
                if(TextUtils.isEmpty(verificationCode))
                {
                    Toast.makeText(VerificationActivity.this, "Please Write Verification Code", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setTitle("Verification Code");
                    progressDialog.setMessage("Please wait, while we are verifying verification code");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationID,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerificationActivity.this, "Invalid Phone Number Please Enter Phone Number With Country Code Number", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
                SendVerificationCodeButton.setVisibility(View.VISIBLE);
                inputPhoneNumber.setVisibility(View.VISIBLE);

                VerifyButton.setVisibility(View.INVISIBLE);
                inputVerificationCode.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationID=s;
                resendingToken=forceResendingToken;

                progressDialog.dismiss();
                System.out.println("message:   " +s);

                Toast.makeText(VerificationActivity.this, "Code has been sent", Toast.LENGTH_SHORT).show();

                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                inputVerificationCode.setVisibility(View.VISIBLE);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    //FirebaseUser user=task.getResult().getUser();
                    Intent intent=new Intent(VerificationActivity.this,NewPasswordActivity.class);
                    intent.putExtra("phone",UserNumber);
                    startActivity(intent);
                    finish();

                    //here
                }
                else
                {
                  String error= task.getException().toString();
                  if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(VerificationActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();}
                }
            }
        });
    }
}