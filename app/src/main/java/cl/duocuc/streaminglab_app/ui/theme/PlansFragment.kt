package cl.duocuc.streaminglab_app.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.duocuc.streaminglab_app.MainActivity
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.adapter.PlansAdapter
import cl.duocuc.streaminglab_app.viewmodel.PlanViewModel

/**
 * Fragmento que muestra la lista de planes disponibles.
 *
 * Permite al usuario ver los planes, seleccionar uno para ver detalles
 * y regresar a la pantalla principal.
 */
class PlansFragment : Fragment() {

    /** ViewModel compartido con la actividad para manejar los planes. */
    private val planVM: PlanViewModel by activityViewModels()

    /** RecyclerView que mostrará los planes. */
    private lateinit var rvPlans: RecyclerView

    /** Adapter para el RecyclerView de planes. */
    private lateinit var adapter: PlansAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_plans, container, false)

        // Configuración del RecyclerView
        rvPlans = view.findViewById(R.id.rvPlans)
        rvPlans.layoutManager = LinearLayoutManager(requireContext())

        // Inicializa adapter vacío solo una vez
        adapter = PlansAdapter(emptyList()) { plan ->
            // Al seleccionar un plan → ir al detalle
            (activity as? MainActivity)?.showPlanDetail(plan)
        }
        rvPlans.adapter = adapter

        // Observa los planes del ViewModel y actualiza la lista del adapter
        planVM.planes.observe(viewLifecycleOwner, Observer { listaPlanes ->
            adapter.submitList(listaPlanes)
        })

        // Carga los planes desde el ViewModel
        planVM.cargarPlanes()

        // Botón volver → regresar al HomeFragment
        val btnBack: Button = view.findViewById(R.id.btnBackPlans)
        btnBack.setOnClickListener {
            (activity as? MainActivity)?.showHomeScreen()
            requireActivity().overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }

        return view
    }
}
