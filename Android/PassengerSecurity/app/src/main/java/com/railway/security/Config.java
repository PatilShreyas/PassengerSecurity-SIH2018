package com.railway.security;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HARSHALI PATIL on 21/12/2017.
 */

public class Config extends Application {

    static{
        try{
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }catch(Exception e){}
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);

        try{
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }catch(Exception e){}
    }
}
