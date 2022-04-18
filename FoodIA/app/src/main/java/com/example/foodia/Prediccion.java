package com.example.foodia;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prediccion extends AppCompatActivity {

    private final static String API_KEY = "ytBA8OcIg1yAw3Yfigv1mQ==5gSlbrHzzb4pvScy";
    ProgressBar progressBar;

    String res_prediccion;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getWindow().setStatusBarColor(ContextCompat.getColor(Prediccion.this, R.color.black));
        setContentView(R.layout.activity_prediccion);

        progressBar = findViewById(R.id.progressBar);

        TextView banner = findViewById(R.id.banner);
        String text = "FoodIA";

        SpannableString ss = new SpannableString(text);

        ForegroundColorSpan fcsVerde = new ForegroundColorSpan(Color.rgb(50,120,50));
        ForegroundColorSpan fcsNegro = new ForegroundColorSpan(Color.BLACK);
        ss.setSpan(fcsVerde, 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsNegro, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        banner.setText(ss);

        res_prediccion = getIntent().getStringExtra("RESULT");
        image = (Bitmap) getIntent().getParcelableExtra("IMAGE");

        getInfo(res_prediccion);
    }

    private void getInfo(String res_prediccion) {

        String url  = "https://api.calorieninjas.com/v1/nutrition?query=" + res_prediccion;

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray response_content = response.getJSONArray("items");
                            JSONObject response_item = response_content.getJSONObject(0);

                            writeInfo(response_item);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Prediccion.this, "Algo fue mal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("X-Api-Key", API_KEY);
                return header;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeInfo(JSONObject response_item) throws JSONException{
        String calorias = response_item.getString("calories");
        String azucares = response_item.getString("sugar_g");
        String fibra = response_item.getString("fiber_g");
        String proteinas = response_item.getString("protein_g");
        String grasas = response_item.getString("fat_total_g");
        String carbohidratos = response_item.getString("carbohydrates_total_g");

        progressBar.setVisibility(View.GONE);

        ImageView img = findViewById(R.id.img);
        TextView resultado = findViewById(R.id.resultado);
        img.setImageBitmap(image);
        resultado.setText(res_prediccion);

        TextView txtCalorias = findViewById(R.id.calorias);
        txtCalorias.setText(calorias + "g");

        TextView txtAzucares = findViewById(R.id.azucares);
        txtAzucares.setText(azucares + "g");

        TextView txtFibra = findViewById(R.id.fibra);
        txtFibra.setText(fibra + "g");

        TextView txtProteinas = findViewById(R.id.proteinas);
        txtProteinas.setText(proteinas + "g");

        TextView txtGrasas = findViewById(R.id.grasas);
        txtGrasas.setText(grasas + "g");

        TextView txtCarbohidratos = findViewById(R.id.carbohidratos);
        txtCarbohidratos.setText(carbohidratos + "g");

        String imagenRegistro = getStringFromBitmap(image);

        Registro newRegistro = new Registro(res_prediccion, calorias, azucares, fibra, proteinas,
                grasas, carbohidratos, imagenRegistro);

        actualizarRegistros(newRegistro);
    }

    public void actualizarRegistros(Registro newRegistro){
        Gson gson = new Gson();
        File dir = getApplicationContext().getFilesDir();
        File path = new File(dir, "historial.json");
        try {
            path.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileReader reader = new FileReader(path);
            ArrayList<Registro> registros = gson.fromJson(reader, new TypeToken<ArrayList<Registro>>() {}.getType());
            reader.close();

            if(registros == null){
                registros = new ArrayList<Registro>();
            }

            registros.add(newRegistro);
            FileWriter writer = new FileWriter(path);
            gson.toJson(registros, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}