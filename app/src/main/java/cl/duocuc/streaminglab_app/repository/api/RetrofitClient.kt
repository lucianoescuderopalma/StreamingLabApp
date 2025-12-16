package cl.duocuc.streaminglab_app.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente singleton de Retrofit para consumir APIs de la aplicación.
 *
 * Proporciona instancias de servicios para consumir endpoints de planes y noticias.
 */
object RetrofitClient {

    /** URL base para los endpoints de planes. */
    private const val BASE_URL = "https://api.streaminglab.com/"

    /** URL base para los endpoints de noticias. */
    private const val NEWS_BASE_URL = "https://newsdata.io/api/1/"


    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL) // Cambiar según el endpoint que uses
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
