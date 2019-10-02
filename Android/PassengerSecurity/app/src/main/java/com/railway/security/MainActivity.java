package com.railway.security;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.railway.security.Panic.PanicActivity;
import com.railway.security.Panic.models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.railway.security.Panic.PanicActivity.PERMISSION_REQUEST_CODE;

public class MainActivity extends HiddenCameraActivity implements SurfaceHolder.Callback {

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    DatabaseReference mDatabase, mDataAlert;
    boolean isAlerted;
    Button alert;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    PanicActivity panic;
    MediaRecorder mMediaRecorder;
    ImageView alertView;
    CameraConfig mCameraConfig;
    TextView alertCount;
    private List<String> usersUid;
    private List userList;
    private int count = 0;
    boolean isEnabledGPS = false;

    public static Camera mCamera;
    public static boolean mPreviewRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ConnectionDetector cd = new ConnectionDetector(this);

        Button register = (Button) findViewById(R.id.button_regfir);
        Button showStatus = (Button) findViewById(R.id.button_showstatus);
        Button call = (Button) findViewById(R.id.button_call);
        alert = (Button) findViewById(R.id.button_panic);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterFirActivity.class);
                startActivity(i);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(getUid());
        mDataAlert = FirebaseDatabase.getInstance().getReference().child("user");
        call.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:182"));
                startActivity(i);
            }
        });

        mCameraConfig = new CameraConfig()
                .getBuilder(this)
                .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                .setCameraResolution(CameraResolution.LOW_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                .build();

        showStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StatusActivity.class);
                startActivity(i);
            }
        });
        if (Build.VERSION.SDK_INT < 23) {

        } else {
            checkAndRequestPermissions();
        }
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cd.isConnectingToInternet()) {
                    panic = new PanicActivity(MainActivity.this, false);
                    getMessage();
                } else {
                    if (isAlerted) {
                        panic.stopAlert();
                    } else {
                        takePicture();
                        panic.addAlert();
                        setMessage();

                    }
                }

            }
        });

        loadInfo();
        checkAlert();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera(mCameraConfig);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    private void loadInfo() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.alert != null) {
                        alert.setVisibility(View.VISIBLE);
                        isAlerted = user.alert.status;
                        if (count == 0) {
                            count++;
                            panic = new PanicActivity(MainActivity.this, isAlerted);
                        }
                        if (isAlerted) {
                            alert.setText("STOP PANIC");
                        } else {
                            alert.setText("PANIC");
                        }
                    } else {
                        isAlerted = false;
                        alert.setText("PANIC");
                        panic = new PanicActivity(MainActivity.this, isAlerted);
                        alert.setVisibility(View.VISIBLE);
                    }
                } else {
                    startReauthentication();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getPhoneNumber() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }

    public String getName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    private boolean checkAndRequestPermissions() {
        int storagePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int finePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coursePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int audioPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
        int smsPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        int callPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        int camPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (finePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coursePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (audioPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (smsPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (callPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (camPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
            startCamera(mCameraConfig);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        } else {
            if (checkPlayServices()) {

            }
        }
        return true;
    }

    public void checkAlert() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.otherAlerts != null) {
                        usersUid = user.otherAlerts;
                        try {
                            userList.clear();
                        } catch (Exception e) {
                        }
                    }
                }
                if (usersUid != null) {
                    check();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void check() {
        userList = new ArrayList<>();
        userList.clear();
        for (int i = 0; i < usersUid.size(); i++) {
            mDataAlert.child(usersUid.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null || user.uid == null) {
                            startReauthentication();
                            return;
                        } else {
                            System.out.println("USERS++++__== " + user.name);
                            if (user.alert != null) {
                                if (user.alert.status) {
                                    if (!userList.contains(user.uid)) {
                                        userList.add(user.uid);
                                    }
                                } else {
                                    userList.remove(user.uid);
                                }
                            }
                        }
                    } else {
                        startReauthentication();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void startReauthentication() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(i);
        finish();
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startCamera(mCameraConfig);
                }
                break;
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PanicActivity.REQUEST_CHECK_SETTINGS_GPS :
                switch(resultCode){
                    case Activity.RESULT_OK :
                        isEnabledGPS = true;
                        startCamera(mCameraConfig);
                        break;
                    case Activity.RESULT_CANCELED :
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("Error");
                        dialog.setMessage("To use this Service, this app needs to access GPS. \nPlease Click 'OK' to Continue...");
                        dialog.setIcon(R.drawable.ic_warning);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(isEnabledGPS){
                                    dialogInterface.dismiss();
                                }else {
                                    panic.enableGPS();
                                }
                            }
                        });
                        dialog.show();
                        break;
                }
        }
    }
    @Override
    public void onBackPressed() {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure want to Exit ?");
            alert.setTitle("Quit ?");
            alert.setIcon(R.drawable.ic_warning);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, AuthActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setMessage(){

        final Dialog d = new Dialog(this);
        d.setCancelable(false);
        d.setTitle("Message");
        d.setContentView(R.layout.item_message);
        FloatingActionButton send = d.findViewById(R.id.fab_send);
        final EditText messageField = d.findViewById(R.id.message_field);
        final Button buttonCancel = d.findViewById(R.id.btn_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageField.getText().toString().trim();

                if(msg.trim().length() == 0){
                    messageField.setError("Enter Message");
                }
                else{
                    panic.setMessage(msg);
                    d.dismiss();

                }
            }
        });
        d.show();
    }
    public void getMessage(){

        final Dialog d = new Dialog(this);
        d.setCancelable(false);
        d.setTitle("Message");
        d.setContentView(R.layout.item_message);
        FloatingActionButton send = d.findViewById(R.id.fab_send);
        final EditText messageField = d.findViewById(R.id.message_field);
        final Button buttonCancel = d.findViewById(R.id.btn_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageField.getText().toString().trim();

                if(msg.trim().length() == 0){
                    messageField.setError("Enter Message");
                }
                else{
                    panic.sendOffLineMessage(msg);
                    d.dismiss();
                }
            }
        });
        d.show();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        try {
            Toast.makeText(this,"Img Captured",Toast.LENGTH_LONG).show();
            StorageReference file = mStorage.child("Photos").child(getUid()+".jpg");

            file.putFile(Uri.fromFile(imageFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String picUrl = String.valueOf(taskSnapshot.getDownloadUrl());

                    mDatabase.child("alert").child("picUrl").setValue(picUrl);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraError(int errorCode) {

    }
}
