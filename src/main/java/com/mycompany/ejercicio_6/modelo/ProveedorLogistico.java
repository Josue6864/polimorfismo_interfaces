package com.mycompany.ejercicio_6.modelo;

import com.mycompany.ejercicio_6.modelo.interfaces.Documentable;


public class ProveedorLogistico extends IntegracionAPI implements Documentable {

    private final String zonaCobertura;

    public ProveedorLogistico(String identificador, String nombre, String proveedor,
            String endpoint, EstadoConexion estadoConexion, double latenciaPromedioMs,
            String zonaCobertura) {
        super(identificador, nombre, proveedor, endpoint, estadoConexion, latenciaPromedioMs);
        this.zonaCobertura = exigirTexto(zonaCobertura, "zona de cobertura");
    }

    public String getZonaCobertura() {
        return zonaCobertura;
    }

    @Override
    public String getTipo() {
        return "Proveedor logístico";
    }

    @Override
    protected String getConfiguracionEspecifica() {
        return "Zona de cobertura: " + zonaCobertura;
    }

    @Override
    public ResultadoOperacion consultarEstado(String referencia) {
        if (!tieneTexto(referencia)) {
            return finalizar("CONSULTA", ResultadoOperacion.fallida("La referencia es obligatoria."));
        }
        if (!isConectada()) {
            return finalizar("CONSULTA", ResultadoOperacion.fallida("El proveedor está desconectado."));
        }
        return finalizar("CONSULTA", ResultadoOperacion.realizada("Envío " + referencia.trim()
                + ": EN TRANSITO (simulado). Proveedor: " + getProveedor()));
    }

    @Override
    public ResultadoOperacion generarDocumento(String referencia) {
        if (!tieneTexto(referencia)) {
            return finalizar("DOCUMENTO", ResultadoOperacion.fallida("La referencia es obligatoria."));
        }
        if (!isConectada()) {
            return finalizar("DOCUMENTO", ResultadoOperacion.fallida("El proveedor está desconectado."));
        }
        String xml = "<guiaEnvio simulada=\"true\">\n"
                + "  <proveedor>" + escaparXml(getProveedor()) + "</proveedor>\n"
                + "  <referencia>" + escaparXml(referencia.trim()) + "</referencia>\n"
                + "  <cobertura>" + escaparXml(zonaCobertura) + "</cobertura>\n"
                + "</guiaEnvio>";
        return finalizar("DOCUMENTO", ResultadoOperacion.realizada("Guía simulada generada.", xml));
    }
}
