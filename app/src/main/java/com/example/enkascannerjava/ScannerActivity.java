package com.example.enkascannerjava;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.enkascannerjava.utils.Comprobaciones;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ScannerActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    ImageButton button_capture;
    Button button_hoy, button_bbdd, button_lista;
    private static final int REQUEST_CAMERA_CODE = 100;
    String ruta_imagen;
    Bitmap bitmap;
    File imagenArchivo;

    Spinner sp_animales, momentoReproductivo, sexo;
    EditText numIde, fechaNacimiento, raza;
    CheckBox vendido;
    String[] opcionesAnimal = {"Bovino", "Porcino"};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Log.d("TAG", "Error AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        db = FirebaseFirestore.getInstance();
        numIde = findViewById(R.id.numIde);
        sp_animales = findViewById(R.id.animal);
        fechaNacimiento = findViewById(R.id.fechaNac);
        momentoReproductivo = findViewById(R.id.momentoReproductivo);
        sexo = findViewById(R.id.sexo);
        raza = findViewById(R.id.raza);
        vendido = findViewById(R.id.vendido);

        button_lista = findViewById(R.id.lista);
        button_capture = findViewById(R.id.sacarFoto);
        button_hoy = findViewById(R.id.hoy);
        button_bbdd = findViewById(R.id.subirBBDD);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, opcionesAnimal);
        sp_animales.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(ScannerActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{
                    android.Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        if (ContextCompat.checkSelfPermission(ScannerActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ScannerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ScannerActivity.this, "Los permisos ya han sido concedidos", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        sp_animales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sp_animales.getSelectedItem().equals("Porcino")){
                    momentoReproductivo.setEnabled(false);
                }else{
                    momentoReproductivo.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    imagenArchivo = null;
                    try{
                        imagenArchivo = crearImagen();
                    }catch (IOException e){
                        Log.e("Error", e.toString());
                    }
                    if(imagenArchivo != null){
                        Uri fotoUri = FileProvider.getUriForFile(ScannerActivity.this, "com.example.pruebaescaner.fileprovider", imagenArchivo);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                    }
                    startActivityForResult(takePictureIntent, 2);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.fondo));
        }

        button_bbdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirAnimal();
            }
        });

        button_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannerActivity.this, ListasAnimales.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == 2){
            CropImage.activity(Uri.fromFile(imagenArchivo))
                    .start(ScannerActivity.this);
        }
    }

    private void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()){
            Toast.makeText(ScannerActivity.this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
        }else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < textBlockSparseArray.size(); i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                //stringBuilder.append("\n");
            }
            //textView_data.setText(stringBuilder.toString());
            numIde.setText(eliminarSaltosDeLinea(stringBuilder.toString()));

        }
    }

    private File crearImagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        ruta_imagen = imagen.getAbsolutePath();
        return imagen;
    }

    public String eliminarSaltosDeLinea(String texto){
        return texto.replaceAll("\n", "");
    }

    public void obtenerFechaActual(View view) {
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        fechaNacimiento.setText(formato.format(fecha));
    }

    public void subirAnimal(){
        Comprobaciones comprobar = new Comprobaciones();
        if(comprobar.verificarNumIde(numIde.getText().toString())){
            if(comprobar.verificarFecha(fechaNacimiento.getText().toString())){
                HashMap<String, Object> animal = new HashMap<>();
                if(sp_animales.getSelectedItem().equals("Bovino")){
                    String momento = momentoReproductivo.getSelectedItem().toString();
                    if(sexo.getSelectedItem().toString().equals("Macho")){
                        momento = "No tiene";
                    }
                    animal.put("Numero identificacion", numIde.getText().toString());
                    animal.put("Fecha nacimiento", fechaNacimiento.getText().toString());
                    animal.put("Momento reproductivo", momento);
                    animal.put("Raza", raza.getText().toString());
                    animal.put("Sexo", sexo.getSelectedItem().toString());
                    animal.put("Vendido", vendido.isSelected());
                    db.collection("animales").document("bovino").collection("bovinos")
                            .add(animal)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(ScannerActivity.this, "Bovino añadido", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    animal.put("Numero identificacion", numIde.getText().toString());
                    animal.put("Fecha nacimiento", fechaNacimiento.getText().toString());
                    animal.put("Raza", raza.getText().toString());
                    animal.put("Sexo", sexo.getSelectedItem().toString());
                    animal.put("Vendido", vendido.isSelected());
                    db.collection("animales").document("porcino").collection("porcinos")
                            .add(animal)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(ScannerActivity.this, "Porcino añadido", Toast.LENGTH_SHORT).show();
                                }
                            });;
                }
            }else{
                Toast.makeText(this, "Fecha mal formada", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Numero erroneo", Toast.LENGTH_SHORT).show();
        }

    }
}