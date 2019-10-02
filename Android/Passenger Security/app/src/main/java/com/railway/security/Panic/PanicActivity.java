package com.railway.security.Panic;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.railway.security.Panic.models.Alert;
import com.railway.security.Panic.models.User;
import com.railway.security.RegisterFirActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by HARSHALI PATIL on 22/12/2017.
 */

public class PanicActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    public static final int REQUEST_CHECK_SETTINGS_GPS = 0x1 ;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; // SEC
    private static int FATEST_INTERVAL = 3000; // SEC
    private static int DISPLACEMENT = 100; // METERS
    private String msg = "I'm In Danger";
    private ProgressDialog mProgressDialog;
    public static final int RECORDING_TIME_OUT = 11000;
    public static final int PERMISSION_REQUEST_CODE = 123;
    private static final int INTENT_REQUEST = 100;

    public double latitude, longitude;
    List<String> mobile;
    List<String> token;
    public Alert alert;
    private Activity context;
    MediaRecorder mRecorder;
    public DatabaseReference mDatabase;
    public DatabaseReference mDatabaseN;
    public String mFileName;
    public StorageReference mStorage;
    String voiceUrl;
    boolean isGPSEnabled = false;
    private User user;
    String time;
    private boolean isAlerted;
    LocationManager locationManager;
    private int count = 0;

    Button resendAlert, resendRecord, turnOff, turnOn;

    public PanicActivity(final Activity context, boolean isAlerted) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(getUid());
        mStorage = FirebaseStorage.getInstance().getReference();
        this.context = context;
        this.isAlerted = isAlerted;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Run-time request permission
            ActivityCompat.requestPermissions(context, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();

            }
        }
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            //GPS is not enabled
            enableGPS();
        }else {
            if (isAlerted) {
                createLocationRequest();
            }
        }

    }

    public void stopAlert() {
        mRequestingLocationUpdates = false;
        mDatabase.child("alert").child("status").setValue(false);
        try {
            if(mRecorder != null) {
                mRecorder.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopLocationUpdates();
        isAlerted = false;
        count = 0;
    }

    public void startRecording() {
        System.out.println("Recording Started");
        mFileName = getUid() + "-" + getName() + ".aac";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mRecorder.start();
        } catch (Exception e) {
            Toast.makeText(context, "Error in Recording", Toast.LENGTH_LONG).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRecording();
            }
        }, RECORDING_TIME_OUT);
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            uploadAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadAudio() {
        try {
            StorageReference file = mStorage.child("Audio").child(mFileName);
            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mFileName));

            file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(context, "Recording Uploaded", Toast.LENGTH_LONG).show();
                    voiceUrl = String.valueOf(taskSnapshot.getDownloadUrl());

                    mDatabase.child("alert").child("voiceUrl").setValue(voiceUrl);
                    Toast.makeText(context, "Recording Successful", Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception e){}
    }

    public void addAlert() {
        mRequestingLocationUpdates = true;
        enableGPS();
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        startRecording();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //sendNotification();
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        time = dateFormat.format(c.getTime());


        alert = new Alert(true,getUid(),getPhoneNumber(),latitude,longitude, time);

        //Setting up alert data in database

        mDatabase.child("alert").setValue(alert).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Alert Successful",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        //finished
        updateDataSet();
    }
    /*
        private void sendNotification() {
            token = new ArrayList<>();
            loadDataForNotification();

        }

        private void loadDataForNotification() {
            mDatabaseN = FirebaseDatabase.getInstance().getReference().child("user");
            mDatabaseN.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        user = dataSnapshot.child(getUid()).getValue(User.class);
                        mobile = user.trusty;
                        proceedToNotify();
                        sendMessage();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    */
    private void sendMessage() {
        try {
            System.out.println("Sending Message...");
            String url = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;

            String message = msg + ". Follow this link for my location\nLocation : " + url;
            for (int i = 0; i < mobile.size(); i++) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mobile.get(i), null, message, null, null);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
/*
    private void proceedToNotify() {
        mDatabaseN.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    User user = d.getValue(User.class);
                    if(user != null || user.phoneNumber != null || user.uid != null) {
                        System.out.println("USERS :++++++==" + user.phoneNumber);
                        List<String> other = new ArrayList<>();
                        if (mobile == null) {
                            Toast.makeText(context, "Add Trusty Persons First", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i = 0; i < mobile.size(); i++) {
                            if (user.phoneNumber.equals(mobile.get(i))) {
                                if (user.otherAlerts != null) {
                                    other = user.otherAlerts;
                                }
                                if (!other.contains(getUid())) {
                                    other.add(getUid());
                                }
                                mDatabaseN.child(user.uid).child("otherAlerts").setValue(other);
                                token.add(user.fcmToken);
                            }
                        }
                    }
                }
                sendFCMNotification();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendFCMNotification() {
        System.out.println(token.size());
        FCMNotification fcm = new FCMNotification(context);
        for(int i=0; i<token.size(); i++){
            fcm.sendMessageToToken("SecurMi",getName()+" is in danger",token.get(i));
        }
    }
*/
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getPhoneNumber(){
        return FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }

    public String getName(){
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }



    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        //Fix first time run app if permission doesn't grant yet so can't get anything
        mGoogleApiClient.connect();


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context, "This device is not supported", Toast.LENGTH_LONG).show();
                context.finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (Exception e){}
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mRequestingLocationUpdates || isAlerted)
            startLocationUpdates();
    }



    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Connection FAILED:::+++== "+connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(count == 0) {
            count++;
            if(!isAlerted) {
                //sendNotification();
            }
        }
        mLastLocation = location;
        longitude = mLastLocation.getLongitude();
        latitude = mLastLocation.getLatitude();
        mDatabase.child("alert").child("latitude").setValue(mLastLocation.getLatitude());
        mDatabase.child("alert").child("longitude").setValue(mLastLocation.getLongitude());
        Toast.makeText(context,"Location Updated",Toast.LENGTH_LONG).show();
    }

    public void enableGPS(){
        isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if(!isGPSEnabled) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            PendingResult result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback() {
                @Override
                public void onResult(@NonNull Result result) {
                    switch (result.getStatus().getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                result.getStatus().startResolutionForResult(context, REQUEST_CHECK_SETTINGS_GPS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            });
        }
    }

    public void setMessage(String message){
        mDatabase.child("alert").child("message").setValue(message);
        this.msg = message;
        sendMessage();
    }

    public void sendOffLineMessage(String msg){
        FirebaseDatabase.getInstance().getReference().child("user").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mobile = user.trusty;
                sendMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateDataSet(){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(com.railway.security.R.string.insertPanic);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("WEB RESPONSE : "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error... Try Again",Toast.LENGTH_LONG).show();
                error.fillInStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",getUid());

                return params;
            }
        };

        queue.add(request);
    }
}

