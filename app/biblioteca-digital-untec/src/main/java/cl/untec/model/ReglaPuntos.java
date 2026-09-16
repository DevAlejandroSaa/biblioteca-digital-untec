package cl.untec.model;

public class ReglaPuntos {

    private int id;
    private String codigo;
    private String descripcion;
    private int puntos;

    public ReglaPuntos() {
    }

    public ReglaPuntos(int id, String codigo, String descripcion, int puntos) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

}
