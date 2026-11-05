package com.mycompany.ejercicio_6.controlador;

import com.mycompany.ejercicio_6.Aplicacion;
import com.mycompany.ejercicio_6.modelo.CatalogoIntegraciones;
import com.mycompany.ejercicio_6.modelo.IntegracionAPI;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Coordina la pantalla del catálogo.
 * Recibe el modelo y solicita la navegación entre vistas.
 */
public final class CatalogoController {

    private CatalogoIntegraciones catalogo;
    private ServicioOperaciones servicio;
    private Aplicacion navegacion;

    @FXML
    private TableView<IntegracionAPI> tablaIntegraciones;

    @FXML
    private TableColumn<IntegracionAPI, String> columnaId;

    @FXML
    private TableColumn<IntegracionAPI, String> columnaNombre;

    @FXML
    private TableColumn<IntegracionAPI, String> columnaTipo;

    @FXML
    private TableColumn<IntegracionAPI, String> columnaProveedor;

    @FXML
    private TableColumn<IntegracionAPI, String> columnaConexion;

    @FXML
    private TableColumn<IntegracionAPI, String> columnaLatencia;

    @FXML
    private TextField campoIdentificador;

    @FXML
    private Label etiquetaEstado;

    @FXML
    private Label etiquetaResumen;

    /**
     * JavaFX llama este método después de cargar los controles del FXML.
     */
    @FXML
    private void initialize() {

        columnaId.setCellValueFactory(celda ->
                new ReadOnlyStringWrapper(
                        celda.getValue().getIdentificador()));

        columnaNombre.setCellValueFactory(celda ->
                new ReadOnlyStringWrapper(
                        celda.getValue().getNombre()));

        columnaTipo.setCellValueFactory(celda ->
                new ReadOnlyStringWrapper(
                        celda.getValue().getTipo()));

        columnaProveedor.setCellValueFactory(celda ->
                new ReadOnlyStringWrapper(
                        celda.getValue().getProveedor()));

        columnaConexion.setCellValueFactory(celda ->
                new ReadOnlyStringWrapper(
                        celda.getValue().isConectada()
                                ? "Conectada"
                                : "Desconectada"));

        columnaLatencia.setCellValueFactory(celda ->
                new ReadOnlyStringWrapper(
                        String.format(
                                Locale.ROOT,
                                "%.2f ms",
                                celda.getValue().getLatenciaPromedioMs())));

        tablaIntegraciones.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        tablaIntegraciones.setPlaceholder(
                new Label("No hay integraciones registradas."));

        tablaIntegraciones.setRowFactory(tabla -> {

            TableRow<IntegracionAPI> fila = new TableRow<>();

            fila.setOnMouseClicked(evento -> {

                if (evento.getClickCount() == 2 && !fila.isEmpty()) {
                    tablaIntegraciones.getSelectionModel()
                            .select(fila.getItem());

                    abrirDetalle();
                }
            });

            return fila;
        });
    }

    public void configurar(
            CatalogoIntegraciones catalogo,
            ServicioOperaciones servicio) {

        this.catalogo = Objects.requireNonNull(
                catalogo, "El catálogo es obligatorio.");

        this.servicio = Objects.requireNonNull(
                servicio, "El servicio es obligatorio.");

        actualizarTabla();
    }

    public void setNavegacion(Aplicacion navegacion) {

        this.navegacion = Objects.requireNonNull(
                navegacion, "La navegación es obligatoria.");
    }

    public void actualizarTabla() {

        String anterior = seleccionada() == null
                ? null
                : seleccionada().getIdentificador();

        /*
         * Envuelve la lista existente del catálogo.
         * No construye otra colección para almacenar las integraciones.
         *
         * Se renueva la envoltura después de ordenar para que la tabla
         * refleje el orden actualizado del modelo.
         */
        tablaIntegraciones.setItems(
                FXCollections.observableList(
                        catalogo.listarIntegraciones()));

        seleccionarIntegracion(anterior);

        int conectadas = 0;

        for (IntegracionAPI integracion
                : catalogo.listarIntegraciones()) {

            if (integracion.isConectada()) {
                conectadas++;
            }
        }

        int desconectadas = catalogo.getCantidad() - conectadas;

        etiquetaResumen.setText(
                catalogo.getCantidad() + " integraciones"
                + "  ·  " + conectadas + " conectadas"
                + "  ·  " + desconectadas + " desconectadas");
    }

    public void seleccionarIntegracion(String identificador) {

        if (identificador == null) {
            return;
        }

        IntegracionAPI integracion =
                catalogo.buscarPorIdentificador(identificador);

        if (integracion != null) {
            tablaIntegraciones.getSelectionModel()
                    .select(integracion);

            tablaIntegraciones.scrollTo(integracion);
        }
    }

    @FXML
    public void buscar() {

        String identificador =
                campoIdentificador.getText().trim();

        if (identificador.isEmpty()) {
            etiquetaEstado.setText(
                    "Escriba el identificador que desea buscar.");
            return;
        }

        IntegracionAPI integracion =
                catalogo.buscarPorIdentificador(identificador);

        if (integracion == null) {
            tablaIntegraciones.getSelectionModel()
                    .clearSelection();

            etiquetaEstado.setText(
                    servicio.consultarConfiguracion(identificador)
                            .getMensaje());
        } else {
            seleccionarIntegracion(identificador);

            etiquetaEstado.setText(
                    "Seleccionada: " + integracion.getNombre()
                    + ". Abra su detalle u operaciones.");
        }
    }

    @FXML
    public void ordenar() {

        etiquetaEstado.setText(
                catalogo.ordenarPorLatencia().getMensaje());

        actualizarTabla();
    }

    @FXML
    public void abrirDetalle() {

        IntegracionAPI integracion = seleccionada();

        if (integracion == null) {
            etiquetaEstado.setText(
                    "Seleccione una integración en la tabla.");
            return;
        }

        try {
            navegacion.mostrarDetalle(
                    integracion.getIdentificador());

        } catch (IOException excepcion) {
            etiquetaEstado.setText(
                    "No se pudo abrir el detalle: "
                    + excepcion.getMessage());
        }
    }

    @FXML
    public void abrirOperaciones() {

        IntegracionAPI integracion = seleccionada();

        if (integracion == null) {
            etiquetaEstado.setText(
                    "Seleccione una integración en la tabla.");
            return;
        }

        try {
            navegacion.mostrarOperaciones(
                    integracion.getIdentificador());

        } catch (IOException excepcion) {
            etiquetaEstado.setText(
                    "No se pudo abrir el formulario: "
                    + excepcion.getMessage());
        }
    }

    private IntegracionAPI seleccionada() {

        return tablaIntegraciones.getSelectionModel()
                .getSelectedItem();
    }
}