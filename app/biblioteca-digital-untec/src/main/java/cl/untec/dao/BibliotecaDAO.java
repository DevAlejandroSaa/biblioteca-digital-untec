package cl.untec.dao;

import java.sql.Date;

import cl.untec.model.Libro;
import cl.untec.model.Prestamo;
import cl.untec.model.PuntosEstudiante;
import cl.untec.model.ReglaPuntos;
import cl.untec.model.Resultado;

public class BibliotecaDAO {

    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;
    private final PuntosEstudianteDAO puntosEstudianteDAO;
    private final ReglaPuntosDAO reglaPuntosDAO;

    public BibliotecaDAO() {
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.puntosEstudianteDAO = new PuntosEstudianteDAO();
        this.reglaPuntosDAO = new ReglaPuntosDAO();
    }

    public Resultado<Boolean> solicitarLibro(Prestamo prestamo) {

        if (prestamo == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La solicitud no puede ser nula.",
                    null);
        }

        if (prestamo.getUsuarioId() <= 0 || prestamo.getLibroId() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "Los identificadores del usuario y libro no son válidos.",
                    null);
        }

        Resultado<Libro> resultadoLibro = libroDAO.buscarPorId(prestamo.getLibroId());

        if (!resultadoLibro.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar el libro.",
                    resultadoLibro.getError());
        }

        Libro libro = resultadoLibro.getDatos();

        if (libro == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no existe.",
                    null);
        }

        if (libro.getCantidadDisponible() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no tiene ejemplares disponibles.",
                    null);
        }

        return prestamoDAO.crearSolicitud(prestamo);
    }

    public Resultado<Boolean> registrarSolicitud(Prestamo prestamo) {

        if (prestamo == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La solicitud no puede ser nula.",
                    null);
        }

        if (prestamo.getUsuarioId() <= 0 || prestamo.getLibroId() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "Los identificadores del usuario y libro no son válidos.",
                    null);
        }

        Resultado<Libro> resultadoLibro = libroDAO.buscarPorId(prestamo.getLibroId());

        if (!resultadoLibro.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar el libro.",
                    resultadoLibro.getError());
        }

        Libro libro = resultadoLibro.getDatos();

        if (libro == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no existe.",
                    null);
        }

        if (libro.getCantidadDisponible() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no tiene ejemplares disponibles.",
                    null);
        }

        return prestamoDAO.crearSolicitud(prestamo);
    }

    public Resultado<Boolean> entregarLibro(
            int prestamoId,
            Date fechaPrestamo,
            Date fechaLimite) {

        if (prestamoId <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del préstamo no es válido.",
                    null);
        }

        if (fechaPrestamo == null || fechaLimite == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "Las fechas del préstamo son obligatorias.",
                    null);
        }

        if (fechaLimite.before(fechaPrestamo)) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La fecha límite no puede ser anterior a la fecha de préstamo.",
                    null);
        }

        Resultado<Prestamo> resultadoPrestamo = prestamoDAO.buscarPorId(prestamoId);

        if (!resultadoPrestamo.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar el préstamo.",
                    resultadoPrestamo.getError());
        }

        Prestamo prestamo = resultadoPrestamo.getDatos();

        if (prestamo == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El préstamo no existe.",
                    null);
        }

        if (!"SOLICITADO".equals(prestamo.getEstado())) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El préstamo no se encuentra en estado SOLICITADO.",
                    null);
        }

        Resultado<Libro> resultadoLibro = libroDAO.buscarPorId(prestamo.getLibroId());

        if (!resultadoLibro.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar el libro.",
                    resultadoLibro.getError());
        }

        Libro libro = resultadoLibro.getDatos();

        if (libro == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro asociado al préstamo no existe.",
                    null);
        }

        if (libro.getCantidadDisponible() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no tiene ejemplares disponibles.",
                    null);
        }

        Resultado<Boolean> resultadoEntrega = prestamoDAO.registrarEntrega(
                prestamoId,
                fechaPrestamo,
                fechaLimite);

        if (!resultadoEntrega.isExito()) {
            return resultadoEntrega;
        }

        Resultado<Boolean> resultadoDisponibilidad = libroDAO.actualizarDisponibilidad(
                libro.getId(),
                libro.getCantidadDisponible() - 1);

        if (!resultadoDisponibilidad.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible actualizar la disponibilidad del libro.",
                    resultadoDisponibilidad.getError());
        }

        return new Resultado<Boolean>(
                true,
                true,
                "Libro entregado correctamente.",
                null);
    }

    public Resultado<Boolean> registrarDevolucion(
            int prestamoId,
            Date fechaDevolucion) {

        if (prestamoId <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del préstamo no es válido.",
                    null);
        }

        if (fechaDevolucion == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La fecha de devolución es obligatoria.",
                    null);
        }

        Resultado<Prestamo> resultadoPrestamo = prestamoDAO.buscarPorId(prestamoId);

        if (!resultadoPrestamo.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar el préstamo.",
                    resultadoPrestamo.getError());
        }

        Prestamo prestamo = resultadoPrestamo.getDatos();

        if (prestamo == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El préstamo no existe.",
                    null);
        }

        if (!"PRESTADO".equals(prestamo.getEstado())) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El préstamo no se encuentra en estado PRESTADO.",
                    null);
        }

        if (fechaDevolucion.before(prestamo.getFechaPrestamo())) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La fecha de devolución no puede ser anterior a la fecha de préstamo.",
                    null);
        }

        String codigoRegla;

        if (fechaDevolucion.before(prestamo.getFechaLimite())
                || fechaDevolucion.equals(prestamo.getFechaLimite())) {

            codigoRegla = "DEVOLUCION_A_TIEMPO";

        } else {

            codigoRegla = "DEVOLUCION_ATRASADA";
        }

        Resultado<ReglaPuntos> resultadoRegla = reglaPuntosDAO.buscarPorCodigo(codigoRegla);

        if (!resultadoRegla.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar la regla de puntos.",
                    resultadoRegla.getError());
        }

        ReglaPuntos regla = resultadoRegla.getDatos();

        if (regla == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La regla de puntos no existe.",
                    null);
        }

        Resultado<PuntosEstudiante> resultadoPuntos = puntosEstudianteDAO.buscarPorUsuario(
                prestamo.getUsuarioId());

        if (!resultadoPuntos.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar los puntos del estudiante.",
                    resultadoPuntos.getError());
        }

        PuntosEstudiante puntosEstudiante = resultadoPuntos.getDatos();

        if (puntosEstudiante == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No existen puntos registrados para el estudiante.",
                    null);
        }

        int nuevosPuntos = puntosEstudiante.getPuntos() + regla.getPuntos();

        if (nuevosPuntos < 0) {
            nuevosPuntos = 0;
        }

        Resultado<Boolean> resultadoPuntosActualizados = puntosEstudianteDAO.actualizarPuntos(
                prestamo.getUsuarioId(),
                nuevosPuntos);

        if (!resultadoPuntosActualizados.isExito()) {
            return resultadoPuntosActualizados;
        }

        Resultado<Boolean> resultadoDevolucion = prestamoDAO.registrarDevolucion(
                prestamoId,
                fechaDevolucion);

        if (!resultadoDevolucion.isExito()) {
            return resultadoDevolucion;
        }

        Resultado<Libro> resultadoLibro = libroDAO.buscarPorId(prestamo.getLibroId());

        if (!resultadoLibro.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible consultar el libro.",
                    resultadoLibro.getError());
        }

        Libro libro = resultadoLibro.getDatos();

        if (libro == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro asociado al préstamo no existe.",
                    null);
        }

        Resultado<Boolean> resultadoDisponibilidad = libroDAO.actualizarDisponibilidad(
                libro.getId(),
                libro.getCantidadDisponible() + 1);

        if (!resultadoDisponibilidad.isExito()) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible actualizar la disponibilidad del libro.",
                    resultadoDisponibilidad.getError());
        }

        return new Resultado<Boolean>(
                true,
                true,
                "Devolución registrada correctamente.",
                null);
    }
}