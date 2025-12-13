package com.example.eventoapp.ui.viewmodel

import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.LoginResponse
import com.example.eventoapp.network.RegisterResponse
import com.example.eventoapp.network.UsuarioNetwork
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

// helper para LiveData (espera postValue)
fun <T> LiveData<T>.getOrAwaitValue(timeoutSeconds: Long = 2): T? {
    val latch = CountDownLatch(1)
    var data: T? = null
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    latch.await(timeoutSeconds, TimeUnit.SECONDS)
    return data
}

class UsuarioViewModelTest {

    @BeforeEach
    fun before() {
        mockkObject(ApiClient)
    }

    @AfterEach
    fun after() {
        unmockkObject(ApiClient)
    }

    @Test
    fun `registrarUsuario setea usuarioActual en caso success`() = runTest {
        val user = UsuarioNetwork(5, "B", "b@b.com", null)
        coEvery { ApiClient.usuarioApi.register(any()) } returns Response.success(RegisterResponse(user))

        val vm = UsuarioViewModel()
        vm.registrarUsuario("B", "b@b.com", "pass123")

        val got = vm.usuarioActual.getOrAwaitValue()
        assertNotNull(got)
        assertEquals("B", got?.nombre)
    }

    @Test
    fun `login exitoso setea token y usuarioActual`() = runTest {
        val user = UsuarioNetwork(6, "C", "c@c.com", null)
        coEvery { ApiClient.usuarioApi.login(any()) } returns Response.success(LoginResponse("tokX", user))

        val vm = UsuarioViewModel()
        vm.login("c@c.com", "pass")

        val token = vm.token.getOrAwaitValue()
        val u = vm.usuarioActual.getOrAwaitValue()

        assertEquals("tokX", token)
        assertNotNull(u)
        assertEquals("C", u?.nombre)
    }

    @Test
    fun `login con 401 pone mensaje de credenciales invalidas`() = runTest {
        val errorBody = "unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { ApiClient.usuarioApi.login(any()) } returns Response.error(401, errorBody)

        val vm = UsuarioViewModel()
        vm.login("x@x.com", "bad")

        val msg = vm.mensajeError.getOrAwaitValue()
        assertNotNull(msg)
        assertTrue(msg!!.contains("Credenciales inválidas"))
    }
}
