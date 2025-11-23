package cl.duocuc.streaminglab_app.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import cl.duocuc.streaminglab_app.MainActivity
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.model.Usuario
import cl.duocuc.streaminglab_app.viewmodel.UsuarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento para el registro de nuevos usuarios.
 *
 * Permite al usuario ingresar nombre, correo electrónico y contraseña,
 * valida los campos, verifica duplicados y registra al usuario.
 */
class RegisterFragment : Fragment() {

    /** ViewModel compartido con la actividad para operaciones de usuario. */
    private val usuarioVM: UsuarioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Referencias a los elementos del layout
        val nameInput = view.findViewById<EditText>(R.id.etName)
        val emailInput = view.findViewById<EditText>(R.id.etEmail)
        val passInput = view.findViewById<EditText>(R.id.etPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnSubmit)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        // Botón para volver a HomeFragment
        btnBack.setOnClickListener {
            (activity as? MainActivity)?.showHomeScreen()
        }

        // Botón de registro
        btnRegister.setOnClickListener {

            val nombre = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passInput.text.toString().trim()

            // Reset de errores visuales
            nameInput.error = null
            emailInput.error = null
            passInput.error = null

            // Validar nombre
            if (nombre.isEmpty()) {
                nameInput.error = "Ingresa un nombre"
                return@setOnClickListener
            }

            /**
             * Función para validar que el correo electrónico cumpla con dominios permitidos y formato correcto.
             *
             * @param email Correo ingresado por el usuario.
             * @return Boolean indicando si es válido o no.
             */
            fun esCorreoValido(email: String): Boolean {
                val dominiosPermitidos = listOf(
                    "gmail.com", "yahoo.com", "hotmail.com", "outlook.com",
                    "icloud.com", "live.com", "aol.com", "protonmail.com",
                    "mail.com", "gmx.com", "me.com", "edu.cl", "uc.cl", "duoc.cl"
                )

                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
                if (!emailRegex.matches(email)) return false

                val partes = email.split("@")
                if (partes.size != 2) return false

                val dominio = partes[1].lowercase()
                if (!dominiosPermitidos.contains(dominio)) return false

                val extension = dominio.substringAfterLast('.', "")
                if (extension.length !in 2..6 || !extension.all { it.isLetter() }) return false

                if (dominio.startsWith('.') || dominio.endsWith('.')) return false

                return true
            }

            // Validar correo
            if (!esCorreoValido(email)) {
                emailInput.error = "Correo inválido"
                return@setOnClickListener
            }

            // Validar contraseña mínima
            if (password.length < 6) {
                passInput.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }

            // 🔹 Corrutina para operaciones de usuario en background
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    // 1️⃣ Verificar si el correo ya existe
                    val existe = usuarioVM.emailExiste(email)
                    if (existe) {
                        withContext(Dispatchers.Main) {
                            emailInput.error = "El correo ya está registrado"
                            Toast.makeText(requireContext(), "Correo ya registrado", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    // 2️⃣ Crear usuario y registrar
                    val nuevoUsuario = Usuario(nombre, email, password)

                    usuarioVM.registrar(nuevoUsuario) { exito ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            if (exito) {
                                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                                (activity as? MainActivity)?.showHomeScreen()
                            } else {
                                Toast.makeText(requireContext(), "Error al registrar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }
        }

        return view
    }
}
