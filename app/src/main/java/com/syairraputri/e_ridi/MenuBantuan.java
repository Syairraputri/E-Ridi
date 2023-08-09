package com.syairraputri.e_ridi;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MenuBantuan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bantuan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    // Tambahkan metode ini untuk menangani klik tombol kembali ke halaman sebelumnya
    public void onBackPressed(View view) {
        onBackPressed();
    }
}
