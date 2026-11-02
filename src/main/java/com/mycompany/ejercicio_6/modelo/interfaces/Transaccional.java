package com.mycompany.ejercicio_6.modelo.interfaces;

import com.mycompany.ejercicio_6.modelo.ResultadoOperacion;
import java.math.BigDecimal;

/** Contrato para operaciones financieras simuladas. Incluye sobrecarga de cobrar. */
public interface Transaccional {

    /** La variante sencilla reutiliza la operación completa. */
    default ResultadoOperacion cobrar(BigDecimal monto) {
        return cobrar(monto, "SIN-REFERENCIA");
    }

    ResultadoOperacion cobrar(BigDecimal monto, String referencia);

    ResultadoOperacion reintegrar(BigDecimal monto, String referencia);
}
