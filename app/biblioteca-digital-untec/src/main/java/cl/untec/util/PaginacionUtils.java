package cl.untec.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.untec.model.Paginacion;
import cl.untec.model.Resultado;

public class PaginacionUtils<T> {

    private int paginaActual;
    private int registrosPorPagina;
    private int totalRegistros;
    private int totalPaginas;
    private boolean primeraPagina;
    private boolean ultimaPagina;
    private boolean tienePaginaAnterior;
    private boolean tienePaginaSiguiente;
    private String table;
    private String campos;
    private String condicion;
    private List<Object> parametros;
    private String orden;
    private MapeadorRegistro<T> mapeador;
    private List<T> registros;

    public PaginacionUtils(String table, String campos, MapeadorRegistro<T> mapeador) {
        this.registrosPorPagina = 10;
        this.table = table;
        this.campos = campos;
        this.mapeador = mapeador;
        this.registros = new ArrayList<>();
    }

    private Resultado<Boolean> totalRegistros(Connection conexion) {
        String sql = "SELECT COUNT(*) FROM " + table;

        if (condicion != null && !condicion.trim().isEmpty()) {
            sql += " WHERE " + condicion;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignarParametros(sentencia);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    this.totalRegistros = resultado.getInt(1);

                    return new Resultado<>(
                            true,
                            true,
                            "Cantidad total de registros obtenida correctamente.",
                            null);
                }
            }

            return new Resultado<>(
                    false,
                    false,
                    "No fue posible obtener la cantidad total de registros.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible obtener la cantidad total de registros.",
                    e.getMessage());
        }
    }

    private void totalPaginas() {
        if (this.totalRegistros == 0) {
            this.totalPaginas = 0;
            return;
        }

        this.totalPaginas = (int) Math.ceil(
                (double) this.totalRegistros / this.registrosPorPagina);
    }

    private void validarPagina(int paginaActual) {
        if (this.totalPaginas == 0) {
            this.paginaActual = 1;
            return;
        }

        if (paginaActual < 1) {
            this.paginaActual = 1;
            return;
        }

        if (paginaActual > this.totalPaginas) {
            this.paginaActual = this.totalPaginas;
            return;
        }

        this.paginaActual = paginaActual;
    }

    private int obtenerOffset() {
        return (this.paginaActual - 1) * this.registrosPorPagina;
    }

    private void calcularEstadoPaginas() {
        this.primeraPagina = this.paginaActual <= 1;
        this.ultimaPagina = this.paginaActual >= this.totalPaginas;
        this.tienePaginaAnterior = this.paginaActual > 1;
        this.tienePaginaSiguiente = this.paginaActual < this.totalPaginas;
    }

    private Resultado<Boolean> obtenerRegistros(Connection conexion) {
        String sql = "SELECT " + campos + " FROM " + table;

        if (condicion != null && !condicion.trim().isEmpty()) {
            sql += " WHERE " + condicion;
        }

        if (orden != null && !orden.trim().isEmpty()) {
            sql += " ORDER BY " + orden;
        }

        sql += " LIMIT ? OFFSET ?";

        this.registros.clear();

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            int indice = asignarParametros(sentencia);

            sentencia.setInt(indice, this.registrosPorPagina);
            sentencia.setInt(indice + 1, obtenerOffset());

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    this.registros.add(mapeador.mapear(resultado));
                }
            }

            return new Resultado<>(
                    true,
                    true,
                    "Registros obtenidos correctamente.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    false,
                    "No fue posible obtener los registros.",
                    e.getMessage());
        }
    }

    private int asignarParametros(PreparedStatement sentencia) throws SQLException {
        int indice = 1;

        if (parametros != null) {
            for (Object parametro : parametros) {
                sentencia.setObject(indice++, parametro);
            }
        }

        return indice;
    }

    public Resultado<Paginacion<T>> obtenerPaginacion(
            int paginaActual,
            String condicion,
            List<Object> parametros,
            String orden) {

        this.condicion = condicion;
        this.parametros = parametros;
        this.orden = orden;

        return obtenerPaginacion(paginaActual);
    }

    public Resultado<Paginacion<T>> obtenerPaginacion(int paginaActual) {
        try (Connection conexion = ConexionDB.obtenerConexion()) {
            Resultado<Boolean> resultadoTotal = totalRegistros(conexion);

            if (!resultadoTotal.isExito()) {
                return new Resultado<>(
                        false,
                        null,
                        resultadoTotal.getMensaje(),
                        resultadoTotal.getError());
            }

            totalPaginas();
            validarPagina(paginaActual);
            calcularEstadoPaginas();

            Resultado<Boolean> resultadoRegistros = obtenerRegistros(conexion);

            if (!resultadoRegistros.isExito()) {
                return new Resultado<>(
                        false,
                        null,
                        resultadoRegistros.getMensaje(),
                        resultadoRegistros.getError());
            }

            Paginacion<T> paginacion = new Paginacion<>(
                    this.registros,
                    this.paginaActual,
                    this.registrosPorPagina,
                    this.totalRegistros);

            paginacion.setTotalPaginas(this.totalPaginas);
            paginacion.setPrimeraPagina(this.primeraPagina);
            paginacion.setUltimaPagina(this.ultimaPagina);
            paginacion.setTienePaginaAnterior(this.tienePaginaAnterior);
            paginacion.setTienePaginaSiguiente(this.tienePaginaSiguiente);

            return new Resultado<>(
                    true,
                    paginacion,
                    "Paginación obtenida correctamente.",
                    null);

        } catch (SQLException e) {
            return new Resultado<>(
                    false,
                    null,
                    "No fue posible obtener la paginación.",
                    e.getMessage());
        }
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public int getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public boolean isPrimeraPagina() {
        return primeraPagina;
    }

    public boolean isUltimaPagina() {
        return ultimaPagina;
    }

    public boolean isTienePaginaAnterior() {
        return tienePaginaAnterior;
    }

    public boolean isTienePaginaSiguiente() {
        return tienePaginaSiguiente;
    }

    public String getTable() {
        return table;
    }

    public String getCampos() {
        return campos;
    }

    public String getCondicion() {
        return condicion;
    }

    public List<Object> getParametros() {
        return parametros;
    }

    public String getOrden() {
        return orden;
    }

    public List<T> getRegistros() {
        return registros;
    }
}