package byl.baresylugares.Persistencia;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import byl.baresylugares.Dominio.Recomendacion;

public class GestorRecomendaciones {

    private ConnectionClass connectionClass;
    private Connection connection;

    public boolean conectSQL() {
        connectionClass = new ConnectionClass();
        try {
            connection = connectionClass.CONN();
            return true;
        } catch (Exception e) {
            Log.d("Hola", "Error connection" + e.getMessage());
            return false;
        }
    }

    public boolean insertRecomendacion(Recomendacion recomendacion) {
        try {
            String sql = "INSERT INTO Recomendacion VALUES('" + recomendacion.getNombreTajeta()
                    + "', '" + recomendacion.getComentario()
                    + "', '" + recomendacion.getFecha()
                    + "', '" +recomendacion.getUsuario()
                    + "', '" +recomendacion.getTipo()
                    + "', '" +recomendacion.getLongitud()
                    + "', '" +recomendacion.getLatitud()
                    + "', '" +recomendacion.getImagenBit() +"')";
            Statement stm = connection.createStatement();
            stm.execute(sql);
            return true;
        } catch (Exception e) {
            Log.d("Hola", "Error insert Recomendacion: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Recomendacion> listRecomendacion(String tipo) {
        ArrayList<Recomendacion> lista = new ArrayList<Recomendacion>();
        try {
            String sql = "SELECT * FROM Recomendacion WHERE tipo='" + tipo + "'";
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while(res.next()){
                String nombreTarjeta = res.getString("nombreTarjeta");
                String comentario = res.getString("comentario");
                String fecha = res.getString("fecha");
                String usuario = res.getString("usuario");
                String tipoi = res.getString("tipo");
                String longitud = res.getString("longitud");
                String latitud = res.getString("latitud");
                String imagen = res.getString("imagen");
                Recomendacion re = new Recomendacion(nombreTarjeta,
                        fecha,comentario,usuario,latitud,longitud,imagen,tipoi);
                lista.add(re);
            }
        } catch (Exception e) {
            Log.d("Hola", "Error listando recomendaciones: "+e.getMessage());
        }
        return lista;
    }
    public ArrayList<Recomendacion> listRecomendacionUser(String nombre) {
        ArrayList<Recomendacion> lista = new ArrayList<Recomendacion>();
        try {
            String sql = "SELECT * FROM Recomendacion WHERE usuario='" + nombre + "'";
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while(res.next()){
                String nombreTarjeta = res.getString("nombreTarjeta");
                String comentario = res.getString("comentario");
                String fecha = res.getString("fecha");
                String usuario = res.getString("usuario");
                String tipoi = res.getString("tipo");
                String longitud = res.getString("longitud");
                String latitud = res.getString("latitud");
                String imagen = res.getString("imagen");
                Recomendacion re = new Recomendacion(nombreTarjeta,
                        fecha,comentario,usuario,latitud,longitud,imagen,tipoi);
                lista.add(re);
            }
        } catch (Exception e) {
            Log.d("Hola", "Error listando recomendaciones: "+e.getMessage());
        }
        return lista;
    }

    public boolean modifyRecomendacion(Recomendacion recomendacion) {
        return true;
    }

    public boolean deleteRecomendacion(Recomendacion recomendacion) {
        return true;
    }
}
