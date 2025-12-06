package com.example.eventoapp.network

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class EventoApiTest {

    private val mockApi = mockk<EventoApi>()

    @Test
    fun `getEventos devuelve lista cuando la API responde`() = runBlocking {
        // Datos de prueba
        val eventosFake = listOf(
            EventoResponse(1, 1, "Evento1", "Desc1", "Direccion1", 1690000000L, 2, null, "Creador1", false)
        )

        // Configuramos mock
        coEvery { mockApi.getEventos() } returns Response.success(eventosFake)

        // Llamada al API
        val response = mockApi.getEventos()

        // Validaciones
        assertTrue(response.isSuccessful)
        assertEquals(1, response.body()?.size)
        assertEquals("Evento1", response.body()?.first()?.nombre)
    }

    @Test
    fun `crearEvento devuelve evento cuando se crea correctamente`() = runBlocking {
        val eventoRequest = EventoCreateRequest(
            usuarioId = 1,
            nombre = "Nuevo Evento",
            descripcion = "Desc",
            direccion = "Direccion",
            fecha = 1690000000L,
            duracionHoras = 2,
            imagenUri = null,
            creadorNombre = "Creador"
        )

        val eventoResponse = EventoResponse(
            1, 1, "Nuevo Evento", "Desc", "Direccion", 1690000000L, 2, null, "Creador", false
        )

        coEvery { mockApi.crearEvento(any(), any()) } returns Response.success(eventoResponse)

        val response = mockApi.crearEvento("Bearer token", eventoRequest)

        assertTrue(response.isSuccessful)
        assertEquals("Nuevo Evento", response.body()?.nombre)
    }
}
