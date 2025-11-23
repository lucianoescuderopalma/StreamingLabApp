package cl.duocuc.streaminglab_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un plan de streaming en la base de datos local.
 *
 * Esta clase se utiliza con Room para persistir los planes en la tabla "planes".
 * Permite almacenar y recuperar información de cada plan de manera persistente.
 *
 * @property id Identificador único del plan. Se genera automáticamente.
 * @property nombre Nombre del plan (ej. "Básico", "Premium").
 * @property descripcion Breve descripción del plan.
 * @property precio Precio del plan en formato de texto (ej. "$4.990").
 * @property imageResName Nombre del recurso de imagen o URL opcional. Puede ser nulo.
 * @property resolucion Resolución máxima soportada por el plan (ej. "HD", "4K").
 * @property dispositivos Cantidad de dispositivos que pueden usar el plan simultáneamente.
 * @property perfiles Número de perfiles de usuario permitidos.
 * @property descargasOffline Indica si el plan permite descargas para ver offline.
 */
@Entity(tableName = "planes")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val imageResName: String?,
    val resolucion: String,
    val dispositivos: Int,
    val perfiles: Int,
    val descargasOffline: Boolean
)
