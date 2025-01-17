package com.rosadi.haullur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.Akun.ProfilActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.HubungiKamiActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.TentangAplikasiActivity;
import com.rosadi.haullur.Kelas.Pendaftaran.InfoPendaftaranActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Artikel.ArtikelActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Akun.AkunActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Haul.ProgramHaulActivity;
import com.rosadi.haullur.Kelas.Almarhum.DataKeluargaActivity;
import com.rosadi.haullur.Kelas.Baca.BacaActivity;
import com.rosadi.haullur.Kelas.Laporan.LaporanActivity;
import com.rosadi.haullur.Kelas.Penarikan.PenarikanActivity;
import com.rosadi.haullur.List.Adapter.ArtikelAdapter;
import com.rosadi.haullur.List.Model.Artikel;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String idHaulAktif = "";

    RecyclerView recyclerView;
    ArtikelAdapter artikelAdapter;
    List<Artikel> artikelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        if (preferences == null) {
            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            MainActivity.this.finish();
        }

        TextView title = findViewById(R.id.title);
        TextView title2 = findViewById(R.id.title2);
        if (preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null).equals("0")) {
            title.setText("Sugeng Rawuh,");
            title2.setText("Haul musholla tiap malem jemuah legi.");
        }

        recyclerView = findViewById(R.id.recycler_view);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_profil:
                        startActivity(new Intent(MainActivity.this, ProfilActivity.class));
                        return true;

                    case R.id.nav_menu_artikel:
                        startActivity(new Intent(MainActivity.this, ArtikelActivity.class));
                        return true;

                    case R.id.nav_menu_haul:
                        startActivity(new Intent(MainActivity.this, ProgramHaulActivity.class));
                        return true;

                    case R.id.nav_menu_akun:
                        startActivity(new Intent(MainActivity.this, AkunActivity.class));
                        return true;
                        
                    case R.id.nav_hubungi:
                        startActivity(new Intent(MainActivity.this, HubungiKamiActivity.class));
                        return true;

                    case R.id.nav_tentang:
                        startActivity(new Intent(MainActivity.this, TentangAplikasiActivity.class));
                        return true;

                    case R.id.nav_log_out:
                        openDialogLogout();
                }

                return true;
            }
        });

        Menu menu = navigationView.getMenu();
        if (preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null).equals("2")) {
            menu.findItem(R.id.nav_menu_haul).setVisible(false);
            menu.findItem(R.id.nav_menu_akun).setVisible(false);
        } else if (preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null).equals("0")) {
            menu.findItem(R.id.nav_menu_profil).setVisible(false);
            menu.findItem(R.id.nav_menu_artikel).setVisible(false);
            menu.findItem(R.id.nav_menu_haul).setVisible(false);
            menu.findItem(R.id.nav_menu_akun).setVisible(false);
        }

        TextView namaDrawer = navigationView.getHeaderView(0).findViewById(R.id.nama);
        namaDrawer.setText(preferences.getString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, null));
        TextView emailDrawer = navigationView.getHeaderView(0).findViewById(R.id.email);
        if (preferences.getString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, null).equals("")) {
            emailDrawer.setText(preferences.getString(Konfigurasi.KEY_USER_TELEPON_PREFERENCE, null));
        } else {
            emailDrawer.setText(preferences.getString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, null));
        }

        RelativeLayout buttonMenu = findViewById(R.id.button_menu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        TextView nama = findViewById(R.id.nama);
        nama.setText(preferences.getString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, null));

        loadProgramHaulAktif();

        findViewById(R.id.button_baca).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProgramHaulAktif();
                if (idHaulAktif.equals("")) {
                    Toast.makeText(MainActivity.this, "Tidak ada program haul yang aktif, silakan hubungi admin untuk mengaktifkan haul jemuah legi.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, BacaActivity.class);
                    i.putExtra("id_haul", idHaulAktif);
                    startActivity(i);
                }
            }
        });
        findViewById(R.id.button_penarikan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null), "0")) {
                    Toast.makeText(MainActivity.this, "Selain admin dan petugas  tidak bisa mengakses menu ini!", Toast.LENGTH_SHORT).show();
                } else {
                    cekProgramHaul();
                }
            }
        });
        findViewById(R.id.button_dataalmarhum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DataKeluargaActivity.class));
            }
        });
        findViewById(R.id.button_laporan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LaporanActivity.class));
            }
        });
        findViewById(R.id.button_daftaralmarhum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InfoPendaftaranActivity.class));
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        artikelAdapter = new ArtikelAdapter(this, artikelList);
        recyclerView.setAdapter(artikelAdapter);

        loadArtikel();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadArtikel() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this, "Informasi", "Memuat data artikel", false, false);
                progressDialog.setCancelable(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                artikelList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Artikel artikel = new Artikel();
                        artikel.setId(object.getString(Konfigurasi.KEY_ID));
                        artikel.setFotoTamnel(object.getString(Konfigurasi.KEY_FOTO_TAMNEL));
                        artikel.setJudul(object.getString(Konfigurasi.KEY_JUDUL));
                        artikel.setTanggal(object.getString(Konfigurasi.KEY_TANGGAL));
                        artikel.setLokasi(object.getString(Konfigurasi.KEY_LOKASI));
                        artikel.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        artikel.setDilihat(object.getString(Konfigurasi.KEY_DILIHAT));
                        artikel.setIdHaul(object.getString(Konfigurasi.KEY_ID_HAUL));
                        artikel.setFoto(object.getString(Konfigurasi.KEY_FOTO));
                        artikel.setFoto2(object.getString(Konfigurasi.KEY_FOTO_2));
                        artikelList.add(artikel);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    artikelAdapter = new ArtikelAdapter(MainActivity.this, artikelList);
                    recyclerView.setAdapter(artikelAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_ARTIKEL);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void openDialogLogout() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Logout");
        teks.setText("Apakah anda yakin ingin logout ?");
        teksiya.setText("Logout");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                preferences.edit().clear().commit();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cekProgramHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.trim().equals("1")) {
                    startActivity(new Intent(MainActivity.this, PenarikanActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Hubungi admin untuk mengaktifkan haul jemuah legi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_CEK_PROGRAM_HAUL);
                return s;
            }


        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void loadProgramHaulAktif() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    JSONObject data = result.getJSONObject(0);

                    idHaulAktif = data.getString(Konfigurasi.KEY_ID);
                    String deskripsi = data.getString(Konfigurasi.KEY_DESKRIPSI);
                    String tanggal = data.getString(Konfigurasi.KEY_TANGGAL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_PROGRAM_AKTIF_HAUL);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}