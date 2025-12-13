package com.example.eventoapp.ui.viewmodel

import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.EventoCreateRequest
import com.example.eventoapp.network.EventoResponse
import com.example.eventoapp.utils.InstantTaskExecutorExtension
import com.example.eventoapp.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response
import org.junit.jupiter.api.Assertions.*


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(
    InstantTaskExecutorExtension::class,
    MainDispatcherRule::class
)
class EventoViewModelTest {

    @BeforeEach
    fun setup() {
        mockkObject(ApiClient)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(ApiClient)
    }

    @Test
    fun `obtenerEventos actualiza eventos cuando api responde ok`() = runTest {
        val evento = EventoResponse(1, 1, "E1", "desc", "dir", 0L, 2, null, "Bryan", false)
        coEvery { ApiClient.eventoApi.getEventos() } returns Response.success(listOf(evento))

        val vm = EventoViewModel()

        val lista = vm.eventos.value
        assertEquals(1, lista?.size)
        assertEquals("E1", lista?.first()?.nombre)
    }

    @Test
    fun `obtenerEventos pone mensajeError cuando api falla`() = runTest {
        val errorBody = "error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { ApiClient.eventoApi.getEventos() } returns Response.error(500, errorBody)

        val vm = EventoViewModel()

        assertNotNull(vm.mensajeError.value)
    }

    @Test
    fun `crearEvento setea lastCreateSuccess true cuando api OK`() = runTest {
        val created = EventoResponse(2, 1, "Nuevo", "desc", "dir", 0L, 1, null, "Bryan", false)
        coEvery { ApiClient.eventoApi.crearEvento(any(), any()) } returns Response.success(created)
        coEvery { ApiClient.eventoApi.getEventos() } returns Response.success(listOf(created))

        val vm = EventoViewModel()
        vm.crearEvento("token", EventoCreateRequest(1, "Nuevo", "desc", "dir", 0L, 1, null, "Bryan"))

        assertTrue(vm.lastCreateSuccess.value == true)
        assertEquals(1, vm.eventos.value?.size)
    }

    @Test
    fun `formatearFecha produce formato esperado`() {
        val vm = EventoViewModel()
        val s = vm.formatearFecha(1672531200000L)
        assertTrue(s.contains("/"))
    }
}
