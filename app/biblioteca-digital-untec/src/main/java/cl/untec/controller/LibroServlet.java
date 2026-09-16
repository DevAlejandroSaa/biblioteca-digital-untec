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
import cl.untec.dao.PrestamoDAO;
import cl.untec.model.Libro;
import cl.untec.model.Paginacion;
import cl.untec.model.Prestamo;
import cl.untec.model.Resultado;
import cl.untec.util.ValidacionesUtils;

public class LibroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String PATH = "/WEB-INF/views/libros.jsp";

    private final LibroDAO libroDAO = new LibroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String libroId = request.getParameter("libroId");

        if (ValidacionesUtils.esTextoValido(libroId)) {
            obtenerLibro(request, response, libroId);
            return;
        }

        obtenerCatalogo(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        if (!ValidacionesUtils.esTextoValido(accion)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (accion.trim()) {
            case "solicitar":
                solicitarLibro(request, response);
                break;

            case "agregar":
                agregarLibro(request, response);
                break;

            case "editar":
                editarLibro(request, response);
                break;

            case "eliminar":
                eliminarLibro(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }
    }

    private void obtenerCatalogo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String isbn = request.getParameter("isbn");
        String editorial = request.getParameter("editorial");
        String anioPublicacion = request.getParameter("anioPublicacion");
        String categoria = request.getParameter("categoria");
        int paginaActual = obtenerPagina(request);

        Resultado<Paginacion<Libro>> resultado = libroDAO.obtenerLibrosPaginados(
                paginaActual,
                titulo,
                autor,
                isbn,
                editorial,
                anioPublicacion,
                categoria);

        if (!resultado.isExito()) {
            request.setAttribute("error", resultado.getMensaje());
        } else {
            request.setAttribute("paginacion", resultado.getDatos());
        }

        request.setAttribute("titulo", titulo);
        request.setAttribute("autor", autor);
        request.setAttribute("isbn", isbn);
        request.setAttribute("editorial", editorial);
        request.setAttribute("anioPublicacion", anioPublicacion);
        request.setAttribute("categoria", categoria);

        request.getRequestDispatcher(PATH).forward(request, response);
    }

    private void obtenerLibro(HttpServletRequest request, HttpServletResponse response, String libroId)
            throws ServletException, IOException {
        int id = obtenerEntero(libroId);

        if (id <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Resultado<Libro> resultado = libroDAO.obtenerLibroPorId(id);

        if (!resultado.isExito()) {
            request.setAttribute("error", resultado.getMensaje());
        } else {
            request.setAttribute("libro", resultado.getDatos());
        }

        request.getRequestDispatcher(PATH).forward(request, response);
    }

    private void solicitarLibro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rol = obtenerRolSesion(request);
        int usuarioId = obtenerIdUsuarioSesion(request);
        int libroId = obtenerEntero(request.getParameter("libroId"));

        if (!"ESTUDIANTE".equals(rol)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (usuarioId <= 0 || libroId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Resultado<Libro> resultadoLibro = libroDAO.obtenerLibroPorId(libroId);

        if (!resultadoLibro.isExito()) {
            response.sendRedirect(request.getContextPath() + "/libros?error=libro");
            return;
        }

        Libro libro = resultadoLibro.getDatos();

        if (libro.getCantidadDisponible() <= 0) {
            response.sendRedirect(request.getContextPath() + "/libros?error=disponibilidad");
            return;
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuarioId(usuarioId);
        prestamo.setLibroId(libroId);
        prestamo.setFechaPrestamo(Date.valueOf(LocalDate.now()));
        prestamo.setFechaLimite(Date.valueOf(LocalDate.now().plusDays(5)));
        prestamo.setEstado("SOLICITADO");

        Resultado<Boolean> resultado = prestamoDAO.crearPrestamo(prestamo);

        if (resultado.isExito()) {
            response.sendRedirect(request.getContextPath() + "/libros?mensaje=solicitud");
        } else {
            response.sendRedirect(request.getContextPath() + "/libros?error=solicitud");
        }
    }

    private void agregarLibro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"PERSONAL".equals(obtenerRolSesion(request))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Libro libro = obtenerLibroDesdeRequest(request);

        if (libro == null) {
            response.sendRedirect(request.getContextPath() + "/libros?error=datos");
            return;
        }

        Resultado<Boolean> resultado = libroDAO.crearLibro(libro);

        if (resultado.isExito()) {
            response.sendRedirect(request.getContextPath() + "/libros?mensaje=creado");
        } else {
            response.sendRedirect(request.getContextPath() + "/libros?error=crear");
        }
    }

    private void editarLibro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"PERSONAL".equals(obtenerRolSesion(request))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int libroId = obtenerEntero(request.getParameter("libroId"));

        if (libroId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Libro libro = obtenerLibroDesdeRequest(request);

        if (libro == null) {
            response.sendRedirect(request.getContextPath() + "/libros?error=datos");
            return;
        }

        libro.setId(libroId);

        Resultado<Boolean> resultado = libroDAO.actualizarLibro(libro);

        if (resultado.isExito()) {
            response.sendRedirect(request.getContextPath() + "/libros?mensaje=actualizado");
        } else {
            response.sendRedirect(request.getContextPath() + "/libros?error=actualizar");
        }
    }

    private void eliminarLibro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"PERSONAL".equals(obtenerRolSesion(request))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int libroId = obtenerEntero(request.getParameter("libroId"));

        if (libroId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Resultado<Boolean> resultado = libroDAO.eliminarLibroPorId(libroId);

        if (resultado.isExito()) {
            response.sendRedirect(request.getContextPath() + "/libros?mensaje=eliminado");
        } else {
            response.sendRedirect(request.getContextPath() + "/libros?error=eliminar");
        }
    }

    private Libro obtenerLibroDesdeRequest(HttpServletRequest request) {
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String isbn = request.getParameter("isbn");
        String editorial = request.getParameter("editorial");
        String anioPublicacion = request.getParameter("anioPublicacion");
        String categoria = request.getParameter("categoria");
        String cantidad = request.getParameter("cantidad");
        String cantidadDisponible = request.getParameter("cantidadDisponible");

        if (!ValidacionesUtils.esTextoValido(titulo)
                || !ValidacionesUtils.esTextoValido(autor)
                || !ValidacionesUtils.esTextoValido(isbn)
                || !ValidacionesUtils.esTextoValido(cantidad)
                || !ValidacionesUtils.esTextoValido(cantidadDisponible)) {
            return null;
        }

        try {
            int cantidadLibro = Integer.parseInt(cantidad);
            int cantidadDisponibleLibro = Integer.parseInt(cantidadDisponible);

            if (cantidadLibro < 0 || cantidadDisponibleLibro < 0 || cantidadDisponibleLibro > cantidadLibro) {
                return null;
            }

            Libro libro = new Libro();
            libro.setTitulo(titulo.trim());
            libro.setAutor(autor.trim());
            libro.setIsbn(isbn.trim());
            libro.setEditorial(ValidacionesUtils.esTextoValido(editorial) ? editorial.trim() : null);
            libro.setCategoria(ValidacionesUtils.esTextoValido(categoria) ? categoria.trim() : null);
            libro.setCantidad(cantidadLibro);
            libro.setCantidadDisponible(cantidadDisponibleLibro);

            if (ValidacionesUtils.esTextoValido(anioPublicacion)) {
                libro.setAnioPublicacion(LocalDate.parse(anioPublicacion));
            }

            return libro;

        } catch (Exception e) {
            return null;
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

    private String obtenerRolSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object rol = session.getAttribute("rol");

        return rol != null ? String.valueOf(rol) : null;
    }

    private int obtenerPagina(HttpServletRequest request) {
        String pagina = request.getParameter("pagina");

        if (!ValidacionesUtils.esTextoValido(pagina)) {
            return 1;
        }

        try {
            return Integer.parseInt(pagina);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int obtenerEntero(String valor) {
        if (!ValidacionesUtils.esTextoValido(valor)) {
            return 0;
        }

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}