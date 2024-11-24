package com.example.lab7_iot_20201638;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class LineasAdapterEmpresa extends RecyclerView.Adapter<LineasAdapterEmpresa.LineasViewHolder> {

    private ArrayList<String> lineasDeBuses;
    private Context context;

    public LineasAdapterEmpresa(ArrayList<String> lineasDeBuses, Context context) {
        this.lineasDeBuses = lineasDeBuses;
        this.context = context;
    }

    @NonNull
    @Override
    public LineasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_linea_bus, parent, false);
        return new LineasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineasViewHolder holder, int position) {
        String linea = lineasDeBuses.get(position);
        holder.textViewLinea.setText(linea);

        holder.buttonDetalles.setOnClickListener(v -> {
            // Acción para redirigir a detalles
            Toast.makeText(context, "Detalles de: " + linea, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return lineasDeBuses.size();
    }

    public static class LineasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLinea;
        Button buttonDetalles;

        public LineasViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLinea = itemView.findViewById(R.id.textViewLinea);
            buttonDetalles = itemView.findViewById(R.id.buttonDetalles);
        }
    }
}
