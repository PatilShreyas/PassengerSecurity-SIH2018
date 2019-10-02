package com.railway.security;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.railway.security.model.FIR;
import com.railway.security.model.User;
import com.railway.security.view.StatusListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusActivity extends BaseActivity {

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private List<FIR> firList;
    private StatusListAdapter mAdapter;
    private FloatingActionButton refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        refresh = (FloatingActionButton)findViewById(R.id.fab_refresh);
        mRecycler = findViewById(R.id.item_status_list);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        init();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });
    }

    private void init() {
        firList = new ArrayList<>();
        mAdapter = new StatusListAdapter(firList,this);
        mRecycler.setAdapter(mAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.getFirDetails);


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("WEB RESPONSE : "+response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0 ; i<jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        FIR fir = new FIR();
                        fir.firNo = json.optString("FIR_NO");
                        fir.status = json.optString("STATUS");
                        fir.time = json.optString("TIMESTAMP");

                        firList.add(fir);
                    }
                    mAdapter.updateList(firList);
                    mAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("UID",getUid());
                return params;
            }
        };

        queue.add(request);
    }
}