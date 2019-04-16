package byl.baresylugares.Presentacion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;

import static android.view.Gravity.CENTER;

public class listarRecomendaciones extends AppCompatActivity {

    private String tipo;
    private ArrayList<Recomendacion> listaR;
    private Usuario usuario;
    private String RECOM_URL = "https://baresylugares.webcindario.com/getRecomendacion.php";
    private String RECOM_PROPIA_URL = "https://baresylugares.webcindario.com/getRecomendacionPropia.php";
    private String RECOM_URL_BORRAR = "https://baresylugares.webcindario.com/borrarRecomendacion.php";
    private String KEY_TIPO = "tipo";
    private String KEY_NOMBRE = "nombre";
    private String KEY = null;
    private String valor = null;
    private RecyclerView lstRecomendaciones;
    private AdaptadorLista adaptadorTipo;
    private AdaptadorListaPropio adaptadorListaPropio;
    private Context context;
    private String modelo;
    private String id;
    private String KEY_ID = "id";


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        listaR = new ArrayList<Recomendacion>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_recomendaciones);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Recomendaciones");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tipo = (String) getIntent().getSerializableExtra("Tipo");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        listar();

    }


    private void listar() {
        String ruta = null;

        if (tipo.equals("bar") || tipo.equals("lugar")) {
            ruta = RECOM_URL;
            KEY = KEY_TIPO;
            valor = tipo;
            modelo = "no";
        } else if (tipo.equals("modificar")) {
            ruta = RECOM_PROPIA_URL;
            KEY = KEY_NOMBRE;
            valor = usuario.getNombre();
            modelo = "modificar";
        }
        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo recomendaciones...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ruta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            loading.dismiss();
                            if (jsonObject.getBoolean("error")) {
                                String message = jsonObject.optString("message");
                                showToast(message);
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("recomendaciones");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    Integer id = jsonObject1.optInt("id");
                                    String nombreTarjeta = jsonObject1.optString("nombreTarjeta");
                                    String comentario = jsonObject1.optString("comentario");
                                    String fecha = jsonObject1.optString("fecha");
                                    String usuario = jsonObject1.optString("usuario");
                                    String tipo = jsonObject1.optString("tipo");
                                    String longitud = jsonObject1.optString("longitud");
                                    String latitud = jsonObject1.optString("latitud");
                                    String imagen = jsonObject1.optString("imagen");
                                    Recomendacion rem = new Recomendacion(nombreTarjeta, fecha, comentario
                                            , usuario, latitud, longitud, imagen, tipo);
                                    rem.setid(id);
                                    listaR.add(rem);
                                    lstRecomendaciones = findViewById(R.id.lstRecomendaciones);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    lstRecomendaciones.setLayoutManager(mLayoutManager);
                                    if (modelo.equals("modificar")) {
                                        adaptadorListaPropio = new AdaptadorListaPropio(context, listaR);
                                        lstRecomendaciones.setAdapter(adaptadorListaPropio);
                                        adaptadorListaPropio.setOnItemClickListener(new AdaptadorListaPropio.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {
                                                click(listaR.get(position));
                                            }

                                            @Override
                                            public void onItenClickBorrar(int position) {
                                                borrar(listaR.get(position));
                                            }

                                            @Override
                                            public void onItemClickGPS(int position) {
                                                Gps(listaR.get(position));
                                            }

                                            @Override
                                            public void onItemClickModificar(int position) {
                                                modificar(listaR.get(position));
                                            }
                                        });
                                    } else {
                                        adaptadorTipo = new AdaptadorLista(context, listaR);
                                        lstRecomendaciones.setAdapter(adaptadorTipo);
                                        adaptadorTipo.setOnItemClickListener(new AdaptadorLista.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {
                                                click(listaR.get(position));
                                            }

                                            @Override
                                            public void onItemClickGPS(int position) {
                                                Gps(listaR.get(position));
                                            }
                                        });
                                    }
                                }
                                showToast("Mostrando recomendaciones, hay: " + listaR.size());
                            }
                        } catch (Exception e) {
                            Log.d("Hola", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        showToast("Error obteniedo recomendacioones Código Error: " + volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                Log.d("Hola", valor);
                params.put(KEY, valor);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Gps(Recomendacion r) {
        Intent intent = new Intent(listarRecomendaciones.this, mapa.class);
        intent.putExtra("Usuario", usuario);
        intent.putExtra("Recomendacion", r);
        intent.putExtra("Tipo", tipo);
        startActivity(intent);
        finish();
    }

    private void modificar(Recomendacion r) {
        Intent intent = new Intent(context, crearRecomendacion.class);
        intent.putExtra("Usuario", usuario);
        intent.putExtra("Recomendacion", r);
        intent.putExtra("Tipo", tipo);
        startActivity(intent);
        finish();
    }

    private void click(Recomendacion r) {
        Intent intent = new Intent(context, mostarRecomendacion.class);
        intent.putExtra("Usuario", usuario);
        intent.putExtra("Recomendacion", r);
        intent.putExtra("Tipo", tipo);
        startActivity(intent);
        finish();
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(listarRecomendaciones.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(listarRecomendaciones.this, Menu.class);
            intent.putExtra("Usuario", usuario);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.btnSobreAutores) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Aplicación creada por Francisco de la Mata y Jorge Muñoz, para la asignatura GSI" +
                    ", Gestión de Sitemas Informáticos.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void borrar(Recomendacion r) {
        id = r.getid().toString();
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECOM_URL_BORRAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Intent intent = new Intent(listarRecomendaciones.this, listarRecomendaciones.class);
                        intent.putExtra("Usuario", usuario);
                        intent.putExtra("Tipo", "modificar");
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        showToast("error" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_ID, id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
