package cl.untec.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.untec.dao.EstudianteDAO;
import cl.untec.dao.LibroDAO;
import cl.untec.model.Libro;
import cl.untec.model.Paginacion;
import cl.untec.model.Prestamo;
import cl.untec.model.Resultado;
import cl.untec.util.ValidacionesUtils;

public class EstudianteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String PATH = "/WEB-INF/views/estudiante.jsp";

    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int usuarioId = obtenerIdUsuarioSesion(request);

        if (usuarioId <= 0) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        obtenerDatosEstudiante(request, response, usuarioId);
    }

    private void obtenerDatosEstudiante(
            HttpServletRequest request,
            HttpServletResponse response,
            int usuarioId) throws ServletException, IOException {

        int paginaSolicitudes = obtenerPagina(request, "paginaSolicitudes");
        int paginaPrestamos = obtenerPagina(request, "paginaPrestamos");
        int paginaHistorial = obtenerPagina(request, "paginaHistorial");

        Resultado<Integer> resultadoPuntos = estudianteDAO.obtenerPuntos(usuarioId);

        Resultado<Paginacion<Prestamo>> resultadoSolicitudes = estudianteDAO.obtenerPrestamosPaginados(
                paginaSolicitudes,
                usuarioId,
                "SOLICITADO");

        Resultado<Paginacion<Prestamo>> resultadoPrestamos = estudianteDAO.obtenerPrestamosPaginados(
                paginaPrestamos,
                usuarioId,
                "PRESTADO");

        Resultado<Paginacion<Prestamo>> resultadoHistorial = estudianteDAO.obtenerPrestamosPaginados(
                paginaHistorial,
                usuarioId,
                null);

        if (resultadoPuntos.isExito()) {
            request.setAttribute("puntos", resultadoPuntos.getDatos());
        } else {
            request.setAttribute("errorPuntos", resultadoPuntos.getMensaje());
        }

        if (resultadoSolicitudes.isExito()) {
            request.setAttribute("solicitudes", resultadoSolicitudes.getDatos());
        } else {
            request.setAttribute("errorSolicitudes", resultadoSolicitudes.getMensaje());
        }

        if (resultadoPrestamos.isExito()) {
            request.setAttribute("prestamosActuales", resultadoPrestamos.getDatos());
        } else {
            request.setAttribute("errorPrestamos", resultadoPrestamos.getMensaje());
        }

        if (resultadoHistorial.isExito()) {
            request.setAttribute("historial", resultadoHistorial.getDatos());
        } else {
            request.setAttribute("errorHistorial", resultadoHistorial.getMensaje());
        }

        Map<Integer, Libro> libros = obtenerLibros(
                resultadoSolicitudes,
                resultadoPrestamos,
                resultadoHistorial);

        request.setAttribute("libros", libros);

        request.getRequestDispatcher(PATH).forward(request, response);
    }

    private Map<Integer, Libro> obtenerLibros(
            Resultado<Paginacion<Prestamo>> resultadoSolicitudes,
            Resultado<Paginacion<Prestamo>> resultadoPrestamos,
            Resultado<Paginacion<Prestamo>> resultadoHistorial) {

        Map<Integer, Libro> libros = new HashMap<>();

        agregarLibros(libros, resultadoSolicitudes);
        agregarLibros(libros, resultadoPrestamos);
        agregarLibros(libros, resultadoHistorial);

        return libros;
    }

    private void agregarLibros(
            Map<Integer, Libro> libros,
            Resultado<Paginacion<Prestamo>> resultado) {

        if (!resultado.isExito()
                || resultado.getDatos() == null
                || resultado.getDatos().getRegistros() == null) {
            return;
        }

        for (Prestamo prestamo : resultado.getDatos().getRegistros()) {
            if (libros.containsKey(prestamo.getLibroId())) {
                continue;
            }

            Resultado<Libro> resultadoLibro = libroDAO.obtenerLibroPorId(prestamo.getLibroId());

            if (resultadoLibro.isExito()) {
                libros.put(
                        prestamo.getLibroId(),
                        resultadoLibro.getDatos());
            }
        }
    }

    private int obtenerIdUsuarioSesion(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return 0;
        }

        Object idUsuario = session.getAttribute("idUsuario");

        if (idUsuario instanceof Integer) {
            return (Integer) idUsuario;
        }

        try {
            return Integer.parseInt(String.valueOf(idUsuario));
        } catch (Exception e) {
            return 0;
        }
    }

    private int obtenerPagina(HttpServletRequest request, String parametro) {

        String pagina = request.getParameter(parametro);

        if (!ValidacionesUtils.esTextoValido(pagina)) {
            return 1;
        }

        try {
            return Integer.parseInt(pagina);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}