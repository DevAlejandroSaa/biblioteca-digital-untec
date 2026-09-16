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

public class EstudianteDAO {

    private static final String CAMPOS_PRESTAMO = "id, usuario_id, libro_id, fecha_prestamo, fecha_limite, fecha_devolucion, estado";

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

    public Resultado<Integer> obtenerPuntos(int usuarioId) {
        String sql = "SELECT puntos FROM puntos_estudiante WHERE usuario_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, usuarioId);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return new Resultado<>(
                            true,
                            resultado.getInt("puntos"),
                            "Puntos obtenidos correctamente.",
                            null);
                }
            }

            return new Resultado<>(
                    false,
                    null,
                    "No se encontraron puntos para el estudiante.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener los puntos del estudiante.",
                    e.getMessage());
        }
    }

    public Resultado<Paginacion<Prestamo>> obtenerPrestamosPaginados(int paginaActual, int usuarioId, String estado) {
        try {
            StringBuilder condicion = new StringBuilder("usuario_id = ?");
            List<Object> parametros = new ArrayList<>();
            parametros.add(usuarioId);

            if (ValidacionesUtils.esTextoValido(estado)) {
                condicion.append(" AND estado = ?");
                parametros.add(estado.trim());
            }

            PaginacionUtils<Prestamo> paginacion = new PaginacionUtils<>(
                    "prestamos",
                    CAMPOS_PRESTAMO,
                    mapeadorPrestamo);

            return paginacion.obtenerPaginacion(
                    paginaActual,
                    condicion.toString(),
                    parametros,
                    "fecha_prestamo DESC");

        } catch (Exception e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener los préstamos del estudiante.",
                    e.getMessage());
        }
    }
}