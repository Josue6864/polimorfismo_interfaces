package com.mycompany.ejercicio_6.modelo;

import java.util.Objects;


public final class ResultadoOperacion {

    private final EstadoOperacion estado;
    private final String mensaje;
    private final String contenido;

    private ResultadoOperacion(EstadoOperacion estado, String mensaje, String contenido) {
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio.");
        this.mensaje = Objects.requireNonNull(mensaje, "El mensaje es obligatorio.");
        this.contenido = Objects.requireNonNull(contenido, "El contenido no puede ser null.");
    }

    public static ResultadoOperacion realizada(String mensaje) {
        return realizada(mensaje, "");
    }

    public static ResultadoOperacion realizada(String mensaje, String contenido) {
        return new ResultadoOperacion(EstadoOperacion.REALIZADA, mensaje, contenido);
    }

    public static ResultadoOperacion omitida(String mensaje) {
        return new ResultadoOperacion(EstadoOperacion.OMITIDA, mensaje, "");
    }

    public static ResultadoOperacion fallida(String mensaje) {
        return new ResultadoOperacion(EstadoOperacion.FALLIDA, mensaje, "");
    }

    public EstadoOperacion getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getContenido() {
        return contenido;
    }

    public boolean isRealizada() {
        return estado == EstadoOperacion.REALIZADA;
    }

    @Override
    public String toString() {
        return estado + ": " + mensaje + (contenido.isEmpty() ? "" : "\n" + contenido);
    }
}
