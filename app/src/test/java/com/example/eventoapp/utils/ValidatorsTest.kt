package com.example.eventoapp.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import com.example.eventoapp.ui.utils.Validators

class ValidatorsTest {

    @Test
    fun `validarCorreo detecta valido e invalido`() {
        assertNull(Validators.validarCorreo("test@example.com"))
        assertNotNull(Validators.validarCorreo("not-an-email"))
        assertNotNull(Validators.validarCorreo(""))
    }

    @Test
    fun `validarNombre validaciones`() {
        assertNull(Validators.validarNombre("Bryan"))
        assertNotNull(Validators.validarNombre(""))
        assertNotNull(Validators.validarNombre("Jo1"))
        assertNotNull(Validators.validarNombre("AB"))
    }

    @Test
    fun `validarDuracion casos`() {
        assertNull(Validators.validarDuracion("5"))
        assertNotNull(Validators.validarDuracion(""))
        assertNotNull(Validators.validarDuracion("abc"))
        assertNotNull(Validators.validarDuracion("0"))
        assertNotNull(Validators.validarDuracion("10000"))
    }
}
