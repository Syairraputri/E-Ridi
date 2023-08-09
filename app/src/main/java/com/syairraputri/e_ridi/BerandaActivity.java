package com.syairraputri.e_ridi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BerandaActivity extends AppCompatActivity {

    public Button infoPenyakitButton;
    public Button bantuanButton;
    public Button keluarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Button cekPadikuButton = findViewById(R.id.cek_padiku_button);
        infoPenyakitButton = findViewById(R.id.info_penyakit_button);
        bantuanButton = findViewById(R.id.bantuan_button);
        keluarButton = findViewById(R.id.keluar_button);

        cekPadikuButton.setOnClickListener(v -> {
            Intent intent = new Intent(BerandaActivity.this, CekPadiku.class);
            startActivity(intent);
        });

        infoPenyakitButton.setOnClickListener(v -> {
            Intent intent = new Intent(BerandaActivity.this, InfoPenyakit.class);
            startActivity(intent);
        });

        bantuanButton.setOnClickListener(v -> {
            Intent intent = new Intent(BerandaActivity.this, Bantuan.class);
            startActivity(intent);
        });

        keluarButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BerandaActivity.this);
            builder.setTitle("Konfirmasi Keluar");
            builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?");
            builder.setPositiveButton("Ya", (dialog, which) -> finish());
            builder.setNegativeButton("Tidak", null);
            builder.show();
        });
    }
}