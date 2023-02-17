package com.rosadi.haullur.List.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Keluarga.DataKeluargaDetailActivity;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;


import java.net.URLEncoder;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KeluargaAdapter extends RecyclerView.Adapter<KeluargaAdapter.ViewHolder> {

    Context context;
    List<Keluarga> keluargaList;

    public KeluargaAdapter(Context context, List<Keluarga> keluargaList) {
        this.context = context;
        this.keluargaList = keluargaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_keluarga, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Keluarga keluarga = keluargaList.get(position);

        holder.nama.setText(keluarga.getNama());
        holder.rt.setText("RT " + keluarga.getRt());

        holder.telepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keluarga.getTelepon().isEmpty()) {
                    Toast.makeText(context, "WhatsApp belum ditambahkan!", Toast.LENGTH_SHORT).show();
                } else {
                    PackageManager packageManager = context.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    try {
                        String pesan = "Haul jemuah legi, Wayah e penarikan";
                        String url = "https://api.whatsapp.com/send?phone=" + keluarga.getTelepon() + "&text=" + URLEncoder.encode(pesan, "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            context.startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        loadTotalAlmarhums(holder.total, keluarga.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DataKeluargaDetailActivity.class);
                i.putExtra(Konfigurasi.KEY_ID, keluarga.getId());
                i.putExtra(Konfigurasi.KEY_NAMA, keluarga.getNama());
                i.putExtra(Konfigurasi.KEY_RT, keluarga.getRt());
                i.putExtra(Konfigurasi.KEY_TELEPON, keluarga.getTelepon());
                context.startActivity(i);
            }
        });
    }

    private void loadTotalAlmarhums(TextView total, String id) {
        class LoadTotalAlmarhums extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                total.setText(s.trim());
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler handler = new RequestHandler();
                String s = handler.sendGetRequestParam(Konfigurasi.URL_LOAD_TOTAL_ALMARHUM, id);
                return s;
            }
        }

        LoadTotalAlmarhums totalAlmarhums = new LoadTotalAlmarhums();
        totalAlmarhums.execute();
    }

    @Override
    public int getItemCount() {
        return keluargaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, rt, total;
        ImageView telepon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            rt = itemView.findViewById(R.id.rt);
            telepon = itemView.findViewById(R.id.telepon);
            total = itemView.findViewById(R.id.total_almarhums);
        }
    }
}
