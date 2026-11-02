package com.mycompany.ejercicio_6.modelo;

import com.mycompany.ejercicio_6.modelo.interfaces.Auditable;
import com.mycompany.ejercicio_6.modelo.interfaces.Documentable;
import com.mycompany.ejercicio_6.modelo.interfaces.Transaccional;
import java.math.BigDecimal;
import java.util.Objects;


public final class ServicioOperaciones {

    private final CatalogoIntegraciones catalogo;

    public ServicioOperaciones(CatalogoIntegraciones catalogo) {
        this.catalogo = Objects.requireNonNull(catalogo, "El catálogo es obligatorio.");
    }

    public ResultadoOperacion cobrar(String identificador, BigDecimal monto) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        if (integracion == null) {
            return noEncontrada();
        }
        if (!(integracion instanceof Transaccional)) {
            return omitir(integracion, "COBRO");
        }
        return procesarCobro((Transaccional) integracion, monto);
    }

    public ResultadoOperacion cobrar(String identificador, BigDecimal monto, String referencia) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        if (integracion == null) {
            return noEncontrada();
        }
        if (!(integracion instanceof Transaccional)) {
            return omitir(integracion, "COBRO");
        }
        return procesarCobro((Transaccional) integracion, monto, referencia);
    }

    public ResultadoOperacion reintegrar(String identificador, BigDecimal monto, String referencia) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        if (integracion == null) {
            return noEncontrada();
        }
        if (!(integracion instanceof Transaccional)) {
            return omitir(integracion, "REINTEGRO");
        }
        return procesarReintegro((Transaccional) integracion, monto, referencia);
    }

    public ResultadoOperacion generarDocumento(String identificador, String referencia) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        if (integracion == null) {
            return noEncontrada();
        }
        if (!(integracion instanceof Documentable)) {
            return omitir(integracion, "DOCUMENTO");
        }
        return solicitarDocumento((Documentable) integracion, referencia);
    }

    public ResultadoOperacion consultarEstado(String identificador) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        return integracion == null ? noEncontrada() : integracion.consultarEstado();
    }

    public ResultadoOperacion consultarEstado(String identificador, String referencia) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        return integracion == null ? noEncontrada() : integracion.consultarEstado(referencia);
    }

    public ResultadoOperacion consultarConfiguracion(String identificador) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        return integracion == null ? noEncontrada()
                : ResultadoOperacion.realizada("Configuración encontrada.", integracion.getConfiguracion());
    }

    public ResultadoOperacion consultarAuditoria(String identificador) {
        IntegracionAPI integracion = catalogo.buscarPorIdentificador(identificador);
        return integracion == null ? noEncontrada() : leerAuditoria(integracion);
    }

    // Parametros por interfaz
    private ResultadoOperacion procesarCobro(Transaccional servicio, BigDecimal monto) {
        return servicio.cobrar(monto);
    }

    private ResultadoOperacion procesarCobro(Transaccional servicio, BigDecimal monto,
            String referencia) {
        return servicio.cobrar(monto, referencia);
    }

    private ResultadoOperacion procesarReintegro(Transaccional servicio, BigDecimal monto,
            String referencia) {
        return servicio.reintegrar(monto, referencia);
    }

    private ResultadoOperacion solicitarDocumento(Documentable servicio, String referencia) {
        return servicio.generarDocumento(referencia);
    }

    private ResultadoOperacion leerAuditoria(Auditable servicio) {
        return ResultadoOperacion.realizada("Auditoría local consultada.", servicio.consultarAuditoria());
    }

    private ResultadoOperacion omitir(Auditable servicio, String accion) {
        ResultadoOperacion resultado = ResultadoOperacion.omitida(
                "La integración no admite la operación " + accion + ". El proceso puede continuar.");
        servicio.registrarEvento(accion, resultado.toString());
        return resultado;
    }

    private ResultadoOperacion noEncontrada() {
        return ResultadoOperacion.fallida("No existe una integración con ese identificador.");
    }
}
