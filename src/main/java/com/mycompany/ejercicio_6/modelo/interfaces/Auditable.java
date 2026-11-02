package com.mycompany.ejercicio_6.modelo.interfaces;

/** Contrato de auditoría local. No depende de la conexión con el proveedor. */
public interface Auditable {

    default void registrarEvento(String detalle) {
        registrarEvento("GENERAL", detalle);
    }

    void registrarEvento(String accion, String detalle);

    String consultarAuditoria();
}
