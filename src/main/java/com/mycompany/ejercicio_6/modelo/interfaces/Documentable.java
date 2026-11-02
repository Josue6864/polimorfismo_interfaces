package com.mycompany.ejercicio_6.modelo.interfaces;

import com.mycompany.ejercicio_6.modelo.ResultadoOperacion;

/** Contrato para generar documentos simulados, sin validez fiscal o legal. */
public interface Documentable {

    ResultadoOperacion generarDocumento(String referencia);
}
