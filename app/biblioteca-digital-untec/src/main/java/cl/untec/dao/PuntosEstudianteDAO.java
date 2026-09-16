package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;

public class PuntosEstudianteDAO {

    public Resultado<Integer> obtenerPuntosPorUsuarioId(int usuarioId) {
        String sql = "SELECT puntos FROM puntos_estudiante WHERE usuario_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, usuarioId);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return new Resultado<>(
                            true,
                            resultado.getInt("puntos"),
                            "Puntos del estudiante obtenidos correctamente.",
                            null);
                }

                return new Resultado<>(
                        false,
                        null,
                        "No existen puntos registrados para el usuario indicado.",
                        null);
            }

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener los puntos del estudiante.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizarPuntosPorUsuarioId(int usuarioId, int puntos) {
        String sql = "UPDATE puntos_estudiante SET puntos = ? WHERE usuario_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, puntos);
            sentencia.setInt(2, usuarioId);

            int filasActualizadas = sentencia.executeUpdate();

            if (filasActualizadas > 0) {
                return new Resultado<>(
                        true,
                        true,
                        "Puntos del estudiante actualizados correctamente.",
                        null);
            }

            return new Resultado<>(
                    false,
                    false,
                    "No existen puntos registrados para el usuario indicado.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible actualizar los puntos del estudiante.",
                    e.getMessage());
        }
    }

}