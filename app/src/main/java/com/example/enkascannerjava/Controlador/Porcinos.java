package com.example.enkascannerjava.Controlador;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.enkascannerjava.CustomAdapterPorcino;
import com.example.enkascannerjava.R;
import com.example.enkascannerjava.ViewAnimalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Porcinos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Porcinos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Cerdo> cerdos;
    FirebaseFirestore firestore;
    CustomAdapterPorcino adapter;
    public Porcinos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Porcinos.
     */
    // TODO: Rename and change types and number of parameters
    public static Porcinos newInstance(String param1, String param2) {
        Porcinos fragment = new Porcinos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_porcinos, container, false);
        firestore = FirebaseFirestore.getInstance();
        ListView listView = (ListView) root.findViewById(R.id.listaCerdos);
        Button refresco = (Button) root.findViewById(R.id.referescarCerdos);
        cerdos = new ArrayList<>();
        getPorcinos();
        adapter = new CustomAdapterPorcino(getContext(), cerdos);
        listView.setAdapter((ListAdapter) adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cerdo cerdo = cerdos.get(position);
                Intent intent = new Intent(getActivity(), ViewAnimalActivity.class);
                intent.putExtra("cerdo", cerdo);
                startActivity(intent);
            }
        });

        refresco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPorcinos();
            }
        });

        return root;
    }

    public void getPorcinos(){
        cerdos.clear();
        ArrayList<QueryDocumentSnapshot> animales = new ArrayList<>();
        firestore.collection("animales")
                .document("porcino").collection("porcinos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                animales.add(document);
                                String numIde = document.getString("Numero identificacion");
                                String raza = document.getString("Raza");
                                String fechaNac = document.getString("Fecha nacimiento");
                                String sexo = document.getString("Sexo");
                                boolean vendido = document.getBoolean("Vendido");
                                String venta = "";
                                if(vendido){
                                    venta = "si";
                                }else{
                                    venta = "no";
                                }

                                Cerdo cerdo = new Cerdo("Nº crotal: " + numIde, R.drawable.cerdo, "Raza: " + raza,
                                        "Fecha de nacimiento: " + fechaNac, "Sexo: " + sexo, "Vendido: "+ venta);
                                cerdos.add(cerdo);
                                Log.d("TAG", document.getId() + "=>" + document.getData() );
                                adapter.notifyDataSetChanged();
                            }
                        }else{
                            Log.d("TAG", "Error", task.getException());
                        }
                        Log.d("TAG", "Tamaño array: " + cerdos.size());
                    }
                });

    }
}