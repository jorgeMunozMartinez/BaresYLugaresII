package byl.baresylugares.Presentacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;

public class Menu extends AppCompatActivity {

    private Usuario user = null;

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
        toolbar.setTitle("Menú principal "+user.getNombre());
    }

    public void listarBares(View view){
        Intent intent = new Intent(Menu.this, listarRecomendaciones.class);
        intent.putExtra("Usuario", user);
        intent.putExtra("Tipo", "Bar");
        startActivity(intent);
        finish();
    }

    public void listarLugares(View view){
        Intent intent = new Intent(Menu.this, listarRecomendaciones.class);
        intent.putExtra("Usuario", user);
        intent.putExtra("Tipo", "Lugar");
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
        intent.putExtra("Tipo", "Propias");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(Menu.this, Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
