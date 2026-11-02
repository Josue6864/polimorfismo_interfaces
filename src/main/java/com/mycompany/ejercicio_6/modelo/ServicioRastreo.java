package com.mycompany.ejercicio_6.modelo;


public class ServicioRastreo extends IntegracionAPI {

    private final String tipoTransporte;

    public ServicioRastreo(String identificador, String nombre, String proveedor,
            String endpoint, EstadoConexion estadoConexion, double latenciaPromedioMs,
            String tipoTransporte) {
        super(identificador, nombre, proveedor, endpoint, estadoConexion, latenciaPromedioMs);
        this.tipoTransporte = exigirTexto(tipoTransporte, "tipo de transporte");
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    @Override
    public String getTipo() {
        return "Servicio de rastreo";
    }

    @Override
    protected String getConfiguracionEspecifica() {
        return "Tipo de transporte: " + tipoTransporte;
    }

    @Override
    public ResultadoOperacion consultarEstado(String referencia) {
        if (!tieneTexto(referencia)) {
            return finalizar("CONSULTA", ResultadoOperacion.fallida("La referencia es obligatoria."));
        }
        if (!isConectada()) {
            return finalizar("CONSULTA", ResultadoOperacion.fallida("El rastreador está desconectado."));
        }
        return finalizar("CONSULTA", ResultadoOperacion.realizada("Paquete " + referencia.trim()
                + ": EN CENTRO DE DISTRIBUCION (simulado). Transporte: " + tipoTransporte));
    }
}
