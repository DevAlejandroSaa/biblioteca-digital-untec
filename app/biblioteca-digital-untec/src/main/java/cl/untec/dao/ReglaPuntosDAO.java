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

    public Resultado<List<ReglaPuntos>> obtenerTodasLasReglasDePuntos() {
        String sql = "SELECT id, codigo, descripcion, puntos FROM reglas_puntos ORDER BY puntos ASC";

        List<ReglaPuntos> reglas = new ArrayList<>();

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                ReglaPuntos regla = new ReglaPuntos();

                regla.setId(resultado.getInt("id"));
                regla.setCodigo(resultado.getString("codigo"));
                regla.setDescripcion(resultado.getString("descripcion"));
                regla.setPuntos(resultado.getInt("puntos"));

                reglas.add(regla);
            }

            return new Resultado<>(
                    true,
                    reglas,
                    "Reglas de puntos obtenidas correctamente.",
                    null);

        } catch (SQLException e) {

            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener las reglas de puntos.",
                    e.getMessage());
        }
    }

    public Resultado<ReglaPuntos> obtenerReglaPorCodigo(String codigo) {
        String sql = "SELECT id, codigo, descripcion, puntos FROM reglas_puntos WHERE codigo = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    ReglaPuntos regla = new ReglaPuntos();

                    regla.setId(resultado.getInt("id"));
                    regla.setCodigo(resultado.getString("codigo"));
                    regla.setDescripcion(resultado.getString("descripcion"));
                    regla.setPuntos(resultado.getInt("puntos"));

                    return new Resultado<>(
                            true,
                            regla,
                            "Regla de puntos obtenida correctamente.",
                            null);
                }

                return new Resultado<>(
                        false,
                        null,
                        "No existe una regla de puntos con el código indicado.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener la regla de puntos.",
                    e.getMessage());
        }
    }

}