package com.mycompany.ejercicio_6.modelo;

import com.mycompany.ejercicio_6.modelo.interfaces.Auditable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class IntegracionAPI implements Auditable, Comparable<IntegracionAPI> {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String identificador;
    private final String nombre;
    private final String proveedor;
    private final String endpoint;
    private EstadoConexion estadoConexion;
    private double latenciaPromedioMs;
    // Registro textual de eventos: no es otra colección de integraciones.
    private final StringBuilder auditoria = new StringBuilder();

    protected IntegracionAPI(String identificador, String nombre, String proveedor,
            String endpoint, EstadoConexion estadoConexion, double latenciaPromedioMs) {
        this.identificador = exigirTexto(identificador, "identificador");
        this.nombre = exigirTexto(nombre, "nombre");
        this.proveedor = exigirTexto(proveedor, "proveedor");
        this.endpoint = exigirTexto(endpoint, "endpoint");
        this.estadoConexion = Objects.requireNonNull(estadoConexion, "Estado obligatorio.");
        validarLatencia(latenciaPromedioMs);
        this.latenciaPromedioMs = latenciaPromedioMs;
        registrarEvento("CARGA", "Integración creada en modo simulación.");
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProveedor() {
        return proveedor;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public EstadoConexion getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(EstadoConexion estadoConexion) {
        this.estadoConexion = Objects.requireNonNull(estadoConexion, "Estado obligatorio.");
        registrarEvento("CONFIGURACION", "Estado de conexión: " + estadoConexion);
    }

    public double getLatenciaPromedioMs() {
        return latenciaPromedioMs;
    }

    public void setLatenciaPromedioMs(double latenciaPromedioMs) {
        validarLatencia(latenciaPromedioMs);
        this.latenciaPromedioMs = latenciaPromedioMs;
        registrarEvento("CONFIGURACION", "Latencia promedio: " + latenciaPromedioMs + " ms");
    }

    public boolean isConectada() {
        return estadoConexion == EstadoConexion.CONECTADA;
    }

    public abstract String getTipo();

    protected abstract String getConfiguracionEspecifica();

    public String getConfiguracion() {
        return "Identificador: " + identificador
                + "\nNombre: " + nombre
                + "\nProveedor: " + proveedor
                + "\nTipo: " + getTipo()
                + "\nEndpoint: " + endpoint
                + "\nConexión: " + estadoConexion
                + "\nLatencia promedio: " + latenciaPromedioMs + " ms"
                + "\nModo: SIMULACION\n" + getConfiguracionEspecifica();
    }

    public ResultadoOperacion consultarEstado() {
        ResultadoOperacion resultado = isConectada()
                ? ResultadoOperacion.realizada("Servicio disponible (simulado): " + nombre)
                : ResultadoOperacion.fallida("El servicio está desconectado: " + nombre);
        return finalizar("CONSULTA", resultado);
    }

    /** Las subclases de logística especializan esta consulta por referencia. */
    public ResultadoOperacion consultarEstado(String referencia) {
        if (!tieneTexto(referencia)) {
            return finalizar("CONSULTA", ResultadoOperacion.fallida("La referencia es obligatoria."));
        }
        ResultadoOperacion resultado = isConectada()
                ? ResultadoOperacion.realizada("Servicio disponible (simulado): " + nombre
                        + ". Referencia: " + referencia.trim())
                : ResultadoOperacion.fallida("El servicio está desconectado: " + nombre);
        return finalizar("CONSULTA", resultado);
    }

    @Override
    public final void registrarEvento(String accion, String detalle) {
        String accionValidada = exigirTexto(accion, "acción");
        String detalleValidado = exigirTexto(detalle, "detalle");
        auditoria.append(LocalDateTime.now().format(FORMATO_FECHA))
                .append(" | ").append(accionValidada)
                .append(" | ").append(detalleValidado).append('\n');
    }

    @Override
    public final String consultarAuditoria() {
        return auditoria.toString();
    }

    /** Registra un resultado sin depender de la consola ni de JavaFX. */
    protected final ResultadoOperacion finalizar(String accion, ResultadoOperacion resultado) {
        registrarEvento(accion, resultado.getEstado() + ": " + resultado.getMensaje());
        return resultado;
    }

    /** Orden natural: latencia ascendente; identificador como desempate. */
    @Override
    public final int compareTo(IntegracionAPI otra) {
        Objects.requireNonNull(otra, "La integración a comparar es obligatoria.");
        int comparacion = Double.compare(latenciaPromedioMs, otra.latenciaPromedioMs);
        return comparacion != 0 ? comparacion : identificador.compareTo(otra.identificador);
    }

    @Override
    public String toString() {
        return identificador + " | " + nombre + " | " + getTipo() + " | "
                + estadoConexion + " | " + latenciaPromedioMs + " ms";
    }

    protected static boolean tieneTexto(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    protected static String exigirTexto(String valor, String campo) {
        if (!tieneTexto(valor)) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    /** Evita que los datos de una referencia alteren el XML simulado. */
    protected static String escaparXml(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    private static void validarLatencia(double latencia) {
        if (Double.isNaN(latencia) || Double.isInfinite(latencia) || latencia < 0) {
            throw new IllegalArgumentException("La latencia debe ser finita y no negativa.");
        }
    }
}
