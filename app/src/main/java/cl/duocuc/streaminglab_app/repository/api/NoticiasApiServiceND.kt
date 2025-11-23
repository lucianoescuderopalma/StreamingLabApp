package cl.duocuc.streaminglab_app.repository.api

import cl.duocuc.streaminglab_app.model.NoticiasResponseND
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para consumir la API de noticias ND.
 *
 * Proporciona métodos para obtener noticias relacionadas con servicios de streaming.
 */
interface NoticiasApiServiceND {

    /**
     * Obtiene una lista de noticias desde la API ND.
     *
     * @param apiKey Clave de acceso a la API.
     * @param query Consulta de búsqueda para filtrar noticias. Por defecto busca noticias sobre
     * "Netflix", "Disney+", "HBO", "Amazon Prime" o "streaming".
     * @param language Idioma de las noticias. Por defecto "en" (inglés).
     * @return [NoticiasResponseND] que contiene el estado de la respuesta y la lista de artículos.
     */
    @GET("news")
    suspend fun obtenerNoticias(
        @Query("apikey") apiKey: String,
        @Query("q") query: String = "Netflix OR Disney+ OR HBO OR Amazon Prime OR streaming",
        @Query("language") language: String = "en"
    ): NoticiasResponseND
}
