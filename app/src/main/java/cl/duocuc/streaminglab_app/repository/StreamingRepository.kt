package cl.duocuc.streaminglab_app.repository

import cl.duocuc.streaminglab_app.model.Plan
import cl.duocuc.streaminglab_app.model.Usuario
import cl.duocuc.streaminglab_app.R

/**
 * Repositorio simulado que maneja usuarios y planes de streaming.
 *
 * Este repositorio mantiene una lista interna de usuarios y planes predeterminados
 * para propósitos de demostración o pruebas.
 */
class StreamingRepository {

    /** Lista interna de usuarios registrados. */
    private val usuarios = mutableListOf<Usuario>()

    /** Lista de planes de streaming disponibles. */
    private val planes = listOf(
        Plan(
            nombre = "Básico",
            descripcion = "Ideal para una persona, contenido en calidad HD",
            precio = "$5.990 CLP / mes",
            imageResId = R.drawable.plan_basico,
            resolucion = "HD",
            dispositivos = 1,
            perfiles = 1,
            descargasOffline = false
        ),
        Plan(
            nombre = "Familiar",
            descripcion = "Calidad Full HD y 4 dispositivos simultáneos",
            precio = "$8.990 CLP / mes",
            imageResId = R.drawable.plan_familiar,
            resolucion = "Full HD",
            dispositivos = 4,
            perfiles = 4,
            descargasOffline = true
        ),
        Plan(
            nombre = "Corporativo",
            descripcion = "4K HDR, hasta 8 dispositivos y descargas ilimitadas",
            precio = "$12.990 CLP / mes",
            imageResId = R.drawable.plan_corporativo,
            resolucion = "4K HDR",
            dispositivos = 4,
            perfiles = 6,
            descargasOffline = true
        )
    )

    /**
     * Registra un nuevo usuario si su correo electrónico no existe previamente.
     *
     * @param usuario Objeto [Usuario] a registrar.
     * @return `true` si el usuario fue agregado correctamente, `false` si el email ya existe.
     */
    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario)
        return true
    }

    /**
     * Obtiene la lista de planes disponibles.
     *
     * @return Lista de [Plan] disponibles.
     */
    fun obtenerPlanes(): List<Plan> = planes
}
