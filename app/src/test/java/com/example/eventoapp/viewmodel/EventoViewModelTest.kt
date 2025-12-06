package com.example.eventoapp.viewmodel

import com.example.eventoapp.network.EventoApi
import com.example.eventoapp.network.EventoCreateRequest
import com.example.eventoapp.network.EventoResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class EventoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var apiMock: EventoApi
    private lateinit var viewModel: EventoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        apiMock = mockk()
        viewModel = EventoViewModelWrapper(apiMock) // usamos wrapper para inyectar API
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `obtenerEventos actualiza state con eventos`() = runTest {
        val eventosFake = listOf(
            EventoResponse(1, 1, "Evento1", "Desc1", "Direccion1", 1690000000L, 2, null, "Creador1", false)
        )
        coEvery { apiMock.getEventos() } returns Response.success(eventosFake)

        viewModel.obtenerEventos()
        testDispatcher.scheduler.advanceUntilIdle() // avanza coroutines

        val result = viewModel.eventos.first()
        assertEquals(1, result.size)
        assertEquals("Evento1", result.first().nombre)
    }

    @Test
    fun `crearEvento actualiza state al agregar nuevo evento`() = runTest {
        val request = EventoCreateRequest(1,"Nuevo","Desc","Dir",1690000000L,2,null,"Creador")
        val response = EventoResponse(1,1,"Nuevo","Desc","Dir",1690000000L,2,null,"Creador",false)

        coEvery { apiMock.crearEvento(any(), any()) } returns Response.success(response)
        coEvery { apiMock.getEventos() } returns Response.success(listOf(response))

        viewModel.crearEvento("token", request)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.eventos.first()
        assertEquals(1, result.size)
        assertEquals("Nuevo", result.first().nombre)
    }
}

// Wrappper para inyectar API
class EventoViewModelWrapper(private val api: EventoApi): EventoViewModel() {
    override val apiClient = api
}
