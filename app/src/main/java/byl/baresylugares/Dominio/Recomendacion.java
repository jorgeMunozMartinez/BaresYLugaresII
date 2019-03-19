package byl.baresylugares.Dominio;



import java.io.Serializable;

public class Recomendacion implements Serializable {
    private String nombreTajeta;
    private String fecha;
    private String comentario;
    private String usuario;
    private String latitud;
    private String longitud;
    private String imagenBit;
    private String tipo;

    public Recomendacion(String nombreTajeta, String fecha, String comentario, String usuario, String latitud, String longitud, String imagenBit, String tipo) {
        this.nombreTajeta = nombreTajeta;
        this.fecha = fecha;
        this.comentario = comentario;
        this.usuario = usuario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagenBit = imagenBit;
        this.tipo = tipo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getImagenBit() {
        return imagenBit;
    }

    public void setImagenBit(String imagenBit) {
        this.imagenBit = imagenBit;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreTajeta() {
        return nombreTajeta;
    }

    public void setNombreTajeta(String nombreTajeta) {
        this.nombreTajeta = nombreTajeta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Recomendacion{" +
                "nombreTajeta='" + nombreTajeta + '\'' +
                ", fecha='" + fecha + '\'' +
                ", comentario='" + comentario + '\'' +
                ", usuario='" + usuario + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", imagenBit='" + imagenBit + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
