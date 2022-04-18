package com.example.foodia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<Registro> {

    private Context ctx;
    private int resource;

    public HistoryAdapter(@NonNull Context ctx, int resource, @NonNull ArrayList<Registro> registros){
        super(ctx, resource, registros);
        this.ctx = ctx;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        convertView = layoutInflater.inflate(resource, parent, false);

        ImageButton btnFoto = convertView.findViewById(R.id.hist_imagen);
        TextView txtNombre = convertView.findViewById(R.id.hist_nombre);
        TextView txtCalorias = convertView.findViewById(R.id.hist_calorias);
        TextView txtGrasas = convertView.findViewById(R.id.hist_grasas);
        TextView txtProteinas = convertView.findViewById(R.id.hist_proteinas);
        TextView txtCarbohidratos = convertView.findViewById(R.id.hist_carbohidratos);

        btnFoto.setImageBitmap(getBitmapFromString(getItem(position).getImagen()));
        txtNombre.setText(getItem(position).getNombre());
        txtCalorias.setText("Calorías: " + getItem(position).getCalorias() + "g");
        txtGrasas.setText("Grasas: " + getItem(position).getAzucares() + "g");
        txtProteinas.setText("Proteínas: " + getItem(position).getProteinas() + "g");
        txtCarbohidratos.setText("Carbohidratos: " + getItem(position).getCarbohidratos() + "g");

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Prediccion.class);
                intent.putExtra("IMAGE", getBitmapFromString(getItem(position).getImagen()));
                intent.putExtra("RESULT", getItem(position).getNombre());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
