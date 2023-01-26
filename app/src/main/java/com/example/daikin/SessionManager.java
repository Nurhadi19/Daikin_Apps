package com.example.daikin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int mode = 0;

    private static final String pref_name = "session";
    private static final String is_login = "islogin";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id_user";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_NOHP = "nohp";

    public SessionManager (Context context){
        this.context = context;
        pref = context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    public void createSession_id(String id_user){
        editor.putBoolean(is_login, true);
        editor.putString(KEY_ID, id_user);
        editor.apply();

    }

    public void createSession_noHp(String noHp){
        editor.putBoolean(is_login, true);
        editor.putString(KEY_NOHP, noHp);
        editor.apply();

    }

    public void createSession_alamat(String alamat){
        editor.putBoolean(is_login, true);
        editor.putString(KEY_ALAMAT, alamat);
        editor.apply();

    }

    public void createSession_nama(String nama_lengkap){
        editor.putBoolean(is_login, true);
        editor.putString(KEY_NAMA, nama_lengkap);
        editor.apply();
    }

    public void createSession_email(String email){
        editor.putBoolean(is_login, true);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void logOut(){
        editor.clear();
        editor.apply();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(pref_name, pref.getString(pref_name, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_NAMA, pref.getString(KEY_NAMA, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_ALAMAT, pref.getString(KEY_ALAMAT, null));
        return user;
    }

    public boolean getSPSudahLogin(){
        return pref.getBoolean(is_login, false);
    }

}
