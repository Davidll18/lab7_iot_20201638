package com.example.lab7_iot_20201638;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OperativoActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewSaldo;
    private RecyclerView recyclerViewLineasBuses;
    private Button buttonEscanearQR;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;
    private double saldoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operativo);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        userId = mAuth.getCurrentUser().getUid();

        // Vincular vistas
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewSaldo = findViewById(R.id.textViewSaldo);
        recyclerViewLineasBuses = findViewById(R.id.recyclerViewLineasBuses);
        buttonEscanearQR = findViewById(R.id.buttonEscanearQR);

        // Configurar RecyclerView
        recyclerViewLineasBuses.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos del usuario
        cargarDatosUsuario();

        // Acción al presionar botón Escanear QR
        buttonEscanearQR.setOnClickListener(v -> {
            Intent intent = new Intent(OperativoActivity.this, QRScannerActivity.class);
            startActivity(intent);
        });

        // Cargar lista de líneas de buses
        cargarLineasDeBuses();
    }

    private void cargarDatosUsuario() {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String nombre = snapshot.child("name").getValue(String.class);
                String apellido = snapshot.child("lastName").getValue(String.class);
                saldoActual = snapshot.child("saldo").getValue(Double.class);

                textViewWelcome.setText("Bienvenido, " + nombre + " " + apellido);
                textViewSaldo.setText("Saldo: S/ " + saldoActual);
            } else {
                Toast.makeText(this, "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarLineasDeBuses() {
        ArrayList<String> lineasDeBuses = new ArrayList<>();
        lineasDeBuses.add("Línea 1 - Precio: S/ 2.50");
        lineasDeBuses.add("Línea 2 - Precio: S/ 3.00");
        lineasDeBuses.add("Línea 3 - Precio: S/ 1.80");

        // Adaptador para RecyclerView
        LineasAdapterEmpresa adapter = new LineasAdapterEmpresa(lineasDeBuses, this);
        recyclerViewLineasBuses.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operativo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(OperativoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

