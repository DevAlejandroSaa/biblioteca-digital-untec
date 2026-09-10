package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.untec.model.PuntosEstudiante;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;

public class PuntosEstudianteDAO {

    public Resultado<PuntosEstudiante> buscarPorUsuario(int usuarioId) {

        String sql = "SELECT id, usuario_id, puntos " +
                "FROM puntos_estudiante WHERE usuario_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, usuarioId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    PuntosEstudiante puntos = new PuntosEstudiante();

                    puntos.setId(rs.getInt("id"));
                    puntos.setUsuarioId(rs.getInt("usuario_id"));
                    puntos.setPuntos(rs.getInt("puntos"));

                    return new Resultado<PuntosEstudiante>(
                            true,
                            puntos,
                            "Puntos del estudiante encontrados.",
                            null);
                }

                return new Resultado<PuntosEstudiante>(
                        false,
                        null,
                        "No se encontraron puntos para el estudiante.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<PuntosEstudiante>(
                    false,
                    null,
                    "No fue posible consultar los puntos del estudiante.",
                    e.getMessage());
        }
    }

    public Resultado<Boolean> actualizarPuntos(int usuarioId, int puntos) {

        if (usuarioId <= 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "El identificador del usuario no es válido.",
                    null);
        }

        if (puntos < 0) {
            return new Resultado<Boolean>(
                    false,
                    false,
                    "Los puntos no pueden ser negativos.",
                    null);
        }

        String sql = "UPDATE puntos_estudiante " +
                "SET puntos = ? WHERE usuario_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, puntos);
            statement.setInt(2, usuarioId);

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas > 0) {

                return new Resultado<Boolean>(
                        true,
                        true,
                        "Puntos actualizados correctamente.",
                        null);
            }

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No se encontró un registro de puntos para el estudiante.",
                    null);

        } catch (SQLException e) {

            return new Resultado<Boolean>(
                    false,
                    false,
                    "No fue posible actualizar los puntos del estudiante.",
                    e.getMessage());
        }
    }

}
