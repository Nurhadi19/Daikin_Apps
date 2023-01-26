package com.example.daikin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class DetailAc extends AppCompatActivity {

    Toolbar toolbar;
    TextView TVNamaAc, TVDescAc, TVHargaAc;
    ImageView IVAc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ac);

        toolbar = findViewById(R.id.idTBDetailAc);
        TVNamaAc = findViewById(R.id.idTVNamaAc);
        TVDescAc = findViewById(R.id.idTVDescription);
        TVHargaAc = findViewById(R.id.idTVHarga);
        IVAc = findViewById(R.id.idIVAc);
        MaterialButton MBpesan = findViewById(R.id.idBTNPesan);
        getDataIntent();





        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PembelianAc.class));
            }
        });

        MBpesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                Intent i = new Intent(getApplicationContext(), Order.class);
                i.putExtra("namaAc", TVNamaAc.getText().toString());
                i.putExtra("descAc", TVDescAc.getText().toString());
                i.putExtra("hargaAc", TVHargaAc.getText().toString());
                i.putExtra("gambarAc", bundle.getString("fotoAc"));
                startActivity(i);
            }
        });


    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            TVNamaAc.setText(bundle.getString("namaAc"));
            TVDescAc.setText(bundle.getString("descAc"));
            TVHargaAc.setText(bundle.getString("hargaAc"));
            Picasso.get().load("https://tekajeapunya.com/kelompok_10/web_admin/img/foto_ac/" +
                    bundle.getString("fotoAc")).into(IVAc);
        }else{
            TVNamaAc.setText("");
            TVDescAc.setText("");
            TVHargaAc.setText("");
        }
    }


}