package com.syairraputri.e_ridi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Bantuan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Mendapatkan referensi ke setiap tombol
        Button cekPadikuButton = findViewById(R.id.cek_padiku_button);
        Button infoPenyakitButton = findViewById(R.id.info_penyakit_button);
        Button bantuanButton = findViewById(R.id.bantuan_button);
        Button keluarButton = findViewById(R.id.keluar_button);

        cekPadikuButton.setOnClickListener(v -> {
            Intent intent = new Intent(Bantuan.this, MenuCekPadiku.class);
            startActivity(intent);
        });

        infoPenyakitButton.setOnClickListener(v -> {
            Intent intent = new Intent(Bantuan.this, MenuInfoPenyakit.class);
            startActivity(intent);
        });

        bantuanButton.setOnClickListener(v -> {
            Intent intent = new Intent(Bantuan.this, MenuBantuan.class);
            startActivity(intent);
        });

        keluarButton.setOnClickListener(v -> {
            Intent intent = new Intent(Bantuan.this, MenuKeluar.class);
            startActivity(intent);
        });
    }

    // Tambahkan metode ini untuk menangani klik tombol kembali ke halaman sebelumnya
    public void onBackPressed(View view) {
        onBackPressed();
    }
}