package byl.baresylugares.Presentacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;

public class Menu extends AppCompatActivity {

    private Usuario user = null;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        user = (Usuario) getIntent().getSerializableExtra("Usuario");
        toolbar.setTitle("Menú principal");
    }

    public void listarBares(View view){
        Intent intent = new Intent(Menu.this, listarRecomendaciones.class);
        intent.putExtra("Usuario", user);
        intent.putExtra("Tipo", "bar");
        startActivity(intent);
        finish();
    }

    public void listarLugares(View view){
        Intent intent = new Intent(Menu.this, listarRecomendaciones.class);
        intent.putExtra("Usuario", user);
        intent.putExtra("Tipo", "lugar");
        startActivity(intent);
        finish();
    }

    public void crear(View view){
        Intent intent = new Intent(Menu.this, crearRecomendacion.class);
        intent.putExtra("Usuario", user);
        intent.putExtra("Tipo", "crear");
        startActivity(intent);
        finish();
    }

    public void modificar(View view){
        Intent intent = new Intent(Menu.this, listarRecomendaciones.class);
        intent.putExtra("Usuario", user);
        intent.putExtra("Tipo", "modificar");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(Menu.this, inicioSesion.class);
            startActivity(intent);
            finish();
        } if (item.getItemId() == R.id.btnSobreAutores) {
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
}
