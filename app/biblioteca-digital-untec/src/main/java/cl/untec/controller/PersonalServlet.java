package cl.untec.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.untec.dao.LibroDAO;
import cl.untec.dao.PersonalDAO;
import cl.untec.dao.PrestamoDAO;
import cl.untec.model.Libro;
import cl.untec.model.Paginacion;
import cl.untec.model.Prestamo;
import cl.untec.model.PrestamoDetalle;
import cl.untec.model.Resultado;
import cl.untec.util.ValidacionesUtils;

public class PersonalServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String PATH = "/WEB-INF/views/personal.jsp";

    private final PersonalDAO personalDAO = new PersonalDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!esUsuarioPersonal(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        obtenerDatosPersonal(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!esUsuarioPersonal(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String accion = request.getParameter("accion");

        if (!ValidacionesUtils.esTextoValido(accion)) {
            redirigirResultado(
                    request,
                    response,
                    "Acción no especificada.");
            return;
        }

        switch (accion) {
            case "registrarSolicitudPresencial":
                registrarSolicitudPresencial(request, response);
                break;

            case "registrarEntrega":
                registrarEntrega(request, response);
                break;

            case "registrarDevolucion":
                registrarDevolucion(request, response);
                break;

            case "crearLibro":
                crearLibro(request, response);
                break;

            case "actualizarLibro":
                actualizarLibro(request, response);
                break;

            case "eliminarLibro":
                eliminarLibro(request, response);
                break;

            default:
                redirigirResultado(
                        request,
                        response,
                        "Acción no válida.");
                break;
        }
    }

    private void obtenerDatosPersonal(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int paginaSolicitudes = obtenerPagina(request, "paginaSolicitudes");

        int paginaPrestamos = obtenerPagina(request, "paginaPrestamos");

        int paginaAtrasados = obtenerPagina(request, "paginaAtrasados");

        int paginaHistorial = obtenerPagina(request, "paginaHistorial");

        int paginaLibros = obtenerPagina(request, "paginaLibros");

        Resultado<Paginacion<PrestamoDetalle>> resultadoSolicitudes = personalDAO.obtenerSolicitudesPaginadas(
                paginaSolicitudes);

        Resultado<Paginacion<PrestamoDetalle>> resultadoPrestamos = personalDAO.obtenerPrestamosActivosPaginados(
                paginaPrestamos);

        Resultado<Paginacion<PrestamoDetalle>> resultadoAtrasados = personalDAO.obtenerPrestamosAtrasadosPaginados(
                paginaAtrasados);

        Resultado<Paginacion<PrestamoDetalle>> resultadoHistorial = personalDAO.obtenerHistorialPaginado(
                paginaHistorial);

        Resultado<Paginacion<Libro>> resultadoLibros = libroDAO.obtenerLibrosPaginados(
                paginaLibros,
                null,
                null,
                null,
                null,
                null,
                null);

        if (resultadoSolicitudes.isExito()) {
            request.setAttribute(
                    "solicitudes",
                    resultadoSolicitudes.getDatos());
        } else {
            request.setAttribute(
                    "errorSolicitudes",
                    resultadoSolicitudes.getMensaje());
        }

        if (resultadoPrestamos.isExito()) {
            request.setAttribute(
                    "prestamosActivos",
                    resultadoPrestamos.getDatos());
        } else {
            request.setAttribute(
                    "errorPrestamos",
                    resultadoPrestamos.getMensaje());
        }

        if (resultadoAtrasados.isExito()) {
            request.setAttribute(
                    "prestamosAtrasados",
                    resultadoAtrasados.getDatos());
        } else {
            request.setAttribute(
                    "errorAtrasados",
                    resultadoAtrasados.getMensaje());
        }

        if (resultadoHistorial.isExito()) {
            request.setAttribute(
                    "historial",
                    resultadoHistorial.getDatos());
        } else {
            request.setAttribute(
                    "errorHistorial",
                    resultadoHistorial.getMensaje());
        }

        if (resultadoLibros.isExito()) {
            request.setAttribute(
                    "libros",
                    resultadoLibros.getDatos());
        } else {
            request.setAttribute(
                    "errorLibros",
                    resultadoLibros.getMensaje());
        }

        request.getRequestDispatcher(PATH)
                .forward(request, response);
    }

    private void registrarSolicitudPresencial(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int usuarioId = obtenerEntero(request, "usuarioId");

        int libroId = obtenerEntero(request, "libroId");

        if (usuarioId <= 0 || libroId <= 0) {
            redirigirResultado(
                    request,
                    response,
                    "El estudiante y el libro son obligatorios.");
            return;
        }

        LocalDate fechaPrestamo = LocalDate.now();

        LocalDate fechaLimite = fechaPrestamo.plusDays(5);

        Prestamo prestamo = new Prestamo();

        prestamo.setUsuarioId(usuarioId);
        prestamo.setLibroId(libroId);
        prestamo.setFechaPrestamo(
                Date.valueOf(fechaPrestamo));
        prestamo.setFechaLimite(
                Date.valueOf(fechaLimite));
        prestamo.setFechaDevolucion(null);
        prestamo.setEstado("SOLICITADO");

        Resultado<Boolean> resultado = prestamoDAO.crearPrestamo(prestamo);

        redirigirResultado(
                request,
                response,
                obtenerMensajeResultado(
                        resultado,
                        "Solicitud presencial registrada correctamente.",
                        "No fue posible registrar la solicitud presencial."));
    }

    private void registrarEntrega(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int prestamoId = obtenerEntero(request, "prestamoId");

        if (prestamoId <= 0) {
            redirigirResultado(
                    request,
                    response,
                    "El préstamo es obligatorio.");
            return;
        }

        Resultado<Boolean> resultado = personalDAO.registrarEntrega(
                prestamoId);

        redirigirResultado(
                request,
                response,
                obtenerMensajeResultado(
                        resultado,
                        "Entrega registrada correctamente.",
                        "No fue posible registrar la entrega."));
    }

    private void registrarDevolucion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int prestamoId = obtenerEntero(request, "prestamoId");

        String fecha = request.getParameter("fechaDevolucion");

        if (prestamoId <= 0) {
            redirigirResultado(
                    request,
                    response,
                    "El préstamo es obligatorio.");
            return;
        }

        if (!ValidacionesUtils.esTextoValido(fecha)) {
            redirigirResultado(
                    request,
                    response,
                    "La fecha de devolución es obligatoria.");
            return;
        }

        Date fechaDevolucion;

        try {
            fechaDevolucion = Date.valueOf(fecha);
        } catch (IllegalArgumentException e) {
            redirigirResultado(
                    request,
                    response,
                    "La fecha de devolución no es válida.");
            return;
        }

        Resultado<Boolean> resultado = personalDAO.registrarDevolucion(
                prestamoId,
                fechaDevolucion);

        redirigirResultado(
                request,
                response,
                obtenerMensajeResultado(
                        resultado,
                        "Devolución registrada correctamente.",
                        "No fue posible registrar la devolución."));
    }

    private void crearLibro(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Libro libro = obtenerLibroDesdeRequest(request);

        if (libro == null) {
            redirigirResultado(
                    request,
                    response,
                    "Los datos del libro son obligatorios.");
            return;
        }

        Resultado<Boolean> resultado = personalDAO.crearLibro(libro);

        redirigirResultado(
                request,
                response,
                obtenerMensajeResultado(
                        resultado,
                        "Libro creado correctamente.",
                        "No fue posible crear el libro."));
    }

    private void actualizarLibro(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int libroId = obtenerEntero(request, "libroId");

        if (libroId <= 0) {
            redirigirResultado(
                    request,
                    response,
                    "El libro es obligatorio.");
            return;
        }

        Libro libro = obtenerLibroDesdeRequest(request);

        if (libro == null) {
            redirigirResultado(
                    request,
                    response,
                    "Los datos del libro son obligatorios.");
            return;
        }

        libro.setId(libroId);

        Resultado<Boolean> resultado = personalDAO.actualizarLibro(libro);

        redirigirResultado(
                request,
                response,
                obtenerMensajeResultado(
                        resultado,
                        "Libro actualizado correctamente.",
                        "No fue posible actualizar el libro."));
    }

    private void eliminarLibro(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int libroId = obtenerEntero(request, "libroId");

        if (libroId <= 0) {
            redirigirResultado(
                    request,
                    response,
                    "El libro es obligatorio.");
            return;
        }

        Resultado<Boolean> resultado = personalDAO.eliminarLibroPorId(
                libroId);

        redirigirResultado(
                request,
                response,
                obtenerMensajeResultado(
                        resultado,
                        "Libro eliminado correctamente.",
                        "No fue posible eliminar el libro."));
    }

    private Libro obtenerLibroDesdeRequest(
            HttpServletRequest request) {

        String titulo = request.getParameter("titulo");

        String autor = request.getParameter("autor");

        String isbn = request.getParameter("isbn");

        if (!ValidacionesUtils.esTextoValido(titulo)
                || !ValidacionesUtils.esTextoValido(autor)
                || !ValidacionesUtils.esTextoValido(isbn)) {
            return null;
        }

        Libro libro = new Libro();

        libro.setTitulo(titulo.trim());
        libro.setAutor(autor.trim());
        libro.setIsbn(isbn.trim());
        libro.setEditorial(
                obtenerTexto(request, "editorial"));
        libro.setCategoria(
                obtenerTexto(request, "categoria"));

        String anioPublicacion = request.getParameter("anioPublicacion");

        if (ValidacionesUtils.esTextoValido(anioPublicacion)) {
            try {
                libro.setAnioPublicacion(
                        LocalDate.parse(anioPublicacion));
            } catch (Exception e) {
                return null;
            }
        }

        int cantidad = obtenerEntero(request, "cantidad");

        int cantidadDisponible = obtenerEntero(request, "cantidadDisponible");

        if (cantidad < 0 || cantidadDisponible < 0) {
            return null;
        }

        libro.setCantidad(cantidad);
        libro.setCantidadDisponible(
                cantidadDisponible);

        return libro;
    }

    private boolean esUsuarioPersonal(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        Object rol = session.getAttribute("rol");

        return rol != null
                && "PERSONAL".equals(
                        String.valueOf(rol));
    }

    private int obtenerEntero(
            HttpServletRequest request,
            String parametro) {

        String valor = request.getParameter(parametro);

        if (!ValidacionesUtils.esTextoValido(valor)) {
            return 0;
        }

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int obtenerPagina(
            HttpServletRequest request,
            String parametro) {

        int pagina = obtenerEntero(request, parametro);

        return pagina > 0 ? pagina : 1;
    }

    private String obtenerTexto(
            HttpServletRequest request,
            String parametro) {

        String valor = request.getParameter(parametro);

        if (!ValidacionesUtils.esTextoValido(valor)) {
            return null;
        }

        return valor.trim();
    }

    private String obtenerMensajeResultado(
            Resultado<Boolean> resultado,
            String mensajeExito,
            String mensajeError) {

        if (resultado != null && resultado.isExito()) {
            return mensajeExito;
        }

        if (resultado != null
                && ValidacionesUtils.esTextoValido(
                        resultado.getMensaje())) {
            return resultado.getMensaje();
        }

        return mensajeError;
    }

    private void redirigirResultado(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/personal?resultado="
                        + java.net.URLEncoder.encode(
                                mensaje,
                                "UTF-8"));
    }
}