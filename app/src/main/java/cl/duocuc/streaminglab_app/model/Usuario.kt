package cl.duocuc.streaminglab_app.model

/**
 * Representa un usuario de la aplicación.
 *
 * Contiene información básica necesaria para la autenticación y gestión de la cuenta.
 *
 * @property nombre Nombre completo del usuario.
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 */
data class Usuario(
    val nombre: String,
    val email: String,
    val password: String
)
