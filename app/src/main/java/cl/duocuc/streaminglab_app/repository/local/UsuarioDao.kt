package cl.duocuc.streaminglab_app.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.duocuc.streaminglab_app.model.UsuarioEntity

/**
 * DAO para operaciones sobre la tabla de usuarios en la base de datos local.
 *
 * Proporciona funciones para obtener, registrar, actualizar, eliminar y verificar usuarios.
 */
@Dao
interface UsuarioDao {

    /**
     * Obtiene todos los usuarios almacenados en la base de datos.
     *
     * @return Lista de [UsuarioEntity] actualmente guardados.
     */
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioEntity>

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * Si un usuario con el mismo id ya existe, será reemplazado.
     *
     * @param usuario [UsuarioEntity] a registrar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarUsuario(usuario: UsuarioEntity)

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param usuario [UsuarioEntity] con los datos actualizados.
     */
    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param usuario [UsuarioEntity] a eliminar.
     */
    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return [UsuarioEntity] si existe, o null si no se encuentra.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): UsuarioEntity?

    /**
     * Verifica si un correo electrónico ya está registrado.
     *
     * @param email Correo electrónico a verificar.
     * @return `true` si el correo existe, `false` en caso contrario.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE email = :email)")
    suspend fun emailExiste(email: String): Boolean
}
