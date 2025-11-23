package cl.duocuc.streaminglab_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.model.Plan
import cl.duocuc.streaminglab_app.model.PlanEntity
import cl.duocuc.streaminglab_app.repository.local.AppDatabase
import cl.duocuc.streaminglab_app.repository.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar los planes disponibles en la app.
 *
 * Obtiene los datos desde la base de datos local (Room) y desde la API.
 * Proporciona LiveData para observación en la UI y funciones de filtrado,
 * ordenamiento, búsqueda y actualización de planes.
 *
 * @param app Contexto de aplicación para obtener acceso a la base de datos.
 */
class PlanViewModel(app: Application) : AndroidViewModel(app) {

    private val planDao = AppDatabase.getDatabase(app).planDao()  // DAO local

    /** LiveData que expone la lista de planes actual */
    private val _planes = MutableLiveData<List<Plan>>()
    val planes: LiveData<List<Plan>> get() = _planes

    /**
     * Convierte un [PlanEntity] de la DB a un [Plan] para la UI,
     * asignando imágenes predeterminadas según el nombre del plan.
     */
    private fun mapEntityToPlan(entity: PlanEntity): Plan {
        val imageResId = when (entity.nombre.lowercase()) {
            "individual" -> R.drawable.plan_basico
            "familiar" -> R.drawable.plan_familiar
            "corporativo" -> R.drawable.plan_corporativo
            else -> R.drawable.ic_default_plan
        }

        return Plan(
            nombre = entity.nombre,
            descripcion = entity.descripcion,
            precio = entity.precio,
            imageResName = entity.imageResName,
            imageResId = imageResId,
            resolucion = entity.resolucion,
            dispositivos = entity.dispositivos,
            perfiles = entity.perfiles,
            descargasOffline = entity.descargasOffline
        )
    }

    /**
     * Carga los planes disponibles:
     * 1. Primero desde DB local (si existen, los publica)
     * 2. Luego desde API (sobrescribe y guarda en DB)
     * 3. Si falla la API y no hay datos, usa planes de fallback.
     */
    fun cargarPlanes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // DB local
                val localPlanes = planDao.obtenerPlanes()
                if (localPlanes.isNotEmpty()) {
                    _planes.postValue(localPlanes.map { mapEntityToPlan(it) })
                }

                // API
                val apiPlanes: List<PlanEntity> = RetrofitClient.apiService.obtenerPlanes()
                val mappedPlanes = apiPlanes.map { mapEntityToPlan(it) }
                _planes.postValue(mappedPlanes)

                // Guardar en DB local
                planDao.clearAll()
                planDao.insertarPlanes(apiPlanes)

            } catch (e: Exception) {
                Log.e("PlanViewModel", "Error cargando planes desde API: ${e.message}")

                // Intentar cargar JSON local como "API simulada"
                try {
                    val inputStream = getApplication<Application>().assets.open("planes.json")
                    val reader = inputStream.bufferedReader()

                    // Gson en modo lenient
                    val gson = com.google.gson.GsonBuilder().setLenient().create()
                    val planesDesdeJson =
                        gson.fromJson(reader, Array<PlanEntity>::class.java).toList()
                    reader.close()

                    _planes.postValue(planesDesdeJson.map { mapEntityToPlan(it) })

                    // Guardar JSON local en DB
                    planDao.clearAll()
                    planDao.insertarPlanes(planesDesdeJson)
                    return@launch

                } catch (jsonEx: Exception) {
                    Log.e(
                        "PlanViewModel",
                        "Error cargando planes desde JSON local: ${jsonEx.message}"
                    )
                }

                // Fallback original si tampoco JSON local funciona
                if (_planes.value.isNullOrEmpty()) {
                    val fallback = listOf(
                        Plan(
                            nombre = "Individual",
                            descripcion = "Gestión de 1 cuenta, seguimiento de facturación y multimedia",
                            precio = "$4.990",
                            imageResName = "plan_basico",
                            imageResId = R.drawable.plan_basico,
                            resolucion = "SD",
                            dispositivos = 1,
                            perfiles = 1,
                            descargasOffline = false
                        ),
                        Plan(
                            nombre = "Familiar",
                            descripcion = "Gestión de varias cuentas, fechas de facturación y multimedia compartido",
                            precio = "$9.990",
                            imageResName = "plan_familiar",
                            imageResId = R.drawable.plan_familiar,
                            resolucion = "HD",
                            dispositivos = 3,
                            perfiles = 3,
                            descargasOffline = true
                        ),
                        Plan(
                            nombre = "Corporativo",
                            descripcion = "Gestión avanzada de cuentas múltiples, reportes y multimedia",
                            precio = "$14.990",
                            imageResName = "plan_corporativo",
                            imageResId = R.drawable.plan_corporativo,
                            resolucion = "4K",
                            dispositivos = 4,
                            perfiles = 4,
                            descargasOffline = true
                        )
                    )
                    _planes.postValue(fallback)

                    // Guardar fallback en DB
                    planDao.clearAll()
                    planDao.insertarPlanes(
                        fallback.mapIndexed { index, plan ->
                            PlanEntity(
                                id = index + 1,
                                nombre = plan.nombre,
                                descripcion = plan.descripcion,
                                precio = plan.precio,
                                imageResName = plan.imageResName,
                                resolucion = plan.resolucion,
                                dispositivos = plan.dispositivos,
                                perfiles = plan.perfiles,
                                descargasOffline = plan.descargasOffline
                            )
                        }
                    )
                }
            }



            /** Filtra planes por nombre que contenga [query] */
            fun filtrarPorNombre(query: String) {
                val current = _planes.value ?: return
                _planes.postValue(current.filter { it.nombre.contains(query, ignoreCase = true) })
            }

            /** Filtra planes cuyo precio esté entre [min] y [max] */
            fun filtrarPorPrecio(min: Int, max: Int) {
                val current = _planes.value ?: return
                _planes.postValue(current.filter {
                    val price = it.precio.replace("[^\\d]".toRegex(), "").toIntOrNull() ?: 0
                    price in min..max
                })
            }

            /** Ordena planes por precio ascendente (default) o descendente */
            fun ordenarPorPrecio(asc: Boolean = true) {
                val current = _planes.value ?: return
                val sorted = current.sortedBy {
                    it.precio.replace("[^\\d]".toRegex(), "").toIntOrNull() ?: 0
                }
                _planes.postValue(if (asc) sorted else sorted.reversed())
            }

            /** Busca un plan por su nombre exacto */
            fun buscarPlanPorNombre(nombre: String): Plan? =
                _planes.value?.find { it.nombre.equals(nombre, ignoreCase = true) }

            /** Recarga todos los planes desde API y DB local */
            fun resetPlanes() {
                cargarPlanes()
            }

            /** Actualiza un plan en la posición [index] */
            fun actualizarPlan(index: Int, plan: Plan) {
                val current = _planes.value?.toMutableList() ?: return
                if (index in current.indices) {
                    current[index] = plan
                    _planes.postValue(current)
                }
            }

            /** Elimina un plan en la posición [index] */
            fun eliminarPlan(index: Int) {
                val current = _planes.value?.toMutableList() ?: return
                if (index in current.indices) {
                    current.removeAt(index)
                    _planes.postValue(current)
                }
            }
        }
    }
}



