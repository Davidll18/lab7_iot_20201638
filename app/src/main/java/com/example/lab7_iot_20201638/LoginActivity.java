package com.example.lab7_iot_20201638;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        // Vincular vistas
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);
        progressBar = findViewById(R.id.progressBar);

        // Acción al presionar "Iniciar Sesión"
        buttonLogin.setOnClickListener(v -> loginUser());

        // Acción al presionar "Registrarse"
        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("mensaje")) {
            String mensaje = intent.getStringExtra("mensaje");
            Log.d("LoginActivity", "Mensaje recibido: " + mensaje);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        } else {
            Log.d("LoginActivity", "No se recibió ningún mensaje desde RegisterActivity.");
        }
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validaciones
        if (email.isEmpty()) {
            editTextEmail.setError("El correo es requerido");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Por favor, ingrese un correo válido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("La contraseña es requerida");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            editTextPassword.setError("La contraseña debe tener al menos 8 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        // Mostrar ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Autenticación con Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // Obtener el UID del usuario
                        String uid = mAuth.getCurrentUser().getUid();
                        verificarRolUsuario(uid);
                    } else {
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verificarRolUsuario(String uid) {
        databaseReference.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String rol = snapshot.child("rol").getValue(String.class);

                if ("Empresa".equals(rol)) {
                    // Redirigir a EmpresaActivity
                    Intent intent = new Intent(LoginActivity.this, EmpresaActivity.class);
                    startActivity(intent);
                    finish();
                } else if ("Operativo".equals(rol)) {
                    // Redirigir a OperativoActivity
                    Intent intent = new Intent(LoginActivity.this, OperativoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
