package byl.baresylugares.Presentacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;

public class mostarRecomendacion extends AppCompatActivity {

    private Usuario usuario;
    private String tipo;
    private Recomendacion recomendacion;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostar_recomendacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        tipo = (String) getIntent().getSerializableExtra("Tipo");


        recomendacion = (Recomendacion) getIntent().getSerializableExtra("Recomendacion");
        toolbar.setTitle("Mostrando Recomendación");
        EditText nombre = findViewById(R.id.lblFechaLugar);
        EditText des = findViewById(R.id.lblDes);
        nombre.setEnabled(false);
        nombre.setText(recomendacion.getNombreTajeta());
        des.setEnabled(false);
        des.setText(recomendacion.getComentario());
        ImageView imageView1 = findViewById(R.id.ivImg);
        Picasso.with(this).load(recomendacion.getImagenBit()).into(imageView1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(mostarRecomendacion.this, listarRecomendaciones.class);
            intent.putExtra("Usuario", usuario);
            intent.putExtra("Tipo", tipo);
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
