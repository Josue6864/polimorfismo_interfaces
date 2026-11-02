package com.mycompany.ejercicio_6.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class CatalogoIntegraciones {

    private final List<IntegracionAPI> integraciones = new ArrayList<>();

    public ResultadoOperacion agregar(IntegracionAPI integracion) {
        if (integracion == null) {
            return ResultadoOperacion.fallida("La integración es obligatoria.");
        }
        if (buscarPorIdentificador(integracion.getIdentificador()) != null) {
            return ResultadoOperacion.fallida("Ya existe ese identificador.");
        }
        integraciones.add(integracion);
        return ResultadoOperacion.realizada("Integración agregada: " + integracion.getIdentificador());
    }

    public IntegracionAPI buscarPorIdentificador(String identificador) {
        if (identificador == null) {
            return null;
        }
        String buscado = identificador.trim();
        for (IntegracionAPI integracion : integraciones) {
            if (integracion.getIdentificador().equalsIgnoreCase(buscado)) {
                return integracion;
            }
        }
        return null;
    }

    /** Envoltura de solo lectura sobre la MISMA lista; no crea otra copia del catálogo. */
    public List<IntegracionAPI> listarIntegraciones() {
        return Collections.unmodifiableList(integraciones);
    }

    public int getCantidad() {
        return integraciones.size();
    }

    /** Ordena la única colección utilizando IntegracionAPI.compareTo. */
    public ResultadoOperacion ordenarPorLatencia() {
        Collections.sort(integraciones);
        return ResultadoOperacion.realizada("Catálogo ordenado por latencia ascendente.");
    }
}
