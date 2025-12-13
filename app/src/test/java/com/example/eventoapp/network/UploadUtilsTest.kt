package com.example.eventoapp.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UploadUtilsTest {

    @Test
    fun `createPartFromString produce requestbody text plain`() {
        val rb = UploadUtils.createPartFromString("hola")
        val ct = rb.contentType()
        assertNotNull(ct)
        // puede incluir charset (ej. "text/plain; charset=utf-8") — comprobamos que empiece por text/plain
        assertTrue(ct.toString().startsWith("text/plain"))
    }
}
