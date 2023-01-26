package com.example.daikin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class Order extends AppCompatActivity {

    EditText ETIdUser, ETNamaPemesan, ETAlamatPemesan, ETNamaAc, ETJumlahUnit, ETTotalHarga;
    TextView TVTotalHarga;
    ImageView IVAcPesanan;
    MaterialButton MBOrder, MBTotal;

    SessionManager sessionManager;
    ProgressDialog progressDialog;

    String nama_user, alamat_user, id_user, namaPemesan, alamatPemesan, idPemesan, namaAC, Id_User;

    int hargaAc;
    int jumlahUnit;
    int total;
    String jumlahPesanan;
    String totalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.idTBOrderAc);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PembelianAc.class));
            }
        });

        ETIdUser = findViewById(R.id.idETIdUserOrder);
        ETNamaPemesan = findViewById(R.id.idETNamaPemesan);
        ETAlamatPemesan = findViewById(R.id.idETAlamatPemesan);
        ETNamaAc = findViewById(R.id.idETNamaAcPesanan);
        ETJumlahUnit = findViewById(R.id.idETJumlahUnitAcPesanan);
        ETTotalHarga = findViewById(R.id.idETTotalHarga);
        TVTotalHarga = findViewById(R.id.idTVTotalHarga);
        IVAcPesanan = findViewById(R.id.idIVAcPesanan);
        MBOrder = findViewById(R.id.idMBOrder);
        MBTotal = findViewById(R.id.idMBTotalHarga);

        ETJumlahUnit.setText(Integer.toString(0));

        progressDialog = new ProgressDialog(this);


        getDataIntent();
        getDataSession();

        Bundle bundle = getIntent().getExtras();

        MBTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumlahUnit = Integer.parseInt(ETJumlahUnit.getText().toString());
                hargaAc = Integer.parseInt(bundle.getString("hargaAc"));

                total = jumlahUnit * hargaAc;

                if (jumlahUnit == 0){
                    ETJumlahUnit.setError("Mohon Di Isi");
                }

                TVTotalHarga.setText(Integer.toString(total));
                ETTotalHarga.setText(Integer.toString(total));
            }
        });

        MBOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memproses Order...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                namaPemesan = ETNamaPemesan.getText().toString();
                alamatPemesan = ETAlamatPemesan.getText().toString();
                idPemesan = ETIdUser.getText().toString();
                jumlahPesanan = ETJumlahUnit.getText().toString();
                totalHarga = ETTotalHarga.getText().toString();
                namaAC = ETNamaAc.getText().toString();


                validasiData();
            }
        });



    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            ETNamaAc.setText(bundle.getString("namaAc"));
            Picasso.get().load("https://tekajeapunya.com/kelompok_10/web_admin/img/foto_ac/" +
                    bundle.getString("gambarAc")).into(IVAcPesanan);

        } else {
            ETNamaAc.setText("");
        }
    }

    void getDataSession(){
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        nama_user = user.get(SessionManager.KEY_NAMA);
        alamat_user = user.get(SessionManager.KEY_ALAMAT);
        id_user = user.get(SessionManager.KEY_ID);

        if(nama_user != null){
            ETNamaPemesan.setText(Html.fromHtml(nama_user));
            ETAlamatPemesan.setText(Html.fromHtml(alamat_user));
            ETIdUser.setText(Html.fromHtml(id_user));
        }
    }

    void validasiData(){
        if(idPemesan.equals("") || namaAC.equals("") || jumlahPesanan.equals("") || namaPemesan.equals("") || alamatPemesan.equals("") || totalHarga.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Order.this, "Data Harus Diisi Semua", Toast.LENGTH_SHORT).show();
        } else {
            order();
        }
    }

    void order(){
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_10/api_daikin/beliAc.php")
                .addBodyParameter("id_user",""+idPemesan)
                .addBodyParameter("nama_ac",""+namaAC)
                .addBodyParameter("jumlah_unit",""+jumlahPesanan)
                .addBodyParameter("nama_pemesan",""+namaPemesan)
                .addBodyParameter("total_pembayaran",""+totalHarga)
                .addBodyParameter("alamat_pemesan",""+alamatPemesan)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Order")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekOrder",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(Order.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Order.this)
                                        .setMessage("Berhasil Menambahkan Order !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Order.this, PembelianAc.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new AlertDialog.Builder(Order.this)
                                        .setMessage("Gagal Menambahkan Order !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Order.this, Order.class);
                                                startActivity(i);
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorTambahOrder",""+anError.getErrorBody());
                    }
                });
    }


}