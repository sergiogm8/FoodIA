package com.example.foodia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Historial extends AppCompatActivity {
    ListView listaRegistros;
    ArrayList<Registro> registros = new ArrayList<>();
    File archivoHistorial;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.borrarHistorial:
                if(archivoHistorial.delete()){
                    recreate();
                    Toast.makeText(this, "El historial se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "Hubo un error al borrar el historial", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        setContentView(R.layout.activity_historial);

        listaRegistros = (ListView) findViewById(R.id.listRegistros);
        registros = leerRegistros();
        
        if(registros == null){
            Toast.makeText(this, "No hay actividad aún", Toast.LENGTH_LONG).show();
        }else {
        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.historial_layout, registros);
        listaRegistros.setAdapter(adapter);
        }
    }

    private ArrayList<Registro> leerRegistros() {
        ArrayList<Registro> array = new ArrayList<>();
        Gson gson = new Gson();
        File dir = getApplicationContext().getFilesDir();
        File path = new File(dir, "historial.json");
        archivoHistorial = path;
        try {
            path.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileReader reader = new FileReader(path);
            array = gson.fromJson(reader, new TypeToken<ArrayList<Registro>>() {}.getType());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return array;
    }
}