package byl.baresylugares.Presentacion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.Persistencia.GestorRecomendaciones;
import byl.baresylugares.R;

import static android.view.Gravity.CENTER;

public class listarRecomendaciones extends AppCompatActivity {

    private String tipo;
    private LinearLayout tareasL;
    private GestorRecomendaciones gestorRecomendaciones = new GestorRecomendaciones();
    private ArrayList<Recomendacion> listaR = new ArrayList<Recomendacion>();
    private Usuario usuario;
    private Recomendacion rem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_recomendaciones);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Listando Recomendaciones");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tipo = (String) getIntent().getSerializableExtra("Tipo");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        tareasL = findViewById(R.id.itReco);



        if (!gestorRecomendaciones.conectSQL()) {
            conectar();
        }
        listar();
    }

    public void conectar() {
        new AlertDialog.Builder(this)
                .setTitle("Conexión BBDD").setMessage("Permiso necesario para leer imagenes de su memoria")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(listarRecomendaciones.this, listarRecomendaciones.class);
                        intent.putExtra("Usuario", usuario);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("cancell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(listarRecomendaciones.this, Menu.class);
                        intent.putExtra("Usuario", usuario);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

    private void listar(){
        if (tipo.equals("Bar")) {
            listaR = getRecomendaciones("Bar");
        } else if(tipo.equals("Lugar")) {
            listaR = getRecomendaciones("Lugar");
        } else if(tipo.equals("Propias")){
            listaR = gestorRecomendaciones.listRecomendacionUser(usuario.getNombre());
        }
        comprobar();

    }

    private void comprobar(){
        if (listaR.size() == 0) {
            showToast("No hay recomedaciones de " + tipo);
        } else {
            showToast("Mostrando las recomendaciones de " + tipo);
            tareasL.removeAllViews();
            for (int i =0 ;i<listaR.size();i++){
                rem=listaR.get(i);
                ImageView iv=new ImageView(getApplicationContext());
                iv.setImageResource(R.drawable.ic_right);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) convertToPixel(40), (int) convertToPixel(40));
                iv.setLayoutParams(layoutParams);
                iv.setTag(""+i);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click();
                    }
                });
                LinearLayout mil=new LinearLayout(getApplicationContext());
                if(i==0){
                    mil.setPadding(5, 5, 5, 5);
                }else{
                    mil.setPadding(5, 0, 5, 5);
                }
                mil.setGravity(Gravity.CENTER);
                mil.setBackgroundResource(R.drawable.fondo);
                TextView tv=new TextView(listarRecomendaciones.this);
                tv.setText("Nombre: "+listaR.get(i).getNombreTajeta()+"\n"+"Fecha: "
                +listaR.get(i).getFecha());
                if(i==0){
                    tv.setPadding(0, 2, 2, 2);
                }else{
                    tv.setPadding(0, 0, 2, 2);
                }
                tv.setHeight((int) convertToPixel(60));
                tv.setTextSize(18);
                tv.setWidth((int) convertToPixel(250));
                tv.setGravity(Gravity.CENTER_VERTICAL);
                mil.addView(tv);
                mil.addView(iv);
                tareasL.addView(mil);
            }
        }
    }

    private void click(){
        if(tipo.equals("Propias")){
            Intent intent = new Intent(listarRecomendaciones.this, crearRecomendacion.class);
            intent.putExtra("Usuario", usuario);
            intent.putExtra("Recom", rem);
            intent.putExtra("Tipo", tipo);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(listarRecomendaciones.this, mostarRecomendacion.class);
            intent.putExtra("Usuario", usuario);
            intent.putExtra("Recom", rem);
            intent.putExtra("Tipo", tipo);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<Recomendacion> getRecomendaciones(String tipo) {
        ArrayList<Recomendacion> reco = gestorRecomendaciones.listRecomendacion(tipo);
        return reco;
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
    private float convertToPixel(int n){
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n, r.getDisplayMetrics());
        return px;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(listarRecomendaciones.this, Menu.class);
            intent.putExtra("Usuario", usuario);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
