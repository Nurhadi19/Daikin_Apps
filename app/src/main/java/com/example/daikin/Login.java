package com.example.daikin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    MaterialButton BTNRegister;
    Button BTNLogin;
    EditText ETusername, ETpassword;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    String email, password;
//    int id_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BTNRegister = findViewById(R.id.idBTNRegister);
        BTNLogin = findViewById(R.id.idBTNLogin);
        ETusername = findViewById(R.id.idETUsername);
        ETpassword = findViewById(R.id.idETPassword);

        progressDialog = new ProgressDialog(this);

        sessionManager = new SessionManager(getApplicationContext());



        BTNRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        BTNLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Berhasil Login",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(), Activity_Dashboard.class));
                email = ETusername.getText().toString();
                password = ETpassword.getText().toString();

                progressDialog.setMessage("Login..");
                progressDialog.setCancelable(false);
                progressDialog.show();


                validasiData();
            }
        });

    }

    void validasiData(){
        if(email.equals("") || password.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Login.this, "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            cekLogin();
        }
    }

    void cekLogin(){
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_10/api_daikin/cekLogin.php")
                .addBodyParameter("email",""+email)
                .addBodyParameter("password",""+password)
                .setPriority(Priority.MEDIUM)
                .setTag("Cek Login")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("Cek Login",""+response);

                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan   = response.getString("result");
                            String nama_user = response.getString("nama_lengkap");
                            String email = response.getString("email");
                            String id_user = response.getString("id_user");
                            String alamat = response.getString("alamat");
                            String nomor_hp = response.getString("nomor_hp");
//                            int id_user = response.getInt("id_user");
                            Toast.makeText(Login.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Login.this)
                                        .setMessage("Berhasil Login")
                                        .setCancelable(false)
                                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                sessionManager.createSession_nama(nama_user);
                                                sessionManager.createSession_email(email);
                                                sessionManager.createSession_id(id_user);
                                                sessionManager.createSession_alamat(alamat);
                                                sessionManager.createSession_noHp(nomor_hp);
                                                Intent intent = new Intent(Login.this, Activity_Dashboard.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new AlertDialog.Builder(Login.this)
                                        .setMessage("Gagal Melakukan Login !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                Intent intent = new Intent(Login.this, Login.class);
//                                                startActivity(intent);
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorLogin",""+anError.getErrorBody());
                    }
                });

    }
}