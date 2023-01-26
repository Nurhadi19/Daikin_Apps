package com.example.daikin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

public class Activity_Dashboard extends AppCompatActivity {

    ImageButton IBMenu1, IBProfil, IBPembelian;
    TextView TVuser;
    String nama_user;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TVuser = findViewById(R.id.idTVHello);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        nama_user = user.get(SessionManager.KEY_NAMA);

        if(nama_user != null){
            TVuser.setText("Selamat Datang, "+nama_user);
        }

        IBMenu1 = findViewById(R.id.menu1);
        IBMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Perawatan.class));
            }
        });

        IBProfil = findViewById(R.id.nav3);
        IBProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), History_Pesanan.class));
            }
        });

        IBProfil = findViewById(R.id.nav4);
        IBProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
            }
        });

        IBPembelian = findViewById(R.id.menu2);
        IBPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PembelianAc.class));
            }
        });
    }
}