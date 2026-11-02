package com.mycompany.ejercicio_6.modelo;

import com.mycompany.ejercicio_6.modelo.interfaces.Documentable;


public class CertificadorFEL extends IntegracionAPI implements Documentable {

    private final String nitEmisor;

    public CertificadorFEL(String identificador, String nombre, String proveedor,
            String endpoint, EstadoConexion estadoConexion, double latenciaPromedioMs,
            String nitEmisor) {
        super(identificador, nombre, proveedor, endpoint, estadoConexion, latenciaPromedioMs);
        this.nitEmisor = exigirTexto(nitEmisor, "NIT del emisor");
    }

    public String getNitEmisor() {
        return nitEmisor;
    }

    @Override
    public String getTipo() {
        return "Certificador FEL";
    }

    @Override
    protected String getConfiguracionEspecifica() {
        return "NIT emisor de demostración: " + nitEmisor;
    }

    @Override
    public ResultadoOperacion generarDocumento(String referencia) {
        if (!tieneTexto(referencia)) {
            return finalizar("DOCUMENTO", ResultadoOperacion.fallida("La referencia es obligatoria."));
        }
        if (!isConectada()) {
            return finalizar("DOCUMENTO", ResultadoOperacion.fallida("El certificador está desconectado."));
        }
        String xml = "<facturaDemostracion simulada=\"true\">\n"
                + "  <certificador>" + escaparXml(getProveedor()) + "</certificador>\n"
                + "  <nitEmisor>" + escaparXml(nitEmisor) + "</nitEmisor>\n"
                + "  <referencia>" + escaparXml(referencia.trim()) + "</referencia>\n"
                + "</facturaDemostracion>";
        return finalizar("DOCUMENTO", ResultadoOperacion.realizada(
                "Documento de facturación simulado generado, sin validez fiscal.", xml));
    }
}
