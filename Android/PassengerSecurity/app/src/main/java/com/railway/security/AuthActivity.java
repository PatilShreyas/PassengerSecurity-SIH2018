package com.railway.security;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.railway.security.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "AuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private String phoneNumber;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    DatabaseReference mDatabase;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private TextView mDetailText;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private EditText mAadhaarNumberField;
    private EditText mVerificationField;

    private RelativeLayout authLayout;
    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private ProgressDialog mProgressDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        if(Build.VERSION.SDK_INT < 23){

        }else{
            if(checkAndRequestPermissions()){

            }
        }
          // Assign views

        mPhoneNumberViews = (ViewGroup) findViewById(R.id.phone_auth_fields);
        authLayout = (RelativeLayout) findViewById(R.id.authLayout);
        mDetailText = (TextView) findViewById(R.id.detail);

        mAadhaarNumberField = (EditText) findViewById(R.id.field_aadhaar_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);

        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);


        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mAadhaarNumberField.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mAadhaarNumberField,InputMethodManager.SHOW_IMPLICIT);

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                hideProgressDialog();
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mAadhaarNumberField.setError("Invalid aadhaar number.");
                    hideProgressDialog();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    hideProgressDialog();

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                hideProgressDialog();
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                hideProgressDialog();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);

        if (currentUser == null) {
            Toast.makeText(getApplicationContext(), "Complete This Step", Toast.LENGTH_LONG).show();
            ConnectionDetector cd = new ConnectionDetector(this);
            if (!cd.isConnectingToInternet()) {
                /* AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Failed")
                        .setCancelable(false)
                        .setMessage("No Internet Connectivity...!")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
                */
            }

        } else {
            if(currentUser.getDisplayName() != null || currentUser.getPhoneNumber() != null) {
                Intent i = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }




    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);

        mVerificationField.setVisibility(View.GONE);
        mVerifyButton.setVisibility(View.GONE);
        mResendButton.setVisibility(View.GONE);

        mStartButton.setVisibility(View.VISIBLE);
        mAadhaarNumberField.setVisibility(View.VISIBLE);

        authLayout.setVisibility(View.VISIBLE);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mAadhaarNumberField);

                mDetailText.setVisibility(View.VISIBLE);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(mVerifyButton, mResendButton, mAadhaarNumberField, mVerificationField);
                mResendButton.setVisibility(View.VISIBLE);
                mVerifyButton.setVisibility(View.VISIBLE);
                mVerificationField.setVisibility(View.VISIBLE);
                mStartButton.setVisibility(View.GONE);
                disableViews(mStartButton);
                mDetailText.setVisibility(View.VISIBLE);
                mDetailText.setText("OTP is sent to "+phoneNumber);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(mStartButton, mVerifyButton, mResendButton, mAadhaarNumberField,
                        mVerificationField);
                hideProgressDialog();
                mDetailText.setTextColor(Color.RED);
                mDetailText.setText("Verification Failed");
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(mStartButton, mVerifyButton, mResendButton, mAadhaarNumberField,
                        mVerificationField);
                mDetailText.setText("Verification Successful...\nWelcome "+this.user.name);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText("instant-validation");
                        mProgressDialog.setMessage("Loading...");
                        showProgressDialog();
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                mDetailText.setText("Sign In Failed");
                hideProgressDialog();
                mDetailText.setTextColor(Color.RED);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                try {
                    hideProgressDialog();
                    addUser();
                    authLayout.setVisibility(View.GONE);
                    startActivity(new Intent(AuthActivity.this,MainActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            authLayout.setVisibility(View.VISIBLE);

//            mStatusText.setText("Signed Out");;
        } else {
            // Signed in
            mPhoneNumberViews.setVisibility(View.GONE);
            enableViews(mAadhaarNumberField, mVerificationField);
            mAadhaarNumberField.setText(null);
            mVerificationField.setText(null);

//            mStatusText.setText("Sign In");
            mDetailText.setText("Verification Completed...Please Wait...");
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mAadhaarNumberField.getText().toString();

        if (phoneNumber.trim().length() == 0 ) {
            mAadhaarNumberField.setError("Invalid Phone Number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                getPhoneNo();
                break;

            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }
                mProgressDialog.setMessage("Loading...");
                showProgressDialog();
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;

          case R.id.button_resend:
                resendVerificationCode(mAadhaarNumberField.getText().toString(), mResendToken);
                break;

        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Sending OTP");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private boolean checkAndRequestPermissions() {
        int storagePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int finePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coursePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int audioPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int smsPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (finePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coursePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (audioPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (smsPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(), "Warning : Some features won't work", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Are you sure want to Exit ?");
                alert.setTitle("Quit ?");
              //  alert.setIcon(android.R.drawable.ic_warning_black_24dp);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
    }
    public void getPhoneNo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.getMobile);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response: "+response);
                try {
                    JSONObject json = new JSONObject(response);
                    user = new User();
                    user.phoneNumber = json.optString("MOBILE_NO");
                    phoneNumber = user.phoneNumber;
                    user.name = json.optString("NAME");
                    user.aadhaarNo = mAadhaarNumberField.getText().toString();
                    user.fcmToken = FirebaseInstanceId.getInstance().getToken();

                    showProgressDialog();
                    startPhoneNumberVerification(phoneNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "No Aadhaar Data found!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("aadhaarNo",mAadhaarNumberField.getText().toString());
                return params;
            }
        };

        queue.add(request);
        showProgressDialog();
    }
    public String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void addUser(){

        user.uid = getUid();
        System.out.println("Add User Started");
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.name).build();

        FirebaseUser fUser = mAuth.getCurrentUser();
        fUser.updateProfile(profile);
        System.out.println("Profile Updated");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(getUid());
        mDatabase.setValue(user);
        System.out.println("Added in firebase");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.insertUser);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",getUid());
                params.put("name",user.name);
                params.put("mobNo",user.phoneNumber);
                params.put("aadhaarNo",user.aadhaarNo);
                params.put("FcmToken",user.fcmToken);
                return params;
            }
        };

        queue.add(request);
        System.out.println("Queue Added");
    }
}