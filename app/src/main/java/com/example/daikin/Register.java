package com.example.daikin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Register extends AppCompatActivity {

    MaterialButton BTNLogin;
    Button BTNRegister;
    EditText ETEmail, ETNamaLengkap, ETAlamat, ETPassword;
    String nama_lengkap,email, password, alamat;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // deklarasikan variabel
        BTNLogin = findViewById(R.id.idBTNLogin);
        BTNRegister = findViewById(R.id.idBTNRegister);
        ETEmail = findViewById(R.id.idETEmail);
        ETNamaLengkap = findViewById(R.id.idETNamaLengkap);
        ETAlamat = findViewById(R.id.idETAlamat);
        ETPassword = findViewById(R.id.idETPassword);

        // Memfungsikan Widget
        BTNLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        BTNRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = ETEmail.getText().toString();
                nama_lengkap = ETNamaLengkap.getText().toString();
                alamat = ETAlamat.getText().toString();
                password = ETPassword.getText().toString();

                validasiData();

               // Toast.makeText(getApplicationContext(),"Berhasil Melakukan Register",Toast.LENGTH_LONG).show();
               // startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

    void validasiData(){
        if(email.equals("") ||nama_lengkap.equals("") || alamat.equals("") || password.equals("")){
            Toast.makeText(Register.this, "Data Tidak Lengkap", Toast.LENGTH_SHORT).show();
        } else {
            kirimData();
        }
    }

    void kirimData(){
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_10/api_daikin/register.php")
                .addBodyParameter("email",""+email)
                .addBodyParameter("nama_lengkap",""+nama_lengkap)
                .addBodyParameter("alamat",""+alamat)
                .addBodyParameter("password",""+password)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cekRegister",""+response);

                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan   = response.getString("result");
                            Toast.makeText(Register.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Register.this)
                                        .setMessage("Berhasil Register")
                                        .setCancelable(false)
                                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Register.this, Login.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new AlertDialog.Builder(Register.this)
                                        .setMessage("Gagal Melakukan Register !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Register.this, Register.class);
                                                startActivity(intent);
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
                        Log.d("ErrorTambahData",""+anError.getErrorBody());
                    }
                });
    }
}