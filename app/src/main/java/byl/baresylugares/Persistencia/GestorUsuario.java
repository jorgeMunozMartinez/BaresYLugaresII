package byl.baresylugares.Persistencia;


import android.util.Log;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import byl.baresylugares.Dominio.Usuario;


public class GestorUsuario {

    private ConnectionClass connectionClass;
    private Connection connection;

    public boolean conectSQL() {
        connectionClass = new ConnectionClass();
        try {
             connection = connectionClass.CONN();
             return true;
        } catch (Exception e) {
            Log.d("Hola", "Error connection" +e.getMessage());
            return false;
        }
    }

    public boolean insertUser(Usuario usuario) {
        try {
            String sql = "INSERT INTO Usuarios VALUES('" + usuario.getNombre() + "', '" + usuario.getPsw() + "', '" + usuario.getEmail() + "')";
            Statement stm = connection.createStatement();
            stm.execute(sql);
            return true;
        } catch (SQLException e) {
            Log.e("Hola", "Insert: " +e.getMessage());
            return false;
        }
    }

    public boolean isUser(String usuario) {
        try {
            String sql = "SELECT * FROM Usuarios WHERE nombre='" + usuario + "'";
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(sql);
            if(res.next()){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            Log.e("Hola", "Select: " +e.getMessage());
            return false;
        }
    }

    public Usuario getUser(String usuario, String psw ) {
        Usuario user = null;
        try {
            String sql = "SELECT * FROM Usuarios WHERE nombre='" + usuario + "' and psw='" + psw + "'";
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()){
                String nombre = res.getString("nombre");
                String pswII = res.getString("psw");
                String email = res.getString("email");
                user = new Usuario(nombre, pswII, email);
            }
            return user;
        } catch (Exception e) {
            Log.e("Hola", "Get User: " +e.getMessage());
            return null;
        }
    }

    public boolean deleteUser(String usuario, String psw ) {
        try {
            String sql = "DELETE FROM Usuarios WHERE nombre='" + usuario + "' and psw='" + psw + "';";
            Statement stm = connection.createStatement();
            stm.execute(sql);
            return true;
        } catch (Exception e) {
            Log.e("Hola", "Delete: " +e.getMessage());
            return false;
        }
    }
}
