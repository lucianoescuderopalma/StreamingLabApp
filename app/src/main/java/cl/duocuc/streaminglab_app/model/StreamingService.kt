package cl.duocuc.streaminglab_app.model

/**
 * Representa un servicio de streaming dentro de la aplicación.
 *
 * Cada servicio contiene un nombre y una lista de planes disponibles,
 * permitiendo agrupar los planes por plataforma.
 *
 * @property nombre Nombre del servicio de streaming (ej. "Netflix", "Disney+").
 * @property planes Lista de objetos [Plan] que ofrece el servicio.
 */
data class StreamingService(
    val nombre: String,
    val planes: List<Plan>
)
