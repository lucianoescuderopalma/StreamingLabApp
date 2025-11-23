package cl.duocuc.streaminglab_app.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duocuc.streaminglab_app.model.PlanEntity

/**
 * DAO para operaciones sobre la tabla de planes en la base de datos local.
 *
 * Proporciona funciones para obtener, insertar y eliminar planes.
 */
@Dao
interface PlanDao {

    /**
     * Obtiene todos los planes almacenados en la base de datos.
     *
     * @return Lista de [PlanEntity] actualmente guardados.
     */
    @Query("SELECT * FROM planes")
    suspend fun obtenerPlanes(): List<PlanEntity>

    /**
     * Inserta una lista de planes en la base de datos.
     *
     * Si un plan ya existe (mismo id), será reemplazado.
     *
     * @param plans Lista de [PlanEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPlanes(plans: List<PlanEntity>)

    /**
     * Elimina todos los planes de la tabla.
     */
    @Query("DELETE FROM planes")
    suspend fun clearAll()
}
