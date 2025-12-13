package com.example.eventoapp.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UploadUtilsTest {

    @Test
    fun `createPartFromString produce requestbody text plain`() {
        val rb = UploadUtils.createPartFromString("hola")
        val ct = rb.contentType()
        assertNotNull(ct)
        assertEquals("text/plain", ct?.toString())
    }
}
