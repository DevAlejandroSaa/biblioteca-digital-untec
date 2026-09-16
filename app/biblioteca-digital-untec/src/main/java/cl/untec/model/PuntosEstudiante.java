package cl.untec.model;

public class PuntosEstudiante {

    private int id;
    private int usuarioId;
    private int puntos;

    public PuntosEstudiante() {
    }

    public PuntosEstudiante(int id, int usuarioId, int puntos) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

}
