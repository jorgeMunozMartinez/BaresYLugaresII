package byl.baresylugares.Dominio;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String psw;
    private String email;

    public Usuario(String nombre,String psw,String email) {
        this.nombre=nombre;
        this.psw=psw;
        this.email=email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", psw='" + psw + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
