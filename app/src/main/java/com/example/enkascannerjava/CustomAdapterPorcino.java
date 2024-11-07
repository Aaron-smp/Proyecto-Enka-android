package com.example.enkascannerjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.enkascannerjava.Controlador.Cerdo;

import java.util.List;

public class CustomAdapterPorcino extends BaseAdapter {

    Context context;
    List<Cerdo> cerdos;

    public CustomAdapterPorcino(Context context, List<Cerdo> cerdos) {
        this.context = context;
        this.cerdos = cerdos;
    }

    @Override
    public int getCount() {
        return cerdos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageView imageViewVaca;
        TextView textViewNum;
        TextView textViewRaza;

        Cerdo c = cerdos.get(position);

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.layout_lista_vacas, null);
        }
        imageViewVaca = (ImageView) view.findViewById(R.id.imagenLista);
        textViewRaza = (TextView) view.findViewById(R.id.razaL);
        textViewNum = (TextView) view.findViewById(R.id.identificacion);

        imageViewVaca.setImageResource(R.drawable.cerdo);
        textViewNum.setText(c.nIde);
        textViewRaza.setText(c.raza);

        return view;
    }
}
