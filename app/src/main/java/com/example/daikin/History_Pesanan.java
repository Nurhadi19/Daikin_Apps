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
import java.util.HashMap;

public class History_Pesanan extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_idUser, array_idPembelian,array_NamaPemesan,array_AlamatPemesan,array_TanggalPembelian,
            array_NamaAc ,array_JumlahPembelian, array_TotalPembayaran, array_BuktiPembayaran,
            array_StatusPembelian;
    ProgressDialog progressDialog;
    ListView listProses;
    String id_user;
    SessionManager sessionManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pesanan);

        Toolbar toolbar = findViewById(R.id.idTBPesanan);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Dashboard.class));
            }
        });

        listProses = findViewById(R.id.idLVPesanan);
        srl_main = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        id_user = user.get(SessionManager.KEY_ID);

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
        array_NamaPemesan = new ArrayList<String>();
        array_AlamatPemesan = new ArrayList<String>();
        array_TanggalPembelian = new ArrayList<String>();
        array_NamaAc = new ArrayList<String>();
        array_JumlahPembelian = new ArrayList<String>();
        array_TotalPembayaran = new ArrayList<String>();
        array_BuktiPembayaran = new ArrayList<String>();
        array_StatusPembelian = new ArrayList<String>();
        array_idUser = new ArrayList<String>();
        array_idPembelian = new ArrayList<String>();

        array_NamaPemesan.clear();
        array_AlamatPemesan.clear();
        array_TanggalPembelian.clear();
        array_NamaAc.clear();
        array_JumlahPembelian.clear();
        array_TotalPembayaran.clear();
        array_BuktiPembayaran.clear();
        array_StatusPembelian.clear();
        array_idUser.clear();
        array_idPembelian.clear();
    }

    public void getDataAc(){
        initializeArray();
        //URL API
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_10/api_daikin/getHistoryAc.php?id_user="+id_user)
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

                                    array_idUser.add(jo.getString("id_user"));
                                    array_idPembelian.add(jo.getString("id_pembelian"));
                                    array_NamaPemesan.add(jo.getString("nama_pemesan"));
                                    array_AlamatPemesan.add(jo.getString("alamat_pemesan"));
                                    array_TanggalPembelian.add(jo.getString("tanggal_pembelian"));
                                    array_NamaAc.add(jo.getString("nama_ac"));
                                    array_JumlahPembelian.add(jo.getString("jumlah_pembelian_unit"));
                                    array_TotalPembayaran.add(jo.getString("total_pembayaran"));
                                    array_BuktiPembayaran.add(jo.getString("bukti_pembayaran"));
                                    array_StatusPembelian.add(jo.getString("status_pembelian"));
                                    //add this code
//                                    array_fotoAc.add(jo.getString("foto_ac"));

                                }

                                //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
                                final CLV_HistoryPesanan adapter = new CLV_HistoryPesanan(History_Pesanan.this,array_idUser,array_idPembelian,array_NamaPemesan,array_AlamatPemesan,
                                        array_TanggalPembelian,array_NamaAc,array_JumlahPembelian,array_TotalPembayaran,array_BuktiPembayaran,array_StatusPembelian);
                                //Set adapter to list
                                listProses.setAdapter(adapter);


                                //edit and delete
                                listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik",""+array_NamaAc.get(position));
                                        Toast.makeText(History_Pesanan.this, array_NamaAc.get(position), Toast.LENGTH_SHORT).show();

                                        //Setelah proses koneksi keserver selesai, maka aplikasi akan berpindah class
                                        //DataActivity.class dan membawa/mengirim data-data hasil query dari server.
                                        Intent i = new Intent(History_Pesanan.this, DetailRiwayat.class);

                                        i.putExtra("id_user",array_idUser.get(position));
                                        i.putExtra("id_pembelian",array_idPembelian.get(position));
                                        i.putExtra("nama_pemesan",array_NamaPemesan.get(position));
                                        i.putExtra("alamat_pemesan",array_AlamatPemesan.get(position));
                                        i.putExtra("tanggal_pembelian",array_TanggalPembelian.get(position));
                                        i.putExtra("nama_ac",array_NamaAc.get(position));
                                        i.putExtra("jumlah_pembelian",array_JumlahPembelian.get(position));
                                        i.putExtra("total_pembelian",array_TotalPembayaran.get(position));
                                        i.putExtra("bukti_pembayaran",array_BuktiPembayaran.get(position));
                                        i.putExtra("status_pembelian",array_StatusPembelian.get(position));
                                        startActivity(i);
                                    }
                                });


                            }else{
                                Toast.makeText(History_Pesanan.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorHistoryOrder",""+anError.getErrorBody());
                    }
                });
    }

}