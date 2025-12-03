package com.example.eventoapp.ui.screens

import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.network.EventoCreateRequest
import com.example.eventoapp.ui.animations.ClickScaleAnimation
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.components.AnimatedTextField
import com.example.eventoapp.ui.utils.Validators
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEventoScreen(
    viewModel: EventoViewModel,
    usuarioViewModel: UsuarioViewModel,
    onBack: () -> Unit,
    usuarioIdFallback: Int = 1,
    creadorNombreFallback: String = "Usuario Actual"
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Observamos usuario y token del ViewModel de usuario
    val usuarioActual by usuarioViewModel.usuarioActual.observeAsState(null)
    val token by usuarioViewModel.token.observeAsState(null)

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(System.currentTimeMillis()) }
    var fechaTexto by remember { mutableStateOf("Seleccionar fecha") }

    var imagePath by remember { mutableStateOf<String?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val filename = "evento_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EventLive")
            }
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let { realUri ->
                val output: OutputStream? = context.contentResolver.openOutputStream(realUri)
                output?.use { stream ->
                    it.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                }
                imagePath = realUri.toString()
            }
        }
    }

    // Date picker
    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            fecha = calendar.timeInMillis
            fechaTexto = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        FadeInAnimation {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Button(onClick = { cameraLauncher.launch(null) }) {
                    Text("📸 Tomar foto")
                }

                Spacer(Modifier.height(12.dp))

                imagePath?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                    )
                }

                AnimatedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del evento") },
                    validator = Validators::validarNombreEvento,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                AnimatedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    validator = Validators::validarDescripcion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                AnimatedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección o lugar") },
                    validator = Validators::validarDireccion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                AnimatedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración (en horas)") },
                    validator = Validators::validarDuracion,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(onClick = { datePicker.show() }) {
                    Text("📅 $fechaTexto")
                }

                Spacer(Modifier.height(16.dp))

                // button press animation wrapper
                var pressed by remember { mutableStateOf(false) }
                ClickScaleAnimation(pressed = pressed) { scale ->
                    Button(
                        onClick = {
                            pressed = true
                            // validate all fields
                            val eNombre = Validators.validarNombreEvento(nombre)
                            val eDesc = Validators.validarDescripcion(descripcion)
                            val eDir = Validators.validarDireccion(direccion)
                            val eDur = Validators.validarDuracion(duracion)

                            if (eNombre == null && eDesc == null && eDir == null && eDur == null && imagePath != null) {
                                val duracionHoras = duracion.toIntOrNull() ?: 0
                                val uid = usuarioActual?.id ?: usuarioIdFallback
                                val creador = usuarioActual?.nombre ?: creadorNombreFallback

                                val request = EventoCreateRequest(
                                    usuarioId = uid,
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    direccion = direccion,
                                    fecha = fecha,
                                    duracionHoras = duracionHoras,
                                    imagenUri = imagePath,
                                    creadorNombre = creador
                                )

                                // Si no hay token, avisar que necesita iniciar sesión
                                if (token.isNullOrEmpty()) {
                                    Toast.makeText(context, "Debes iniciar sesión para crear eventos", Toast.LENGTH_SHORT).show()
                                    scope.launch {
                                        delay(180)
                                        pressed = false
                                    }
                                } else {
                                    // llamamos al viewModel con el request
                                    viewModel.crearEvento(token!!, request)
                                    scope.launch {
                                        // pequeña espera para que la petición salga (no bloqueante)
                                        delay(240)
                                        pressed = false
                                        onBack()
                                    }
                                }
                            } else {
                                scope.launch {
                                    delay(180)
                                    pressed = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale; scaleY = scale
                            }
                    ) {
                        Text("💾 Guardar evento")
                    }
                }
            }
        }
    }
}
