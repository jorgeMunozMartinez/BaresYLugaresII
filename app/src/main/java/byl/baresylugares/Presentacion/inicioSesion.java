package byl.baresylugares.Presentacion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Hashtable;
import java.util.Map;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;

import static android.view.Gravity.CENTER;

public class inicioSesion extends AppCompatActivity {

    private EditText userName;
    private EditText userPsw;
    private String KEY_NOMBRE = "nombre";
    private String KEY_PSW = "psw";
    private String LOGIN_URL = "https://baresylugares.webcindario.com/login.php";
    private Usuario usuario = null;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("LogIn");
        setSupportActionBar(toolbar);
        if (!comprobarConexion()) {
            showToast("Sin conexión a internet");
        }
    }


    public void login(View view) {
        userName = findViewById(R.id.txtUserName);
        userPsw = findViewById(R.id.txtPsw);
        if (userName.getText().length() == 0 && userPsw.getText().length() == 0) {
            showToast("Credenciales incompletas");
        } else {
            final ProgressDialog loading = ProgressDialog.show(this, "Haciendo Login...", "Espere por favor...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("usuarios");
                                    if (jsonArray.length() == 1) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String nombre = jsonObject1.optString("nombre");
                                            String psw = jsonObject1.optString("psw");
                                            String email = jsonObject1.optString("email");
                                            usuario = new Usuario(nombre, psw, email);
                                            inicioSesion();
                                        }
                                    } else {
                                        showToast("Error en el login" + jsonArray.length());
                                    }
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
                            showToast("Error hacer Login Código Error: " + volleyError.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams(){
                    String nombre = userName.getText().toString();
                    String psw = userPsw.getText().toString();
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(KEY_NOMBRE, nombre);
                    params.put(KEY_PSW, psw);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void inicioSesion() {
        Intent intent = new Intent(inicioSesion.this, Menu.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
        finish();
    }


    public void newUser(View view) {
        Intent intent = new Intent(inicioSesion.this, crearUsuario.class);
        startActivity(intent);
        finish();
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(inicioSesion.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }

    public boolean comprobarConexion() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
