package com.railway.security;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.railway.security.model.FIR;
import com.railway.security.model.PNR;
import com.railway.security.model.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFirActivity extends BaseActivity {
    private EditText nameField;
    private EditText mobNoField;
    private EditText seatNoField;
    private EditText aadhaarField;
    private EditText pnrNoField;
    private EditText trainNoField;
    private EditText lastStnField;
    private EditText addInfoField;
    private Spinner crimeSpinner;
    private EditText srcStationField;
    private EditText destStationField;
    private Button register;

    private User user;
    private PNR pnr;
    private FIR fir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fir);

        setTitle("Register FIR");
        pnrNoField = (EditText) findViewById(R.id.editText_PNR_No);
        trainNoField = (EditText) findViewById(R.id.editText_Train);
        seatNoField = (EditText) findViewById(R.id.editText_SeatNo);
        lastStnField = (EditText) findViewById(R.id.editText_LastSTN);
        addInfoField = (EditText) findViewById(R.id.editText_info);
        srcStationField = (EditText) findViewById(R.id.editText_srcStn);
        destStationField = (EditText) findViewById(R.id.editText_destStn);
        register = (Button) findViewById(R.id.button_reg);

        crimeSpinner = (Spinner) findViewById(R.id.spinner_crime);
        init();

        pnrNoField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pnrNo = charSequence.toString();

                if(pnrNo.trim().length()==10){
                    showProgressDialog();
                    getPNRDetails();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFIR();
            }
        });

    }

    private void registerFIR() {
        if(isValid()){
            fir = new FIR();
            fir.crime = crimeSpinner.getSelectedItem().toString();
            fir.info = addInfoField.getText().toString();
            fir.name = getName();
            fir.mobNo = getPhoneNumber();
            fir.uid = getUid();
            fir.lastStn = lastStnField.getText().toString();
            fir.pnrNo = pnr.pnrNo;
            fir.trainNo = pnr.trainNo;
            showProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getResources().getString(R.string.registerFIR);

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    hideProgressDialog();
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.optString("status");

                        if(status.equals("Success")){
                            String firNo = json.optString("FIR_NO");

                            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterFirActivity.this);
                            alert.setTitle("FIR Successful");
                            alert.setMessage("FIR No : " + firNo);
                            alert.setCancelable(false);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });
                            alert.show();
                        }
                        else {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),"Error: Try Again",Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        hideProgressDialog();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),"Error... Try Again",Toast.LENGTH_LONG).show();
                    error.fillInStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("uid",fir.uid);
                    params.put("pnrNo",fir.pnrNo);
                    params.put("trainNo",fir.trainNo);
                    params.put("lastStation",fir.lastStn);
                    params.put("addInfo",fir.info);
                    params.put("crime",fir.crime);
                    return params;
                }
            };

            queue.add(request);
        }
    }

    private void init() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.CrimeTypes,android.R.layout.simple_spinner_dropdown_item);
        crimeSpinner.setAdapter(adapter);

        pnrNoField.requestFocus();
    }

    public void getPNRDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.getPNRDetails);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                try {
                    JSONObject json = new JSONObject(response);
                    String status = json.optString("status");

                    if(status.equals("Success")){
                        String trainNo = json.optString("TRAIN_NO");
                        String trainName = json.optString("TRAIN_NAME");
                        String seat = json.optString("SEAT_DETAIL");
                        String srcStation = json.optString("SRC_STATION");
                        String destStation = json.optString("DEST_STATION");

                        pnr = new PNR(trainName,trainNo,pnrNoField.getText().toString(),srcStation,destStation,seat);
                        fillFields();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Error: Try Again",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),"Error: Try Again",Toast.LENGTH_LONG).show();
                error.fillInStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("pnrNo",pnrNoField.getText().toString());
                return params;
            }
        };

        queue.add(request);
    }

    private void fillFields() {
        hideProgressDialog();
        trainNoField.setText(pnr.trainNo+" - "+ pnr.trainName);
        srcStationField.setText(pnr.srcStn);
        destStationField.setText(pnr.destStn);
        seatNoField.setText(pnr.seatDetails);
        lastStnField.requestFocus();
    }

    public boolean isValid(){
        if(pnrNoField.getText().toString().trim().length() == 0){
            pnrNoField.setError("Invalid PNR");
            return false;
        }
        else if(lastStnField.getText().toString().trim().length() == 0){
           lastStnField.setError("Enter Last Station");
            return false;
        }
        else{
            return true;
        }
    }
}


