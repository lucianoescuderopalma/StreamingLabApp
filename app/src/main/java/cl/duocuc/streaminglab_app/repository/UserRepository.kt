package cl.duocuc.streaminglab_app.repository

import android.content.Context
import cl.duocuc.streaminglab_app.model.Usuario

/**
 * Repositorio para manejar el almacenamiento local de un usuario usando SharedPreferences.
 *
 * Permite guardar, cargar y eliminar los datos de un usuario de manera persistente.
 */
object UserRepository {

    /** Nombre del archivo de SharedPreferences donde se almacenan los usuarios. */
    private const val PREFS = "usuarios"

    /**
     * Guarda un usuario en SharedPreferences.
     *
     * @param context Contexto de la aplicación.
     * @param usuario Objeto [Usuario] a guardar.
     */
    fun saveUser(context: Context, usuario: Usuario) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString("email", usuario.email)
            .putString("password", usuario.password)
            .apply()
    }

    /**
     * Carga un usuario almacenado en SharedPreferences.
     *
     * @param context Contexto de la aplicación.
     * @return [Usuario] si existe, o `null` si no hay usuario guardado.
     */
    fun loadUser(context: Context): Usuario? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val nombre = prefs.getString("nombre", null)
        val email = prefs.getString("email", null)
        val pass = prefs.getString("password", null)

        return if (!nombre.isNullOrEmpty() && !email.isNullOrEmpty() && !pass.isNullOrEmpty()) {
            Usuario(nombre, email, pass)
        } else {
            null
        }
    }

    /**
     * Elimina los datos del usuario almacenado en SharedPreferences.
     *
     * @param context Contexto de la aplicación.
     */
    fun clearUser(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
