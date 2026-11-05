package com.mycompany.ejercicio_6;

import com.mycompany.ejercicio_6.controlador.ControladorConsola;
import com.mycompany.ejercicio_6.modelo.CargadorIntegraciones;
import com.mycompany.ejercicio_6.modelo.CatalogoIntegraciones;
import com.mycompany.ejercicio_6.modelo.ServicioOperaciones;
import com.mycompany.ejercicio_6.vista.consola.VistaConsola;

/**
  Punto de entrada general.
  Sin argumentos inicia la consola.
 */
public final class Ejercicio_6 {

    private Ejercicio_6() {
    }

    public static void main(String[] args) {

        String modo = args.length == 0
                ? "--consola"
                : args[0];

        switch (modo) {

            case "--consola":
                iniciarConsola();
                break;

            case "--grafica":
                Aplicacion.main(new String[0]);
                break;

            case "--ayuda":
            case "-h":

                System.out.println(
                        "Ejercicio_6 "
                        + "[--consola | --grafica | --ayuda]");

                break;

            default:

                System.err.println(
                        "Modo desconocido: " + modo);

                System.err.println(
                        "Utilice --consola, --grafica o --ayuda.");
        }
    }

    private static void iniciarConsola() {

        CatalogoIntegraciones catalogo =
                CargadorIntegraciones.crearCatalogoInicial();

        ServicioOperaciones servicio =
                new ServicioOperaciones(catalogo);

        ControladorConsola controlador =
                new ControladorConsola(catalogo, servicio);

        VistaConsola vista = new VistaConsola();

        controlador.iniciar(vista);
    }
}