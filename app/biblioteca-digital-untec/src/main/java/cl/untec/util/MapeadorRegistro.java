package cl.untec.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MapeadorRegistro<T> {

    T mapear(ResultSet resultado) throws SQLException;

}
