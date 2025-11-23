package cl.duocuc.streaminglab_app.model

/**
 * Representa un plan de streaming dentro de la aplicación.
 *
 * Contiene información detallada sobre cada plan, incluyendo
 * nombre, descripción, precio, imagen, resolución, cantidad de dispositivos,
 * perfiles de usuario y si permite descargas para ver offline.
 *
 * @property nombre Nombre del plan (ej. "Básico", "Premium").
 * @property descripcion Breve descripción del plan.
 * @property precio Precio del plan en formato de texto (ej. "$4.990").
 * @property imageResName Nombre del recurso de imagen o URL opcional. Puede ser nulo.
 * @property imageResId ID del recurso drawable asociado al plan.
 * @property resolucion Resolución máxima soportada por el plan (ej. "HD", "4K").
 * @property dispositivos Cantidad de dispositivos que pueden usar el plan simultáneamente.
 * @property perfiles Número de perfiles de usuario permitidos.
 * @property descargasOffline Indica si el plan permite descargas para ver offline.
 */
data class Plan(
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val imageResName: String? = null,
    val imageResId: Int,
    val resolucion: String,
    val dispositivos: Int,
    val perfiles: Int,
    val descargasOffline: Boolean
)
