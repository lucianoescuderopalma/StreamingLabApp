package cl.duocuc.streaminglab_app.repository

import cl.duocuc.streaminglab_app.model.UsuarioEntity
import cl.duocuc.streaminglab_app.repository.local.UsuarioDao

/**
 * Repositorio para gestionar usuarios almacenados en la base de datos local (Room).
 *
 * Proporciona funciones para obtener, registrar, actualizar, eliminar y verificar usuarios.
 *
 * @param dao Instancia de [UsuarioDao] para acceder a la base de datos.
 */
class UserRoomRepository(private val dao: UsuarioDao) {

    /**
     * Obtiene todos los usuarios almacenados en la base de datos.
     *
     * @return Lista de [UsuarioEntity] actualmente guardados.
     */
    suspend fun obtenerUsuarios(): List<UsuarioEntity> = dao.obtenerUsuarios()

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario [UsuarioEntity] a registrar.
     */
    suspend fun registrarUsuario(usuario: UsuarioEntity) = dao.registrarUsuario(usuario)

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param usuario [UsuarioEntity] con los datos actualizados.
     */
    suspend fun actualizarUsuario(usuario: UsuarioEntity) = dao.actualizarUsuario(usuario)

    /**
     * Elimina un usuario de la base de datos por su correo electrónico.
     *
     * @param email Correo electrónico del usuario a eliminar.
     */
    suspend fun eliminarUsuario(email: String) {
        val usuario = dao.obtenerUsuarioPorEmail(email)
        usuario?.let { dao.eliminarUsuario(it) }
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return [UsuarioEntity] si existe, o null si no se encuentra.
     */
    suspend fun obtenerUsuarioPorEmail(email: String): UsuarioEntity? = dao.obtenerUsuarioPorEmail(email)

    /**
     * Verifica si un correo electrónico ya está registrado.
     *
     * @param email Correo electrónico a verificar.
     * @return `true` si el correo existe, `false` en caso contrario.
     */
    suspend fun emailExiste(email: String): Boolean = dao.emailExiste(email)
}
