package com.example.eventoapp.ui.utils

object Validators {

    // ---------- Usuario / Registro / Login ----------
    fun validarNombre(nombre: String): String? {
        if (nombre.isBlank()) return "El nombre no puede estar vacío"
        if (nombre.any { it.isDigit() }) return "El nombre no debe contener números"
        if (nombre.length < 3) return "Debe tener al menos 3 caracteres"
        return null
    }

    fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) return "El correo no puede estar vacío"
        // Regex simple, portable a JVM y Android. Cubre la mayoría de casos razonables.
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailRegex.matches(correo)) return "Formato de correo inválido"
        return null
    }

    fun validarContrasena(pass: String): String? {
        if (pass.length < 6) return "La contraseña debe tener mínimo 6 caracteres"
        return null
    }

    fun validarContrasenaLogin(contra: String): String? {
        return when {
            contra.isBlank() -> "La contraseña no puede estar vacía"
            contra.length < 4 -> "Debe tener mínimo 4 caracteres"
            else -> null
        }
    }

    // ---------- Evento: validaciones específicas ----------
    fun validarNombreEvento(nombre: String): String? {
        if (nombre.isBlank()) return "El nombre del evento no puede estar vacío"
        if (nombre.length < 3) return "Nombre muy corto"
        return null
    }

    fun validarDescripcion(desc: String): String? {
        if (desc.isBlank()) return "La descripción no puede estar vacía"
        if (desc.length < 10) return "Describe un poco más el evento (mín. 10 caracteres)"
        return null
    }

    fun validarDireccion(direccion: String): String? {
        if (direccion.isBlank()) return "La dirección no puede estar vacía"
        if (direccion.length < 5) return "Dirección muy corta"
        return null
    }

    fun validarDuracion(duracion: String): String? {
        if (duracion.isBlank()) return "La duración no puede estar vacía"
        if (!duracion.matches(Regex("^[0-9]+$"))) return "Solo tienes permitido poner números"
        val v = duracion.toIntOrNull() ?: return "Duración inválida"
        if (v <= 0) return "La duración debe ser mayor a 0"
        if (v > 1000) return "Duración poco realista"
        return null
    }
}
