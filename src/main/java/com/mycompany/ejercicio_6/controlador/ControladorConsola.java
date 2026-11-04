package com.mycompany.ejercicio_6.controlador;

import com.mycompany.ejercicio_6.modelo.CatalogoIntegraciones;
import com.mycompany.ejercicio_6.modelo.IntegracionAPI;
import com.mycompany.ejercicio_6.modelo.ResultadoOperacion;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
  Controlador de la interacción por consola. Recibe el modelo del arranque;
  no carga integraciones ni mantiene otra lista. Adapta las entradas de texto y
  devuelve datos/resultados para que VistaConsola los presente.
  El ciclo del menú se conectará al implementar la vista de consola.
 */
public final class ControladorConsola {

    private final CatalogoIntegraciones catalogo;
    private final ServicioOperaciones servicio;

    public ControladorConsola(CatalogoIntegraciones catalogo, ServicioOperaciones servicio) {
        this.catalogo = Objects.requireNonNull(catalogo, "El catálogo es obligatorio.");
        this.servicio = Objects.requireNonNull(servicio, "El servicio es obligatorio.");
    }

    /** Retorna la vista de lectura sobre la unica lista del modelo. */
    public List<IntegracionAPI> listarIntegraciones() {
        return catalogo.listarIntegraciones();
    }

    public int getCantidadIntegraciones() {
        return catalogo.getCantidad();
    }

    public ResultadoOperacion buscarIntegracion(String identificador) {
        return servicio.consultarConfiguracion(identificador);
    }

    public ResultadoOperacion ordenarPorLatencia() {
        return catalogo.ordenarPorLatencia();
    }

    public ResultadoOperacion consultarEstado(String identificador) {
        return servicio.consultarEstado(identificador);
    }

    /** Una referencia vacía solicita el estado general del servicio. */
    public ResultadoOperacion consultarEstado(String identificador, String referencia) {
        return tieneTexto(referencia)
                ? servicio.consultarEstado(identificador, referencia)
                : consultarEstado(identificador);
    }

    public ResultadoOperacion cobrar(String identificador, String montoTexto) {
        return cobrar(identificador, montoTexto, "");
    }

    /** Una referencia vacía utiliza la sobrecarga sencilla del contrato de cobro. */
    public ResultadoOperacion cobrar(String identificador, String montoTexto, String referencia) {
        BigDecimal monto = convertirMonto(montoTexto);
        if (monto == null) {
            return montoInvalido();
        }
        return tieneTexto(referencia)
                ? servicio.cobrar(identificador, monto, referencia)
                : servicio.cobrar(identificador, monto);
    }

    public ResultadoOperacion reintegrar(String identificador, String montoTexto,
            String referencia) {
        BigDecimal monto = convertirMonto(montoTexto);
        return monto == null ? montoInvalido()
                : servicio.reintegrar(identificador, monto, referencia);
    }

    public ResultadoOperacion generarDocumento(String identificador, String referencia) {
        return servicio.generarDocumento(identificador, referencia);
    }

    public ResultadoOperacion consultarAuditoria(String identificador) {
        return servicio.consultarAuditoria(identificador);
    }

    /**
      Convierte directamente a BigDecimal, sin pasar por double. Los errores de
      formato se manejan aquí; las reglas de monto positivo pertenecen al modelo.
     */
    private BigDecimal convertirMonto(String texto) {
        if (!tieneTexto(texto)) {
            return null;
        }
        String normalizado = texto.trim();
        // Se acepta notación decimal común; no exponentes ni separadores de miles.
        if (!normalizado.matches("[+-]?[0-9]+(\\.[0-9]+)?")) {
            return null;
        }
        try {
            return new BigDecimal(normalizado);
        } catch (NumberFormatException excepcion) {
            return null;
        }
    }

    private boolean tieneTexto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    private ResultadoOperacion montoInvalido() {
        return ResultadoOperacion.fallida(
                "Monto inválido. Escriba un número sin separadores de miles "
                + "y use punto para los decimales; por ejemplo: 150.50.");
    }
}