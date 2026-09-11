package cl.untec.util;

public class ValidacionesUtils {

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

}
