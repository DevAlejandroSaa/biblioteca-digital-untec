package cl.untec.model;

public class Resultado<T> {

    private boolean exito;
    private T datos;
    private String mensaje;
    private String error;

    public Resultado(boolean exito, T datos, String mensaje, String error) {
        this.exito = exito;
        this.datos = datos;
        this.mensaje = mensaje;
        this.error = error;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}