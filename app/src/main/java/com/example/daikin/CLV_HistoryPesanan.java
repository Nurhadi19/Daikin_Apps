package com.example.daikin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CLV_HistoryPesanan extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> vIdUser;
    private ArrayList<String> vIdPembelian;
    private ArrayList<String> vNamaPemesan;
    private ArrayList<String> vAlamatPemesan;
    private ArrayList<String> vTanggalPembelian;
    private ArrayList<String> vNamaAc;
    private ArrayList<String> vJumlahPembelian;
    private ArrayList<String> vTotalPembayaran;
    private ArrayList<String> vBuktiPembayaran;
    private ArrayList<String> vStatusPembelian;




    public CLV_HistoryPesanan (Activity context , ArrayList<String> id_user, ArrayList<String> IdPembelian, ArrayList<String> NamaPemesan, ArrayList<String> AlamatPemesan, ArrayList<String> TanggalPembelian,
                               ArrayList<String> NamaAc, ArrayList<String> JumlahPembelian,
                               ArrayList<String> TotalPembayaran, ArrayList<String> BuktiPembayaran, ArrayList<String> StatusPembelian){
        super(context, R.layout.list_item_pesanan, NamaPemesan);
        this.context = context;
        this.vIdUser = id_user;
        this.vIdPembelian = IdPembelian;
        this.vNamaPemesan = NamaPemesan;
        this.vAlamatPemesan = AlamatPemesan;
        this.vTanggalPembelian = TanggalPembelian;
        this.vNamaAc = NamaAc;
        this.vJumlahPembelian = JumlahPembelian;
        this.vTotalPembayaran = TotalPembayaran;
        this.vBuktiPembayaran = BuktiPembayaran;
        this.vStatusPembelian = StatusPembelian;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate(R.layout.list_item_pesanan, null, true);

        //Declarasi komponen
        TextView namaAc       = rowView.findViewById(R.id.idTVNamaAc2);
        TextView hargaAc       = rowView.findViewById(R.id.idTVHargaAc2);
//        ImageView foto     = rowView.findViewById(R.id.idIVAc2);
        TextView tanggal = rowView.findViewById(R.id.idTVTglAc2);
        TextView status = rowView.findViewById(R.id.idTVVerfikasiAc2);

        //Set Parameter Value sesuai widget textview
        tanggal.setText(vTanggalPembelian.get(position));
        namaAc.setText(vNamaAc.get(position));
        hargaAc.setText(vTotalPembayaran.get(position));
        status.setText(vStatusPembelian.get(position));

//        if (vFotoAc.get(position).equals(""))
//        {
//            Picasso.get().load("https://tekajeapunya.com/kelompok_10/web_admin/img/foto_ac/noimage.jpeg").into(foto);
//        }
//        else
//        {
//            Picasso.get().load("https://tekajeapunya.com/kelompok_10/web_admin/img/foto_ac/"+vFotoAc.get(position)).into(foto);
//        }

        //Load the animation from the xml file and set it to the row
        //load animasi untuk listview
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        return rowView;
    }
}
