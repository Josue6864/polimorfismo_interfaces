package com.mycompany.ejercicio_6.modelo;

import com.mycompany.ejercicio_6.modelo.interfaces.Transaccional;
import java.math.BigDecimal;


public class PasarelaPago extends IntegracionAPI implements Transaccional {

    private final String medioPago;

    public PasarelaPago(String identificador, String nombre, String proveedor,
            String endpoint, EstadoConexion estadoConexion, double latenciaPromedioMs,
            String medioPago) {
        super(identificador, nombre, proveedor, endpoint, estadoConexion, latenciaPromedioMs);
        this.medioPago = exigirTexto(medioPago, "medio de pago");
    }

    public String getMedioPago() {
        return medioPago;
    }

    @Override
    public String getTipo() {
        return "Pasarela de pago";
    }

    @Override
    protected String getConfiguracionEspecifica() {
        return "Medio de pago: " + medioPago;
    }

    @Override
    public ResultadoOperacion cobrar(BigDecimal monto, String referencia) {
        return simularMovimiento("COBRO", monto, referencia);
    }

    @Override
    public ResultadoOperacion reintegrar(BigDecimal monto, String referencia) {
        return simularMovimiento("REINTEGRO", monto, referencia);
    }

    private ResultadoOperacion simularMovimiento(String accion, BigDecimal monto,
            String referencia) {
        if (monto == null || monto.signum() <= 0) {
            return finalizar(accion, ResultadoOperacion.fallida("El monto debe ser positivo."));
        }
        if (!tieneTexto(referencia)) {
            return finalizar(accion, ResultadoOperacion.fallida("La referencia es obligatoria."));
        }
        if (!isConectada()) {
            return finalizar(accion, ResultadoOperacion.fallida("La pasarela está desconectada."));
        }
        return finalizar(accion, ResultadoOperacion.realizada(accion + " SIMULADO por "
                + getNombre() + ". Monto: " + monto.toPlainString()
                + ". Referencia: " + referencia.trim()));
    }
}
