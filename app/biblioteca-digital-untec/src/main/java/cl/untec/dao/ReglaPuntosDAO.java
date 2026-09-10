package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.ReglaPuntos;
import cl.untec.model.Resultado;
import cl.untec.util.ConexionDB;

public class ReglaPuntosDAO {

    public Resultado<ReglaPuntos> buscarPorCodigo(String codigo) {

        String sql = "SELECT id, codigo, descripcion, puntos " +
                "FROM reglas_puntos WHERE codigo = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, codigo);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    ReglaPuntos regla = new ReglaPuntos();

                    regla.setId(rs.getInt("id"));
                    regla.setCodigo(rs.getString("codigo"));
                    regla.setDescripcion(rs.getString("descripcion"));
                    regla.setPuntos(rs.getInt("puntos"));

                    return new Resultado<ReglaPuntos>(
                            true,
                            regla,
                            "Regla encontrada.",
                            null);
                }

                return new Resultado<ReglaPuntos>(
                        false,
                        null,
                        "Regla de puntos no encontrada.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<ReglaPuntos>(
                    false,
                    null,
                    "No fue posible buscar la regla de puntos.",
                    e.getMessage());
        }
    }

    public Resultado<List<ReglaPuntos>> listarTodas() {

        String sql = "SELECT id, codigo, descripcion, puntos " +
                "FROM reglas_puntos ORDER BY id";

        List<ReglaPuntos> reglas = new ArrayList<ReglaPuntos>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                ReglaPuntos regla = new ReglaPuntos();

                regla.setId(rs.getInt("id"));
                regla.setCodigo(rs.getString("codigo"));
                regla.setDescripcion(rs.getString("descripcion"));
                regla.setPuntos(rs.getInt("puntos"));

                reglas.add(regla);
            }

            return new Resultado<List<ReglaPuntos>>(
                    true,
                    reglas,
                    "Reglas de puntos consultadas correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<List<ReglaPuntos>>(
                    false,
                    null,
                    "No fue posible listar las reglas de puntos.",
                    e.getMessage());
        }
    }

}
