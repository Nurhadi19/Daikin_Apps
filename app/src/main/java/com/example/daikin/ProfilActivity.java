package com.example.daikin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class ProfilActivity extends AppCompatActivity {

    Toolbar TBProfil1;
    TextView TVAturAlamat1, TVnama, TVemail, TVtentangAplikasi;
    Button BTNlogout;
    SessionManager sessionManager;
    String nama, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        TBProfil1 = findViewById(R.id.TBProfil);
        TVAturAlamat1 = findViewById(R.id.TVAturAlamat);
        BTNlogout = findViewById(R.id.idBTNLogout);
        TVnama = findViewById(R.id.TVNama);
        TVemail = findViewById(R.id.TVEmail);
        TVtentangAplikasi = findViewById(R.id.idTVTentangAplikasi);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user =  sessionManager.getUserDetails();
        nama = user.get(SessionManager.KEY_NAMA);
        email = user.get(SessionManager.KEY_EMAIL);

        TVnama.setText(nama);
        TVemail.setText(email);

        BTNlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logOut();
                Toast.makeText(ProfilActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
            }
        });

        TBProfil1.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        TBProfil1.setTitle("Profil");
        TBProfil1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Dashboard.class));
            }
        });


        TVAturAlamat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Alamat.class));
            }
        });

        TVtentangAplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AboutApp.class));
            }
        });

    }
}