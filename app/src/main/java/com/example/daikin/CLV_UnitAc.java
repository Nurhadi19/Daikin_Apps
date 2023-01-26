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

public class CLV_UnitAc extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> vNamaAc;
    private ArrayList<String> vHargaAc;
    private ArrayList<String> vDeskripsiAc;
    private ArrayList<String> vFotoAc;


    public CLV_UnitAc (Activity context , ArrayList<String> NamaAc, ArrayList<String> Harga, ArrayList<String> Deskripsi, ArrayList<String> Foto){
        super(context, R.layout.list_item_ac, NamaAc);
        this.context = context;
        this.vNamaAc = NamaAc;
        this.vHargaAc = Harga;
        this.vDeskripsiAc = Deskripsi;
        this.vFotoAc = Foto;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate(R.layout.list_item_ac, null, true);

        //Declarasi komponen
        TextView namaAc       = rowView.findViewById(R.id.idTVNamaAc);
        TextView hargaAc       = rowView.findViewById(R.id.idTVHargaAc);
        ImageView foto     = rowView.findViewById(R.id.idIVAc);

        //Set Parameter Value sesuai widget textview
        namaAc.setText(vNamaAc.get(position));
        hargaAc.setText(vHargaAc.get(position));

        if (vFotoAc.get(position).equals(""))
        {
            Picasso.get().load("https://tekajeapunya.com/kelompok_10/web_admin/img/foto_ac/noimage.jpeg").into(foto);
        }
        else
        {
            Picasso.get().load("https://tekajeapunya.com/kelompok_10/web_admin/img/foto_ac/"+vFotoAc.get(position)).into(foto);
        }

        //Load the animation from the xml file and set it to the row
        //load animasi untuk listview
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        return rowView;
    }
}
