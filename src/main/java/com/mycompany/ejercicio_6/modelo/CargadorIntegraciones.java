package com.mycompany.ejercicio_6.modelo;


public final class CargadorIntegraciones {

    private CargadorIntegraciones() {
    }

    public static CatalogoIntegraciones crearCatalogoInicial() {
        CatalogoIntegraciones catalogo = new CatalogoIntegraciones();
        registrar(catalogo, new PasarelaPago("PAG-01", "Tarjeta Demo", "Pago Uno",
                "https://tarjeta.example/api", EstadoConexion.CONECTADA, 120, "Tarjeta de crédito"));
        registrar(catalogo, new PasarelaPago("PAG-02", "Transferencia Demo", "Pago Dos",
                "https://transferencia.example/api", EstadoConexion.CONECTADA, 210, "Transferencia bancaria"));
        registrar(catalogo, new PasarelaPago("PAG-03", "Billetera Demo", "Pago Tres",
                "https://billetera.example/api", EstadoConexion.DESCONECTADA, 95, "Billetera digital"));
        registrar(catalogo, new ProveedorLogistico("LOG-01", "Envío Nacional Demo", "Ruta Uno",
                "https://nacional.example/api", EstadoConexion.CONECTADA, 180, "Nacional"));
        registrar(catalogo, new ProveedorLogistico("LOG-02", "Última Milla Demo", "Ruta Dos",
                "https://ultimamilla.example/api", EstadoConexion.CONECTADA, 80, "Metropolitana"));
        registrar(catalogo, new ServicioRastreo("RAS-01", "Rastreo Terrestre Demo", "Rastreo Uno",
                "https://terrestre.example/api", EstadoConexion.CONECTADA, 60, "Terrestre"));
        registrar(catalogo, new ServicioRastreo("RAS-02", "Rastreo Aéreo Demo", "Rastreo Dos",
                "https://aereo.example/api", EstadoConexion.DESCONECTADA, 250, "Aéreo"));
        registrar(catalogo, new CertificadorFEL("FEL-01", "Facturación Uno Demo", "Certificador Uno",
                "https://facturauno.example/api", EstadoConexion.CONECTADA, 140, "DEMO-001"));
        registrar(catalogo, new CertificadorFEL("FEL-02", "Facturación Dos Demo", "Certificador Dos",
                "https://facturados.example/api", EstadoConexion.CONECTADA, 120, "DEMO-002"));
        registrar(catalogo, new CertificadorFEL("FEL-03", "Facturación Tres Demo", "Certificador Tres",
                "https://facturatres.example/api", EstadoConexion.CONECTADA, 300, "DEMO-003"));
        return catalogo;
    }

    private static void registrar(CatalogoIntegraciones catalogo, IntegracionAPI integracion) {
        ResultadoOperacion resultado = catalogo.agregar(integracion);
        if (!resultado.isRealizada()) {
            throw new IllegalStateException("Error en los datos iniciales: " + resultado.getMensaje());
        }
    }
}
