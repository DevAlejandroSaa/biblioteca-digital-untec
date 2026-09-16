package cl.untec.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.untec.dao.UsuarioDAO;
import cl.untec.model.Resultado;
import cl.untec.model.Usuario;
import cl.untec.util.ValidacionesUtils;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String PATH = "/WEB-INF/views/login.jsp";

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (ValidacionesUtils.esTextoValido(username) && ValidacionesUtils.esTextoValido(password)) {
            request.setAttribute("error", "Debe ingresar usuario y contraseña.");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        Resultado<Usuario> resultado = this.usuarioDAO.autenticar(username, password);

        if (!resultado.isExito()) {
            request.setAttribute("error", resultado.getMensaje());
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        Usuario usuario = resultado.getDatos();

        HttpSession session = request.getSession();

        session.setAttribute("idUsuario", usuario.getId());
        session.setAttribute("nombreUsuario", usuario.getUsername());
        session.setAttribute("rol", usuario.getRol());

        response.sendRedirect(request.getContextPath() + "/libros");
    }
}