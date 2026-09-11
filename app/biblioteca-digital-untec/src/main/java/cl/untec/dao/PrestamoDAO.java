package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Prestamo;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;

public class PrestamoDAO {

    public Resultado<List<Prestamo>> obtenerPrestamosPorUsuarioId(int usuarioId) {
        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, "
                + "fecha_limite, fecha_devolucion, estado "
                + "FROM prestamos "
                + "WHERE usuario_id = ? "
                + "ORDER BY fecha_prestamo DESC";

        List<Prestamo> prestamos = new ArrayList<>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, usuarioId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    prestamos.add(mapearPrestamo(resultado));
                }
            }

            return new Resultado<>(
                    true,
                    prestamos,
                    "Préstamos del usuario obtenidos correctamente.",
                    null);
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener los préstamos del usuario.",
                    e.getMessage());
        }
    }

    public Resultado<Prestamo> obtenerPrestamoPorId(int prestamoId) {
        String sql = "SELECT id, usuario_id, libro_id, fecha_prestamo, "
                + "fecha_limite, fecha_devolucion, estado "
                + "FROM prestamos "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, prestamoId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Resultado<>(
                            true,
                            mapearPrestamo(resultado),
                            "Préstamo obtenido correctamente.",
                            null);
                }

                return new Resultado<>(
                        false,
                        null,
                        "No existe un préstamo con el ID indicado.",
                        null);
            }
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener el préstamo.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> crearPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos "
                + "(usuario_id, libro_id, fecha_prestamo, fecha_limite, "
                + "fecha_devolucion, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, prestamo.getUsuarioId());
            sentencia.setInt(2, prestamo.getLibroId());
            sentencia.setDate(3, prestamo.getFechaPrestamo());
            sentencia.setDate(4, prestamo.getFechaLimite());

            if (prestamo.getFechaDevolucion() != null) {
                sentencia.setDate(5, prestamo.getFechaDevolucion());
            } else {
                sentencia.setDate(5, null);
            }

            sentencia.setString(6, prestamo.getEstado());

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                return new Resultado<>(
                        true,
                        true,
                        "Préstamo creado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible crear el préstamo.",
                    null);
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible crear el préstamo.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizarPrestamo(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET "
                + "usuario_id = ?, "
                + "libro_id = ?, "
                + "fecha_prestamo = ?, "
                + "fecha_limite = ?, "
                + "fecha_devolucion = ?, "
                + "estado = ? "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, prestamo.getUsuarioId());
            sentencia.setInt(2, prestamo.getLibroId());
            sentencia.setDate(3, prestamo.getFechaPrestamo());
            sentencia.setDate(4, prestamo.getFechaLimite());

            if (prestamo.getFechaDevolucion() != null) {
                sentencia.setDate(5, prestamo.getFechaDevolucion());
            } else {
                sentencia.setDate(5, null);
            }

            sentencia.setString(6, prestamo.getEstado());
            sentencia.setInt(7, prestamo.getId());

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                return new Resultado<>(
                        true,
                        true,
                        "Préstamo actualizado correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No existe un préstamo con el ID indicado.",
                    null);
        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible actualizar el préstamo.",
                    e.getMessage());
        }
    }

    private Prestamo mapearPrestamo(ResultSet resultado) throws SQLException {
        Prestamo prestamo = new Prestamo();

        prestamo.setId(resultado.getInt("id"));
        prestamo.setUsuarioId(resultado.getInt("usuario_id"));
        prestamo.setLibroId(resultado.getInt("libro_id"));
        prestamo.setFechaPrestamo(resultado.getDate("fecha_prestamo"));
        prestamo.setFechaLimite(resultado.getDate("fecha_limite"));
        prestamo.setFechaDevolucion(resultado.getDate("fecha_devolucion"));
        prestamo.setEstado(resultado.getString("estado"));

        return prestamo;
    }
}