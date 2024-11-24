package com.example.lab7_iot_20201638;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class EmpresaActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewRecaudacionTotal;
    private RecyclerView recyclerViewLineasBuses;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Empresas");
        userId = mAuth.getCurrentUser().getUid();

        // Vincular vistas
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewRecaudacionTotal = findViewById(R.id.textViewRecaudacionTotal);
        recyclerViewLineasBuses = findViewById(R.id.recyclerViewLineasBuses);

        // Configurar RecyclerView
        recyclerViewLineasBuses.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos del usuario
        cargarDatosEmpresa();

        // Cargar lista de líneas de buses
        cargarLineasDeBuses();
    }

    private void cargarDatosEmpresa() {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String nombre = snapshot.child("name").getValue(String.class);
                double recaudacionTotal = snapshot.child("recaudacion").getValue(Double.class);

                textViewWelcome.setText("Bienvenido, " + nombre);
                textViewRecaudacionTotal.setText("Recaudación total: S/ " + recaudacionTotal);
            } else {
                Toast.makeText(this, "Error al cargar datos de la empresa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarLineasDeBuses() {
        ArrayList<String> lineasDeBuses = new ArrayList<>();
        lineasDeBuses.add("Línea 1 - Recaudación: S/ 5000");
        lineasDeBuses.add("Línea 2 - Recaudación: S/ 3000");
        lineasDeBuses.add("Línea 3 - Recaudación: S/ 2000");

        // Adaptador para RecyclerView
        LineasAdapterEmpresa adapter = new LineasAdapterEmpresa(lineasDeBuses, this);
        recyclerViewLineasBuses.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empresa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(EmpresaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
