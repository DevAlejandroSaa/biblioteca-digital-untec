package cl.untec.model;

import java.util.List;

public class Paginacion<T> {

    private List<T> registros;
    private int paginaActual;
    private int registrosPorPagina;
    private int totalRegistros;
    private int totalPaginas;
    private boolean primeraPagina;
    private boolean ultimaPagina;
    private boolean tienePaginaAnterior;
    private boolean tienePaginaSiguiente;

    public Paginacion() {
    }

    public Paginacion(List<T> registros, int paginaActual, int registrosPorPagina, int totalRegistros) {
        this.registros = registros;
        this.paginaActual = paginaActual;
        this.registrosPorPagina = registrosPorPagina;
        this.totalRegistros = totalRegistros;
        this.totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        this.primeraPagina = paginaActual <= 1;
        this.ultimaPagina = paginaActual >= totalPaginas;
        this.tienePaginaAnterior = paginaActual > 1;
        this.tienePaginaSiguiente = paginaActual < totalPaginas;
    }

    public List<T> getRegistros() {
        return registros;
    }

    public void setRegistros(List<T> registros) {
        this.registros = registros;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }

    public int getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    public void setRegistrosPorPagina(int registrosPorPagina) {
        this.registrosPorPagina = registrosPorPagina;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public boolean isPrimeraPagina() {
        return primeraPagina;
    }

    public void setPrimeraPagina(boolean primeraPagina) {
        this.primeraPagina = primeraPagina;
    }

    public boolean isUltimaPagina() {
        return ultimaPagina;
    }

    public void setUltimaPagina(boolean ultimaPagina) {
        this.ultimaPagina = ultimaPagina;
    }

    public boolean isTienePaginaAnterior() {
        return tienePaginaAnterior;
    }

    public void setTienePaginaAnterior(boolean tienePaginaAnterior) {
        this.tienePaginaAnterior = tienePaginaAnterior;
    }

    public boolean isTienePaginaSiguiente() {
        return tienePaginaSiguiente;
    }

    public void setTienePaginaSiguiente(boolean tienePaginaSiguiente) {
        this.tienePaginaSiguiente = tienePaginaSiguiente;
    }
}