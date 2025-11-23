package cl.duocuc.streaminglab_app.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import cl.duocuc.streaminglab_app.MainActivity
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.model.Plan

/**
 * Fragmento que muestra los detalles de un plan seleccionado.
 *
 * Incluye información del plan, características, imagen, precio, descripción,
 * y permite continuar al proceso de pago.
 */
class PlanDetailFragment : Fragment() {

    /** Plan que se mostrará en detalle. */
    private lateinit var plan: Plan

    companion object {
        private const val ARG_NOMBRE = "nombre"
        private const val ARG_DESC = "descripcion"
        private const val ARG_PRECIO = "precio"
        private const val ARG_IMG = "imagen"
        private const val ARG_RESOLUCION = "resolucion"
        private const val ARG_DISPOSITIVOS = "dispositivos"
        private const val ARG_PERFILES = "perfiles"
        private const val ARG_OFFLINE = "offline"

        /**
         * Crea una nueva instancia de [PlanDetailFragment] con los datos del plan.
         *
         * @param plan Plan que se mostrará en detalle.
         * @return Nueva instancia de [PlanDetailFragment].
         */
        fun newInstance(plan: Plan): PlanDetailFragment {
            val fragment = PlanDetailFragment()
            val bundle = Bundle()

            bundle.putString(ARG_NOMBRE, plan.nombre)
            bundle.putString(ARG_DESC, plan.descripcion)
            bundle.putString(ARG_PRECIO, plan.precio)
            bundle.putInt(ARG_IMG, plan.imageResId)
            bundle.putString(ARG_RESOLUCION, plan.resolucion)
            bundle.putInt(ARG_DISPOSITIVOS, plan.dispositivos)
            bundle.putInt(ARG_PERFILES, plan.perfiles)
            bundle.putBoolean(ARG_OFFLINE, plan.descargasOffline)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { b ->
            plan = Plan(
                nombre = b.getString(ARG_NOMBRE) ?: "",
                descripcion = b.getString(ARG_DESC) ?: "",
                precio = b.getString(ARG_PRECIO) ?: "",
                imageResId = b.getInt(ARG_IMG),
                resolucion = b.getString(ARG_RESOLUCION) ?: "",
                dispositivos = b.getInt(ARG_DISPOSITIVOS),
                perfiles = b.getInt(ARG_PERFILES),
                descargasOffline = b.getBoolean(ARG_OFFLINE)
            )
        }
    }

    /**
     * Infla la vista del fragmento, configura los elementos de UI,
     * muestra la información del plan y configura botones y animaciones.
     *
     * @param inflater Inflater para inflar la vista.
     * @param container Contenedor padre del fragmento.
     * @param savedInstanceState Estado previamente guardado.
     * @return Vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_plan_detail, container, false)

        // Animación de entrada
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        view.startAnimation(fadeIn)

        // Views
        val imgPlan: ImageView = view.findViewById(R.id.imgPlanDetail)
        val tvName: TextView = view.findViewById(R.id.tvDetailName)
        val tvPrice: TextView = view.findViewById(R.id.tvDetailPrice)
        val tvDescription: TextView = view.findViewById(R.id.tvDetailDescription)

        val tvResolution: TextView = view.findViewById(R.id.tvDetailResolution)
        val tvDevices: TextView = view.findViewById(R.id.tvDetailDevices)
        val tvProfiles: TextView = view.findViewById(R.id.tvDetailProfiles)
        val tvOffline: TextView = view.findViewById(R.id.tvDetailOffline)

        val tvTrust: TextView = view.findViewById(R.id.tvTrust)

        val btnContinue: Button = view.findViewById(R.id.btnContinueToPayment)
        val btnBack: Button = view.findViewById(R.id.btnPlanDetailBack)

        // Cargar información del plan
        imgPlan.setImageResource(plan.imageResId)
        tvName.text = plan.nombre
        tvPrice.text = plan.precio
        tvDescription.text = plan.descripcion

        // Características
        tvResolution.text = "Resolución máxima: ${plan.resolucion}"
        tvDevices.text = "Dispositivos simultáneos: ${plan.dispositivos}"
        tvProfiles.text = "Perfiles disponibles: ${plan.perfiles}"
        tvOffline.text = "Descargas Offline: ${if (plan.descargasOffline) "Sí" else "No"}"

        // Información de seguridad
        tvTrust.text = "En StreamingLab protegemos tus datos con encriptación avanzada y protocolos internacionales. Tus datos de pago están 100% seguros."

        // Botón atrás
        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Animación del botón continuar
        val btnAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.button_pop)

        // Botón continuar → Ir a la pantalla de pago
        btnContinue.setOnClickListener {
            it.startAnimation(btnAnim)
            (activity as? MainActivity)?.showPayment(plan.nombre, plan.precio)
        }

        return view
    }
}
