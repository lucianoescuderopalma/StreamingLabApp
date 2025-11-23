package cl.duocuc.streaminglab_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.MainActivity
import cl.duocuc.streaminglab_app.viewmodel.UsuarioViewModel

/**
 * Fragmento principal de la aplicación que muestra la bienvenida al usuario
 * y proporciona acceso a las secciones de registro, planes y noticias.
 */
class HomeFragment : Fragment() {

    /** ViewModel compartido para manejar la información del usuario. */
    private val usuarioVM: UsuarioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val tvWelcome: TextView = view.findViewById(R.id.tvWelcome)

        // Observar LiveData para actualizar automáticamente cuando cambie el usuario
        usuarioVM.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            tvWelcome.text = usuario?.let { "Bienvenido ${it.nombre}" } ?: "Bienvenido invitado"
        }

        // Botón de registro → navegar al fragmento de registro
        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            (activity as? MainActivity)?.showRegister() ?: run {
                Toast.makeText(context, "Error al navegar al registro", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón de planes → navegar al fragmento de planes
        view.findViewById<Button>(R.id.btnPlans).setOnClickListener {
            (activity as? MainActivity)?.showPlans() ?: run {
                Toast.makeText(context, "Error al navegar a planes", Toast.LENGTH_SHORT).show()
            }
        }

        // NUEVO: Botón de noticias → navegar al fragmento de noticias
        view.findViewById<Button>(R.id.btnVerNoticias)?.setOnClickListener {
            (activity as? MainActivity)?.showNoticias() ?: run {
                Toast.makeText(context, "Error al navegar a noticias", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
