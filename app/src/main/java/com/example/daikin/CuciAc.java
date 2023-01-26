package com.example.daikin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CuciAc extends AppCompatActivity {

    Toolbar Toolbar;
    EditText ETJadwal, ETWaktu;
    Calendar myCalendar, myTime;
    DatePickerDialog.OnDateSetListener date, time;

    int myHour, myMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuci);

        Toolbar = findViewById(R.id.TBCuciAc);
        ETJadwal = findViewById(R.id.idETJadwal);
        ETWaktu = findViewById(R.id.idETWaktu);

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
               TimePickerDialog timePickerDialog = new TimePickerDialog(CuciAc.this, new TimePickerDialog.OnTimeSetListener() {
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
                new DatePickerDialog(CuciAc.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
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

    }
}