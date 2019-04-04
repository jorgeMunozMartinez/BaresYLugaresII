package byl.baresylugares.Presentacion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import byl.baresylugares.R;

import static android.view.Gravity.CENTER;

public class inicio extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inicio);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(inicio.this, inicioSesion.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }

    private void verifyPermissions(){
        Log.d("Hola", "Permisos ");
        String [] permisos = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if(ContextCompat.checkSelfPermission(
                this.getApplicationContext(),permisos[0])== PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(
                        this.getApplicationContext(),permisos[1])== PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(
                        this.getApplicationContext(),permisos[2])== PackageManager.PERMISSION_GRANTED
                ){
            showToast("Permisos correctos");
        }else{
           if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(this)
                        .setTitle("Permiso necesario").setMessage("Permiso necesario para leer imagenes de su memoria")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(inicio.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                            }
                        })
                        .setNegativeButton("cancell", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
           }else{
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
           }
        }
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(inicio.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showToast("Permiso corecto");
            }else{
                showToast("Sin permiso");
            }
        }
    }
}
