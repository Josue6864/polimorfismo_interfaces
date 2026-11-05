package com.mycompany.ejercicio_6.controlador;

import com.mycompany.ejercicio_6.modelo.CatalogoIntegraciones;
import com.mycompany.ejercicio_6.modelo.IntegracionAPI;
import com.mycompany.ejercicio_6.modelo.ResultadoOperacion;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import com.mycompany.ejercicio_6.vista.consola.VistaConsola;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Controlador de la interacción por consola. Recibe el modelo del arranque; no
 * carga integraciones ni mantiene otra lista. Adapta las entradas de texto y
 * devuelve datos/resultados para que VistaConsola los presente. El ciclo del
 * menú se conectará al implementar la vista de consola.
 */
public final class ControladorConsola {

    private final CatalogoIntegraciones catalogo;
    private final ServicioOperaciones servicio;

    public ControladorConsola(CatalogoIntegraciones catalogo, ServicioOperaciones servicio) {
        this.catalogo = Objects.requireNonNull(catalogo, "El catálogo es obligatorio.");
        this.servicio = Objects.requireNonNull(servicio, "El servicio es obligatorio.");
    }

    /**
     * Retorna la vista de lectura sobre la unica lista del modelo.
     */
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

    /**
     * Una referencia vacía solicita el estado general del servicio.
     */
    public ResultadoOperacion consultarEstado(String identificador, String referencia) {
        return tieneTexto(referencia)
                ? servicio.consultarEstado(identificador, referencia)
                : consultarEstado(identificador);
    }

    public ResultadoOperacion cobrar(String identificador, String montoTexto) {
        return cobrar(identificador, montoTexto, "");
    }

    /**
     * Una referencia vacía utiliza la sobrecarga sencilla del contrato de
     * cobro.
     */
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
     * Convierte directamente a BigDecimal, sin pasar por double. Los errores de
     * formato se manejan aquí; las reglas de monto positivo pertenecen al
     * modelo.
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
                "Monto invalido. Escriba un número sin separadores de miles "
                + "y use punto para los decimales; por ejemplo: 150.50.");
    }

    public void iniciar(VistaConsola vista) {

        if (vista == null) {
            throw new IllegalArgumentException("La vista es obligatoria.");
        }

        vista.mostrarMensaje(
                "Ejercicio 6 - Administracion de integraciones simuladas");

        vista.mostrarMensaje(
                "Catalogo inicial: "
                + getCantidadIntegraciones()
                + " integraciones.");

        while (true) {

            vista.mostrarMenu();

            String opcion = vista.leerTexto("Seleccione una opcion");

            if (opcion == null) {
                vista.mostrarMensaje("Entrada finalizada. Hasta luego.");
                return;
            }

            opcion = opcion.trim();

            if ("9".equals(opcion)) {
                vista.mostrarMensaje("Programa finalizado. Hasta luego.");
                return;
            }

            if (!atenderOpcion(opcion, vista)) {
                vista.mostrarMensaje("Entrada finalizada. Hasta luego.");
                return;
            }
        }
    }

    private boolean atenderOpcion(String opcion, VistaConsola vista) {

        if ("1".equals(opcion)) {
            vista.mostrarIntegraciones(listarIntegraciones());
            return true;
        }

        if ("3".equals(opcion)) {
            vista.mostrarResultado(ordenarPorLatencia());
            vista.mostrarIntegraciones(listarIntegraciones());
            return true;
        }

        if (!"2".equals(opcion)
                && !"4".equals(opcion)
                && !"5".equals(opcion)
                && !"6".equals(opcion)
                && !"7".equals(opcion)
                && !"8".equals(opcion)) {

            vista.mostrarMensaje(
                    "Opcion invalida. Seleccione un numero del 1 al 9.");

            return true;
        }

        String identificador = leerObligatorio(
                vista, "Identificador de la integracion");

        if (identificador == null) {
            return false;
        }

        switch (opcion) {

            case "2":
                vista.mostrarResultado(
                        buscarIntegracion(identificador));
                break;

            case "4": {
                String referencia = vista.leerTexto(
                        "Referencia (Enter para estado general)");

                if (referencia == null) {
                    return false;
                }

                vista.mostrarResultado(
                        consultarEstado(identificador, referencia));
                break;
            }

            case "5": {
                String monto = vista.leerTexto(
                        "Monto (por ejemplo 150.50)");

                if (monto == null) {
                    return false;
                }

                String referencia = vista.leerTexto(
                        "Referencia (opcional)");

                if (referencia == null) {
                    return false;
                }

                vista.mostrarResultado(
                        cobrar(identificador, monto, referencia));
                break;
            }

            case "6": {
                String monto = vista.leerTexto(
                        "Monto (por ejemplo 150.50)");

                if (monto == null) {
                    return false;
                }

                String referencia = leerObligatorio(
                        vista, "Referencia del reintegro");

                if (referencia == null) {
                    return false;
                }

                vista.mostrarResultado(
                        reintegrar(identificador, monto, referencia));
                break;
            }

            case "7": {
                String referencia = leerObligatorio(
                        vista, "Referencia del documento");

                if (referencia == null) {
                    return false;
                }

                vista.mostrarResultado(
                        generarDocumento(identificador, referencia));
                break;
            }

            case "8":
                vista.mostrarResultado(
                        consultarAuditoria(identificador));
                break;

            default:
                throw new IllegalStateException(
                        "Opcion no contemplada por el controlador.");
        }

        return true;
    }

    private String leerObligatorio(
            VistaConsola vista, String indicacion) {

        while (true) {

            String texto = vista.leerTexto(indicacion);

            if (texto == null) {
                return null;
            }

            if (!texto.trim().isEmpty()) {
                return texto.trim();
            }

            vista.mostrarMensaje(
                    "Este dato es obligatorio. Intentelo de nuevo.");
        }
    }
}
