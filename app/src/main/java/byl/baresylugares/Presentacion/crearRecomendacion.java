package byl.baresylugares.Presentacion;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static android.view.Gravity.CENTER;

public class crearRecomendacion extends AppCompatActivity {


    private int PICK_IMAGE = 0;
    private int PICK_PHOTO = 1;
    private int REQUEST_LOCATION = 1;
    private TextView es;
    private String tipo = "";
    private EditText nombre;
    private EditText des;
    private String pathToFile;
    private Uri imageUri;
    private long captureTime;
    private ImageView imageView;
    private Button btnImagen;
    private Button btnFoto;
    private Button btnGps;
    private Bitmap bitmap;
    private String image;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String longitud;
    private String latitud;
    private Usuario user;
    private String formattedDate;
    private Recomendacion recomendacion = null;
    private String funcion;
    private String RECOM_URL = "https://baresylugares.webcindario.com/uploadRecomendacion.php";
    private String RECOM_URL_BORRAR = "https://baresylugares.webcindario.com/borrarRecomendacion.php";
    private String KEY_NOMBRE_TARJETA = "nombreTarjeta";
    private String KEY_COMENTARIO = "comentario";
    private String KEY_FECHA = "fecha";
    private String KEY_USUARIO = "usuario";
    private String KEY_TIPO = "tipo";
    private String KEY_LONGITUD = "longitud";
    private String KEY_LATITUD = "latitud";
    private String KEY_IMAGEN = "imagen";
    private String KEY_ID = "id";
    //$actualpath = "https://baresylugares.webcindario.com/ImagenConNombre/$path";

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recomendacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Crear Recomandación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = (Usuario) getIntent().getSerializableExtra("Usuario");
        funcion = (String) getIntent().getSerializableExtra("Tipo");


        btnImagen = findViewById(R.id.btnGaleria);
        btnFoto = findViewById(R.id.btnFoto);
        imageView = findViewById(R.id.imgFoto);
        btnGps = findViewById(R.id.btnGps);

        if (funcion.equals("modificar")) {
            recomendacion = (Recomendacion) getIntent().getSerializableExtra("Recomendacion");
            nombre = findViewById(R.id.txtNombreSitio);
            des = findViewById(R.id.txtComentario);
            es = findViewById(R.id.txtEs);
            nombre.setText(recomendacion.getNombreTajeta());
            des.setText(recomendacion.getComentario());
            longitud = recomendacion.getLongitud();
            latitud = recomendacion.getLatitud();
            formattedDate = recomendacion.getFecha();
            es.setText(recomendacion.getTipo());
            tipo = recomendacion.getTipo();
            Picasso.with(this).load(recomendacion.getImagenBit()).into(imageView);
            image = recomendacion.getImagenBit();
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto();
            }
        });

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPS();
            }
        });


    }

    public void GPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new android.app.AlertDialog.Builder(crearRecomendacion.this)
                    .setTitle("Permiso necesario").setMessage("Encienda su GPS")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(crearRecomendacion.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (crearRecomendacion.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(crearRecomendacion.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                Location location3 = locationManager.getLastKnownLocation(LOCATION_SERVICE);


                if (location != null) {
                    latitud = String.valueOf(location.getLatitude());
                    longitud = String.valueOf(location.getLongitude());
                    showToast("Longitud: " + longitud + ", latitud: " + latitud);
                } else if (location1 != null) {
                    latitud = String.valueOf(location1.getLatitude());
                    longitud = String.valueOf(location1.getLongitude());
                    showToast("Longitud: " + longitud + ", latitud: " + latitud);
                } else if (location2 != null) {
                    latitud = String.valueOf(location2.getLatitude());
                    longitud = String.valueOf(location2.getLongitude());
                    showToast("Longitud: " + longitud + ", latitud: " + latitud);
                } else if (location3 != null) {
                    latitud = String.valueOf(location3.getLatitude());
                    longitud = String.valueOf(location3.getLongitude());
                    showToast("Longitud: " + longitud + ", latitud: " + latitud);
                }
                if (longitud == null && latitud == null) {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            latitud = String.valueOf(location.getLatitude());
                            longitud = String.valueOf(location.getLongitude());
                            showToast("Longitud: " + longitud + ", latitud: " + latitud);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                }
            }
        }
    }

    public void foto() {
        if (ActivityCompat.checkSelfPermission(crearRecomendacion.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (crearRecomendacion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(crearRecomendacion.this, new String[]{Manifest.permission.CAMERA}, REQUEST_LOCATION);

        } else if
        (ActivityCompat.checkSelfPermission(crearRecomendacion.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (crearRecomendacion.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(crearRecomendacion.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

        } else {
            File path = Environment.getExternalStorageDirectory();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureTime = System.currentTimeMillis();
            pathToFile = path.getAbsolutePath()
                    + "/DCIM/Camera/" + captureTime + ".jpg";
            File foto = new File(pathToFile);
            Uri output = Uri.fromFile(foto);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            startActivityForResult(Intent.createChooser(intent, "Hacer Foto"), PICK_PHOTO);
        }

    }

    public void openGallery() {
        if (ActivityCompat.checkSelfPermission(crearRecomendacion.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (crearRecomendacion.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(crearRecomendacion.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (Exception e) {
                Log.d("Hola", "Error parsing bitmap");
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_PHOTO) {
            //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = BitmapFactory.decodeFile(pathToFile);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageView.setImageBitmap(bitmap);
        byte[] imageBytes = stream.toByteArray();
        image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void newRecom(View view) {
        if (funcion.equals("modificar")) {
            borrar();
        }
        nombre = findViewById(R.id.txtNombreSitio);
        des = findViewById(R.id.txtComentario);

        if (nombre.getText().length() == 0 && des.getText().length() == 0) {
            showToast("Rellene los campos de texto");
        } else {
            if (tipo.equals("")) {
                showToast("Seleccione Bar o Lugar");
            } else {
                if (longitud == null || latitud == null) {
                    showToast("Localización necesaria");
                } else {
                    if (image == null) {
                        showToast("Necesaria una foto del lugar");
                    } else {
                        Calendar cal = Calendar.getInstance();
                        Date fecha = cal.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        formattedDate = dateFormat.format(fecha);
                        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECOM_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        loading.dismiss();
                                        showToast(response);
                                        Intent intent = new Intent(crearRecomendacion.this, Menu.class);
                                        intent.putExtra("Usuario", user);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        showToast(error.toString());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new Hashtable<String, String>();
                                params.put(KEY_NOMBRE_TARJETA, nombre.getText().toString());
                                params.put(KEY_COMENTARIO, des.getText().toString());
                                params.put(KEY_FECHA, formattedDate);
                                params.put(KEY_USUARIO, user.getNombre());
                                params.put(KEY_TIPO, tipo);
                                params.put(KEY_LONGITUD, longitud);
                                params.put(KEY_LATITUD, latitud);
                                params.put(KEY_IMAGEN, image);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(stringRequest);
                    }
                }
            }
        }
    }

    public void borrar() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECOM_URL_BORRAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_ID, recomendacion.getid().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(crearRecomendacion.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }

    public void esBar(View view) {
        es = findViewById(R.id.txtEs);
        es.setText("Bar");
        tipo = "bar";
    }

    public void esLugar(View view) {
        es = findViewById(R.id.txtEs);
        es.setText("Lugar");
        tipo = "lugar";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(crearRecomendacion.this, Menu.class);
            intent.putExtra("Usuario", user);
            startActivity(intent);
            finish();
        }else if(item.getItemId() ==R.id.btnSobreAutores){
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