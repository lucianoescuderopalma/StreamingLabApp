package cl.duocuc.streaminglab_app.repository.api

import retrofit2.http.GET
import retrofit2.http.Query
import cl.duocuc.streaminglab_app.model.NoticiasResponseND
import cl.duocuc.streaminglab_app.model.PlanEntity

interface ApiService {

    // Método para obtener planes
    @GET("planes.json")
    suspend fun obtenerPlanes(): List<PlanEntity>

    // Método para obtener noticias filtradas de streaming
    @GET("news")
    suspend fun obtenerNoticias(
        @Query("apikey") apiKey: String,
        @Query("q") query: String = "Netflix OR Disney+ OR HBO OR Amazon Prime OR streaming",
        @Query("language") language: String = "en"
    ): NoticiasResponseND
}
