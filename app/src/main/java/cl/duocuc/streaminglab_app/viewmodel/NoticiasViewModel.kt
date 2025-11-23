package cl.duocuc.streaminglab_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cl.duocuc.streaminglab_app.model.Noticia
import cl.duocuc.streaminglab_app.repository.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la obtención de noticias relacionadas con plataformas
 * de streaming y multimedia.
 *
 * Maneja la carga de noticias desde la API de Noticias, el filtrado por palabras clave
 * y expone LiveData para la UI.
 *
 * @property app Contexto de la aplicación, requerido por [AndroidViewModel].
 */
class NoticiasViewModel(app: Application) : AndroidViewModel(app) {

    /** LiveData que contiene la lista de noticias cargadas. */
    private val _noticias = MutableLiveData<List<Noticia>>()
    val noticias: LiveData<List<Noticia>> get() = _noticias

    /** LiveData que contiene mensajes de error o null si no hay error. */
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    /** Palabras clave para filtrar noticias de streaming y multimedia (string OR). */
    private val streamingKeywords =
        "Netflix OR Disney+ OR HBO OR Amazon Prime OR Hulu OR Apple TV OR YouTube OR streaming"

    /** Lista de palabras clave individuales para iterar en la API. */
    private val streamingKeywordsList = listOf(
        "Netflix", "Disney+", "HBO", "Amazon Prime", "Hulu", "Apple TV", "YouTube", "streaming"
    )

    /**
     * Carga noticias de plataformas de streaming usando la API.
     *
     * Se realiza una petición por cada palabra clave de [streamingKeywordsList].
     * Los resultados se transforman en objetos [Noticia] y se eliminan duplicados
     * basados en la URL antes de publicar en [_noticias].
     *
     * En caso de error, se publica un mensaje en [_error].
     *
     * @param apiKey Clave de API para autenticar la solicitud.
     */
    fun cargarNoticiasPorPlataforma(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listaNoticias = mutableListOf<Noticia>()

                for (keyword in streamingKeywordsList) {
                    val response = RetrofitClient.apiService.obtenerNoticias(
                        apiKey = apiKey,
                        query = keyword
                    )
                    response.results.forEach { articulo ->
                        listaNoticias.add(
                            Noticia(
                                title = articulo.title ?: "Sin título",
                                description = articulo.description,
                                url = articulo.link ?: "",
                                urlToImage = articulo.image_url,
                                publishedAt = articulo.pubDate ?: ""
                            )
                        )
                    }
                }

                // Evitar duplicados basados en la URL
                _noticias.postValue(listaNoticias.distinctBy { it.url })
                _error.postValue(null) // limpiar errores previos
            } catch (e: Exception) {
                Log.e("NoticiasVM", "Error cargando noticias por plataforma: ${e.message}")
                _error.postValue("Error al cargar noticias por plataforma: ${e.message}")
            }
        }
    }
}
