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
import cl.untec.model.PrestamoDetalle;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;
import cl.untec.util.PaginacionUtils;

public class PersonalDAO {

    private static final String TABLA_PRESTAMOS_DETALLE = "prestamos p "
            + "INNER JOIN usuarios u ON p.usuario_id = u.id "
            + "INNER JOIN libros l ON p.libro_id = l.id";

    private static final String CAMPOS_PRESTAMOS_DETALLE = "p.id, "
            + "p.usuario_id, "
            + "p.libro_id, "
            + "p.fecha_prestamo, "
            + "p.fecha_limite, "
            + "p.fecha_devolucion, "
            + "p.estado, "
            + "u.username, "
            + "u.nombre AS nombre_estudiante, "
            + "u.apellido AS apellido_estudiante, "
            + "u.email AS email_estudiante, "
            + "l.titulo AS titulo_libro, "
            + "l.autor AS autor_libro, "
            + "l.isbn AS isbn_libro, "
            + "l.editorial AS editorial_libro, "
            + "l.anio_publicacion AS anio_publicacion_libro, "
            + "l.categoria AS categoria_libro";

    private static final String ORDEN_PRESTAMOS = "p.fecha_prestamo DESC, p.id DESC";

    public Resultado<Paginacion<PrestamoDetalle>> obtenerSolicitudesPaginadas(
            int paginaActual) {

        PaginacionUtils<PrestamoDetalle> paginacion = new PaginacionUtils<>(
                TABLA_PRESTAMOS_DETALLE,
                CAMPOS_PRESTAMOS_DETALLE,
                this::mapearPrestamoDetalle);

        List<Object> parametros = new ArrayList<>();
        parametros.add("SOLICITADO");

        return paginacion.obtenerPaginacion(
                paginaActual,
                "p.estado = ?",
                parametros,
                ORDEN_PRESTAMOS);
    }

    public Resultado<Boolean> registrarEntrega(int prestamoId) {

        String sql = "UPDATE prestamos "
                + "SET estado = 'PRESTADO' "
                + "WHERE id = ? "
                + "AND estado = 'SOLICITADO'";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, prestamoId);

            int filasActualizadas = sentencia.executeUpdate();

