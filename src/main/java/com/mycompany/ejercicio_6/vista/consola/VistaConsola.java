package com.mycompany.ejercicio_6.vista.consola;

import com.mycompany.ejercicio_6.modelo.IntegracionAPI;
import com.mycompany.ejercicio_6.modelo.ResultadoOperacion;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
  Vista de consola: presenta el menú, lee líneas y muestra el modelo recibido.
  Las decisiones del menú corresponden al controlador y las reglas al modelo.
 */
public final class VistaConsola {

    private final Scanner entrada;
    private final PrintStream salida;

    public VistaConsola() {
        this(new Scanner(System.in), System.out);
    }

    /** Permite proporcionar flujos de prueba sin modificar la lógica de la vista. */
    public VistaConsola(Scanner entrada, PrintStream salida) {
        this.entrada = Objects.requireNonNull(entrada, "La entrada es obligatoria.");
        this.salida = Objects.requireNonNull(salida, "La salida es obligatoria.");
    }

    public void mostrarMenu() {
        salida.println();
        salida.println("============== INTEGRACIONES EXTERNAS ==============");
        salida.println("1. Listar integraciones");
        salida.println("2. Buscar por identificador y consultar configuracion");
        salida.println("3. Ordenar por latencia");
        salida.println("4. Consultar estado");
        salida.println("5. Simular cobro");
        salida.println("6. Simular reintegro");
        salida.println("7. Generar documento");
        salida.println("8. Consultar auditoria");
        salida.println("9. Salir");
        salida.println("=====================================================");
    }

    /** Devuelve null al agotarse la entrada; no cierra el flujo recibido. */
    public String leerTexto(String indicacion) {
        salida.print(indicacion + ": ");
        salida.flush();
        return entrada.hasNextLine() ? entrada.nextLine() : null;
    }

    /** Recorre directamente el catálogo recibido sin copiar sus integraciones. */
    public void mostrarIntegraciones(List<IntegracionAPI> integraciones) {
        Objects.requireNonNull(integraciones, "La lista es obligatoria.");
        salida.println();
        salida.println("Integraciones registradas: " + integraciones.size());
        if (integraciones.isEmpty()) {
            salida.println("El catalogo está vacío.");
            return;
        }
        salida.printf("%-9s %-25s %-21s %-18s %-14s %12s%n",
                "ID", "Nombre", "Tipo", "Proveedor", "Conexion", "Latencia ms");
        for (IntegracionAPI integracion : integraciones) {
            salida.printf(java.util.Locale.ROOT, "%-9s %-25s %-21s %-18s %-14s %12.2f%n",
                    integracion.getIdentificador(), abreviar(integracion.getNombre(), 25),
                    abreviar(integracion.getTipo(), 21), abreviar(integracion.getProveedor(), 18),
                    integracion.getEstadoConexion(), integracion.getLatenciaPromedioMs());
        }
    }

    public void mostrarResultado(ResultadoOperacion resultado) {
        Objects.requireNonNull(resultado, "El resultado es obligatorio.");
        salida.println();
        salida.println("[" + resultado.getEstado() + "] " + resultado.getMensaje());
        if (!resultado.getContenido().isEmpty()) {
            salida.println(resultado.getContenido());
        }
    }

    public void mostrarMensaje(String mensaje) {
        salida.println(mensaje);
    }

    private String abreviar(String texto, int ancho) {
        return texto.length() <= ancho ? texto : texto.substring(0, ancho - 3) + "...";
    }
}
