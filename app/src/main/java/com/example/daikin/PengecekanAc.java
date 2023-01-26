package com.example.daikin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PengecekanAc extends AppCompatActivity {

    Toolbar Toolbar;
    EditText ETJadwal, ETWaktu, ETidUser;
    Calendar myCalendar, myTime;
    DatePickerDialog.OnDateSetListener date, time;
    ListView listView;

    String id_user;

    int myHour, myMinute, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengecekan);

        Toolbar = findViewById(R.id.TBPengecekanAc);
        ETJadwal = findViewById(R.id.idETJadwal);
        ETWaktu = findViewById(R.id.idETWaktu);
        listView = findViewById(R.id.LLDaftarAc);
        ETidUser = findViewById(R.id.idETidUser);


        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MMMM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                ETJadwal.setText(sdf.format(myCalendar.getTime()));
            }
        };

        myTime = Calendar.getInstance();
        myHour = myTime.get(Calendar.HOUR_OF_DAY);
        myMinute = myTime.get(Calendar.MINUTE);

        ETWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(PengecekanAc.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        ETWaktu.setText(hourOfDay + ":" + minute);
                    }
                }, myHour, myMinute, false);
                timePickerDialog.show();
            }
        });

        ETJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PengecekanAc.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        Toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                scrollRefresh();
//                srl_main.setRefreshing(false);
//            }
//        });
//        // Scheme colors for animation
//        srl_main.setColorSchemeColors(
//                getResources().getColor(android.R.color.holo_blue_bright),
//                getResources().getColor(android.R.color.holo_green_light),
//                getResources().getColor(android.R.color.holo_orange_light),
//                getResources().getColor(android.R.color.holo_red_light)
//
//        );
//
//        scrollRefresh();
//        }
//
//        public void scrollRefresh(){
//            progressDialog.setMessage("Mengambil Data.....");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getDataAc();
//                }
//            },2000);
//        }
//
//    void initializeArray(){
//        array_namaAc = new ArrayList<String>();
//        array_modelAc = new ArrayList<String>();
//        array_pkAc = new ArrayList<String>();
//
//        array_namaAc.clear();
//        array_modelAc.clear();
//        array_pkAc.clear();
//
//        id_user = ETidUser.getText().toString();
//
//    }
//
//    public void getDataAc(){
//        initializeArray();
//
//        AndroidNetworking.post("https://tekajeapunya.com/kelompok_10/api_daikin/getAcUser.php")
//                .addBodyParameter("id_user",""+id_user)
//                .setTag("Get Data")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
//
//                        try{
//                            Boolean status = response.getBoolean("status");
//                            if(status){
//                                JSONArray ja = response.getJSONArray("result");
//                                Log.d("respon",""+ja);
//                                for(int i = 0 ; i < ja.length() ; i++){
//                                    JSONObject jo = ja.getJSONObject(i);
//
//                                    array_namaAc.add(jo.getString("nama_ac"));
//                                    array_modelAc.add(jo.getString("model_ac"));
//                                    array_pkAc.add(jo.getString("kapasitas_ac"));
//
//                                }
//
//                                //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
//                                final CLV_userAc adapter = new CLV_userAc(PengecekanAc.this, array_namaAc, array_modelAc, array_pkAc);
//
//                                listView.setAdapter(adapter);
//
////                                edit and delete
////                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                    @Override
////                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                                        Log.d("TestKlik",""+array_namaAc.get(position));
////                                        Toast.makeText(PengecekanAc.this, array_namaAc.get(position), Toast.LENGTH_SHORT).show();
////
////                                        //Setelah proses koneksi keserver selesai, maka aplikasi akan berpindah class
////                                        //DataActivity.class dan membawa/mengirim data-data hasil query dari server.
//////                                        Intent i = new Intent(PengecekanAc.this, edit_mahasiswa.class);
//////                                        i.putExtra("nim",array_nim.get(position));
//////                                        i.putExtra("name",array_name.get(position));
//////                                        i.putExtra("address",array_address.get(position));
//////                                        i.putExtra("hobby",array_hobby.get(position));
//////                                        i.putExtra("photo",array_photo.get(position));
//////                                        startActivity(i);
////                                    }
////                                });
////
//
//                            }else{
//                                Toast.makeText(PengecekanAc.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
//    }
    }
}