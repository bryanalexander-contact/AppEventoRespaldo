    package com.example.eventoapp.model
    
    import com.example.eventoapp.data.Model.entities.EventoEntity
    import org.junit.jupiter.api.Assertions.*
    import org.junit.jupiter.api.Test
    
    class EventoEntityTest {
    
        @Test
        fun `evento entity guarda campos correctamente`() {
            val e = EventoEntity(0, 1, "N", "D", "Dir", 12345L, 2, null, "Creador", false)
            assertEquals("N", e.nombre)
            assertEquals(1, e.usuarioId)
            assertEquals("Creador", e.creadorNombre)
        }
    }
