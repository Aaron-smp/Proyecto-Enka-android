package com.example.enkascannerjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enkascannerjava.Controlador.Vaca;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<Vaca> vacas;
    LayoutInflater inflater;

    public CustomAdapter(Context context, List<Vaca> vacas) {
        this.context = context;
        this.vacas = vacas;
    }

    @Override
    public int getCount() {
        return vacas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ImageView imageViewVaca;
        TextView textViewNum;
        TextView textViewRaza;

        Vaca v = vacas.get(position);

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.layout_lista_vacas, null);
        }
        imageViewVaca = (ImageView) view.findViewById(R.id.imagenLista);
        textViewRaza = (TextView) view.findViewById(R.id.razaL);
        textViewNum = (TextView) view.findViewById(R.id.identificacion);

        imageViewVaca.setImageResource(R.drawable.vaca);
        textViewNum.setText(v.nIde);
        textViewRaza.setText(v.raza);

        return view;
    }
}
