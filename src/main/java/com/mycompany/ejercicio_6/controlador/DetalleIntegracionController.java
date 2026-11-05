package com.mycompany.ejercicio_6.controlador;

import com.mycompany.ejercicio_6.Aplicacion;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Presenta la configuración y la auditoría de una integración.
 */
public final class DetalleIntegracionController {

    private ServicioOperaciones servicio;
    private String identificadorSeleccionado;
    private Aplicacion navegacion;

    @FXML
    private Label etiquetaSeleccion;

    @FXML
    private Label etiquetaEstado;

    @FXML
    private TextArea areaConfiguracion;

    @FXML
    private TextArea areaAuditoria;

    public void configurar(
            ServicioOperaciones servicio,
            String identificador) {

        this.servicio = Objects.requireNonNull(
                servicio, "El servicio es obligatorio.");

        this.identificadorSeleccionado = Objects.requireNonNull(
                identificador, "El identificador es obligatorio.");

        etiquetaSeleccion.setText(
                "Integración " + identificadorSeleccionado);

        cargarDetalle();
        actualizarAuditoria();
    }

    public void setNavegacion(Aplicacion navegacion) {

        this.navegacion = Objects.requireNonNull(
                navegacion, "La navegación es obligatoria.");
    }

    public void cargarDetalle() {

        areaConfiguracion.setText(
                servicio.consultarConfiguracion(
                        identificadorSeleccionado).toString());
    }

    @FXML
    public void actualizarAuditoria() {

        areaAuditoria.setText(
                servicio.consultarAuditoria(
                        identificadorSeleccionado).toString());

        areaAuditoria.positionCaret(
                areaAuditoria.getLength());
    }

    @FXML
    public void abrirOperaciones() {

        try {
            navegacion.mostrarOperaciones(
                    identificadorSeleccionado);

        } catch (IOException excepcion) {
            etiquetaEstado.setText(
                    "No se pudo abrir el formulario: "
                    + excepcion.getMessage());
        }
    }

    @FXML
    public void volver() {

        try {
            navegacion.mostrarCatalogo();

        } catch (IOException excepcion) {
            etiquetaEstado.setText(
                    "No se pudo volver al catálogo: "
                    + excepcion.getMessage());
        }
    }
}