            if (filasActualizadas == 1) {
                return new Resultado<>(
                        true,
                        true,
                        "Entrega registrada correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible registrar la entrega.",
                    "La solicitud no existe o no se encuentra en estado SOLICITADO.");

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible registrar la entrega.",
                    e.getMessage());
        }
    }

    public Resultado<Paginacion<PrestamoDetalle>> obtenerPrestamosActivosPaginados(
            int paginaActual) {

        PaginacionUtils<PrestamoDetalle> paginacion = new PaginacionUtils<>(
                TABLA_PRESTAMOS_DETALLE,
                CAMPOS_PRESTAMOS_DETALLE,
                this::mapearPrestamoDetalle);

        List<Object> parametros = new ArrayList<>();
        parametros.add("PRESTADO");

        return paginacion.obtenerPaginacion(
                paginaActual,
                "p.estado = ?",
                parametros,
                ORDEN_PRESTAMOS);
    }

    public Resultado<Boolean> registrarDevolucion(
            int prestamoId,
            Date fechaDevolucion) {

        if (fechaDevolucion == null) {
            return new Resultado<>(
                    false,
                    false,
                    "La fecha de devolución es obligatoria.",
                    "fechaDevolucion no puede ser null.");
        }

        String sql = "UPDATE prestamos "
                + "SET fecha_devolucion = ?, "
                + "estado = 'DEVUELTO' "
                + "WHERE id = ? "
                + "AND estado = 'PRESTADO'";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setDate(1, fechaDevolucion);
            sentencia.setInt(2, prestamoId);

            int filasActualizadas = sentencia.executeUpdate();

            if (filasActualizadas == 1) {
                return new Resultado<>(
                        true,
                        true,
                        "Devolución registrada correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible registrar la devolución.",
                    "El préstamo no existe o no se encuentra en estado PRESTADO.");

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible registrar la devolución.",
                    e.getMessage());
        }
    }

    public Resultado<Paginacion<PrestamoDetalle>> obtenerPrestamosAtrasadosPaginados(
            int paginaActual) {

        PaginacionUtils<PrestamoDetalle> paginacion = new PaginacionUtils<>(
                TABLA_PRESTAMOS_DETALLE,
                CAMPOS_PRESTAMOS_DETALLE,
                this::mapearPrestamoDetalle);

        List<Object> parametros = new ArrayList<>();
        parametros.add("PRESTADO");

        return paginacion.obtenerPaginacion(
                paginaActual,
                "p.estado = ? AND p.fecha_limite < CURDATE()",
                parametros,
                "p.fecha_limite ASC, p.id DESC");
    }

    public Resultado<Paginacion<PrestamoDetalle>> obtenerHistorialPaginado(
            int paginaActual) {

        PaginacionUtils<PrestamoDetalle> paginacion = new PaginacionUtils<>(
                TABLA_PRESTAMOS_DETALLE,
                CAMPOS_PRESTAMOS_DETALLE,
                this::mapearPrestamoDetalle);

        List<Object> parametros = new ArrayList<>();
        parametros.add("DEVUELTO");

        return paginacion.obtenerPaginacion(
                paginaActual,
                "p.estado = ?",
                parametros,
                "p.fecha_devolucion DESC, p.id DESC");
    }

    public Resultado<Boolean> crearLibro(Libro libro) {

        if (libro == null) {
            return new Resultado<>(
                    false,
                    false,
                    "El libro es obligatorio.",
                    "libro no puede ser null.");
        }

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
                sentencia.setDate(
                        5,
                        Date.valueOf(libro.getAnioPublicacion()));
            } else {
                sentencia.setDate(5, null);
            }

            sentencia.setString(6, libro.getCategoria());
            sentencia.setInt(7, libro.getCantidad());
            sentencia.setInt(8, libro.getCantidadDisponible());

            int filasInsertadas = sentencia.executeUpdate();

            if (filasInsertadas == 1) {
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
                    "No se insertó ningún registro.");

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible crear el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizarLibro(Libro libro) {

        if (libro == null) {
            return new Resultado<>(
                    false,
                    false,
                    "El libro es obligatorio.",
                    "libro no puede ser null.");
        }

        String sql = "UPDATE libros "
                + "SET titulo = ?, "
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
                sentencia.setDate(
                        5,
                        Date.valueOf(libro.getAnioPublicacion()));
            } else {
                sentencia.setDate(5, null);
            }

            sentencia.setString(6, libro.getCategoria());
            sentencia.setInt(7, libro.getCantidad());
            sentencia.setInt(8, libro.getCantidadDisponible());
            sentencia.setInt(9, libro.getId());

            int filasActualizadas = sentencia.executeUpdate();

            if (filasActualizadas == 1) {
                return new Resultado<>(
                        true,
                        true,
                        "Libro actualizado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible actualizar el libro.",
                    "El libro no existe.");

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible actualizar el libro.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> eliminarLibroPorId(int libroId) {

        String sql = "DELETE FROM libros "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, libroId);

            int filasEliminadas = sentencia.executeUpdate();

            if (filasEliminadas == 1) {
                return new Resultado<>(
                        true,
                        true,
                        "Libro eliminado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible eliminar el libro.",
                    "El libro no existe.");

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible eliminar el libro.",
                    e.getMessage());
        }
    }

    private PrestamoDetalle mapearPrestamoDetalle(ResultSet resultado)
            throws SQLException {

        PrestamoDetalle detalle = new PrestamoDetalle();

        detalle.setId(resultado.getInt("id"));
        detalle.setUsuarioId(resultado.getInt("usuario_id"));
        detalle.setLibroId(resultado.getInt("libro_id"));
        detalle.setFechaPrestamo(resultado.getDate("fecha_prestamo"));
        detalle.setFechaLimite(resultado.getDate("fecha_limite"));
        detalle.setFechaDevolucion(resultado.getDate("fecha_devolucion"));
        detalle.setEstado(resultado.getString("estado"));

        detalle.setUsername(resultado.getString("username"));
        detalle.setNombreEstudiante(
                resultado.getString("nombre_estudiante"));
        detalle.setApellidoEstudiante(
                resultado.getString("apellido_estudiante"));
        detalle.setEmailEstudiante(
                resultado.getString("email_estudiante"));

        detalle.setTituloLibro(resultado.getString("titulo_libro"));
        detalle.setAutorLibro(resultado.getString("autor_libro"));
        detalle.setIsbnLibro(resultado.getString("isbn_libro"));
        detalle.setEditorialLibro(
                resultado.getString("editorial_libro"));
        detalle.setCategoriaLibro(
                resultado.getString("categoria_libro"));

        Date anioPublicacion = resultado.getDate(
                "anio_publicacion_libro");

        if (anioPublicacion != null) {
            detalle.setAnioPublicacionLibro(
                    anioPublicacion.toLocalDate());
        }

        return detalle;
    }
}