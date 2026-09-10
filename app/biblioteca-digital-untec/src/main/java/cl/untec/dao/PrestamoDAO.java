package cl.untec.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Prestamo;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;

public class PrestamoDAO {

    public Resultado<Boolean> crearSolicitud(Prestamo prestamo) {

        if (prestamo == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El préstamo no puede ser nulo.",
                    null);
        }

        if (prestamo.getUsuarioId() <= 0 || prestamo.getLibroId() <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "Los identificadores del usuario y libro no son válidos.",
                    null);
        }

        if (prestamo.getFechaPrestamo() == null
                || prestamo.getFechaLimite() == null) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "Las fechas del préstamo son obligatorias.",
                    null);
        }

        String sql = "INSERT INTO prestamos " +
                "(usuario_id, libro_id, fecha_prestamo, fecha_limite, estado) " +
                "VALUES (?, ?, ?, ?, 'SOLICITADO')";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, prestamo.getUsuarioId());
            statement.setInt(2, prestamo.getLibroId());
            statement.setDate(3, prestamo.getFechaPrestamo());
            statement.setDate(4, prestamo.getFechaLimite());

            int filasInsertadas = statement.executeUpdate();

            if (filasInsertadas > 0) {
                return new Resultado<Boolean>(
                        true,
                        true,
                        "Solicitud creada correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible crear la solicitud.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible crear la solicitud.",
                    e.getMessage());
        }
    }

    public Resultado<Prestamo> buscarPorId(int id) {

        if (id <= 0) {
            return new Resultado<Prestamo>(
                    false,
                    null,
                    "El identificador del préstamo no es válido.",
                    null);
        }

        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, " +
                "fecha_limite, fecha_devolucion, estado " +
                "FROM prestamos WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Prestamo prestamo = new Prestamo();

                    prestamo.setId(rs.getInt("id"));
                    prestamo.setUsuarioId(rs.getInt("usuario_id"));
                    prestamo.setLibroId(rs.getInt("libro_id"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamo.setEstado(rs.getString("estado"));

                    return new Resultado<Prestamo>(
                            true,
                            prestamo,
                            "Préstamo encontrado.",
                            null);
                }

                return new Resultado<Prestamo>(
                        false,
                        null,
                        "Préstamo no encontrado.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<Prestamo>(
                    false,
                    null,
                    "No fue posible buscar el préstamo.",
                    e.getMessage());
        }
    }

    public Resultado<List<Prestamo>> listarPorUsuario(int usuarioId) {

        if (usuarioId <= 0) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "El identificador del usuario no es válido.",
                    null);
        }

        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, " +
                "fecha_limite, fecha_devolucion, estado " +
                "FROM prestamos WHERE usuario_id = ? " +
                "ORDER BY fecha_prestamo DESC";

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, usuarioId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Prestamo prestamo = new Prestamo();

                    prestamo.setId(rs.getInt("id"));
                    prestamo.setUsuarioId(rs.getInt("usuario_id"));
                    prestamo.setLibroId(rs.getInt("libro_id"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamo.setEstado(rs.getString("estado"));

                    prestamos.add(prestamo);
                }
            }

            return new Resultado<List<Prestamo>>(
                    true,
                    prestamos,
                    "Préstamos del usuario consultados correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "No fue posible consultar los préstamos del usuario.",
                    e.getMessage());
        }
    }

    public Resultado<List<Prestamo>> listarPorUsuarioYEstado(
            int usuarioId, String estado) {

        if (usuarioId <= 0) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "El identificador del usuario no es válido.",
                    null);
        }

        if (estado == null || estado.trim().isEmpty()) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "El estado del préstamo es obligatorio.",
                    null);
        }

        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, " +
                "fecha_limite, fecha_devolucion, estado " +
                "FROM prestamos " +
                "WHERE usuario_id = ? AND estado = ? " +
                "ORDER BY fecha_prestamo DESC";

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, usuarioId);
            statement.setString(2, estado);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Prestamo prestamo = new Prestamo();

                    prestamo.setId(rs.getInt("id"));
                    prestamo.setUsuarioId(rs.getInt("usuario_id"));
                    prestamo.setLibroId(rs.getInt("libro_id"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamo.setEstado(rs.getString("estado"));

                    prestamos.add(prestamo);
                }
            }

            return new Resultado<List<Prestamo>>(
                    true,
                    prestamos,
                    "Préstamos consultados correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "No fue posible consultar los préstamos por estado.",
                    e.getMessage());
        }
    }

    public Resultado<List<Prestamo>> listarTodos(
            int pagina, int cantidadPorPagina) {

        if (pagina <= 0 || cantidadPorPagina <= 0) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "Los parámetros de paginación no son válidos.",
                    null);
        }

        int offset = (pagina - 1) * cantidadPorPagina;

        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, " +
                "fecha_limite, fecha_devolucion, estado " +
                "FROM prestamos " +
                "ORDER BY fecha_prestamo DESC " +
                "LIMIT ? OFFSET ?";

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, cantidadPorPagina);
            statement.setInt(2, offset);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Prestamo prestamo = new Prestamo();

                    prestamo.setId(rs.getInt("id"));
                    prestamo.setUsuarioId(rs.getInt("usuario_id"));
                    prestamo.setLibroId(rs.getInt("libro_id"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamo.setEstado(rs.getString("estado"));

                    prestamos.add(prestamo);
                }
            }

            return new Resultado<List<Prestamo>>(
                    true,
                    prestamos,
                    "Préstamos consultados correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "No fue posible listar los préstamos.",
                    e.getMessage());
        }
    }

    public Resultado<List<Prestamo>> listarPorEstado(
            String estado, int pagina, int cantidadPorPagina) {

        if (estado == null || estado.trim().isEmpty()) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "El estado del préstamo es obligatorio.",
                    null);
        }

        if (pagina <= 0 || cantidadPorPagina <= 0) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "Los parámetros de paginación no son válidos.",
                    null);
        }

        int offset = (pagina - 1) * cantidadPorPagina;

        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, " +
                "fecha_limite, fecha_devolucion, estado " +
                "FROM prestamos WHERE estado = ? " +
                "ORDER BY fecha_prestamo DESC LIMIT ? OFFSET ?";

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, estado);
            statement.setInt(2, cantidadPorPagina);
            statement.setInt(3, offset);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Prestamo prestamo = new Prestamo();

                    prestamo.setId(rs.getInt("id"));
                    prestamo.setUsuarioId(rs.getInt("usuario_id"));
                    prestamo.setLibroId(rs.getInt("libro_id"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamo.setEstado(rs.getString("estado"));

                    prestamos.add(prestamo);
                }
            }

            return new Resultado<List<Prestamo>>(
                    true,
                    prestamos,
                    "Préstamos filtrados correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "No fue posible listar los préstamos por estado.",
                    e.getMessage());
        }
    }

    public Resultado<Integer> contarPorEstado(String estado) {

        if (estado == null || estado.trim().isEmpty()) {
            return new Resultado<Integer>(
                    false,
                    null,
                    "El estado del préstamo es obligatorio.",
                    null);
        }

        String sql = "SELECT COUNT(*) FROM prestamos WHERE estado = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, estado);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Resultado<Integer>(
                            true,
                            rs.getInt(1),
                            "Cantidad de préstamos obtenida correctamente.",
                            null);
                }

                return new Resultado<Integer>(
                        true,
                        0,
                        "No se encontraron préstamos.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<Integer>(
                    false,
                    null,
                    "No fue posible contar los préstamos por estado.",
                    e.getMessage());
        }
    }

    public Resultado<List<Prestamo>> listarAtrasados(
            int pagina, int cantidadPorPagina) {

        if (pagina <= 0 || cantidadPorPagina <= 0) {
            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "Los parámetros de paginación no son válidos.",
                    null);
        }

        int offset = (pagina - 1) * cantidadPorPagina;

        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, " +
                "fecha_limite, fecha_devolucion, estado " +
                "FROM prestamos " +
                "WHERE estado = 'PRESTADO' " +
                "AND fecha_limite < CURRENT_DATE " +
                "ORDER BY fecha_limite ASC " +
                "LIMIT ? OFFSET ?";

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, cantidadPorPagina);
            statement.setInt(2, offset);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Prestamo prestamo = new Prestamo();

                    prestamo.setId(rs.getInt("id"));
                    prestamo.setUsuarioId(rs.getInt("usuario_id"));
                    prestamo.setLibroId(rs.getInt("libro_id"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamo.setEstado(rs.getString("estado"));

                    prestamos.add(prestamo);
                }
            }

            return new Resultado<List<Prestamo>>(
                    true,
                    prestamos,
                    "Préstamos atrasados consultados correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<Prestamo>>(
                    false,
                    null,
                    "No fue posible listar los préstamos atrasados.",
                    e.getMessage());
        }
    }

    public Resultado<Integer> contarAtrasados() {

        String sql = "SELECT COUNT(*) FROM prestamos " +
                "WHERE estado = 'PRESTADO' " +
                "AND fecha_limite < CURRENT_DATE";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {

                return new Resultado<Integer>(
                        true,
                        rs.getInt(1),
                        "Cantidad de préstamos atrasados obtenida correctamente.",
                        null);
            }

            return new Resultado<Integer>(
                    true,
                    0,
                    "No se encontraron préstamos atrasados.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Integer>(
                    false,
                    null,
                    "No fue posible contar los préstamos atrasados.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> registrarEntrega(
            int prestamoId, Date fechaPrestamo, Date fechaLimite) {

        if (prestamoId <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del préstamo no es válido.",
                    null);
        }

        if (fechaPrestamo == null || fechaLimite == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "Las fechas de entrega son obligatorias.",
                    null);
        }

        if (fechaLimite.before(fechaPrestamo)) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La fecha límite no puede ser anterior a la fecha de préstamo.",
                    null);
        }

        String sql = "UPDATE prestamos SET " +
                "fecha_prestamo = ?, fecha_limite = ?, estado = 'PRESTADO' " +
                "WHERE id = ? AND estado = 'SOLICITADO'";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(1, fechaPrestamo);
            statement.setDate(2, fechaLimite);
            statement.setInt(3, prestamoId);

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Entrega registrada correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "La solicitud no existe o no se encuentra en estado SOLICITADO.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible registrar la entrega.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> registrarDevolucion(
            int prestamoId, Date fechaDevolucion) {

        if (prestamoId <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del préstamo no es válido.",
                    null);
        }

        if (fechaDevolucion == null) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "La fecha de devolución es obligatoria.",
                    null);
        }

        String sql = "UPDATE prestamos SET " +
                "fecha_devolucion = ?, estado = 'DEVUELTO' " +
                "WHERE id = ? AND estado = 'PRESTADO'";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(1, fechaDevolucion);
            statement.setInt(2, prestamoId);

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Devolución registrada correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "El préstamo no existe o no se encuentra en estado PRESTADO.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible registrar la devolución.",
                    e.getMessage());
        }
    }

}
