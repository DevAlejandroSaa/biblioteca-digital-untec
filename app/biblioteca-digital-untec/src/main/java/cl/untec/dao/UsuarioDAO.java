package cl.untec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.untec.model.Resultado;
import cl.untec.model.Usuario;
import cl.untec.util.ConexionDB;

public class UsuarioDAO {

    public Resultado<Usuario> autenticar(String username, String password) {

        String sql = "SELECT id, username, password, nombre, apellido, email, rol " +
                "FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setRol(rs.getString("rol"));

                    return new Resultado<Usuario>(
                            true,
                            usuario,
                            "Autenticación exitosa.",
                            null);
                }

                return new Resultado<Usuario>(
                        false,
                        null,
                        "Usuario o contraseña incorrectos.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<Usuario>(
                    false,
                    null,
                    "No fue posible realizar la autenticación.",
                    e.getMessage());
        }
    }

    public Resultado<Usuario> buscarPorId(int id) {

        String sql = "SELECT id, username, password, nombre, apellido, email, rol " +
                "FROM usuarios WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setRol(rs.getString("rol"));

                    return new Resultado<Usuario>(
                            true,
                            usuario,
                            "Usuario encontrado.",
                            null);
                }

                return new Resultado<Usuario>(
                        false,
                        null,
                        "Usuario no encontrado.",
                        null);
            }

        } catch (SQLException e) {

            return new Resultado<Usuario>(
                    false,
                    null,
                    "No fue posible buscar el usuario.",
                    e.getMessage());
        }
    }

}
