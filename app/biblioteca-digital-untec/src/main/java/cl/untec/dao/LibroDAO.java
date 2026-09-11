package cl.untec.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Libro;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;
import cl.untec.util.ValidacionesUtils;

public class LibroDAO {

    public Resultado<List<Libro>> obtenerLibrosFiltrados(Libro libro) {
        StringBuilder sql = new StringBuilder(
                "SELECT id, titulo, autor, isbn, editorial, "
                        + "anio_publicacion, categoria, cantidad, cantidad_disponible "
                        + "FROM libros "
                        + "WHERE 1 = 1");

        List<Object> parametros = new ArrayList<>();

        if (ValidacionesUtils.esTextoValido(libro.getTitulo())) {
            sql.append(" AND titulo LIKE ?");
            parametros.add("%" + libro.getTitulo().trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(libro.getAutor())) {
            sql.append(" AND autor LIKE ?");
            parametros.add("%" + libro.getAutor().trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(libro.getIsbn())) {
            sql.append(" AND isbn LIKE ?");
            parametros.add("%" + libro.getIsbn().trim() + "%");
        }

        if (ValidacionesUtils.esTextoValido(libro.getEditorial())) {
            sql.append(" AND editorial LIKE ?");
            parametros.add("%" + libro.getEditorial().trim() + "%");
        }

        if (libro.getAnioPublicacion() != null) {
            sql.append(" AND anio_publicacion = ?");
            parametros.add(Date.valueOf(libro.getAnioPublicacion()));
        }

        if (ValidacionesUtils.esTextoValido(libro.getCategoria())) {
            sql.append(" AND categoria LIKE ?");
            parametros.add("%" + libro.getCategoria().trim() + "%");
        }

        sql.append(" ORDER BY titulo ASC");

        List<Libro> libros = new ArrayList<>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                sentencia.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    libros.add(mapearLibro(resultado));
                }
            }

            return new Resultado<>(
                    true,
                    libros,
                    "Libros obtenidos correctamente.",
                    null);
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener los libros.",
                    e.getMessage());
        }
    }

    public Resultado<Libro> obtenerLibroPorId(int libroId) {
        String sql = "SELECT id, titulo, autor, isbn, editorial, "
                + "anio_publicacion, categoria, cantidad, cantidad_disponible "
                + "FROM libros "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, libroId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Resultado<>(
                            true,
                            mapearLibro(resultado),
                            "Libro obtenido correctamente.",
                            null);
                }

                return new Resultado<>(
                        false,
                        null,
                        "No existe un libro con el ID indicado.",
                        null);
            }
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> crearLibro(Libro libro) {
        String sql = "INSERT INTO libros "
                + "(titulo, autor, isbn, editorial, anio_publicacion, "
                + "categoria, cantidad, cantidad_disponible) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                return new Resultado<>(
                        true,
                        true,
                        "Libro creado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible crear el libro.",
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
        String sql = "UPDATE libros SET "
                + "titulo = ?, "
                + "autor = ?, "
                + "isbn = ?, "
                + "editorial = ?, "
                + "anio_publicacion = ?, "
                + "categoria = ?, "
                + "cantidad = ?, "
                + "cantidad_disponible = ? "
                + "WHERE id = ?";

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

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                return new Resultado<>(
                        true,
                        true,
                        "Libro actualizado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No existe un libro con el ID indicado.",
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

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                return new Resultado<>(
                        true,
                        true,
                        "Libro eliminado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No existe un libro con el ID indicado.",
                    null);
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible eliminar el libro.",
                    e.getMessage());
        }
    }

    private Libro mapearLibro(ResultSet resultado) throws SQLException {
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
}