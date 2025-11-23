package cl.duocuc.streaminglab_app.repository.api

/**
 * Representa la respuesta de la API ND al obtener noticias.
 *
 * @property results Lista de artículos de noticias obtenidos de la API.
 */
data class NoticiasResponseND(
    val results: List<NoticiaArticuloND>
)

/**
 * Representa un artículo de noticia individual proveniente de la API ND.
 *
 * @property title Título de la noticia. Puede ser nulo.
 * @property description Descripción breve del artículo. Puede ser nulo.
 * @property link URL del artículo completo. Puede ser nulo.
 * @property image_url URL de la imagen asociada al artículo. Puede ser nulo.
 * @property pubDate Fecha de publicación del artículo en formato de texto. Puede ser nulo.
 */
data class NoticiaArticuloND(
    val title: String?,
    val description: String?,
    val link: String?,
    val image_url: String?,
    val pubDate: String?
)
