package com.example.daikin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DetailRiwayat extends AppCompatActivity {

    EditText ETNamaPemesan, ETAlamatPemesan, ETTanggalPembelian, ETNamaAc,
            ETJumlahUnit, ETTotalPembayaran, ETIdUser, ETIdPembelian;
    ImageButton IBBuktiPembayaran;

    MaterialButton MBUpload;

    ProgressDialog progressDialog;

    String id_user, id_pembelian, nama_pemesan, alamat_pemesan,
            tanggal_pembelian, nama_ac, unit, total;

    int jumlah_unit, total_pembayaran;

    String pilihan;
    private static final int PHOTO_REQUEST_GALLERY = 1;//gallery
    static final int REQUEST_TAKE_PHOTO = 1;
    final int CODE_GALLERY_REQUEST = 999;
    String rPilihan[]= {"Take a photo","From Album"};
    public final String APP_TAG = "MyApp";
    Bitmap bitMap = null;
    public String photoFileName = "photo.jpg";
    File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat);

        Toolbar toolbar = findViewById(R.id.idToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), History_Pesanan.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        ETIdUser = findViewById(R.id.idETIdUserOrder);
        ETIdPembelian = findViewById(R.id.idETIdPembelian);
        ETNamaPemesan = findViewById(R.id.idETNamaPemesan);
        ETAlamatPemesan = findViewById(R.id.idETAlamatPemesan);
        ETTanggalPembelian = findViewById(R.id.idETtanggal);
        ETNamaAc = findViewById(R.id.idETNamaAcPesanan);
        ETJumlahUnit = findViewById(R.id.idETJumlahUnitAcPesanan);
        ETTotalPembayaran = findViewById(R.id.idETTotalHarga);
        IBBuktiPembayaran = findViewById(R.id.idIBBuktiPembayaran);
        MBUpload = findViewById(R.id.idBTNUpdate);

        jumlah_unit = Integer.parseInt(getIntent().getStringExtra("jumlah_pembelian"));
        total_pembayaran = Integer.parseInt(getIntent().getStringExtra("total_pembelian"));

        ETIdUser.setText(getIntent().getStringExtra("id_user"));
        ETIdPembelian.setText(getIntent().getStringExtra("id_pembelian"));
        ETNamaPemesan.setText(getIntent().getStringExtra("nama_pemesan"));
        ETAlamatPemesan.setText(getIntent().getStringExtra("alamat_pemesan"));
        ETTanggalPembelian.setText(getIntent().getStringExtra("tanggal_pembelian"));
        ETNamaAc.setText(getIntent().getStringExtra("nama_ac"));
        ETJumlahUnit.setText(Integer.toString(jumlah_unit));
        ETTotalPembayaran.setText(Integer.toString(total_pembayaran));

        IBBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitMap != null) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailRiwayat.this);
                    alertDialogBuilder.setMessage("Do yo want to take photo again?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(context,"You clicked yes button",Toast.LENGTH_LONG).show();
                            //call fuction of TakePhoto
                            TakePhoto();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {

                    TakePhoto();
                }
            }
        });

        MBUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Upload Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();



                id_user = ETIdUser.getText().toString();
                id_pembelian = ETIdPembelian.getText().toString();
                nama_pemesan = ETNamaPemesan.getText().toString();
                alamat_pemesan = ETAlamatPemesan.getText().toString();
                tanggal_pembelian = ETTanggalPembelian.getText().toString();
                nama_ac = ETNamaAc.getText().toString();
                unit = ETJumlahUnit.getText().toString();
                total = ETTotalPembayaran.getText().toString();

                if(bitMap == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailRiwayat.this);
                    builder.setMessage("Mohon masukkan bukti transfer anda");
                    AlertDialog alert1 = builder.show();
                    alert1.show();
                    progressDialog.dismiss();
                } else {
                    validasiData();
                }
            }
        });
    }

    void validasiData(){
        if (id_user.equals("") || id_pembelian.equals("") || nama_pemesan.equals("") || alamat_pemesan.equals("") || tanggal_pembelian.equals("") || nama_ac.equals("") || unit.equals("") || total.equals("")){
            progressDialog.dismiss();
            Toast.makeText(DetailRiwayat.this, "Perikas kembali data anda !", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    public  void TakePhoto(){
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailRiwayat.this);
        builder.setTitle("Select");
        builder.setItems(rPilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                pilihan = String.valueOf(rPilihan[which]);

                if (pilihan.equals("Take a photo"))
                {
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Create a File reference to access to future access
                    photoFile = getPhotoFileUri(photoFileName);

                    // wrap File object into a content provider
                    // required for API >= 24
                    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                    String authorities = getPackageName() + ".fileprovider";
                    Uri fileProvider = FileProvider.getUriForFile(DetailRiwayat.this, authorities, photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);

                    }
                }
                else
                {

                    ActivityCompat.requestPermissions(DetailRiwayat.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);

                }



            }
        });
        builder.show();


    }
    //permission
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //set photo size
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(String.valueOf(photoFile));
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                bitMap = decodeSampledBitmapFromFile(String.valueOf(photoFile), 1000, 700);
                IBBuktiPembayaran.setImageBitmap(bitMap);
            } else { // Result was a failure
                Toast.makeText(DetailRiwayat.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (intent != null) {
                Uri photoUri = intent.getData();
                // Do something with the photo based on Uri
                bitMap = null;
                try {
                    //InputStream inputStream = getContentResolver().openInputStream(photoUri);
                    //bitMap = BitmapFactory.decodeStream(inputStream);

                    //btnImage.setVisibility(View.VISIBLE);
                    bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                IBBuktiPembayaran.setImageBitmap(bitMap);
            }
        }
    }
    //get data photo
    public File getPhotoFileUri(String fileName)  {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use getExternalFilesDir on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;

        }
        return null;
    }

    //set photo
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    // get encode image to minimize image
    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void uploadData() {
        String foto = getStringImage(bitMap);
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_10/api_daikin/updateHistoryAc.php")
                .addBodyParameter("id_pembelian",""+id_pembelian)
                .addBodyParameter("id_user",""+id_user)
                .addBodyParameter("nama_pemesan",""+nama_pemesan)
                .addBodyParameter("alamat_pemesan",""+alamat_pemesan)
                .addBodyParameter("tanggal_pembelian",""+tanggal_pembelian)
                .addBodyParameter("nama_ac",""+nama_ac)
                .addBodyParameter("jumlah_unit",""+unit)
                .addBodyParameter("total_pembayaran",""+total)
                .addBodyParameter("bukti_pembayaran",""+foto)
                .setPriority(Priority.MEDIUM)
                .setTag("Update Order")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekUpdate", "" + response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(DetailRiwayat.this, "" + pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status", "" + status);
                            if (status) {
                                new AlertDialog.Builder(DetailRiwayat.this)
                                        .setMessage("Data berhasil di upload")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent page;
                                                page = new Intent(DetailRiwayat.this, History_Pesanan.class);
                                                startActivity(page);
                                            }
                                        }).show();
                            } else {
                                new AlertDialog.Builder(DetailRiwayat.this)
                                        .setMessage("Gagal upload data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Intent i = getIntent();
                                                //setResult(RESULT_CANCELED,i);
                                                //add_mahasiswa.this.finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Tidak dapat memperbarui", "" + anError.getErrorBody());
                    }
                });
        }
    }
