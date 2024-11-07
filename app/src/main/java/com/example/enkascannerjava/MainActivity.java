package com.example.enkascannerjava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usuario;
    private EditText contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        usuario = findViewById(R.id.usuario);
        contra = findViewById(R.id.contra);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gris));
        }

    }

    public void iniciarSesion(View view){
        if(!usuario.getText().toString().isEmpty() && !contra.getText().toString().isEmpty()){
            mAuth.getInstance().signInWithEmailAndPassword(usuario.getText().toString(), contra.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "El correo o contraseña es incorrecto", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }

}