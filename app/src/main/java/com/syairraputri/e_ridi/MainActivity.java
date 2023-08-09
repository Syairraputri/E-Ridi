package com.syairraputri.e_ridi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Inisialisasi view
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        // Mengatur listener untuk tombol login
        loginButton.setOnClickListener(v -> {
            // Mendapatkan username dan password yang dimasukkan
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Memeriksa apakah username dan password cocok
            if (username.equals("admin") && password.equals("admin123")) {
                // Jika cocok, navigasikan ke halaman beranda
                pindahKeBeranda();
            } else {
                // Jika tidak cocok, tampilkan pesan gagal
                Toast.makeText(MainActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pindahKeBeranda() {
        Intent intent = new Intent(MainActivity.this, BerandaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}