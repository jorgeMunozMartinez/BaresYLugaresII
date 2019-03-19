package byl.baresylugares.Presentacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;

public class mostarRecomendacion extends AppCompatActivity {

    private Usuario usuario;
    private String tipo;

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


        Recomendacion recomendacion = (Recomendacion) getIntent().getSerializableExtra("Recom");
        toolbar.setTitle("Mostrando Recomendación de: " +recomendacion.getUsuario());
        EditText nombre = findViewById(R.id.lblNombre);
        EditText des = findViewById(R.id.lblDes);
        ImageView imageView = findViewById(R.id.ivImg);
        nombre.setEnabled(false);
        nombre.setText(recomendacion.getNombreTajeta());
        des.setEnabled(false);
        des.setText(recomendacion.getComentario());
        Bitmap img = StringToBitMap(recomendacion.getImagenBit());
        imageView.setImageBitmap(img);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(mostarRecomendacion.this, listarRecomendaciones.class);
            intent.putExtra("Usuario", usuario);
            intent.putExtra("Tipo", tipo);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
