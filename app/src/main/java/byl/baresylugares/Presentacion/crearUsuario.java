package byl.baresylugares.Presentacion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Map;

import byl.baresylugares.R;

import static android.view.Gravity.CENTER;

public class crearUsuario extends AppCompatActivity {
    private EditText userName;
    private EditText userPsw;
    private EditText userPswII;
    private EditText userEmail;
    private String KEY_NOMBRE = "nombre";
    private String KEY_PSW = "psw";
    private String KEY_EMAIL = "email";
    private String UPLOAD_URL = "https://baresylugares.webcindario.com/createUser.php";

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Crear Usuario");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public void createUser(View view) {
        userName = findViewById(R.id.txtUserName);
        userPsw = findViewById(R.id.txtPsw);
        userPswII = findViewById(R.id.txtPsw2);
        userEmail = findViewById(R.id.txtEmail);
        if (userName.getText().length() == 0 && userPsw.getText().length() == 0 && userPswII.getText().length() == 0 && userEmail.getText().length() == 0) {
            showToast("Rellene todos los campos");
        } else {
            if (!userPsw.getText().toString().equals(userPswII.getText().toString())) {
                showToast("Contraseñas no iguales");
            } else {
                final ProgressDialog loading = ProgressDialog.show(this, "Creando...", "Espere por favor...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                loading.dismiss();
                                showToast(s);
                                Intent intent = new Intent(crearUsuario.this, inicioSesion.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                loading.dismiss();
                                userName.setText("");
                                userEmail.setText("");
                                userPsw.setText("");
                                userPswII.setText("");
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String nombre = userName.getText().toString();
                        String psw = userPsw.getText().toString();
                        String email = userEmail.getText().toString();
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put(KEY_NOMBRE, nombre);
                        params.put(KEY_PSW,psw);
                        params.put(KEY_EMAIL,email);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(crearUsuario.this, inicioSesion.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(crearUsuario.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }
}
