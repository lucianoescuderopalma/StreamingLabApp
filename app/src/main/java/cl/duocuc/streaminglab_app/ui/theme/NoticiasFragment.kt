package cl.duocuc.streaminglab_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.adapter.NoticiasAdapter
import cl.duocuc.streaminglab_app.viewmodel.NoticiasViewModel

/**
 * Fragmento que muestra una lista de noticias sobre plataformas de streaming.
 *
 * Utiliza un [RecyclerView] para mostrar las noticias y observa un [NoticiasViewModel]
 * para obtener los datos desde la API y manejar errores.
 */
class NoticiasFragment : Fragment() {

    /** ViewModel compartido que maneja la lógica de noticias. */
    private val noticiasVM: NoticiasViewModel by activityViewModels()

    /** RecyclerView donde se mostrarán las noticias. */
    private lateinit var recyclerView: RecyclerView

    /** Adapter para vincular la lista de noticias al RecyclerView. */
    private lateinit var adapter: NoticiasAdapter

    /** API key para acceder al servicio de noticias. */
    private val apiKey = "pub_ef627faad4b34fe7927d74d6193fcd29"

    /**
     * Infla la vista del fragmento y configura el RecyclerView, adapters y observadores.
     *
     * @param inflater Inflater para inflar la vista.
     * @param container Contenedor padre del fragmento.
     * @param savedInstanceState Estado previamente guardado.
     * @return Vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_noticias, container, false)
        recyclerView = view.findViewById(R.id.rvNoticias)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializar adapter vacío
        adapter = NoticiasAdapter(emptyList())
        recyclerView.adapter = adapter

        // Botón de retroceso
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Observer de noticias
        noticiasVM.noticias.observe(viewLifecycleOwner) { noticias ->
            adapter.setNoticias(noticias)
        }

        // Observer de errores
        noticiasVM.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // Cargar noticias de streaming
        noticiasVM.cargarNoticiasPorPlataforma(apiKey)

        return view
    }
}
