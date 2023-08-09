package com.syairraputri.e_ridi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.syairraputri.e_ridi.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class CekPadiku extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_GALLERY = 3;

    private ImageView imageView;
    private TextView waktuPengambilanTextView;
    private TextView jenisPenyakitTextView;
    private TextView akurasiTextView;

    private Bitmap capturedImage;
    private int detectedDiseaseIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_padiku);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        imageView = findViewById(R.id.image_view);
        waktuPengambilanTextView = findViewById(R.id.waktu_pengambilan_text_view);
        jenisPenyakitTextView = findViewById(R.id.jenis_penyakit_text_view);
        akurasiTextView = findViewById(R.id.akurasi_text_view);
    }

    public void ambilGambar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ambil Gambar");
        builder.setItems(new CharSequence[]{"Kamera", "Galeri"}, (dialog, which) -> {
            if (which == 0) {
                // Ambil gambar dari kamera
                if (ContextCompat.checkSelfPermission(CekPadiku.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    requestCameraPermission();
                }
            } else {
                // Ambil gambar dari galeri
                openGallery();
            }
        });
        builder.show();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(CekPadiku.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied. Unable to access camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (data != null && data.getExtras() != null) {
                    capturedImage = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(capturedImage);
                    displayImageInfo();
                    classifyImage(this, convertBitmapToByteBuffer(capturedImage));
                }
            } else if (requestCode == REQUEST_GALLERY) {
                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    try {
                        capturedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        imageView.setImageBitmap(capturedImage);
                        displayImageInfo();
                        classifyImage(this, convertBitmapToByteBuffer(capturedImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int modelInputSize = 150; // Ukuran input yang diharapkan oleh model

        // Membuat ByteBuffer dengan ukuran yang sesuai untuk menampung data gambar
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * modelInputSize * modelInputSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        // Mengalokasikan array untuk menyimpan nilai piksel gambar dalam format float
        float[] floatValues = new float[modelInputSize * modelInputSize * 3];

        // Menormalkan dan mengonversi nilai piksel gambar ke format float
        int[] intValues = new int[modelInputSize * modelInputSize];
        bitmap = Bitmap.createScaledBitmap(bitmap, modelInputSize, modelInputSize, true);
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < intValues.length; i++) {
            final int val = intValues[i];
            floatValues[i * 3] = ((val >> 16) & 0xFF) / 255.0f;
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF) / 255.0f;
            floatValues[i * 3 + 2] = (val & 0xFF) / 255.0f;
        }

        // Menyalin nilai piksel dalam format float ke ByteBuffer
        for (float floatValue : floatValues) {
            byteBuffer.putFloat(floatValue);
        }

        byteBuffer.rewind(); // Mengatur posisi ByteBuffer ke awal

        return byteBuffer;
    }

    private void displayImageInfo() {
    }

    public void classifyImage(Context context, ByteBuffer byteBuffer) {
        try {
            Model model = Model.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 150, 150, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // Find the index of the class with the highest confidence.
            int maxPos = 0;
            float maxConfidence = 0;

            if (confidences != null) {
                for (int i = 0; i < confidences.length; i++) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i];
                        maxPos = i;
                    }
                }
            }

            String[] classes = {"Bacterial Leaf Blight", "Brown Spot", "Hispa", "Leaf Blast", "Healthy Leaf", "Tidak Diketahui"};
            String jenisPenyakit = classes[maxPos];

            displayImageInfo(jenisPenyakit, maxConfidence); // Memanggil metode displayImageInfo()

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Handle the exception
        }
    }

    private void displayImageInfo(String jenisPenyakit, float confidence) {
        String currentTime = DateFormat.format("dd-MM-yyyy HH:mm:ss", new Date()).toString();
        waktuPengambilanTextView.setText("Waktu Pengambilan: " + currentTime);
        jenisPenyakitTextView.setText("Jenis Penyakit: " + jenisPenyakit);
        float akurasiPersentase = confidence * 100;
        akurasiTextView.setText("Akurasi: " + String.format("%.2f%%", akurasiPersentase));
    }

    // Tambahkan metode ini untuk menangani klik tombol kembali ke halaman sebelumnya
    public void onBackPressed(View view) {
        onBackPressed();
    }
}