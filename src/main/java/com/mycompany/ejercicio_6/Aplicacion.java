package com.mycompany.ejercicio_6;

import com.mycompany.ejercicio_6.controlador.CatalogoController;
import com.mycompany.ejercicio_6.controlador.DetalleIntegracionController;
import com.mycompany.ejercicio_6.controlador.OperacionesController;
import com.mycompany.ejercicio_6.modelo.CargadorIntegraciones;
import com.mycompany.ejercicio_6.modelo.CatalogoIntegraciones;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  Arranque gráfico y navegación entre las tres vistas.
  Los controladores comparten las mismas instancias del modelo.
 */
public final class Aplicacion extends Application {

    private static final String RECURSOS =
            "/com/mycompany/ejercicio_6/vista/grafica/";

    private CatalogoIntegraciones catalogo;
    private ServicioOperaciones servicio;

    private Stage escenario;
    private String identificadorSeleccionado;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage escenario) throws IOException {

        this.escenario = escenario;

        // La carga inicial se realiza una sola vez.
        catalogo = CargadorIntegraciones.crearCatalogoInicial();

        servicio = new ServicioOperaciones(catalogo);

        escenario.setTitle(
                "Ejercicio 6 | Integraciones externas");

        escenario.setMinWidth(980);
        escenario.setMinHeight(680);

        mostrarCatalogo();

        escenario.show();
    }

    public void mostrarCatalogo() throws IOException {

        FXMLLoader cargador = cargar("catalogo.fxml");

        Parent raiz = cargador.load();

        CatalogoController controlador =
                cargador.getController();

        controlador.configurar(catalogo, servicio);
        controlador.setNavegacion(this);

        controlador.seleccionarIntegracion(
                identificadorSeleccionado);

        mostrar(raiz);
    }

    public void mostrarDetalle(
            String identificador) throws IOException {

        identificadorSeleccionado = identificador;

        FXMLLoader cargador =
                cargar("detalle-integracion.fxml");

        Parent raiz = cargador.load();

        DetalleIntegracionController controlador =
                cargador.getController();

        controlador.configurar(
                servicio, identificador);

        controlador.setNavegacion(this);

        mostrar(raiz);
    }

    public void mostrarOperaciones(
            String identificador) throws IOException {

        identificadorSeleccionado = identificador;

        FXMLLoader cargador =
                cargar("operaciones.fxml");

        Parent raiz = cargador.load();

        OperacionesController controlador =
                cargador.getController();

        controlador.configurar(
                servicio, identificador);

        controlador.setNavegacion(this);

        mostrar(raiz);
    }

    private FXMLLoader cargar(
            String archivo) throws IOException {

        URL url = getClass().getResource(
                RECURSOS + archivo);

        if (url == null) {
            throw new IOException(
                    "No se encontró el recurso " + archivo);
        }

        return new FXMLLoader(url);
    }

    private void mostrar(Parent raiz) throws IOException {

        if (escenario.getScene() == null) {

            Scene escena = new Scene(raiz, 1180, 760);

            URL css = getClass().getResource(
                    RECURSOS + "estilos.css");

            if (css == null) {
                throw new IOException(
                        "No se encontró estilos.css.");
            }

            escena.getStylesheets().add(
                    css.toExternalForm());

            escenario.setScene(escena);

        } else {

            // Cambia la vista dentro de la misma ventana.
            escenario.getScene().setRoot(raiz);
        }
    }
}