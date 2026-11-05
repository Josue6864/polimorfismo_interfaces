package com.mycompany.ejercicio_6;


import com.mycompany.ejercicio_6.controlador.ControladorConsola;
import com.mycompany.ejercicio_6.modelo.CargadorIntegraciones;
import com.mycompany.ejercicio_6.modelo.CatalogoIntegraciones;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import com.mycompany.ejercicio_6.vista.consola.VistaConsola;

public class Ejercicio_6 {

    public static void main(String[] args) {

        // Cargar una sola vez las diez integraciones iniciales.
        CatalogoIntegraciones catalogo =
                CargadorIntegraciones.crearCatalogoInicial();

        // El servicio trabaja con ese mismo catalogo.
        ServicioOperaciones servicio =
                new ServicioOperaciones(catalogo);

        // Crea el controlador y la vista de consola.
        ControladorConsola controlador =
                new ControladorConsola(catalogo, servicio);

        VistaConsola vista = new VistaConsola();

        // Iniciar la interacción con el usuario.
        controlador.iniciar(vista);
    }
}
