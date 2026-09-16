package cl.untec.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("nombreUsuario") == null) {
            httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/login");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        if (uri.equals(contextPath + "/estudiante")
                && !"ESTUDIANTE".equals(rol)) {

            httpResponse.sendRedirect(
                    contextPath + "/libros");
            return;
        }

        if (uri.equals(contextPath + "/personal")
                && !"PERSONAL".equals(rol)) {

            httpResponse.sendRedirect(
                    contextPath + "/libros");
            return;
        }

        chain.doFilter(request, response);
    }

}
