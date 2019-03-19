package byl.baresylugares.Presentacion;


import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import byl.baresylugares.Dominio.Recomendacion;
import byl.baresylugares.Dominio.Usuario;
import byl.baresylugares.Persistencia.GestorRecomendaciones;
import byl.baresylugares.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private String longitud = "";
    private String latitud = "";
    private Usuario user;
    private String formattedDate;
    private Recomendacion recomendacion = null;
    private String funcion;
    private LatLng latLng;
    private byte[] bytes;
    private GestorRecomendaciones gestorRecomendaciones = new GestorRecomendaciones();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recomendacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Creando Recomendación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = (Usuario) getIntent().getSerializableExtra("Usuario");
        funcion = (String) getIntent().getSerializableExtra("Tipo");

        btnImagen = findViewById(R.id.btnGaleria);
        btnFoto = findViewById(R.id.btnFoto);
        btnGps = findViewById(R.id.btnGps);
        imageView = findViewById(R.id.imgFoto);

        if (!gestorRecomendaciones.conectSQL()) {
            conectar();
        }
        if (funcion.equals("Propia")) {
            recomendacion = (Recomendacion) getIntent().getSerializableExtra("Recom");
            nombre = findViewById(R.id.txtNombreSitio);
            des = findViewById(R.id.txtNombreSitio);
            es = findViewById(R.id.txtEs);
            nombre.setText(recomendacion.getNombreTajeta());
            des.setText(recomendacion.getComentario());
            longitud = recomendacion.getLongitud();
            latitud = recomendacion.getLatitud();
            formattedDate = recomendacion.getFecha();
            image = recomendacion.getImagenBit();
            es.setText(recomendacion.getTipo());

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

    public void conectar() {
        new AlertDialog.Builder(this)
                .setTitle("Conexión BBDD").setMessage("Reintentado comexión con la BBDD")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(crearRecomendacion.this, crearRecomendacion.class);
                        intent.putExtra("Usuario", user);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("cancell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(crearRecomendacion.this, Menu.class);
                        intent.putExtra("Usuario", user);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

    public void GPS() {
        Log.d("Hola", "GPS");
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
                    .setNegativeButton("cancell", new DialogInterface.OnClickListener() {
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
                } else {
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
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
                        });
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
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
                        });
                    }
                }
            }
        }
    }

    public void foto() {
        if (ActivityCompat.checkSelfPermission(crearRecomendacion.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (crearRecomendacion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(crearRecomendacion.this, new String[]{Manifest.permission.CAMERA}, REQUEST_LOCATION);

        } else {
            File path = Environment.getExternalStorageDirectory();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureTime = System.currentTimeMillis();
            pathToFile = path.getAbsolutePath()
                    + "/DCIM/Camera/" + captureTime + ".jpg";
            File foto = new File(pathToFile);
            Log.d("photo", pathToFile);
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

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                imageView.setImageBitmap(bitmap);
                image = BitMapToString(bitmap);
            } catch (Exception e) {
                Log.d("Hola", "Error parsing bitmap");
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_PHOTO) {
            //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = BitmapFactory.decodeFile(pathToFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            imageView.setImageBitmap(bitmap);
            image = BitMapToString(bitmap);
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public byte[] bitmapToBlop(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    public void newRecom(View view) {
        nombre = findViewById(R.id.txtNombreSitio);
        des = findViewById(R.id.txtNombreSitio);

        if (nombre.getText().length() == 0 && des.getText().length() == 0) {
            showToast("Rellene los campos de texto");
        } else {
            if (tipo.equals("")) {
                showToast("Seleccione Bar o Lugar");
            } else {
                if (longitud.equals("") || latitud.equals("")) {
                    showToast("Coordenadas necesarias");
                } else {
                    if (image == null) {
                        showToast("Necesaria una foto del lugar");
                    } else {
                        Calendar cal = Calendar.getInstance();
                        Date fecha = cal.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        formattedDate = dateFormat.format(fecha);
                        recomendacion = new Recomendacion(nombre.getText().toString(),
                                formattedDate, des.getText().toString(), user.getNombre(), latitud,
                                longitud, image, tipo);
                        if (!gestorRecomendaciones.insertRecomendacion(recomendacion)) {
                            showToast("Error al almacenar la recomendación");
                        } else {
                            showToast("Recomendación creada correctamente");
                        }
                    }
                }
            }
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

}
