package com.example.eventoapp.ui.viewmodel

import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.EventoCreateRequest
import com.example.eventoapp.network.EventoResponse
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class EventoViewModelTest {

    @Before
    fun before() {
        mockkObject(ApiClient)
    }

    @After
    fun after() {
        unmockkObject(ApiClient)
        Dispatchers.resetMain()
    }

    @Test
    fun `obtenerEventos actualiza eventos cuando api responde ok`() = runTest {
        val testEvento = EventoResponse(1, 1, "E1", "desc", "dir", 0L, 2, null, "Bryan", false)
        coEvery { ApiClient.eventoApi.getEventos() } returns Response.success(listOf(testEvento))

        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val vm = EventoViewModel()

        testScheduler.advanceUntilIdle()

        val lista = vm.eventos.value
        assertEquals(1, lista.size)
        assertEquals("E1", lista[0].nombre)
    }

    @Test
    fun `obtenerEventos pone mensajeError cuando api falla`() = runTest {
        val errorBody = "error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { ApiClient.eventoApi.getEventos() } returns Response.error(500, errorBody)

        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val vm = EventoViewModel()
        testScheduler.advanceUntilIdle()

        val msg = vm.mensajeError.value
        assertNotNull(msg)
        assertTrue(msg!!.contains("Error al obtener eventos"))
    }

    @Test
    fun `crearEvento setea lastCreateSuccess true cuando api OK`() = runTest {
        val created = EventoResponse(2, 1, "Nuevo", "desc", "dir", 0L, 1, null, "Bryan", false)
        coEvery { ApiClient.eventoApi.crearEvento(any(), any()) } returns Response.success(created)
        coEvery { ApiClient.eventoApi.getEventos() } returns Response.success(listOf(created))

        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val vm = EventoViewModel()

        vm.crearEvento("tokenX", EventoCreateRequest(1, "Nuevo", "desc", "dir", 0L, 1, null, "Bryan"))
        testScheduler.advanceUntilIdle()

        val flag = vm.lastCreateSuccess.value
        assertEquals(true, flag)
        assertNull(vm.mensajeError.value)
        assertEquals(1, vm.eventos.value.size)
    }

    @Test
    fun `crearEvento setea lastCreateSuccess false cuando api falla`() = runTest {
        val errorBody = "bad".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { ApiClient.eventoApi.crearEvento(any(), any()) } returns Response.error(400, errorBody)

        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val vm = EventoViewModel()

        vm.crearEvento("tokenX", EventoCreateRequest(1, "Nuevo", "desc", "dir", 0L, 1, null, "Bryan"))
        testScheduler.advanceUntilIdle()

        assertEquals(false, vm.lastCreateSuccess.value)
        assertNotNull(vm.mensajeError.value)
    }

    @Test
    fun `crearEventoMultipart exitoso actualiza flag y eventos`() = runTest {
        val created = EventoResponse(3, 1, "Multipart", "d", "dir", 0L, 1, null, "Bry", false)

        // mockear la llamada al API multipart (9 parámetros en la API)
        coEvery {
            ApiClient.eventoApi.crearEventoMultipart(
                any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } returns Response.success(created)

        coEvery { ApiClient.eventoApi.getEventos() } returns Response.success(listOf(created))

        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val vm = EventoViewModel()

        // LLAMADA CON NAMED PARAMETERS EXACTOS (como en la firma del ViewModel)
        vm.crearEventoMultipart(
            token = "tokenX",
            usuarioId = 1,
            nombre = "Multipart",
            descripcion = "d",
            direccion = "dir",
            fecha = 0L,
            duracionHoras = 1,
            creadorNombre = "Bry",
            imagenPart = null
        )

        testScheduler.advanceUntilIdle()

        assertEquals(true, vm.lastCreateSuccess.value)
        assertNull(vm.mensajeError.value)
        assertEquals(1, vm.eventos.value.size)
    }

    @Test
    fun `formatearFecha produce formato esperado`() {
        val vm = EventoViewModel()
        val ts = 1672531200000L
        val s = vm.formatearFecha(ts)
        assertTrue(s.contains("/"))
    }
}
