package cl.duocuc.streaminglab_app.model

/**
 * Modelo que representa una noticia dentro de la aplicación.
 *
 * @property title Título de la noticia.
 * @property description Descripción o contenido breve de la noticia. Puede ser nulo.
 * @property url URL del artículo completo de la noticia.
 * @property urlToImage URL de la imagen asociada a la noticia. Puede ser nulo.
 * @property publishedAt Fecha y hora de publicación de la noticia en formato ISO 8601.
 */
data class Noticia(
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String
)
