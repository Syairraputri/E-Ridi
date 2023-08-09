package com.syairraputri.e_ridi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoPenyakit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_penyakit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Mendapatkan referensi ke setiap tombol
        Button bacterialLeafBlightButton = findViewById(R.id.bacterial_leaf_blight_button);
        Button brownSpotButton = findViewById(R.id.brown_spot_button);
        Button hispaButton = findViewById(R.id.hispa_button);
        Button leafBlastButton = findViewById(R.id.leaf_blast_button);
        Button healthyButton = findViewById(R.id.healthy_button);

        // Menambahkan fungsi onClickListener untuk setiap tombol
        bacterialLeafBlightButton.setOnClickListener(v -> {
            // Pindah ke halaman berikutnya (ganti dengan halaman yang sesuai)
            Intent intent = new Intent(InfoPenyakit.this, BacterialLeafBlight.class);
            startActivity(intent);
        });

        brownSpotButton.setOnClickListener(v -> {
            // Pindah ke halaman berikutnya (ganti dengan halaman yang sesuai)
            Intent intent = new Intent(InfoPenyakit.this, BrownSpot.class);
            startActivity(intent);
        });

        hispaButton.setOnClickListener(v -> {
            // Pindah ke halaman berikutnya (ganti dengan halaman yang sesuai)
            Intent intent = new Intent(InfoPenyakit.this, hispa.class);
            startActivity(intent);
        });

        leafBlastButton.setOnClickListener(v -> {
            // Pindah ke halaman berikutnya (ganti dengan halaman yang sesuai)
            Intent intent = new Intent(InfoPenyakit.this, LeafBlast.class);
            startActivity(intent);
        });

        healthyButton.setOnClickListener(v -> {
            // Pindah ke halaman berikutnya (ganti dengan halaman yang sesuai)
            Intent intent = new Intent(InfoPenyakit.this, Healthy.class);
            startActivity(intent);
        });
    }
    // Tambahkan metode ini untuk menangani klik tombol kembali ke halaman sebelumnya
    public void onBackPressed(View view) {
        onBackPressed();
    }
}