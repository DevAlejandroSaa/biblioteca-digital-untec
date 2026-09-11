package cl.untec.util;

public class ValidacionesUtils {

    public static boolean validarCredenciales(String username, String password) {
        return username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty();
    }

}
