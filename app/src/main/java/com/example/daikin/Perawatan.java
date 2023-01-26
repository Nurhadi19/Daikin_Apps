package com.example.daikin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Perawatan extends AppCompatActivity {

    Toolbar TBPerawatan;
    CardView CVMenuCuciAc, CVMenuPerawatanAc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perawatan);

        TBPerawatan = findViewById(R.id.idTBPerawatan);
        CVMenuCuciAc = findViewById(R.id.idCVMenu1);
        CVMenuPerawatanAc = findViewById(R.id.idCVMenu2);

        TBPerawatan.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        TBPerawatan.setTitle("Perawatan");
        TBPerawatan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Dashboard.class));
            }
        });

        CVMenuCuciAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CuciAc.class));
            }
        });

        CVMenuPerawatanAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PengecekanAc.class));
            }
        });
    }
}