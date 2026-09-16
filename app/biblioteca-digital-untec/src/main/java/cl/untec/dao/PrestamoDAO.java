package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Paginacion;
import cl.untec.model.Prestamo;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;
import cl.untec.util.MapeadorRegistro;
import cl.untec.util.PaginacionUtils;
import cl.untec.util.ValidacionesUtils;

public class PrestamoDAO {

    private static final String CAMPOS = "id, usuario_id, libro_id, fecha_prestamo, fecha_limite, fecha_devolucion, estado";

    private final MapeadorRegistro<Prestamo> mapeadorPrestamo = new MapeadorRegistro<Prestamo>() {
        @Override
        public Prestamo mapear(ResultSet resultado) throws SQLException {
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
    };

    public Resultado<Paginacion<Prestamo>> obtenerPrestamosPaginados(int paginaActual, int usuarioId) {
        String condicion = "usuario_id = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(usuarioId);

        PaginacionUtils<Prestamo> paginacion = new PaginacionUtils<>(
                "prestamos",
                CAMPOS,
                mapeadorPrestamo);

        return paginacion.obtenerPaginacion(
                paginaActual,
                condicion,
                parametros,
                "fecha_prestamo DESC");
    }

    public Resultado<Paginacion<Prestamo>> obtenerTodosLosPrestamosPaginados(int paginaActual, String estado) {
        StringBuilder condicion = new StringBuilder();
        List<Object> parametros = new ArrayList<>();

        if (ValidacionesUtils.esTextoValido(estado)) {
            condicion.append("estado = ?");
            parametros.add(estado.trim());
        }

        PaginacionUtils<Prestamo> paginacion = new PaginacionUtils<>(
                "prestamos",
                CAMPOS,
                mapeadorPrestamo);

        return paginacion.obtenerPaginacion(
                paginaActual,
                condicion.toString(),
                parametros,
                "fecha_prestamo DESC");
    }

    public Resultado<Prestamo> obtenerPrestamoPorId(int prestamoId) {
        String sql = "SELECT " + CAMPOS + " FROM prestamos WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, prestamoId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Resultado<>(
                            true,
                            mapeadorPrestamo.mapear(resultado),
                            "Préstamo obtenido correctamente.",
                            null);
                }
            }

            return new Resultado<>(
                    false,
                    null,
                    "No se encontró el préstamo.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener el préstamo.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> crearPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (usuario_id, libro_id, fecha_prestamo, fecha_limite, fecha_devolucion, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, prestamo.getUsuarioId());
            sentencia.setInt(2, prestamo.getLibroId());
            sentencia.setDate(3, prestamo.getFechaPrestamo());
            sentencia.setDate(4, prestamo.getFechaLimite());
            sentencia.setDate(5, prestamo.getFechaDevolucion());
            sentencia.setString(6, prestamo.getEstado());

            int filas = sentencia.executeUpdate();

            return new Resultado<>(
                    filas > 0,
                    filas > 0,
                    filas > 0 ? "Préstamo creado correctamente." : "No fue posible crear el préstamo.",
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
        String sql = "UPDATE prestamos SET usuario_id = ?, libro_id = ?, fecha_prestamo = ?, fecha_limite = ?, fecha_devolucion = ?, estado = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, prestamo.getUsuarioId());
            sentencia.setInt(2, prestamo.getLibroId());
            sentencia.setDate(3, prestamo.getFechaPrestamo());
            sentencia.setDate(4, prestamo.getFechaLimite());
            sentencia.setDate(5, prestamo.getFechaDevolucion());
            sentencia.setString(6, prestamo.getEstado());
            sentencia.setInt(7, prestamo.getId());

            int filas = sentencia.executeUpdate();

            return new Resultado<>(
                    filas > 0,
                    filas > 0,
                    filas > 0 ? "Préstamo actualizado correctamente." : "No se encontró el préstamo.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible actualizar el préstamo.",
                    e.getMessage());
        }
    }
}