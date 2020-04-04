package com.example.iscaamanda.tugasakhir;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        preferences = ctx.getSharedPreferences("diaretino", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String getUsername(){
        String username = preferences.getString("username","");
        return  username;
    }

    public void setUsername(String username){
        preferences.edit().putString("username", username).apply();
    }

    public String getUserpass(){
        String userpass = preferences.getString("userpass","");
        return  userpass;
    }

    public void setUserpass(String userpass){
        preferences.edit().putString("userpass", userpass).apply();
    }

    public String getInstitution(){
        String institution = preferences.getString("institution","");
        return  institution;
    }

    public void setInstitution(String institution){
        preferences.edit().putString("institution", institution).apply();
    }

    public String getAddress(){
        String address = preferences.getString("address","");
        return  address;
    }

    public void setAddress(String address){
        preferences.edit().putString("address", address).apply();
    }


    public void setLoggedIn(boolean loggedIn){
        editor.putBoolean("loggedInmode", loggedIn);
        editor.commit();
    }

    public boolean loggedIn(){
        return preferences.getBoolean("loggedInmode", false);

    }
}
