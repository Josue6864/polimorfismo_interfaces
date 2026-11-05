package com.mycompany.ejercicio_6.controlador;

import com.mycompany.ejercicio_6.Aplicacion;
import com.mycompany.ejercicio_6.modelo.ResultadoOperacion;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
  Adapta los datos del formulario a las operaciones del modelo.
  ServicioOperaciones decide qué capacidades admite la integración.
 */
public final class OperacionesController {

    private ServicioOperaciones servicio;
    private String identificadorSeleccionado;
    private Aplicacion navegacion;

    @FXML
    private Label etiquetaSeleccion;

    @FXML
    private Label etiquetaEstado;

    @FXML
    private Label etiquetaReferencia;

    @FXML
    private Label etiquetaAyuda;

    @FXML
    private ComboBox<String> selectorOperacion;

    @FXML
    private TextField campoMonto;

    @FXML
    private TextField campoReferencia;

    @FXML
    private TextArea areaResultado;

    @FXML
    private VBox bloqueMonto;

    @FXML
    private VBox bloqueReferencia;

    @FXML
    private void initialize() {

        selectorOperacion.getItems().addAll(
                "Consultar estado",
                "Cobrar",
                "Reintegrar",
                "Generar documento",
                "Consultar auditoría");

        selectorOperacion.valueProperty().addListener(
                (observable, anterior, actual) ->
                        actualizarFormulario());

        selectorOperacion.getSelectionModel().selectFirst();
    }

    public void configurar(
            ServicioOperaciones servicio,
            String identificador) {

        this.servicio = Objects.requireNonNull(
                servicio, "El servicio es obligatorio.");

        this.identificadorSeleccionado = Objects.requireNonNull(
                identificador, "El identificador es obligatorio.");

        etiquetaSeleccion.setText(
                "Operaciones de " + identificadorSeleccionado);
    }

    public void setNavegacion(Aplicacion navegacion) {

        this.navegacion = Objects.requireNonNull(
                navegacion, "La navegación es obligatoria.");
    }

    public void actualizarFormulario() {

        String operacion = selectorOperacion.getValue();

        boolean financiera =
                "Cobrar".equals(operacion)
                || "Reintegrar".equals(operacion);

        boolean auditoria =
                "Consultar auditoría".equals(operacion);

        bloqueMonto.setVisible(financiera);
        bloqueMonto.setManaged(financiera);

        bloqueReferencia.setVisible(!auditoria);
        bloqueReferencia.setManaged(!auditoria);

        boolean referenciaOpcional =
                "Consultar estado".equals(operacion)
                || "Cobrar".equals(operacion);

        etiquetaReferencia.setText(
                referenciaOpcional
                        ? "Referencia opcional"
                        : "Referencia obligatoria");

        if ("Consultar estado".equals(operacion)) {

            etiquetaAyuda.setText(
                    "Deje la referencia vacía para consultar "
                    + "la disponibilidad general.");

        } else if (auditoria) {

            etiquetaAyuda.setText(
                    "La auditoría local también está disponible "
                    + "sin conexión.");

        } else {

            etiquetaAyuda.setText(
                    "Las operaciones son simuladas. Si el proveedor "
                    + "no admite esta capacidad, se omitirá.");
        }
    }

    @FXML
    public void ejecutarOperacion() {

        String operacion = selectorOperacion.getValue();
        String referencia = campoReferencia.getText().trim();

        ResultadoOperacion resultado;

        if (operacion == null) {

            resultado = ResultadoOperacion.fallida(
                    "Seleccione una operación.");

        } else {

            switch (operacion) {

                case "Consultar estado":

                    resultado = referencia.isEmpty()
                            ? servicio.consultarEstado(
                                    identificadorSeleccionado)
                            : servicio.consultarEstado(
                                    identificadorSeleccionado,
                                    referencia);

                    break;

                case "Consultar auditoría":

                    resultado = servicio.consultarAuditoria(
                            identificadorSeleccionado);

                    break;

                case "Generar documento":

                    resultado = servicio.generarDocumento(
                            identificadorSeleccionado,
                            referencia);

                    break;

                case "Cobrar":
                case "Reintegrar":

                    BigDecimal monto =
                            convertirMonto(campoMonto.getText());

                    if (monto == null) {

                        resultado = ResultadoOperacion.fallida(
                                "Monto inválido. Use punto para los "
                                + "decimales y no agregue "
                                + "separadores de miles.");

                    } else if ("Reintegrar".equals(operacion)) {

                        resultado = servicio.reintegrar(
                                identificadorSeleccionado,
                                monto,
                                referencia);

                    } else {

                        resultado = referencia.isEmpty()
                                ? servicio.cobrar(
                                        identificadorSeleccionado,
                                        monto)
                                : servicio.cobrar(
                                        identificadorSeleccionado,
                                        monto,
                                        referencia);
                    }

                    break;

                default:

                    resultado = ResultadoOperacion.fallida(
                            "Operación desconocida.");
            }
        }

        areaResultado.setText(resultado.toString());

        etiquetaEstado.setText(
                resultado.getEstado()
                + ": "
                + resultado.getMensaje());
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

    /**
      Convierte el texto directamente a BigDecimal.
      La validación del monto positivo corresponde al modelo.
     */
    private BigDecimal convertirMonto(String texto) {

        if (texto == null
                || !texto.trim().matches(
                        "[+-]?[0-9]+(\\.[0-9]+)?")) {

            return null;
        }

        try {
            return new BigDecimal(texto.trim());

        } catch (NumberFormatException excepcion) {
            return null;
        }
    }
}