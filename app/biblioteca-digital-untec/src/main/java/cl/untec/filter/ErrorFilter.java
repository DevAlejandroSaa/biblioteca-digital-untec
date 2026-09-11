package cl.untec.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            httpRequest.setAttribute("error", "Ocurrió un error al procesar la solicitud.");

            String uri = httpRequest.getRequestURI();
            String contextPath = httpRequest.getContextPath();
            String pagina = uri.substring(contextPath.length());

            httpRequest.getRequestDispatcher(pagina).forward(request, response);
        }
    }
}
