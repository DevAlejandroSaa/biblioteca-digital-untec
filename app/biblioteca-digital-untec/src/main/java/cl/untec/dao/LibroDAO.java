package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Libro;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;

public class LibroDAO {

    public Resultado<List<Libro>> listar(int pagina, int cantidadPorPagina) {

        if (pagina <= 0 || cantidadPorPagina <= 0) {
            return new Resultado<List<Libro>>(
                    false,
                    null,
                    "Los parámetros de paginación no son válidos.",
                    null);
        }

        int offset = (pagina - 1) * cantidadPorPagina;

        String sql = "SELECT id, titulo, autor, isbn, editorial, " +
                "anio_publicacion, categoria, cantidad, cantidad_disponible " +
                "FROM libros ORDER BY titulo LIMIT ? OFFSET ?";

        List<Libro> libros = new ArrayList<Libro>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, cantidadPorPagina);
            statement.setInt(2, offset);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Libro libro = new Libro();

                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setEditorial(rs.getString("editorial"));
                    libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                    libro.setCategoria(rs.getString("categoria"));
                    libro.setCantidad(rs.getInt("cantidad"));
                    libro.setCantidadDisponible(rs.getInt("cantidad_disponible"));

                    libros.add(libro);
                }
            }

            return new Resultado<List<Libro>>(
                    true,
                    libros,
                    "Libros consultados correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Libro>>(
                    false,
                    null,
                    "No fue posible listar los libros.",
                    e.getMessage());
        }
    }

    public Resultado<List<Libro>> buscar(
            String criterio, int pagina, int cantidadPorPagina) {

        if (pagina <= 0 || cantidadPorPagina <= 0) {
            return new Resultado<List<Libro>>(
                    false,
                    null,
                    "Los parámetros de paginación no son válidos.",
                    null);
        }

        if (criterio == null || criterio.trim().isEmpty()) {
            return listar(pagina, cantidadPorPagina);
        }

        int offset = (pagina - 1) * cantidadPorPagina;

        String sql = "SELECT id, titulo, autor, isbn, editorial, " +
                "anio_publicacion, categoria, cantidad, cantidad_disponible " +
                "FROM libros " +
                "WHERE titulo LIKE ? " +
                "OR autor LIKE ? " +
                "OR isbn LIKE ? " +
                "OR categoria LIKE ? " +
                "ORDER BY titulo LIMIT ? OFFSET ?";

        String busqueda = "%" + criterio.trim() + "%";

        List<Libro> libros = new ArrayList<Libro>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, busqueda);
            statement.setString(2, busqueda);
            statement.setString(3, busqueda);
            statement.setString(4, busqueda);
            statement.setInt(5, cantidadPorPagina);
            statement.setInt(6, offset);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Libro libro = new Libro();

                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setEditorial(rs.getString("editorial"));
                    libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                    libro.setCategoria(rs.getString("categoria"));
                    libro.setCantidad(rs.getInt("cantidad"));
                    libro.setCantidadDisponible(
                            rs.getInt("cantidad_disponible"));

                    libros.add(libro);
                }
            }

            return new Resultado<List<Libro>>(
                    true,
                    libros,
                    "Búsqueda de libros realizada correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Libro>>(
                    false,
                    null,
                    "No fue posible buscar los libros.",
                    e.getMessage());
        }
    }

    public Resultado<Integer> contarResultados(String criterio) {

        String sql;

        if (criterio == null || criterio.trim().isEmpty()) {

            sql = "SELECT COUNT(*) FROM libros";

        } else {

            sql = "SELECT COUNT(*) FROM libros " +
                    "WHERE titulo LIKE ? " +
                    "OR autor LIKE ? " +
                    "OR isbn LIKE ? " +
                    "OR categoria LIKE ?";
        }

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            if (criterio != null && !criterio.trim().isEmpty()) {

                String busqueda = "%" + criterio.trim() + "%";

                statement.setString(1, busqueda);
                statement.setString(2, busqueda);
                statement.setString(3, busqueda);
                statement.setString(4, busqueda);
            }

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Resultado<Integer>(
                            true,
                            rs.getInt(1),
                            "Cantidad de resultados obtenida correctamente.",
                            null);
                }

                return new Resultado<Integer>(
                        true,
                        0,
                        "No se encontraron resultados.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<Integer>(
                    false,
                    null,
                    "No fue posible contar los resultados.",
                    e.getMessage());
        }
    }

    public Resultado<Libro> buscarPorId(int id) {

        if (id <= 0) {
            return new Resultado<Libro>(
                    false,
                    null,
                    "El identificador del libro no es válido.",
                    null);
        }

        String sql = "SELECT id, titulo, autor, isbn, editorial, " +
                "anio_publicacion, categoria, cantidad, cantidad_disponible " +
                "FROM libros WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Libro libro = new Libro();

                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setEditorial(rs.getString("editorial"));
                    libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                    libro.setCategoria(rs.getString("categoria"));
                    libro.setCantidad(rs.getInt("cantidad"));
                    libro.setCantidadDisponible(
                            rs.getInt("cantidad_disponible"));

                    return new Resultado<Libro>(
                            true,
                            libro,
                            "Libro encontrado.",
                            null);
                }

                return new Resultado<Libro>(
                        false,
                        null,
                        "Libro no encontrado.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<Libro>(
                    false,
                    null,
                    "No fue posible buscar el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> crear(Libro libro) {

        if (libro == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no puede ser nulo.",
                    null);
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()
                || libro.getAutor() == null || libro.getAutor().trim().isEmpty()
                || libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()
                || libro.getCantidad() < 1) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "Los datos obligatorios del libro no son válidos.",
                    null);
        }

        if (libro.getCantidadDisponible() < 0
                || libro.getCantidadDisponible() > libro.getCantidad()) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "La cantidad disponible no es válida.",
                    null);
        }

        String sql = "INSERT INTO libros " +
                "(titulo, autor, isbn, editorial, anio_publicacion, " +
                "categoria, cantidad, cantidad_disponible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getAnioPublicacion());
            statement.setString(6, libro.getCategoria());
            statement.setInt(7, libro.getCantidad());
            statement.setInt(8, libro.getCantidadDisponible());

            int filasInsertadas = statement.executeUpdate();

            if (filasInsertadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Libro creado correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible crear el libro.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible crear el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizar(Libro libro) {

        if (libro == null || libro.getId() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El libro no es válido.",
                    null);
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()
                || libro.getAutor() == null || libro.getAutor().trim().isEmpty()
                || libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()
                || libro.getCantidad() < 1) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "Los datos obligatorios del libro no son válidos.",
                    null);
        }

        if (libro.getCantidadDisponible() < 0
                || libro.getCantidadDisponible() > libro.getCantidad()) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "La cantidad disponible no es válida.",
                    null);
        }

        String sql = "UPDATE libros SET " +
                "titulo = ?, autor = ?, isbn = ?, editorial = ?, " +
                "anio_publicacion = ?, categoria = ?, cantidad = ?, " +
                "cantidad_disponible = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getAnioPublicacion());
            statement.setString(6, libro.getCategoria());
            statement.setInt(7, libro.getCantidad());
            statement.setInt(8, libro.getCantidadDisponible());
            statement.setInt(9, libro.getId());

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Libro actualizado correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "Libro no encontrado.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible actualizar el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> eliminar(int id) {

        if (id <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del libro no es válido.",
                    null);
        }

        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            int filasEliminadas = statement.executeUpdate();

            if (filasEliminadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Libro eliminado correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "Libro no encontrado.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible eliminar el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizarDisponibilidad(
            int id, int cantidadDisponible) {

        if (id <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del libro no es válido.",
                    null);
        }

        if (cantidadDisponible < 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La cantidad disponible no puede ser negativa.",
                    null);
        }

        String sql = "UPDATE libros " +
                "SET cantidad_disponible = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, cantidadDisponible);
            statement.setInt(2, id);

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Disponibilidad actualizada correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "Libro no encontrado.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible actualizar la disponibilidad del libro.",
                    e.getMessage());
        }
    }

}
