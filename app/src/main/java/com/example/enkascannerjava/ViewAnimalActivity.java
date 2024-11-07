package com.example.enkascannerjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enkascannerjava.Controlador.Cerdo;
import com.example.enkascannerjava.Controlador.Vaca;

public class ViewAnimalActivity extends AppCompatActivity {

    TextView crotal, fechaNac, momentRepro, raza, sexo, vendido;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewbovino_layout);
        Intent intent = getIntent();
        crotal = findViewById(R.id.crotal);
        fechaNac = findViewById(R.id.fechaNac);
        momentRepro = findViewById(R.id.momentRepro);
        raza = findViewById(R.id.raza);
        sexo = findViewById(R.id.sexo);
        vendido = findViewById(R.id.vendido);
        imagen = findViewById(R.id.imagen);

        Vaca vaca = (Vaca) intent.getSerializableExtra("vaca");
        Cerdo cerdo = (Cerdo) intent.getSerializableExtra("cerdo");

        if(vaca != null) {
            crotal.setText(vaca.getnIde());
            fechaNac.setText(vaca.getFechaNac());
            momentRepro.setText(vaca.getMomenRepro());
            raza.setText(vaca.getRaza());
            sexo.setText(vaca.getSexo());
            vendido.setText(vaca.getVendido());
            imagen.setImageResource(R.drawable.vacagrande);
        }else{
            crotal.setText(cerdo.getnIde());
            fechaNac.setText(cerdo.getFechaNac());
            raza.setText(cerdo.getRaza());
            sexo.setText(cerdo.getSexo());
            momentRepro.setVisibility(View.INVISIBLE);
            vendido.setText(cerdo.getVendido());
            imagen.setImageResource(R.drawable.cerdo2);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.teal_700));
        }
    }
}