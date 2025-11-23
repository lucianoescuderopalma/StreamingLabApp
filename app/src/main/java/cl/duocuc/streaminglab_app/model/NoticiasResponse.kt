package cl.duocuc.streaminglab_app.model

/**
 * Modelo que representa la respuesta de la API de noticias ND.
 *
 * @property status Estado de la respuesta (por ejemplo, "ok" o "error"). Puede ser nulo.
 * @property results Lista de artículos de noticias obtenidos de la API.
 */
data class NoticiasResponseND(
    val status: String?,
    val results: List<NoticiaArticuloND>
)

/**
 * Modelo que representa un artículo de noticia individual de la API ND.
 *
 * @property title Título de la noticia. Puede ser nulo.
 * @property link URL del artículo completo de la noticia. Puede ser nulo.
 * @property description Descripción breve del artículo. Puede ser nulo.
 * @property image_url URL de la imagen asociada a la noticia. Puede ser nulo.
 * @property pubDate Fecha de publicación del artículo en formato de texto. Puede ser nulo.
 */
data class NoticiaArticuloND(
    val title: String?,
    val link: String?,
    val description: String?,
    val image_url: String?,
    val pubDate: String?
)
