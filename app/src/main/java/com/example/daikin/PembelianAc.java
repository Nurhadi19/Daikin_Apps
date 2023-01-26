package com.example.daikin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PembelianAc extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_namaAc,array_hargaAc,array_deskripsiAc, array_fotoAc;
    ProgressDialog progressDialog;
    ListView listProses;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian_ac);

        Toolbar toolbar = findViewById(R.id.idTBPembelian);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Dashboard.class));
            }
        });

        listProses = findViewById(R.id.idLVPembelian);
        srl_main = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);

        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                srl_main.setRefreshing(false);
            }
        });
        // Scheme colors for animation
        srl_main.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)

            );

            scrollRefresh();
        }

        public void scrollRefresh(){
            progressDialog.setMessage("Mengambil Data.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataAc();
                }
            },2000);
        }

        void initializeArray(){
            array_namaAc = new ArrayList<String>();
            array_hargaAc = new ArrayList<String>();
            array_deskripsiAc = new ArrayList<String>();
            array_fotoAc = new ArrayList<String>();

            array_namaAc.clear();
            array_hargaAc.clear();
            array_deskripsiAc.clear();
            array_fotoAc.clear();
        }

        public void getDataAc(){
            initializeArray();
            //URL API
            AndroidNetworking.get("https://tekajeapunya.com/kelompok_10/api_daikin/getDataAc.php")
                    .setTag("Get Data")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            try{
                                Boolean status = response.getBoolean("status");
                                if(status){
                                    JSONArray ja = response.getJSONArray("result");
                                    Log.d("respon",""+ja);
                                    for(int i = 0 ; i < ja.length() ; i++){
                                        JSONObject jo = ja.getJSONObject(i);

                                        array_namaAc.add(jo.getString("nama_ac"));
                                        array_hargaAc.add(jo.getString("harga_ac"));
                                        array_deskripsiAc.add(jo.getString("deskripsi_ac"));
                                        //add this code
                                        array_fotoAc.add(jo.getString("foto_ac"));

                                    }

                                    //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
                                    final CLV_UnitAc adapter = new CLV_UnitAc(PembelianAc.this,array_namaAc,array_hargaAc,array_deskripsiAc,array_fotoAc);

                                    //Set adapter to list
                                    listProses.setAdapter(adapter);


                                    //edit and delete
                                    listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Log.d("TestKlik",""+array_namaAc.get(position));
                                            Toast.makeText(PembelianAc.this, array_namaAc.get(position), Toast.LENGTH_SHORT).show();

                                            //Setelah proses koneksi keserver selesai, maka aplikasi akan berpindah class
                                            //DataActivity.class dan membawa/mengirim data-data hasil query dari server.
                                            Intent i = new Intent(PembelianAc.this, DetailAc.class);
                                            i.putExtra("namaAc",array_namaAc.get(position));
                                            i.putExtra("descAc",array_deskripsiAc.get(position));
                                            i.putExtra("hargaAc",array_hargaAc.get(position));
                                            i.putExtra("fotoAc",array_fotoAc.get(position));
                                            startActivity(i);
                                        }
                                    });


                                }else{
                                    Toast.makeText(PembelianAc.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }


                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }

}