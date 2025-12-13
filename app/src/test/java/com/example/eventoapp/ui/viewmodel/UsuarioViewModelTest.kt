package com.example.eventoapp.ui.viewmodel
import org.junit.jupiter.api.extension.ExtendWith
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.eventoapp.network.*
import com.example.eventoapp.utils.InstantTaskExecutorExtension
import com.example.eventoapp.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.*
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

// ----------------------
// LiveData helper
// ----------------------
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

@ExtendWith(
    InstantTaskExecutorExtension::class,
    MainDispatcherRule::class
)
class UsuarioViewModelTest {

    @BeforeEach
    fun setup() {
        mockkObject(ApiClient)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(ApiClient)
    }

    @Test
    fun `registrarUsuario setea usuarioActual en caso success`() = runTest {
        val user = UsuarioNetwork(5, "B", "b@b.com", null)
        coEvery {
            ApiClient.usuarioApi.register(any())
        } returns Response.success(RegisterResponse(user))

        val vm = UsuarioViewModel()
        vm.registrarUsuario("B", "b@b.com", "pass123")

        val got = vm.usuarioActual.getOrAwaitValue()
        Assertions.assertNotNull(got)
        Assertions.assertEquals("B", got?.nombre)
    }

    @Test
    fun `login exitoso setea token y usuarioActual`() = runTest {
        val user = UsuarioNetwork(6, "C", "c@c.com", null)
        coEvery {
            ApiClient.usuarioApi.login(any())
        } returns Response.success(LoginResponse("tokX", user))

        val vm = UsuarioViewModel()
        vm.login("c@c.com", "pass")

        val token = vm.token.getOrAwaitValue()
        val usuario = vm.usuarioActual.getOrAwaitValue()

        Assertions.assertEquals("tokX", token)
        Assertions.assertNotNull(usuario)
        Assertions.assertEquals("C", usuario?.nombre)
    }

    @Test
    fun `login con 401 pone mensaje de credenciales invalidas`() = runTest {
        val errorBody =
            "unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())

        coEvery {
            ApiClient.usuarioApi.login(any())
        } returns Response.error(401, errorBody)

        val vm = UsuarioViewModel()
        vm.login("x@x.com", "bad")

        val msg = vm.mensajeError.getOrAwaitValue()
        Assertions.assertNotNull(msg)
        Assertions.assertTrue(msg!!.contains("Credenciales inválidas"))
    }
}
