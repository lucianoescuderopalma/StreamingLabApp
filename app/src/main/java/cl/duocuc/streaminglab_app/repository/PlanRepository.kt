package cl.duocuc.streaminglab_app.repository

import android.content.Context
import cl.duocuc.streaminglab_app.model.Plan
import cl.duocuc.streaminglab_app.model.PlanEntity
import cl.duocuc.streaminglab_app.repository.api.RetrofitClient
import cl.duocuc.streaminglab_app.repository.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que maneja la obtención y persistencia de planes de streaming.
 *
 * Este repositorio combina datos remotos (API) y locales (Room) para ofrecer
 * acceso offline y sincronización de información.
 *
 * @param context Contexto de la aplicación, necesario para acceder a la base de datos local.
 */
class PlanRepository(context: Context) {

    /** Servicio de API para obtener planes desde el servidor remoto. */
    private val api = RetrofitClient.apiService

    /** DAO de Room para acceder a la base de datos local de planes. */
    private val planDao = AppDatabase.getDatabase(context).planDao()

    /**
     * Convierte un [PlanEntity] de la base de datos en un objeto [Plan].
     *
     * @param context Contexto de la aplicación, usado para resolver recursos de imagen.
     * @param defaultImageResId ID de recurso drawable por defecto si no se encuentra imagen.
     * @return Objeto [Plan] correspondiente al [PlanEntity].
     */
    private fun PlanEntity.toPlan(
        context: Context,
        defaultImageResId: Int = android.R.drawable.ic_menu_report_image
    ): Plan {
        val imageRes = imageResName?.let {
            context.resources.getIdentifier(it, "drawable", context.packageName)
        } ?: defaultImageResId

        return Plan(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            imageResId = imageRes,
            resolucion = resolucion,
            dispositivos = dispositivos,
            perfiles = perfiles,
            descargasOffline = descargasOffline
        )
    }

    /**
     * Convierte un [Plan] en un [PlanEntity] para guardarlo en la base de datos local.
     *
     * @param imageResName Nombre del recurso de imagen asociado al plan (opcional).
     * @return Objeto [PlanEntity] correspondiente al [Plan].
     */
    private fun Plan.toEntity(imageResName: String? = null): PlanEntity {
        return PlanEntity(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            imageResName = imageResName,
            resolucion = resolucion,
            dispositivos = dispositivos,
            perfiles = perfiles,
            descargasOffline = descargasOffline
        )
    }

    /**
     * Obtiene la lista de planes desde el servidor remoto.
     *
     * Si la llamada remota falla, se retornan los planes almacenados en la base de datos local.
     * Además, sincroniza la base de datos local con los datos remotos.
     *
     * @param context Contexto de la aplicación, necesario para convertir recursos de imagen.
     * @return Lista de objetos [Plan].
     */
    suspend fun obtenerPlanes(context: Context): List<Plan> = withContext(Dispatchers.IO) {
        try {
            val remote: List<PlanEntity> = api.obtenerPlanes()
            planDao.clearAll()
            planDao.insertarPlanes(remote)
            remote.map { it.toPlan(context) }
        } catch (e: Exception) {
            val local = planDao.obtenerPlanes()
            local.map { it.toPlan(context) }
        }
    }

    /**
     * Guarda un único plan en la base de datos local.
     *
     * @param plan Objeto [Plan] a almacenar.
     */
    suspend fun guardarPlan(plan: Plan) = withContext(Dispatchers.IO) {
        planDao.insertarPlanes(listOf(plan.toEntity()))
    }

    /**
     * Guarda una lista de planes en la base de datos local.
     *
     * @param plans Lista de objetos [Plan] a almacenar.
     */
    suspend fun guardarPlanes(plans: List<Plan>) = withContext(Dispatchers.IO) {
        planDao.insertarPlanes(plans.map { it.toEntity() })
    }
}
