package cl.untec.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Libro;
import cl.untec.model.Paginacion;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;
import cl.untec.util.MapeadorRegistro;
import cl.untec.util.PaginacionUtils;
import cl.untec.util.ValidacionesUtils;

public class LibroDAO {

    private static final String CAMPOS = "id, titulo, autor, isbn, editorial, anio_publicacion, categoria, cantidad, cantidad_disponible";

    private final MapeadorRegistro<Libro> mapeadorLibro = new MapeadorRegistro<Libro>() {
        @Override
        public Libro mapear(ResultSet resultado) throws SQLException {
            Libro libro = new Libro();

            libro.setId(resultado.getInt("id"));
            libro.setTitulo(resultado.getString("titulo"));
            libro.setAutor(resultado.getString("autor"));
            libro.setIsbn(resultado.getString("isbn"));
            libro.setEditorial(resultado.getString("editorial"));

            Date fechaPublicacion = resultado.getDate("anio_publicacion");

            if (fechaPublicacion != null) {
                libro.setAnioPublicacion(fechaPublicacion.toLocalDate());
            }

            libro.setCategoria(resultado.getString("categoria"));
            libro.setCantidad(resultado.getInt("cantidad"));
            libro.setCantidadDisponible(resultado.getInt("cantidad_disponible"));

            return libro;
        }
    };

    public Resultado<Paginacion<Libro>> obtenerLibrosPaginados(int paginaActual, String titulo, String autor,
            String isbn, String editorial, String anioPublicacion, String categoria) {
        StringBuilder condicion = new StringBuilder();
        List<Object> parametros = new ArrayList<>();

        if (ValidacionesUtils.esTextoValido(titulo)) {
            condicion.append("titulo LIKE ?");
            parametros.add("%" + titulo.trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(autor)) {
            agregarCondicion(condicion, "autor LIKE ?");
            parametros.add("%" + autor.trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(isbn)) {
            agregarCondicion(condicion, "isbn LIKE ?");
            parametros.add("%" + isbn.trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(editorial)) {
            agregarCondicion(condicion, "editorial LIKE ?");
            parametros.add("%" + editorial.trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(anioPublicacion)) {
            agregarCondicion(condicion, "anio_publicacion = ?");
            parametros.add(Date.valueOf(anioPublicacion));
        }

        if (ValidacionesUtils.esTextoValido(categoria)) {
            agregarCondicion(condicion, "categoria LIKE ?");
            parametros.add("%" + categoria.trim() + "%");
        }

        PaginacionUtils<Libro> paginacion = new PaginacionUtils<>(
                "libros",
                CAMPOS,
                mapeadorLibro);

        return paginacion.obtenerPaginacion(
                paginaActual,
                condicion.toString(),
                parametros,
                "titulo ASC");
    }

    private void agregarCondicion(StringBuilder condicion, String nuevaCondicion) {
        if (condicion.length() > 0) {
            condicion.append(" AND ");
        }

        condicion.append(nuevaCondicion);
    }

    public Resultado<Libro> obtenerLibroPorId(int libroId) {
        String sql = "SELECT " + CAMPOS + " FROM libros WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, libroId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Resultado<>(
                            true,
                            mapeadorLibro.mapear(resultado),
                            "Libro obtenido correctamente.",
                            null);
                }
            }

            return new Resultado<>(
                    false,
                    null,
                    "No se encontró el libro.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> crearLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, anio_publicacion, categoria, cantidad, cantidad_disponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, libro.getTitulo());
            sentencia.setString(2, libro.getAutor());
            sentencia.setString(3, libro.getIsbn());
            sentencia.setString(4, libro.getEditorial());

            if (libro.getAnioPublicacion() != null) {
                sentencia.setDate(5, Date.valueOf(libro.getAnioPublicacion()));
            } else {
                sentencia.setDate(5, null);
            }

            sentencia.setString(6, libro.getCategoria());
            sentencia.setInt(7, libro.getCantidad());
            sentencia.setInt(8, libro.getCantidadDisponible());

            int filas = sentencia.executeUpdate();

            return new Resultado<>(
                    filas > 0,
                    filas > 0,
                    filas > 0 ? "Libro creado correctamente." : "No fue posible crear el libro.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible crear el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, editorial = ?, anio_publicacion = ?, categoria = ?, cantidad = ?, cantidad_disponible = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, libro.getTitulo());
            sentencia.setString(2, libro.getAutor());
            sentencia.setString(3, libro.getIsbn());
            sentencia.setString(4, libro.getEditorial());

            if (libro.getAnioPublicacion() != null) {
                sentencia.setDate(5, Date.valueOf(libro.getAnioPublicacion()));
            } else {
                sentencia.setDate(5, null);
            }

            sentencia.setString(6, libro.getCategoria());
            sentencia.setInt(7, libro.getCantidad());
            sentencia.setInt(8, libro.getCantidadDisponible());
            sentencia.setInt(9, libro.getId());

            int filas = sentencia.executeUpdate();

            return new Resultado<>(
                    filas > 0,
                    filas > 0,
                    filas > 0 ? "Libro actualizado correctamente." : "No se encontró el libro.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible actualizar el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> eliminarLibroPorId(int libroId) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, libroId);

            int filas = sentencia.executeUpdate();

            return new Resultado<>(
                    filas > 0,
                    filas > 0,
                    filas > 0 ? "Libro eliminado correctamente." : "No se encontró el libro.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible eliminar el libro.",
                    e.getMessage());
        }
    }
}